package com.ring.welkin.common.web.base;

import com.ring.welkin.common.core.result.Response;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.persistence.entity.gene.Tagsable;
import com.ring.welkin.common.persistence.service.BaseTagsableIdableService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Validated
@RestController
public interface BaseTagsableIdableController<ID extends Serializable, T extends Idable<ID> & Tagsable> {

	BaseTagsableIdableService<ID, T> getBaseTagsableIdableService();

    @ApiOperation(value = "标签：批量修改标签", notes = "根据ID批量修改标签，支持批量")
    @PutMapping("/updateTags")
    default Response<?> updateTags(
        @ApiParam(value = "修改参数", required = true) @RequestBody @Valid Tagsable.TagsIdsEntity<ID> updateParam) {
        getBaseTagsableIdableService().updateTagsByIds(updateParam.getIds(), updateParam.getTags());
        return Response.ok().build();
    }

    @ApiOperation(value = "标签：批量删除标签", notes = "根据ID批量删除标签")
    @DeleteMapping("/clearTags")
    default Response<?> clearTags(@ApiParam(value = "记录ID", required = true) @RequestBody @NotEmpty List<ID> ids) {
        getBaseTagsableIdableService().clearTagsByIds(ids);
        return Response.ok().build();
    }

}
