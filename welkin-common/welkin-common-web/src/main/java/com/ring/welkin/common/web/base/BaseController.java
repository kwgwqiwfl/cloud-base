package com.ring.welkin.common.web.base;

import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.persistence.service.BaseIdableService;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@Validated
public interface BaseController<ID extends Serializable, T extends Idable<ID>> {
    BaseIdableService<ID, T> getBaseIdableService();
}
