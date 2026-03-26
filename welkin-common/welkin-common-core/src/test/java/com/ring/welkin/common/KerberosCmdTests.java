package com.ring.welkin.common;

import cn.hutool.core.util.RuntimeUtil;
import com.ring.welkin.common.utils.KerberosUtils;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Scanner;

public class KerberosCmdTests {
    private static final String CMD_LINE = "C:\\Program Files\\MIT\\Kerberos\\bin\\kinit.exe -k -t C:\\Users\\EDZ\\merce.keytab merce@HADOOP.COM";
    private static final String KINIT = "C:\\Program Files\\MIT\\Kerberos\\bin\\kinit.exe";
    private static final String KEYTAB = "C:\\Users\\EDZ\\merce.keytab";
    private static final String PRINCIPAL = "merce@HADOOP.COM";

    @Test
    public void test1() throws Exception {
        Process process = Runtime.getRuntime().exec(CMD_LINE);
        Scanner sc = new Scanner(System.in);
        OutputStream out = process.getOutputStream();
        out.write(("中国" + "\n").getBytes());
        String str = null;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while ((str = (buffer.readLine())) != null) {
            System.out.println(str);
        }
    }

    @Test
    public void test2() throws Exception {
        List<String> execForLines = RuntimeUtil.execForLines(CMD_LINE);
        System.out.println(execForLines);
    }

    @Test
    public void test3() throws Exception {
        KerberosUtils.kinit(KINIT, KEYTAB, PRINCIPAL);
    }

}
