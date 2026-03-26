package com.ring.welkin.common.core.http;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * hdfs客户端kerberos参数封装类
 * 
 * @author cloud
 * @date 2023年11月6日 上午10:11:38
 */
@Data
@AllArgsConstructor
public class KerberosClientParameter {

	private String hdfsUrl;

	private KerberosParameter kerberosParameter;

	public KerberosClientParameter(String hdfsUrl) {
		this.hdfsUrl = hdfsUrl;
	}

}
