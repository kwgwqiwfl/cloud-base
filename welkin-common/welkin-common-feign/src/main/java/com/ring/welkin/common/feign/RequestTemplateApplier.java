package com.ring.welkin.common.feign;

import feign.RequestTemplate;

public interface RequestTemplateApplier {
    void apply(RequestTemplate requestTemplate);
}
