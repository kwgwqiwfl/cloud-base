package com.ring.welkin.common.datasource.lookup;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class LookupContext {
    private static final ThreadLocal<String> CONTEXTHOLDER = new ThreadLocal<>();

    public static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static final String BOUND_PREFIX = "BOUND_";

    public static void set(String lookupKey, boolean bound) {
        if (bound) {
            lookupKey = wrapedLookupKey(lookupKey);
        }
		CONTEXTHOLDER.set(lookupKey);
		log.debug("routing to datasource: lookupKey=" + lookupKey + ", bound=" + bound);
    }

    public static void set(String lookupKey) {
        set(lookupKey, false);
    }

    public static String get() {
        String lookupKey = CONTEXTHOLDER.get();
        if (StringUtils.isEmpty(lookupKey)) {
            master();
        }
        return CONTEXTHOLDER.get();
    }

    public static void clear() {
        toDefault();
        log.debug("clear lookup context, routing to default.");
    }

    public static void master() {
        toDefault();
        log.debug("routing to datasource " + LookupType.master.name());
    }

    private static void toDefault() {
        set(LookupType.master.name());
    }

    public static void slave(MasterSlavesDataSources<?> dbConfig) {
        // 如果从节点数为0，则设置master
        int slavesNum = dbConfig.getSlavesNum();
        if (slavesNum == 0) {
			set(LookupType.master.name());
			log.debug("no slaves, routing to master:" + LookupType.master.name());
            return;
        }

		int i = COUNTER.get();
		if (i >= slavesNum) {
			COUNTER.set(0);
		}
		String lookupKey = makeLookupKey(COUNTER.getAndIncrement());
		log.debug("slaves number is" + slavesNum + ", routing to slave: lookupKey=" + lookupKey);
		set(lookupKey);
	}

	public static boolean isMaster() {
		return isMaster(get());
	}

	public static boolean isMaster(String lookupKey) {
		return !isSlave(lookupKey);
	}

	public static boolean isSlave() {
		return isSlave(get());
	}

	public static boolean isSlave(String lookupKey) {
		return unwrapedLookupKey(lookupKey).startsWith(LookupType.slave.name());
	}

	public static String makeLookupKey(int index) {
		return LookupType.slave.name().concat(index + "");
	}

	public static int getSlaveIndex(String lookupKey) {
		String name = LookupType.slave.name();
		if (StringUtils.isNotEmpty(lookupKey) && lookupKey.startsWith(name)) {
			return Integer.valueOf(lookupKey.replaceFirst(name, ""));
		}
        return 0;
    }

    public static boolean isBound(String lookupKey) {
        return lookupKey.startsWith(BOUND_PREFIX);
    }

    public static String wrapedLookupKey(String lookupKey) {
        if (!isBound(lookupKey)) {
            return BOUND_PREFIX.concat(lookupKey);
        }
        return lookupKey;
    }

    public static String unwrapedLookupKey(String lookupKey) {
        if (isBound(lookupKey)) {
            return lookupKey.replaceFirst(BOUND_PREFIX, "");
        }
        return lookupKey;
    }
}
