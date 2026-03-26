package com.ring.welkin.common.core.http;

import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.security.auth.Subject;
import javax.security.auth.kerberos.KerberosPrincipal;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import java.net.URI;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.*;

/**
 * 自定义rest接口客户端，继承{@link RestTemplate}类，扩展支持kerberos认证请求
 *
 * @author cloud
 * @date 2023年10月16日 下午6:26:41
 */
public class IRestClient extends RestTemplate {

    private static final Credentials credentials = new NullCredentials();

    /**
     * 是否启用kerberos
     */
    private final boolean kerberosEnable;

    /**
     * kerberos keytab路径
     */
    private final String kerberosKeytab;
    /**
     * kerberos principal名称
     */
    private final String kerberosPrincipal;

    /**
     * kerberos登录参数项
     */
    private final Map<String, Object> kerberosLoginOptions;

    /**
     * 默认构造器，默认情况下不开启kerberos认证
     */
    public IRestClient() {
        this(false);
    }

    /**
     * 加入kerberos开关的构造器，开启kerberos后会自动构建SPNEGO客户端，使用缓存令牌
     *
     * @param kerberosEnable 是否开启kerberos认证
     */
    public IRestClient(boolean kerberosEnable) {
        this(kerberosEnable, null, null, null, buildDefaultHttpClient(kerberosEnable));
    }

    /*
     * public IRestTemplate(boolean kerberosEnable, HttpClient httpClient) {
     * this(kerberosEnable, null, null, null, httpClient); }
     */

    /**
     * kerberos认证的构造器，需提供kerberos keytab和principal，如果两个参数为空则使用缓存令牌
     *
     * @param kerberosKeytab    kerberos keytab路径
     * @param kerberosPrincipal kerberos principal名称
     */
    public IRestClient(String kerberosKeytab, String kerberosPrincipal) {
        this(true, kerberosKeytab, kerberosPrincipal, null, buildDefaultHttpClient(true));
    }

    /*
     * public IRestTemplate(String kerberosKeytab, String kerberosPrincipal,
     * HttpClient httpClient) { this(true, kerberosKeytab, kerberosPrincipal, null,
     * httpClient); }
     */

    /**
     * kerberos认证的构造器，需提供kerberos 登录参数
     *
     * @param kerberosLoginOptions kerberos 登录参数
     */
    public IRestClient(Map<String, Object> kerberosLoginOptions) {
        this(true, null, null, kerberosLoginOptions, buildDefaultHttpClient(true));
    }

    /*
     * public IRestTemplate(Map<String, Object> loginOptions, HttpClient httpClient)
     * { this(true, null, null, loginOptions, httpClient); }
     */

    /**
     * kerberos认证的构造器，需提供kerberos keytab、principal、loginOptions
     *
     * @param kerberosKeytab       kerberos keytab路径
     * @param kerberosPrincipal    kerberos principal名称
     * @param kerberosLoginOptions kerberos 登录参数
     */
    public IRestClient(String kerberosKeytab, String kerberosPrincipal, Map<String, Object> kerberosLoginOptions) {
        this(true, kerberosKeytab, kerberosPrincipal, kerberosLoginOptions, buildDefaultHttpClient(true));
    }

    /**
     * kerberos认证的构造器，需提供kerberos keytab、principal、loginOptions参数的包装类
     *
     * @param kerberosParameter kerberos keytab、principal、loginOptions参数的包装类
     */
    public IRestClient(KerberosParameter kerberosParameter) {
        this(kerberosParameter.getKeytab(), kerberosParameter.getPrincipal(), kerberosParameter.getLoginOptions());
    }

    private IRestClient(boolean kerberosEnable, String kerberosKeytab, String kerberosPrincipal,
                        Map<String, Object> kerberosLoginOptions, HttpClient httpClient) {
        super(new HttpComponentsClientHttpRequestFactory(httpClient));
        this.kerberosEnable = kerberosEnable;
        this.kerberosKeytab = kerberosKeytab;
        this.kerberosPrincipal = kerberosPrincipal;
        this.kerberosLoginOptions = kerberosLoginOptions;
    }

    /**
     * 构建默认的HttpClient实例，如果开启kerberos认证则需要实现spnego模式
     *
     * @param kerberosEnable 是否开启kerberos认证
     * @return HttpClient实例
     */
    private static HttpClient buildDefaultHttpClient(boolean kerberosEnable) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        if (kerberosEnable) {
            Lookup<AuthSchemeProvider> authSchemeRegistry = RegistryBuilder.<AuthSchemeProvider>create()
                    .register(AuthSchemes.SPNEGO, new SPNegoSchemeFactory(true)).build();
            builder.setDefaultAuthSchemeRegistry(authSchemeRegistry);
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(new AuthScope(null, -1, null), credentials);
            builder.setDefaultCredentialsProvider(credentialsProvider);
        }
        return builder.build();
    }

    @Override
    protected final <T> T doExecute(final URI url, final HttpMethod method, final RequestCallback requestCallback,
                                    final ResponseExtractor<T> responseExtractor) throws RestClientException {
        try {
            if (kerberosEnable) {
                ClientLoginConfig loginConfig = new ClientLoginConfig(kerberosKeytab, kerberosPrincipal,
                        kerberosLoginOptions);
                Set<Principal> princ = new HashSet<Principal>(1);
                princ.add(new KerberosPrincipal(kerberosPrincipal));
                Subject sub = new Subject(false, princ, new HashSet<Object>(), new HashSet<Object>());
                LoginContext lc = new LoginContext("", sub, null, loginConfig);
                lc.login();
                Subject serviceSubject = lc.getSubject();
                return Subject.doAs(serviceSubject, new PrivilegedAction<T>() {
                    @Override
                    public T run() {
                        return IRestClient.this.doExecuteSubject(url, method, requestCallback, responseExtractor);
                    }
                });
            } else {
                return IRestClient.this.doExecuteSubject(url, method, requestCallback, responseExtractor);
            }
        } catch (Exception e) {
            throw new RestClientException("Error running rest call", e);
        }
    }

    private <T> T doExecuteSubject(URI url, HttpMethod method, RequestCallback requestCallback,
                                   ResponseExtractor<T> responseExtractor) throws RestClientException {
        return super.doExecute(url, method, requestCallback, responseExtractor);
    }

    private static class ClientLoginConfig extends Configuration {

        private final String kerberosKeytab;
        private final String kerberosPrincipal;
        private final Map<String, Object> kerberosLoginOptions;

        public ClientLoginConfig(String kerberosKeytab, String kerberosPrincipal,
                                 Map<String, Object> kerberosLoginOptions) {
            super();
            this.kerberosKeytab = kerberosKeytab;
            this.kerberosPrincipal = kerberosPrincipal;
            this.kerberosLoginOptions = kerberosLoginOptions;
        }

        @Override
        public AppConfigurationEntry[] getAppConfigurationEntry(String name) {

            Map<String, Object> options = new HashMap<String, Object>();

            // if we don't have keytab or principal only option is to rely on
            // credentials cache.
            if (!StringUtils.hasText(kerberosKeytab) || !StringUtils.hasText(kerberosPrincipal)) {
                // cache
                options.put("useTicketCache", "true");
            } else {
                // keytab
                options.put("useKeyTab", "true");
                options.put("keyTab", this.kerberosKeytab);
                options.put("principal", this.kerberosPrincipal);
                options.put("storeKey", "true");
            }
            options.put("doNotPrompt", "true");
            options.put("isInitiator", "true");

            if (kerberosLoginOptions != null) {
                options.putAll(kerberosLoginOptions);
            }

            return new AppConfigurationEntry[]{
                    new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule",
                            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options)};
        }

    }

    private static class NullCredentials implements Credentials {

        @Override
        public Principal getUserPrincipal() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

    }

    public <T> ResponseEntity<T> getForJson(String url, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers), responseType);
    }

    public <T> T getForJsonObject(String url, Class<T> responseType) {
        return getForJson(url, responseType).getBody();
    }

    public <T> ResponseEntity<T> postForJson(String url, Object t, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return exchange(url, HttpMethod.POST, new HttpEntity<Object>(t, headers), responseType);
    }

    public <T> T postForJsonObject(String url, Object t, Class<T> responseType) {
        return postForJson(url, t, responseType).getBody();
    }

    public static void main(String[] args) {
        String url = "http://into1:8088/ws/v1/cluster/apps/application_1696912277895_0648";
        IRestClient iHttpClient = new IRestClient(/* "C:\\Users\\EDZ\\merce.keytab", "merce@HADOOP.COM" */);
//		Object json = iHttpClient.getForJsonObject(url, Object.class);
//		System.out.println(JsonUtils.toJson(json));
        ResponseEntity<Object> response = iHttpClient.getForJson(url, Object.class);
        int statusCodeValue = response.getStatusCodeValue();
        System.out.println(statusCodeValue);
    }
}
