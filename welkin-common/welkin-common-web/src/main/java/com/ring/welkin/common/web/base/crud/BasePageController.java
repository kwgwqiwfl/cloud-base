package com.ring.welkin.common.web.base.crud;

import com.ring.welkin.common.core.page.IPage;
import com.ring.welkin.common.core.result.Response;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import com.ring.welkin.common.web.base.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@Validated
@RestController
public interface BasePageController<ID extends Serializable, T extends Idable<ID>> extends BaseController<ID, T> {

    @ApiOperation(value = "分页查询", notes = "根据条件分页查询数据，复杂的查询条件需要构建一个ExampleQuery对象")
    @PostMapping("/page")
    default Response<IPage<T>> page(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        return Response.ok(getBaseIdableService().selectPage(ExampleQuery.builder(query)));
    }

}
