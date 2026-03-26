package com.ring.welkin.common.web.base.crud;

import com.ring.welkin.common.persistence.entity.gene.Idable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

/**
 * 基础的crud基类
 *
 * @param <ID> 主键类型
 * @param <T>  操作类型
 * @author cloud
 * @date 2020年10月30日 上午11:09:24
 * @since 0.1.3
 */
@Validated
@RestController
public interface BaseCrudController<ID extends Serializable, T extends Idable<ID>> extends BaseSaveController<ID, T>,
    BaseUpdateController<ID, T>, BaseDeleteController<ID, T>, BasePageController<ID, T>, BaseInfoController<ID, T> {
}
