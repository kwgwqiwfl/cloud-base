package com.ring.welkin.common.persistence.entity.gene;

public interface Expireable {

    /**
     * 获取过期时间
     *
     * @return 过期时间
     */
    Long getExpiredTime();

    /**
     * 设置过期时间
     *
     * @param expiredTime 过期时间
     */
    void setExpiredTime(Long expiredTime);

}
