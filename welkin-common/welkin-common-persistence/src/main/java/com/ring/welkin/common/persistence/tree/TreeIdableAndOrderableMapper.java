package com.ring.welkin.common.persistence.tree;

import com.ring.welkin.common.persistence.entity.gene.Orderable;
import com.ring.welkin.common.persistence.service.tree.id.TreeIdable;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.io.Serializable;
import java.util.List;

/**
 * 树数据查询和排序查询公共方法提取
 *
 * @param <ID> 主键类型
 * @param <T>  实体类型
 * @author cloud
 * @date 2022年9月15日 上午11:45:44
 */
@RegisterMapper
public interface TreeIdableAndOrderableMapper<T extends TreeIdable<ID, T> & Orderable<T>, ID extends Serializable> {

    /**
     * 根据parentId查询子目录的数据列表
     *
     * @param parentId 父目录ID
     * @return 目录下数据列表
     */
    @SelectProvider(type = TreeIdableAndOrderableProvider.class, method = "dynamicSQL")
    List<T> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 根据父目录ID查询该目录下最大的排序序号
     *
     * @param parentId 父目录ID
     * @return 最大的序号
     */
    @SelectProvider(type = TreeIdableAndOrderableProvider.class, method = "dynamicSQL")
    Integer selectMaxOrderByParentId(@Param("parentId") Long parentId);

    /**
     * 查询表中最大根目录的排序序号
     *
     * @return 最大的根目录的序号
     */
    @SelectProvider(type = TreeIdableAndOrderableProvider.class, method = "dynamicSQL")
    Integer selectMaxOrderOfRoots();

}
