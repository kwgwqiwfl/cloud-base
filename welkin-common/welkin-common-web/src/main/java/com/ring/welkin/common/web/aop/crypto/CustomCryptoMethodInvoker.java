package com.ring.welkin.common.web.aop.crypto;

import com.ring.welkin.common.core.crypto.delegater.CryptorDelegater;
import com.ring.welkin.common.core.crypto.method.AbstractCryptoMethodInvoker;
import com.ring.welkin.common.core.crypto.method.CryptoConfig;
import com.ring.welkin.common.core.page.IPage;
import com.ring.welkin.common.core.result.Response;
import com.ring.welkin.common.persistence.jpa.page.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 自定义的方法级别参数和返回值加解密调用器
 *
 * @author cloud
 * @date 2019年12月6日 下午3:44:21
 */
@SuppressWarnings("deprecation")
@Component
public class CustomCryptoMethodInvoker extends AbstractCryptoMethodInvoker {

    public CustomCryptoMethodInvoker(final CryptoConfig config, final CryptorDelegater cryptorDelegater) {
        super(config.getSecretKey(), cryptorDelegater);
    }

    @Override
    public Object afterHandleResult(Object result, Object obj) {
        if (result instanceof String) {
            return obj;
        }
        return result;
    }

    @SuppressWarnings({"unchecked", "resource"})
    @Override
    public Object beforeHandleResult(Object result) {
        // 拿到返回报文对象的能够处理的层级
        Object obj = null;
        if (result instanceof Response) {
            Response<Object> responseObj = (Response<Object>) result;
            obj = responseObj.getContent();
        } else if (result instanceof ResponseEntity) {
            ResponseEntity<Object> responseObj = (ResponseEntity<Object>) result;
            obj = responseObj.getBody();
        } else {
            obj = result;
        }

        if (obj instanceof Page) {
            Page<Object> page = (Page<Object>) obj;
            obj = page.getContent();
        }

        if (obj instanceof IPage) {
            IPage<Object> page = (IPage<Object>) obj;
            obj = page.getList();
        }

        if (obj instanceof com.github.pagehelper.Page) {
            com.github.pagehelper.Page<Object> page = (com.github.pagehelper.Page<Object>) obj;
            obj = page.getResult();
        }
        return obj;
    }

}
