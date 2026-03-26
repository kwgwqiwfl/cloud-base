package com.ring.welkin.common.web.base;

import com.ring.welkin.common.core.result.Response;
import com.ring.welkin.common.core.saas.SaasContext;
import com.ring.welkin.common.persistence.service.tree.id.TreeIdable;
import com.ring.welkin.common.persistence.tree.TreeableQueryService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;

@Validated
public interface TreeQueryController<ID extends Serializable, T extends TreeIdable<ID, T>> {

    TreeableQueryService<ID, T> getTreeableQueryService();

    @ApiOperation(value = "全部目录树", notes = "查询租户下所有的目录树")
    @PostMapping("/tree/all")
    default Response<List<T>> all(@ApiParam(value = "查询条件", required = true) @RequestBody ExampleQuery query) {
        return Response.ok(getTreeableQueryService().selectAllTree(SaasContext.getCurrentTenantId(), query));
    }
}
