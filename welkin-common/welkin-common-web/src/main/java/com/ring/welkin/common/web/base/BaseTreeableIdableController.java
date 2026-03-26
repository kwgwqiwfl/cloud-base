package com.ring.welkin.common.web.base;

import com.ring.welkin.common.core.result.Response;
import com.ring.welkin.common.persistence.service.tree.id.TreeIdable;
import com.ring.welkin.common.persistence.service.tree.id.TreeIdableExampleQueryService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Validated
@RestController
public interface BaseTreeableIdableController<ID extends Serializable, T extends TreeIdable<ID, T>> {

    TreeIdableExampleQueryService<ID, T> getTreeIdableExampleQueryService();

    @ApiOperation(value = "树操作：批量移动节点", notes = "批量移动节点到指定父节点")
    @PostMapping("/move/{parentId}")
    default Response<Integer> move(
        @ApiParam(value = "移动的目标父节点ID", required = true) @PathVariable("parentId") ID parentId,
        @ApiParam(value = "移动的节点ID集合", required = true) @RequestBody @NotEmpty List<ID> moveIds) {
        return Response.ok(getTreeIdableExampleQueryService().move(parentId, moveIds));
    }

    @ApiOperation(value = "树操作：查询子节点列表", notes = "根据父节点ID查询子节点列表并排序")
    @GetMapping("/children/{parentId}")
    default Response<List<T>> children(
        @ApiParam(value = "父节点ID", required = true) @PathVariable("parentId") ID parentId) {
        return Response.ok(getTreeIdableExampleQueryService().selectByParentId(parentId));
    }
}
