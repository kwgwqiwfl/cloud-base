package com.ring.welkin.common.persistence.entity.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.persistence.mybatis.genid.UuidGenId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.annotations.Comment;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.*;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class UuidEntity implements Idable<String> {
    private static final long serialVersionUID = 8199030533526416114L;

    @ApiModelProperty(value = "主键", required = false, accessMode = AccessMode.READ_ONLY)
    @Comment("主键")
    @Id
    @KeySql(genId = UuidGenId.class)
    @Column(length = 50)
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    protected String id;
}
