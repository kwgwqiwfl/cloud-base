package com.ring.welkin.common.core.saas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultUserInfo implements UserInfo, Serializable {
	private static final long serialVersionUID = 7244337646406020531L;

	private String clientId;
	private String version;

	private String tenantId;
	private String tenantName;

	private String userId;
	private String username;
	private boolean admin;
	private UserType userType;

	private String organizationId;

	private Long safetyLevelId;

	public static DefaultUserInfo from(UserInfo other) {
		return new DefaultUserInfo(other.getClientId(), other.getVersion(), other.getTenantId(), other.getTenantName(),
				other.getUserId(), other.getUsername(), other.isAdmin(), other.getUserType(), other.getOrganizationId(),
				other.getSafetyLevelId());
	}
}
