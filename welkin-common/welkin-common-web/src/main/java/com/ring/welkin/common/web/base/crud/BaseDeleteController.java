package com.ring.welkin.common.web.base.crud;

import com.ring.welkin.common.core.result.Response;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.web.base.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Validated
@RestController
public interface BaseDeleteController<ID extends Serializable, T extends Idable<ID>> extends BaseController<ID, T> {

    @ApiOperation(value = "删除记录", notes = "根据ID批量删除记录")
    @DeleteMapping
    default Response<?> deleteByIds(
            @ApiParam(value = "记录ID集合", required = true) @RequestBody @NotEmpty List<ID> ids) {
    	getBaseIdableService().deleteByIds(ids);
        return Response.ok().build();
    }

}
