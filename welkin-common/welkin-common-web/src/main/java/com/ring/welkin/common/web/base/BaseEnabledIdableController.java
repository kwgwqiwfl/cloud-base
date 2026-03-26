package com.ring.welkin.common.web.base;

import com.ring.welkin.common.core.result.Response;
import com.ring.welkin.common.persistence.entity.gene.Enabled;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.persistence.service.BaseEnabledIdableService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;

@Validated
@RestController
public interface BaseEnabledIdableController<ID extends Serializable, E extends Serializable, T extends Enabled<E> & Idable<ID>> {

	BaseEnabledIdableService<ID, E, T> getBaseEnabledIdableService();

	@ApiOperation(value = "启用停用：批量修改启用状态", notes = "根据ID集合和enabled值批量修改启用停用状态")
	@PutMapping("/enabled")
	default Response<?> updateEnabled(
		@ApiParam(value = "修改参数", required = true) @RequestBody Enabled.EnabledIdsEntity<ID, E> updateParam) {
		getBaseEnabledIdableService().updateEnabled(updateParam.getIds(), updateParam.getEnabled());
		return Response.ok().build();
	}
}
