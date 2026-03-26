package com.ring.welkin.common.web.base;

import com.ring.welkin.common.persistence.entity.base.BaseEntity;
import com.ring.welkin.common.persistence.entity.base.BaseEntityService;
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
public interface BaseEntityController<T extends BaseEntity> extends BaseIdableController<Long, T> {

    BaseEntityService<T> getBaseEntityService();

    @Override
    default BaseIdableService<Long, T> getBaseIdableService() {
        return getBaseEntityService();
    }

}
