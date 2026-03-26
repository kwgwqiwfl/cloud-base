package com.ring.welkin.common.core.saas;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class SaasContext extends DefaultUserInfo implements Serializable {
	private static final long serialVersionUID = 7238846400682679965L;
	public static final String SAAS_CONTEXT_KEY = "SaasContext";

	private String host;
	private String internalToken; // user from INTERNAL-TOKEN
	private String privilegeUser;

	private static ThreadLocal<SaasContext> saasContextThreadLocal = new InheritableThreadLocal<>();

	public static SaasContext getCurrentSaasContext() {
		SaasContext ctx = saasContextThreadLocal.get();
		if (ctx == null) {
			saasContextThreadLocal.set(ctx = new SaasContext());
		}
		return ctx;
	}

	public static void setCurrentSaasContext(SaasContext saasContext) {
		saasContextThreadLocal.set(saasContext);
	}

	public static String getCurrentClienId() {
		return getCurrentSaasContext().getClientId();
	}

	public static String getCurrentTenantId() {
		return getCurrentSaasContext().getTenantId();
	}

	public static String getCurrentTenantName() {
		return getCurrentSaasContext().getTenantName();
	}

	public static boolean getCurrentTenantIsRoot() {
		return "root".equalsIgnoreCase(getCurrentTenantName());
	}

	public static String getCurrentUserId() {
		return getCurrentSaasContext().getUserId();
	}

	public static String getCurrentUsername() {
		return getCurrentSaasContext().getUsername();
	}

	public static boolean getCurrentUserIsRoot() {
		return "root".equalsIgnoreCase(getCurrentUsername());
	}

	public static boolean getCurrentUserIsAdmin() {
		return getCurrentSaasContext().isAdmin();
	}

	public static UserType getCurrentUserType() {
		return getCurrentSaasContext().getUserType();
	}

	public static String getCurrentOrganizationId() {
		return getCurrentSaasContext().getOrganizationId();
	}

	public static Long getCurrentSafetyLevelId() {
		return getCurrentSaasContext().getSafetyLevelId();
	}

	public static Map<String, Object> current() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tenantId", getCurrentTenantId());
		map.put("owner", getCurrentUserId());
		map.put("admin", getCurrentUserIsAdmin());
		return map;
	}

	public static void clear() {
		SaasContext context = getCurrentSaasContext();
		context.setHost(null);
		context.setClientId(null);
		context.setTenantId(null);
		context.setTenantName(null);
		context.setUserId(null);
		context.setUsername(null);
	}

	public static SaasContext initSaasContext(String host, String clientId, String tenantId, String tenantName,
											  String userId, String username, boolean admin, UserType userType, String internalToken,
											  String privilegeUser, String organizationId, String safetyLevelId) {
		return initSaasContext(host, clientId, tenantId, tenantName, userId, username, admin, userType, internalToken,
				privilegeUser, organizationId,
				StringUtils.isNotBlank(safetyLevelId) && !"null".equalsIgnoreCase(safetyLevelId)
						? Long.parseLong(safetyLevelId)
						: null);
	}

	public static SaasContext initSaasContext(String host, String clientId, String tenantId, String tenantName,
											  String userId, String username, boolean admin, UserType userType, String internalToken,
											  String privilegeUser, String organizationId, Long safetyLevelId) {
		SaasContext ctx = getCurrentSaasContext();
		ctx.setHost(host);
		ctx.setClientId(clientId);
		ctx.setTenantId(tenantId);
		ctx.setTenantName(tenantName);
		ctx.setUserId(userId);
		ctx.setUsername(username);
		ctx.setAdmin(admin);
		ctx.setUserType(userType);
		ctx.setInternalToken(internalToken);
		ctx.setPrivilegeUser(privilegeUser);
		ctx.setOrganizationId(organizationId);
		if (safetyLevelId != null) {
			ctx.setSafetyLevelId(safetyLevelId);
		}
		return ctx;
	}

	public static SaasContext initSaasContext(String tenantId, String tenantName, String userId, String username,
											  boolean admin, String userType, String organizationId, String safetyLevelId) {
		return initSaasContext(tenantId, tenantName, userId, username, admin,
				StringUtils.isEmpty(userType) ? UserType.Manager : UserType.valueOf(userType), organizationId,
				safetyLevelId);
	}

	public static SaasContext initSaasContext(String tenantId, String tenantName, String userId, String username,
											  boolean admin, UserType userType, String organizationId, String safetyLevelId) {
		SaasContext ctx = getCurrentSaasContext();
		return initSaasContext(ctx.getHost(), ctx.getClientId(), tenantId, tenantName, userId, username, admin,
				userType, ctx.getInternalToken(), ctx.getPrivilegeUser(), organizationId, safetyLevelId);
	}

	public static SaasContext initSaasContext(String tenantId, String userId, boolean isAdmin) {
		SaasContext ctx = getCurrentSaasContext();
		ctx.setTenantId(tenantId);
		ctx.setUserId(userId);
		ctx.setAdmin(isAdmin);
		return ctx;
	}

	public static SaasContext initSaasContext(String tenantId, String userId) {
		return initSaasContext(tenantId, userId, false);
	}

	public static SaasContext initSaasContext(UserInfo userInfo) {
		if (userInfo != null) {
			return initSaasContext("", userInfo.getClientId(), userInfo.getTenantId(), userInfo.getTenantName(),
					userInfo.getUserId(), userInfo.getUsername(), userInfo.isAdmin(), userInfo.getUserType(), "", "",
					userInfo.getOrganizationId(), userInfo.getSafetyLevelId());
		}
		return getCurrentSaasContext();
	}

	/**
	 * 如果操作执行中途需要临时改变上下文信息，则需将当前上下文信息暂存，等操作完成后再从该对象中恢复
	 */
	private DefaultUserInfo staging;

	public static DefaultUserInfo toUserInfo() {
		return DefaultUserInfo.from(getCurrentSaasContext());
	}

	/**
	 * 暂存上下文到暂存区中
	 */
	public static SaasContext staging() {
		SaasContext ctx = getCurrentSaasContext();
		ctx.setStaging(toUserInfo());
		return ctx;
	}

	// 从暂存区中恢复

	/**
	 * 从暂存区中将原来的上下文恢复
	 *
	 * @return 恢复后的上下文信息
	 */
	public static SaasContext restore() {
		SaasContext ctx = getCurrentSaasContext();
		UserInfo staging = ctx.getStaging();
		if (staging != null) {
			return initSaasContext(staging);
		}
		return ctx;
	}
}
