package com.ring.welkin.common.persistence.service.tree.path;

/**
 * 包含 path 属性的对象，用于树形结构的数据根据层级编码拼接的全路径进行多层子节点匹配检索
 *
 * @author cloud
 * @date 2021年11月18日 上午10:03:02
 */
public interface Pathable {

    /**
     * 路劲层级分隔符
     */
    public static final String PATH_SEPARATOR = ";";

    /**
     * 获取名称，名称用于path拼接
     *
     * @return 名称
     */
    String getName();

    /**
     * 设置全路径
     *
     * @param path 全路径
     */
    void setPath(String path);

    /**
     * 获取全路径
     *
     * @return 全路径
     */
    String getPath();

}
