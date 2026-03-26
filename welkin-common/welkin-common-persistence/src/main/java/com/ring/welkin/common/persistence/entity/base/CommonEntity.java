package com.ring.welkin.common.persistence.entity.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ring.welkin.common.core.jackson.deserializer.DateJsonDeserializer;
import com.ring.welkin.common.core.saas.SaasContext;
import com.ring.welkin.common.persistence.entity.gene.Enabled;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.persistence.entity.gene.Nameable;
import com.ring.welkin.common.persistence.entity.preprocess.PreEntity;
import com.ring.welkin.common.persistence.mybatis.type.routing.DateTypeRoutingHandler;
import com.ring.welkin.common.validation.constraints.MustIn;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" }, ignoreUnknown = true)
public abstract class CommonEntity<ID extends Serializable> extends OwnerEntity<ID>
		implements Idable<ID>, Enabled<Integer>, Nameable, PreEntity, Serializable {
	private static final long serialVersionUID = 4394421573081538612L;

	@ApiModelProperty(value = "名称", required = true)
	@Comment("名称")
	@Column(length = 255)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@NotBlank
	protected String name;

	@ApiModelProperty(value = "是否启用：0-未启用，1-启用，默认0", allowableValues = "0,1", required = false)
	@Comment("是否启用：0-未启用，1-启用，默认0")
	@Column(length = 1)
	@ColumnType(jdbcType = JdbcType.INTEGER)
	@ColumnDefault("1")
	@MustIn(value = {"0", "1"}, message = "Field [enabled] value must be in {values}")
	protected Integer enabled;

	@ApiModelProperty(value = "创建人", required = false, accessMode = AccessMode.READ_ONLY)
	@Comment("创建人")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	protected String creator;

	@ApiModelProperty(value = "创建时间", required = false, accessMode = AccessMode.READ_ONLY)
	@Comment("创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@ColumnType(jdbcType = JdbcType.TIMESTAMP, typeHandler = DateTypeRoutingHandler.class)
	@JsonDeserialize(using = DateJsonDeserializer.class)
	protected Date createTime;

	@ApiModelProperty(value = "修改人", required = false, accessMode = AccessMode.READ_ONLY)
	@Comment("修改人")
	@Column(length = 50)
	@ColumnType(jdbcType = JdbcType.VARCHAR)
	protected String lastModifier;

	@ApiModelProperty(value = "修改时间", required = false, accessMode = AccessMode.READ_ONLY)
	@Comment("修改时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnType(jdbcType = JdbcType.TIMESTAMP, typeHandler = DateTypeRoutingHandler.class)
	@JsonDeserialize(using = DateJsonDeserializer.class)
	protected Date lastModifiedTime;

	@Override
	public void preInsert() {
		String currentTenantId = SaasContext.getCurrentTenantId();
		if (StringUtils.isEmpty(this.tenantId) && StringUtils.isNotEmpty(currentTenantId)) {
			this.setTenantId(currentTenantId);
		}

		Date createTime = new Date();
		this.setCreateTime(createTime);
		this.setLastModifiedTime(createTime);

		String currentUserId = SaasContext.getCurrentUserId();
		if (StringUtils.isNotEmpty(currentUserId)) {
			if (StringUtils.isEmpty(owner)) {
				this.setOwner(currentUserId);
			}
			if (StringUtils.isEmpty(creator)) {
				this.setCreator(SaasContext.getCurrentUsername());
			}
			if (StringUtils.isEmpty(lastModifier)) {
				this.setLastModifier(SaasContext.getCurrentUsername());
			}
		}
	}

	@Override
	public void preUpdate() {
		this.setLastModifiedTime(new Date());
		String currentUserId = SaasContext.getCurrentUserId();
		if (StringUtils.isNotEmpty(currentUserId)) {
			this.setLastModifier(SaasContext.getCurrentUsername());
		}
	}

	public <T extends CommonEntity<?>> void copyOwnerProperties(T other) {
		this.owner = other.getOwner();
		this.tenantId = other.getTenantId();
		this.creator = other.getCreator();
		this.lastModifier = other.getLastModifier();
	}

	public <T extends CommonEntity<?>> void clearOwnerProperties() {
		this.owner = null;
		this.tenantId = null;
		this.creator = null;
		this.createTime = null;
		this.lastModifier = null;
		this.lastModifiedTime = null;
	}
}