package com.ring.welkin.common.persistence.mybatis.enhance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;

/**
 * EntityColumn属性增强，添加查询自动忽略列属性
 *
 * @author cloud
 * @date 2021年7月9日 下午3:16:41
 */
@Setter
@Getter
@NoArgsConstructor
public class TkEntityColumn extends EntityColumn {

    public TkEntityColumn(EntityTable table) {
        super(table);
    }

    /**
     * 查询忽略属性
     */
    private boolean ignoreByQuery;

}
