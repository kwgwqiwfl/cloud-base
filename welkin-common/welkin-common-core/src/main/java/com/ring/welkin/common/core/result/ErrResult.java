package com.ring.welkin.common.core.result;

/**
 * 错误信息
 *
 * @author: cloud
 * @date: 2019年4月23日 下午2:53:43
 */
public interface ErrResult {

    /**
     * 获取消息状态码
     *
     * @return 消息状态码
     */
    Integer getStatus();

    /**
     * 获取消息
     *
     * @return 消息内容
     */
    String getMessage();

}
