package com.ring.welkin.common.web.base.crud;

import com.ring.welkin.common.core.result.Response;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.web.base.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@Validated
@RestController
public interface BaseDeleteByIdController<ID extends Serializable, T extends Idable<ID>> extends BaseController<ID, T> {

    @ApiOperation(value = "根据ID删除", notes = "根据ID删除记录")
    @DeleteMapping(value = "/{id}")
    default Response<?> deleteById(@ApiParam(value = "记录ID集合", required = true) @PathVariable("id") ID id) {
    	getBaseIdableService().deleteById(id);
        return Response.ok().build();
    }
}
