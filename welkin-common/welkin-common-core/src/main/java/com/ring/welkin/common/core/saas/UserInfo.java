package com.ring.welkin.common.core.saas;

public interface UserInfo {

    /**
     * 获取客户端ID
     */
    String getClientId();

    /**
     * 获取租户ID
     */
    String getTenantId();

    /**
     * 获取租户名称
     */
    String getTenantName();

    /**
     * 获取用户ID
     */
    UserType getUserType();

    /**
     * 获取用户ID
     */
    String getUserId();

    /**
     * 获取用户名称
     */
    String getUsername();

    /**
     * 用户是否是超级管理员
     */
    boolean isAdmin();

    /**
     * 组织机构ID
     *
     * @return 组织结构ID
     */
    String getOrganizationId();

    Long getSafetyLevelId();

    /**
     * 获取系统版本号
     */
    String getVersion();

    public enum UserType {
        Manager, @Deprecated Customer;
    }

}
