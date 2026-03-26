package com.ring.welkin.common.web.base;

import com.ring.welkin.common.core.result.Response;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.persistence.entity.gene.Orderable;
import com.ring.welkin.common.persistence.service.BaseOrderableIdableService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Validated
@RestController
public interface BaseOrderableIdableController<ID extends Serializable, T extends Orderable<T> & Idable<ID>> {

    BaseOrderableIdableService<ID, T> getBaseOrderableIdableService();

    @ApiOperation(value = "排序：全量排序", notes = "根据传入的记录ID重新从1排序，按照上传的ID顺序定义序号")
    @PostMapping("/sort")
    default Response<Integer> sort(
            @ApiParam(value = "排序后的ID列表", required = true) @RequestBody @NotEmpty List<ID> sortIds) {
        return Response.ok(getBaseOrderableIdableService().sortAll(sortIds));
    }

    @ApiOperation(value = "排序：指定记录排序", notes = "指定若干条排好序的记录，按照新顺序交换对应记录序号为正确的值")
    @PostMapping("/sortByRecords")
    default Response<Integer> sortByRecords(
            @ApiParam(value = "重新调整位置的记录列表，按照上传的记录列表重新排序,只包含id，order属性即可", required = true) @RequestBody @NotEmpty List<T> list) {
        return Response.ok(getBaseOrderableIdableService().sortByRecords(list));
    }

    @ApiOperation(value = "排序：按步长移动", notes = "一条记录向前或者向后移动指定步数，可用于前移/后移/跨页移动等")
    @PostMapping("/sortByStep/{id}")
    default Response<Integer> sortByStep(@ApiParam(value = "移动记录ID", required = true) @PathVariable("id") ID id,
                                         @ApiParam(value = "是否逆序移动:true-逆序，false-正序，默认正序", required = false, defaultValue = "false") @RequestParam(defaultValue = "false") boolean reverse,
                                         @ApiParam(value = "是否向后移动:true-向后，false-向前，默认向前", required = false, defaultValue = "false") @RequestParam(defaultValue = "false") boolean backward,
                                         @ApiParam(value = "移动的步数", required = false, defaultValue = "1") @RequestParam(defaultValue = "1") int step,
                                         @ApiParam(value = "查询到列表的条件", required = true) @RequestBody ExampleQuery query) {
        return Response.ok(getBaseOrderableIdableService().sortByStep(id, reverse, backward, step, query));
    }
}
