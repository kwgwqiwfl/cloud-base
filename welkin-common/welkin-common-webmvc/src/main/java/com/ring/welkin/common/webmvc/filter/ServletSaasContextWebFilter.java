package com.ring.welkin.common.webmvc.filter;

import com.ring.welkin.common.core.saas.SaasContext;
import com.ring.welkin.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * SaasContext 拦截初始化
 *
 * @author cloud
 * @date 2019年12月17日 上午10:37:51
 */
@Slf4j
@Component
public class ServletSaasContextWebFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            String saasContextHeader = request.getHeader(SaasContext.SAAS_CONTEXT_KEY);
            String userId = request.getHeader("userId");
            if (StringUtils.isNotEmpty(saasContextHeader)) {
                String decode = null;
                try {
                    decode = URLDecoder.decode(saasContextHeader, "UTF-8");
                    SaasContext saasContext = JsonUtils.fromJson(decode, SaasContext.class);
                    if (saasContext != null) {
                        SaasContext.setCurrentSaasContext(saasContext);
                    }
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage(), e);
                }
            } else if (StringUtils.isNotEmpty(userId)) {
                String admin = request.getHeader("admin");
                SaasContext.initSaasContext(//
                        request.getHeader("tenantId"), //
                        request.getHeader("tenantName"), //
                        userId, //
                        request.getHeader("username"), //
                        StringUtils.isNotEmpty(admin) && "true".equalsIgnoreCase(admin), //
                        request.getHeader("userType"), //
                        request.getHeader("organizationId"),
                        request.getHeader("safetyLevelId")
                );
            }
            log.trace("fix current SaasContext :{}", SaasContext.getCurrentSaasContext());
            chain.doFilter(request, response);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            SaasContext.clear();
        }
    }
}
