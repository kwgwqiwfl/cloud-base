package com.ring.welkin.common;

import com.ring.welkin.common.utils.DataBaseUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

public class DatabaseUtilTests {

    @Test
    public void testMysql56() throws ClassNotFoundException, SQLException {
        try {
            Connection conn = DataBaseUtil.getConnection("com.mysql.jdbc.Driver",
                    "jdbc:mysql://192.168.1.100:3306/test?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true", "yuanbin", "1!2@3#4$5%",
                    null, null);
            Assertions.assertNotNull(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMysql8() throws ClassNotFoundException, SQLException {
        try {
            Connection conn = DataBaseUtil.getConnection("com.mysql.cj.jdbc.Driver",
                    "jdbc:mysql://192.168.2.220:3306/mysql?useSSL=false&useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true", "root", "123456", null,
                    null);
            Assertions.assertNotNull(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOracle() throws ClassNotFoundException, SQLException {
        try {
            Properties params = new Properties();
            params.put("oracle.net.CONNECT_TIMEOUT", "10000000");
            params.put("oracle.jdbc.ReadTimeout", "2000");
            Connection conn = DataBaseUtil.getConnection("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@192.168.1.82:1521:orcl?user=carpo&password=123456", "carpo", "123456",
                    params, null);
            Assertions.assertNotNull(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPostgresql() throws ClassNotFoundException, SQLException {
        try {
            Connection conn = DataBaseUtil.getConnection("org.postgresql.Driver", "jdbc:postgresql://192.168.1.67:5432/postgres", "postgres", "postgres", null, null);
            DatabaseMetaData metaData = conn.getMetaData();
            String schema = "public";
            ResultSet rs = null;
            List<String> columns = new ArrayList<>();
            rs = metaData.getColumns(schema, null, "p", null);
            while (rs.next()) {
                String name = rs.getString("COLUMN_NAME");
                columns.add(name);
                String type = rs.getString("TYPE_NAME");
                if (type.equalsIgnoreCase("NUMBER") || type.equalsIgnoreCase("NUMERIC") || type.equalsIgnoreCase("DECIMAL")) {
                    int precision = rs.getInt("COLUMN_SIZE");
                    int scale = rs.getInt("DECIMAL_DIGITS");
                    columns.add(type + "(" + precision + "," + scale + ")");
                } else {
                    columns.add(type);
                }
            }

            rs.close();
            Assertions.assertNotNull(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDm() throws ClassNotFoundException, SQLException {
        try {
            Connection conn = DataBaseUtil.getConnection("dm.jdbc.driver.DmDriver", "jdbc:dm://192.168.1.250:5236/TEST", "test", "123456", null, null);
            Assertions.assertNotNull(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGreenplum() throws ClassNotFoundException, SQLException {
        try {
            Connection conn = DataBaseUtil.getConnection("com.pivotal.jdbc.GreenplumDriver",
                    "jdbc:pivotal:greenplum://192.168.1.145:30507;DatabaseName=postgres", "gpadmin", "123456", null, null);
            Assertions.assertNotNull(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSqlserver() throws ClassNotFoundException, SQLException {
        try {
            Connection conn = DataBaseUtil.getConnection("com.microsoft.sqlserver.jdbc.SQLServerDriver",
                    "jdbc:sqlserver://192.168.1.145:30502;DatabaseName=Test", "sa", "12345678", null, null);
            Assertions.assertNotNull(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testMysql1() throws ClassNotFoundException, SQLException, InterruptedException {
        for (int i = 0; i < 30; i++) {
            try {
                DataBaseUtil.connect("com.mysql.jdbc.Driver", "jdbc:mysql://192.168.10.19:3306/t1", "merce", "merce", null,
                        "D:\\repository\\mysql\\mysql-connector-java\\8.0.29\\mysql-connector-java-8.0.29.jar");
                printRegisteredDrivers();
            } catch (Exception e) {
            }
        }
        printRegisteredDrivers();
    }

    private static void printRegisteredDrivers() {
        int i = 0;
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = (Driver) drivers.nextElement();
            System.out.println(driver.toString());
            System.out.println(++i);
        }
    }
}
