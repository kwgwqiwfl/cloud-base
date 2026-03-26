package com.ring.welkin.common.core.http;

import java.util.Map;

import lombok.Data;

/**
 * kerberos参数封装类，kerberos认证所需参数包装，可作为参数传递
 * 
 * @author cloud
 * @date 2023年11月6日 上午10:10:29
 */
@Data
public class KerberosParameter {
	/**
	 * 认证主体，如：merce@HADOOP.COM，merce/admin@HADOOP.COM
	 */
	private String principal;

	/**
	 * keytab路径，如：/etc/security/keytabs/merce.teytab
	 */
	private String keytab;

	/**
	 * krb5Conf路径，如：/etc/krb5.conf
	 */
	private String krb5Conf;

	/**
	 * 代理用户名称，如果proxyUser不为空，则使用superuser（principal凭证主体的用户）代理该用户进行请求，请求参数是
	 * "doas={proxyUser}"
	 */
	private String proxyUser;

	/**
	 * kerberos其他登录信息
	 */
	private Map<String, Object> loginOptions;

	public KerberosParameter(String principal, String keytab, String krb5Conf) {
		this.principal = principal;
		this.keytab = keytab;
		this.krb5Conf = krb5Conf;
	}

	public KerberosParameter(String principal, String keytab, String krb5Conf, String proxyUser) {
		this(principal, keytab, krb5Conf);
		this.proxyUser = proxyUser;
	}
}
