package com.ring.welkin.common.persistence.tree;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ring.welkin.common.persistence.entity.base.BaseEntity;
import com.ring.welkin.common.persistence.entity.gene.Orderable;
import com.ring.welkin.common.persistence.service.tree.path.TreeIdablePathable;
import com.ring.welkin.common.utils.ICollections;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class BasePathableEntity<T extends BasePathableEntity<T>> extends BaseEntity
        implements TreeIdablePathable<Long, T>, Orderable<T> {
    private static final long serialVersionUID = -8423295285599686952L;

    @ApiModelProperty(value = "递归路径", required = false, hidden = true)
    @Comment("递归路径")
    @Column(length = 255, nullable = false)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String path;

    @ApiModelProperty(value = "父节点ID", required = false)
    @Comment("父节点ID")
    @Column(length = 20)
    @ColumnType(jdbcType = JdbcType.BIGINT)
    @ColumnDefault("0")
    protected Long parentId;

    @ApiModelProperty(value = "父节点name", required = false)
    @Comment("父节点name")
    @Column(length = 255)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String parentName;

    @ApiModelProperty(value = "排序序号", required = false, accessMode = AccessMode.READ_ONLY)
    @Comment("排序序号")
    @Column(name = "ord", length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    @ColumnDefault("1")
    protected Integer order;

    @ApiModelProperty("子级节点列表")
    @Transient
    protected List<T> children = new ArrayList<>();

    @ApiModelProperty("关联数据数量")
    @Transient
    protected Integer recordCount = 0;

    // 按正序排列
    @Override
    public List<T> getChildren() {
        if (ICollections.hasElements(children)) {
            Collections.sort(children);
        }
        return children;
    }
}
