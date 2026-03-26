package com.ring.welkin.common.persistence.mybatis.type;

public interface TypeTranslater<T> extends JsonFormatTypeHandler {

    /**
     * 指定默认字符集
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 默认的字符件的分隔符
     */
    public static final String DEFAULT_SEPARATOR = ",";

    /**
     * 说明： 将实体序列化为字符串. <br>
     *
     * @param t 实体对象
     * @return 转化后的字符串
     */
    default String translate2Str(T t) {
        return toJson(t);
    }

	/**
	 * 说明： 将序列化的数据转成的字符串转化成java对象. <br>
	 *
	 * @param result 结果字符串
	 * @return java对象
	 */
	T translate2Bean(String result);

}
