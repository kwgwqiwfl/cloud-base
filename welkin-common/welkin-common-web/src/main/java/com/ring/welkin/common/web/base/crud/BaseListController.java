package com.ring.welkin.common.web.base.crud;

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
import java.util.List;

@Validated
@RestController
public interface BaseListController<ID extends Serializable, T extends Idable<ID>> extends BaseController<ID, T> {

    @ApiOperation(value = "列表查询", notes = "根据条件查询数据列表，不分页，查询所有复合条件的数据，最多支持10000条数据查询，避免内存不够用，一般支持数据量有限不需分页一次查询所有的场景，复杂的查询条件需要构建一个ExampleQuery对象")
    @PostMapping("/list")
    default Response<List<T>> list(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        return Response.ok(
                getBaseIdableService().selectList(ExampleQuery.builder(query).unpaged().limit(10000)));
    }

}
