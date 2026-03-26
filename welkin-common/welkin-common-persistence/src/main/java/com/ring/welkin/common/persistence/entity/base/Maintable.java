package com.ring.welkin.common.persistence.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ring.welkin.common.persistence.mybatis.genid.UuidGenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class Maintable extends CommonEntity<String> implements Cloneable {
    private static final long serialVersionUID = -2286814072741496025L;
    // 9999-12-31
    public static final Long MAX_DATE_TIME = 253402214400L;

    @ApiModelProperty(value = "主键", required = false, accessMode = AccessMode.READ_ONLY)
    @Comment("主键")
    @Id
    @KeySql(genId = UuidGenId.class)
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String id;

    @ApiModelProperty(value = "版本号", required = false, accessMode = AccessMode.READ_ONLY)
    @Comment("版本号")
    @Column(length = 11)
    @ColumnType(jdbcType = JdbcType.INTEGER)
    protected Integer version;

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("can't clone " + getClass().getName(), e);
        }
    }
}
