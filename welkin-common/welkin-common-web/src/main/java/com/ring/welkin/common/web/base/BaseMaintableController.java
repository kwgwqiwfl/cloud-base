package com.ring.welkin.common.web.base;

import com.ring.welkin.common.persistence.entity.base.BaseMaintableService;
import com.ring.welkin.common.persistence.entity.base.Maintable;
import com.ring.welkin.common.persistence.service.BaseIdableService;
import com.ring.welkin.common.web.base.crud.BaseCrudController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * @deprecated 该类已弃用，参考：{@link BaseCrudController}
 */
@Validated
@RestController
@Deprecated
public interface BaseMaintableController<T extends Maintable> extends BaseIdableController<String, T> {

    BaseMaintableService<T> getBaseMaintableService();

    @Override
    default BaseIdableService<String, T> getBaseIdableService() {
        return getBaseMaintableService();
    }
}
