package com.ring.welkin.common.utils;

import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class KerberosUtils {
    public static final String CMD_KINIT = "kinit";

    public static List<String> kinit(String kinitCmd, String keytab, String principal) {
        String cmd = kinitCmd + " -k -t " + keytab + " " + principal;
        log.info("cmds => {}", cmd);
        List<String> execForLines = RuntimeUtil.execForLines(cmd);
        if (ICollections.hasElements(execForLines)) {
            for (String line : execForLines) {
                log.info(line);
            }
        }
        return execForLines;
    }

    public static List<String> kinit(String keytab, String principal) {
        return kinit(CMD_KINIT, keytab, principal);
    }

}
