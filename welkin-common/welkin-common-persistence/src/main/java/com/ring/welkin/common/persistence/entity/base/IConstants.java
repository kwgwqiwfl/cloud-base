package com.ring.welkin.common.persistence.entity.base;

import com.google.common.collect.Lists;
import com.ring.welkin.common.persistence.entity.gene.Enabled;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.persistence.entity.gene.Nameable;

import java.util.List;

/**
 * 常量定义
 *
 * @author cloud
 * @date 2021年10月13日 上午10:13:30
 */
public class IConstants {
    public static final String TENANT_ROOT = "root";
    public static final String TENANT_DEFAULT = "default";
    public static final String USER_ROOT = "root";
    public static final String USER_ADMIN = "admin";

    public static final String CLUSTER_RANDOM = "random";
    public static final String CLUSTER_QUEUE_DEFAULT = "default";
    public static final List<String> CLUSTER_QUEUES_DEFAULT = Lists.newArrayList("root.default");

    public static final String DEPLOY_MODE_CLUSTER = "cluster";
    public static final String DEPLOY_MODE_CLIENT = "client";

    public static final String PROPERTY_ID = Idable.ID;
    public static final String PROPERTY_OID = "oid";
    public static final String PROPERTY_NAME = Nameable.NAME;
    public static final String PROPERTY_LOGINID = "loginId";
    public static final String PROPERTY_USERNAME = "username";
    public static final String PROPERTY_USERID = "userId";
    public static final String PROPERTY_TENANTID = "tenantId";
    public static final String PROPERTY_CLIENTID = "clientId";
    public static final String PROPERTY_TENANTNAME = "tenantName";
    public static final String PROPERTY_ASSIGNEDTENANTID = "assignedTenantId";
    public static final String PROPERTY_ASSIGNEDTENANTNAME = "assignedTenantName";
    public static final String PROPERTY_ROLEID = "roleId";
    public static final String PROPERTY_ROLENAME = "roleName";
    public static final String PROPERTY_PERMID = "permId";
    public static final String PROPERTY_PARENTID = "parentId";
    public static final String PROPERTY_EXPIREDTIME = "expiredTime";
    public static final String PROPERTY_ENABLED = Enabled.ENABLED;
    public static final String PROPERTY_OWNER = "owner";
    public static final String PROPERTY_CREATOR = "creator";
    public static final String PROPERTY_CREATETIME = "createTime";
    public static final String PROPERTY_LASTMODIFIER = "lastModifier";
    public static final String PROPERTY_LASTMODIFIEDTIME = "lastModifiedTime";
    public static final String PROPERTY_ALIAS = "alias";
    public static final String PROPERTY_ABBR = "abbr";
    public static final String PROPERTY_STATUS = "status";
    public static final String PROPERTY_STATUSTYPE = "statusType";
    public static final String PROPERTY_VERSION = "version";
    public static final String PROPERTY_ORDER = "order";
    public static final String PROPERTY_RESOURCEID = "resourceId";
    public static final String PROPERTY_RESTYPE = "resType";
    public static final String PROPERTY_SCHEMAID = "schemaId";
    public static final String PROPERTY_SCHEMANAME = "schemaName";

    public static final String PROPERTY_SCHEMAINFO = "schemaInfo";
    public static final String PROPERTY_SCHEMVERSION= "schemaVersion";
    public static final String PROPERTY_DATASETID = "datasetId";
    public static final String PROPERTY_DATASETNAME = "datasetName";
    public static final String PROPERTY_FLOWID = "flowId";
    public static final String PROPERTY_FLOWNAME = "flowName";
    public static final String PROPERTY_FLOWTYPE = "flowType";
    public static final String PROPERTY_DATASOURCEID = "datasourceId";
    public static final String PROPERTY_DATASOURCENAME = "datasourceName";
    public static final String PROPERTY_FILESETID = "filesetId";
    public static final String PROPERTY_FILESETNAME = "filesetName";
    public static final String PROPERTY_EXECUTIONID = "executionId";
    public static final String PROPERTY_JOBID = "jobId";
    public static final String PROPERTY_FLOWSCHEDULERID = "flowSchedulerId";
    public static final String PROPERTY_SCHEDULERID = "schedulerId";
    public static final String PROPERTY_PATH = "path";
    public static final String PROPERTY_CODE = "code";
    public static final String PROPERTY_TYPE = "type";
    public static final String PROPERTY_APPROVERID = "approverId";
    public static final String PROPERTY_APPROVERNAME = "approverName";
    public static final String PROPERTY_PLATFORM = "platform";
    public static final String PROPERTY_MESSAGE = "message";
    public static final String PROPERTY_MODULE = "module";
    public static final String PROPERTY_SOURCEID = "sourceId";
    public static final String PROPERTY_TARGETID = "targetId";
    public static final String PROPERTY_ORGANIZATIONID = "organizationId";
    public static final String PROPERTY_PROCESSID = "processId";
    public static final String PROPERTY_APPROVAL_NODEID = "approvalNodeId";
    public static final String PROPERTY_CLUSTERID = "clusterId";
    public static final String PROPERTY_CLUSTERNAME = "clusterName";
    public static final String PROPERTY_QUEUE = "queue";
    public static final String PROPERTY_QUEUES = "queues";
    public static final String PROPERTY_PROXYUSER = "proxyUser";
    public static final String PROPERTY_PRINCIPAL = "principal";
    public static final String PROPERTY_POLICYID = "policyId";
    public static final String PROPERTY_GRANTSCOPE = "grantScope";
    public static final String PROPERTY_SUBJECTID = "subjectId";
    public static final String PROPERTY_RULEID = "ruleId";
    public static final String PROPERTY_SCOPETYPE = "scopeType";
    public static final String PROPERTY_RULETYPE = "ruleType";
    public static final String PROPERTY_ACCESSTYPE = "accessType";

    public static final String PROPERTY_ISSHARE = "isShare";
    public static final String PROPERTY_ISDEFAULT = "isDefault";
    public static final String PROPERTY_ISHIDE = "isHide";
    public static final String PROPERTY_EXPIRED = "expired";

    public static final String PROPERTY_LASTMENU = "lastMenu";
    public static final String PROPERTY_ROUTE = "route";
    public static final String AGG_COUNT_VALUE = "countValue";
    public static final String AGG_AVG_VALUE = "avgValue";
    public static final String AGG_MIN_VALUE = "minValue";
    public static final String AGG_MAX_VALUE = "maxValue";
    public static final String AGG_SUM_VALUE = "sumValue";

    public static final String PROPERTY_STORAGE = "storage";
    public static final String PROPERTY_SERVICENAME = "serviceName";

    public static final int module = 1;

    public static final int menu = 2;

    public static final int button = 3;
    public static final int LASTMENU = 1;
    public static final int NOT_LASTMENU = 0;
}
