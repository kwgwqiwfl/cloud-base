package com.ring.welkin.common.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DebugSy on 2018/5/17.
 */
@Slf4j
public class DataBaseUtil {

    public static DBConf DB_MYSQL = new DBConf(true, "?", "&", false, false, false, false, "com.mysql.jdbc.Driver");
    public static DBConf DB_MYSQL8 = new DBConf(true, "?", "&", false, false, false, false, "com.mysql.cj.jdbc.Driver");
    public static DBConf DB_Teradata = new DBConf(true, ",", ",", false, true, false, false, "com.teradata.jdbc.TeraDriver");
    public static DBConf DB_PostSQL = new DBConf(true, "?", "&", false, false, false, false, "org.postgresql.Driver");
    public static DBConf DB_MsSQL = new DBConf(true, ";", ";", false, true, false, false, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
    public static DBConf DB_Sybase = new DBConf(false, ";", ";", true, true, false, false, "net.sourceforge.jtds.jdbc.Driver");
    public static DBConf DB_HSQLDB = new DBConf(true, ";", ";", false, true, false, false, "org.hsqldb.jdbcDriver");
    public static DBConf DB_Greenplum = new DBConf(true, ";", ";", false, true, false, false, "com.pivotal.jdbc.GreenplumDriver");
    public static DBConf DB_ODBCBridge = new DBConf(false, ";", ";", false, true, false, false, "sun.jdbc.odbc.JdbcOdbcDriver");
    public static DBConf DB_Informix = new DBConf(true, ":", ";", false, true, false, false, "com.informix.jdbc.IfxDriver");
    public static DBConf DB_Oracle = new DBConf(false, ":", ";", true, false, false, true, "oracle.jdbc.driver.OracleDriver");
    public static DBConf DB_ONE = new DBConf(false, ":", ";", false, false, false, true, "com.intple.dbone.Driver");
    public static DBConf DB_DB2 = new DBConf(false, ":", ";", false, false, false, true, "com.ibm.db2.jcc.DB2Driver");
    public static DBConf DB_Kingbase = new DBConf(false, ":", ";", false, false, false, true, "com.kingbase.Driver");
    public static DBConf DB_Kingbase8 = new DBConf(false, ":", ";", false, false, false, true, "com.kingbase8.Driver");
    public static DBConf DB_CLICKHOUSE = new DBConf(true, "?", "&", false, true, false, false, "ru.yandex.clickhouse.ClickHouseDriver");
    public static DBConf DB_DMbase = new DBConf(true, ":", ";", false, false, false, true, "dm.jdbc.driver.DmDriver");
    public static DBConf DB_HIVE = new DBConf(false, ":", ";", false, false, false, true, "org.apache.hive.jdbc.HiveDriver");
    public static DBConf DB_OSCAR = new DBConf(true, "?", "&", false, true, false, false, "com.oscar.Driver");
    public static DBConf DB_Oceanbase = new DBConf(true, "?", "&", false, true, false, false, "com.alipay.oceanbase.jdbc.Driver");
    public static DBConf DB_Gaussdb = new DBConf(true, "?", "&", false, true, false, false, "com.huawei.gauss200.jdbc.Driver");
    public static DBConf DB_Trino = new DBConf(false, "?", "&", true, false, true, true, "io.trino.jdbc.TrinoDriver");
    public static DBConf[] dbs = {DB_MYSQL, DB_MYSQL8, DB_Teradata, DB_PostSQL, DB_MsSQL, DB_Sybase, DB_HSQLDB, DB_Greenplum, DB_ODBCBridge, DB_Informix,
            DB_Oracle, DB_ONE, DB_DB2, DB_Kingbase, DB_Kingbase8, DB_CLICKHOUSE, DB_DMbase, DB_HIVE, DB_OSCAR, DB_Oceanbase, DB_Gaussdb, DB_Trino};

    private  static final String dmRegex = "^jdbc:dm:\\/\\/(?:\\d{1,3}\\.){3}\\d{1,3}:\\d+$";
    private static final Pattern dmPattern = Pattern.compile(dmRegex,Pattern.CASE_INSENSITIVE);

    private static final Map<String, Driver> tmpDriverMap = new HashMap<String, Driver>();

    public static void connect(String driver, String url, String user, String password, Properties params, String jarPath)
            throws SQLException, ClassNotFoundException {
        Connection conn = getConnection(driver, url, user, password, params, jarPath);
        closeConnection(conn);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("close connection failed");
            }
        }
    }

    public static void closeDBResource(Connection conn, Statement statement, ResultSet rs) {
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("close resultset failed");
            }
        }
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                log.error("close statement failed");
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("close connection failed");
            }
        }
    }

    public static DBConf getDB(String driver) {
        for (DBConf db : dbs) {
            if (db.driver.equals(driver)) {
                return db;
            }
        }
        return null;
    }

    public static Connection getConnection(String driver, String url, String user, String password, Properties params, String jarPath)
            throws SQLException, ClassNotFoundException {
        long start = System.currentTimeMillis();
        if (driver == null || url == null)
            throw new IllegalArgumentException("driver and url are required!");
        url = url.trim();
        driver = driver.trim();
        Connection conn = null;
        for (DBConf db : dbs) {
            if (db.driver.equals(driver)) {
                conn = getCommonConnection(url, user, password, params, db, jarPath);
                break;
            }
        }
        if (conn == null)
            conn = getGenericConnection(driver, url, user, password, params);

        long end = System.currentTimeMillis();
        log.info("static getConnection() takes " + (end - start) / 1000 + " seconds to get connection");
        return conn;
    }

    public static List<String> getDatabaseList(String driver, String url, String user, String password, Properties params, String jarPath)
            throws SQLException, ClassNotFoundException {
        Connection conn = getConnection(driver, url, user, password, params, jarPath);
        try {
            DBConf db = getDB(driver);
            return getDatabaseList(conn, db, user);
        } finally {
            closeConnection(conn);
        }
    }

    public static List<String> getTableList(String driver, String url, String user, String password, String catalog, String schema, Properties params,
                                            String jarPath) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection(driver, url, user, password, params, jarPath);
        try {
            DBConf db = getDB(driver);
            return getTableList(conn, db, user, catalog, schema);
        } finally {
            closeConnection(conn);
        }
    }

    /*Postgres的表和视图需要从两个地方查询并汇总
        1. 先查询table count和view count
        2. 返回结果受limit和offset限制，并优先返回table
    */
    private static TablePage getPgTablePage(Connection conn, String catalog, String schema, String table, int offset, int limit) throws SQLException, ClassNotFoundException {
        Long tableCount = 0L;
        Long viewCount = 0L;
        String tableFilter = "";
        String viewFilter = "";
        String sql_table = "";
        String sql_view = "";
        if (StringUtils.isNotEmpty(table)) {
            tableFilter = "and tablename like '%" + table + "%'";
            viewFilter = "and viewname like '%" + table + "%'";
        }
        String table_count_sql = String.format("select count(*) from %s.pg_catalog.pg_tables where schemaname = '%s' %s", catalog, schema, tableFilter);
        String view_count_sql = String.format("select count(*) from %s.pg_catalog.pg_views where schemaname = '%s' %s", catalog, schema, viewFilter);
        PreparedStatement statement = conn.prepareStatement(table_count_sql);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            tableCount = rs.getLong(1);
        }
        closeDBResource(null, statement, rs);
        statement = conn.prepareStatement(view_count_sql);
        rs = statement.executeQuery();
        while (rs.next()) {
            viewCount = rs.getLong(1);
        }
        closeDBResource(null, statement, rs);

        //根据tableCount, viewCount， offset, limit计算返回的列表记录
        List<String> tableList = new ArrayList<>();

        if(offset+limit > tableCount + viewCount){
            if(offset < tableCount){
                //返回所有table和view
                sql_table = String.format("select tablename as TABLE_NAME from %s.pg_catalog.pg_tables pt where schemaname = '%s' %s order by tablename limit %s offset %s", catalog, schema, tableFilter, tableCount-offset, offset);
                sql_view = String.format("select viewname as TABLE_NAME from %s.pg_catalog.pg_views pt where schemaname = '%s' %s order by viewname limit %s offset %s", catalog, schema, viewFilter, viewCount, 0);
                //查询table
                statement = conn.prepareStatement(sql_table);
                rs = statement.executeQuery();
                while (rs.next()) {
                    tableList.add(rs.getString(1));
                }
                closeDBResource(null, statement, rs);
                //查询view
                statement = conn.prepareStatement(sql_view);
                rs = statement.executeQuery();
                while (rs.next()) {
                    tableList.add(rs.getString(1));
                }
                closeDBResource(null, statement, rs);
            }else if(offset < tableCount + viewCount){
                sql_view = String.format("select viewname as TABLE_NAME from %s.pg_catalog.pg_views pt where schemaname = '%s' %s order by viewname limit %s offset %s", catalog, schema, viewFilter, limit, offset-tableCount);
                //查询view
                statement = conn.prepareStatement(sql_view);
                rs = statement.executeQuery();
                while (rs.next()) {
                    tableList.add(rs.getString(1));
                }
                closeDBResource(null, statement, rs);
            }else{
                //没有符合的记录
            }
        }else if(tableCount >= offset+limit){
            //只查询table表记录数就够了
            sql_table = String.format("select tablename as TABLE_NAME from %s.pg_catalog.pg_tables pt where schemaname = '%s' %s order by tablename limit %s offset %s", catalog, schema, tableFilter, limit, offset);
            //查询table
            statement = conn.prepareStatement(sql_table);
            rs = statement.executeQuery();
            while (rs.next()) {
                tableList.add(rs.getString(1));
            }
            closeDBResource(null, statement, rs);
        }else if(offset>=tableCount){
            //越过table只查询view表记录数就够了
            sql_view = String.format("select viewname as TABLE_NAME from %s.pg_catalog.pg_views pt where schemaname = '%s' %s order by viewname limit %s offset %s", catalog, schema, viewFilter, limit, offset-tableCount);
            //查询view
            statement = conn.prepareStatement(sql_view);
            rs = statement.executeQuery();
            while (rs.next()) {
                tableList.add(rs.getString(1));
            }
            closeDBResource(null, statement, rs);
        }else{
            //返回部分table和view
            sql_table = String.format("select tablename as TABLE_NAME from %s.pg_catalog.pg_tables pt where schemaname = '%s' %s order by tablename limit %s offset %s", catalog, schema, tableFilter, tableCount-offset, offset);
            sql_view = String.format("select viewname as TABLE_NAME from %s.pg_catalog.pg_views pt where schemaname = '%s' %s order by viewname limit %s offset %s", catalog, schema, viewFilter, limit-(tableCount-offset), 0);
            //查询table
            statement = conn.prepareStatement(sql_table);
            rs = statement.executeQuery();
            while (rs.next()) {
                tableList.add(rs.getString(1));
            }
            closeDBResource(null, statement, rs);
            //查询view
            statement = conn.prepareStatement(sql_view);
            rs = statement.executeQuery();
            while (rs.next()) {
                tableList.add(rs.getString(1));
            }
            closeDBResource(null, statement, rs);
        }

        TablePage page = new TablePage();
        page.setContent(tableList);
        page.setTotal(tableCount + viewCount);
        page.setTotalPages(page.getTotal() % limit > 0 ? page.getTotal() / limit + 1 : page.getTotal() / limit);
        page.setFirst(offset < limit);
        page.setLast(offset + limit >= page.getTotal());
        return page;
    }

    public static TablePage getTablePageList(String driver, String url, String user, String password, String catalog, String schema, Properties params,
                                             String jarPath, int offset, int limit, String table) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection(driver, url, user, password, params, jarPath);
        if (StringUtils.isEmpty(catalog)) {
            catalog = conn.getCatalog();
        }
        if (StringUtils.isEmpty(schema)) {
            schema = user.toUpperCase();
        }
        try {
            DBConf db = getDB(driver);

            if (db == DB_PostSQL || db == DB_Greenplum) {
                return getPgTablePage(conn, catalog, schema, table, offset, limit);
            }

            PreparedStatement stmt = fetchTableStatement(conn, db, catalog, schema, offset, limit, table);
            if (stmt != null) {
                ResultSet rs = stmt.executeQuery();
                List<String> tableList = new ArrayList<>();
                while (rs.next()) {
                    tableList.add(rs.getString(1));
                }
                TablePage page = new TablePage();
                page.setContent(tableList);
                page.setTotal(getTableCount(conn, db, catalog, schema, table));
                page.setTotalPages(page.getTotal() % limit > 0 ? page.getTotal() / limit + 1 : page.getTotal() / limit);
                page.setFirst(offset < limit);
                page.setLast(offset + limit >= page.getTotal());
                return page;
            }
            return null;
        } finally {
            closeConnection(conn);
        }
    }

    private static PreparedStatement fetchTableStatement(Connection conn, DBConf db, String catalog, String schema, int offset, int limit, String table) throws SQLException {
        String sql;
        String tableFilter = "";
        if (db == DB_MYSQL || db == DB_MYSQL8) {
            if (StringUtils.isNotEmpty(table)) {
                tableFilter = "and `table_name` like '%" + table + "%'";
            }
            sql = String.format("SELECT `table_name` FROM information_schema.tables where table_schema='%s' %s ORDER BY `table_name` limit %s,%s", catalog, tableFilter, offset, limit);
        } else if (db == DB_Oracle) {
            if (StringUtils.isNotEmpty(table)) {
                tableFilter = "and TABLE_NAME like '%" + table + "%'";
            }
            sql = String.format("select TABLE_NAME FROM (SELECT TABLE_NAME,rownum AS num FROM all_tables WHERE OWNER='%s' %s AND ROWNUM <= (%s+%s)) t where t.num> %s", schema, tableFilter, offset, limit, offset);
        } else if (db == DB_MsSQL) {
            if (StringUtils.isNotEmpty(table)) {
                tableFilter = "and table_name like '%" + table + "%'";
            }
            sql = String.format("SELECT TOP %s table_name as TABLE_NAME FROM information_schema.tables WHERE table_catalog = '%s' and table_schema='%s' and table_name not in (\n" +
                    "\tSELECT TOP %s table_name FROM information_schema.tables WHERE table_catalog = '%s' and table_schema= '%s' order by TABLE_NAME \n" +
                    ") %s order by TABLE_NAME", limit, catalog, schema, offset, catalog, schema, tableFilter);
        } else if (db == DB_PostSQL || db == DB_Greenplum) {
            if (StringUtils.isNotEmpty(table)) {
                tableFilter = "and tablename like '%" + table + "%'";
            }
            sql = String.format("select tablename as TABLE_NAME from %s.pg_catalog.pg_tables pt where schemaname = '%s' %s order by tablename limit %s offset %s", catalog, schema, tableFilter, limit, offset);
        } /*else if (db == DB_HIVE) {
            if (StringUtils.isNotEmpty(table)) {
                tableFilter = "where tables.table_name like '%" + table + "%'";
            }
            sql = String.format("select tables.table_name from information_schema.tables %s order by tables.table_name limit %s offset %s", tableFilter, limit, offset);
        } */else {
            return null;
        }
        return conn.prepareStatement(sql);
    }

    private static long getTableCount(Connection conn, DBConf db, String catalog, String schema, String table) {
        try {
            PreparedStatement stmt = tableCountStatement(conn, db, catalog, schema, table);
            if (stmt != null) {
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.warn(e.getMessage(), e);
        }
        return 0;
    }

    private static PreparedStatement tableCountStatement(Connection conn, DBConf db, String catalog, String schema, String table) throws SQLException {
        String sql;
        String tableFilter = "";
        if (db == DB_MYSQL || db == DB_MYSQL8) {
            if (StringUtils.isNotEmpty(table)) {
                tableFilter = "and `table_name` like '%" + table + "%'";
            }
            sql = String.format("SELECT count(*) FROM information_schema.tables where table_schema='%s' %s", catalog, tableFilter);
        } else if (db == DB_Oracle) {
            if (StringUtils.isNotEmpty(table)) {
                tableFilter = "and TABLE_NAME like '%" + table + "%'";
            }
            sql = String.format("SELECT count(*) FROM all_tables WHERE OWNER='%s' %s", schema, tableFilter);
        } else if (db == DB_MsSQL) {
            if (StringUtils.isNotEmpty(table)) {
                tableFilter = "and table_name like '%" + table + "%'";
            }
            sql = String.format("SELECT count(*) FROM information_schema.tables WHERE table_catalog = '%s' and table_schema='%s' %s", catalog, schema, tableFilter);
        } else if (db == DB_PostSQL || db == DB_Greenplum) {
            if (StringUtils.isNotEmpty(table)) {
                tableFilter = "and tablename like '%" + table + "%'";
            }
            sql = String.format("select count(*) from %s.pg_catalog.pg_tables where schemaname = '%s' %s", catalog, schema, tableFilter);
        } /*else if (db == DB_HIVE) {
            if (StringUtils.isNotEmpty(table)) {
                tableFilter = "and tables.table_name like '%" + table + "%'";
            }
            sql = String.format("select count(*) from information_schema.tables where tables.table_catalog='%s' \n" +
                    "and tables.table_schema='%s' %s", catalog, catalog, tableFilter);
        }*/ else {
            return null;
        }
        return conn.prepareStatement(sql);
    }

    public static List<String> getTableColumnList(String driver, String url, String user, String password, Properties params, String table, String catalog,
                                                  String schema, String jarPath) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection(driver, url, user, password, params, jarPath);
        try {
            DBConf db = getDB(driver);
            return getTableColumnList(conn, db, user, table, catalog, schema);
        } finally {
            closeConnection(conn);
        }
    }

    public static Map<String, Object> getTableData(String driver, String url, String user, String password, Properties params, String sql, String catalog,
                                                   String schema, Map<String, Object> paraMap, String jarPath) throws SQLException, ClassNotFoundException {
        Connection conn = getConnection(driver, url, user, password, params, jarPath);
        if (StringUtils.isNotBlank(catalog)) {
            conn.setCatalog(catalog);
        }
        if (StringUtils.isNotBlank(schema)) {
            conn.setSchema(schema);
        }
        try {
            DBConf db = getDB(driver);
            return getTableData(conn, db, user, sql, paraMap);
        } finally {
            closeConnection(conn);
        }
    }

    public static List<String> getDatabaseList(Connection conn, DBConf db, String user) throws SQLException {
        log.info("getTableList DBConf = {}, user = {}, catalog = {}, schema = {}", db, user);
        long start = System.currentTimeMillis();
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet catalogs = metaData.getCatalogs();
        List<String> databases = new ArrayList<>();
        while (catalogs.next())
            databases.add(catalogs.getString("TABLE_CAT"));
        catalogs.close();
        long end = System.currentTimeMillis();
        log.info("static getDatabaseList() takes " + (end - start) / 1000 + " seconds to fetch metadata catalogs");
        return databases;
    }

    public static List<String> getTableList(Connection conn, DBConf db, String user, String catalog, String schema) throws SQLException {
        log.info("getTableList DBConf = {}, user = {}, catalog = {}, schema = {}", db, user, catalog, schema);
        long start = System.currentTimeMillis();
        DatabaseMetaData metaData = conn.getMetaData();
        String originSchema = schema;
        if (StringUtils.isBlank(catalog)) {
            catalog = conn.getCatalog();
        }
        if (StringUtils.isBlank(schema)) {
            schema = user.toUpperCase();
        }
        ResultSet rs = null;
        List<String> tables = new ArrayList<>();
        if (db == DB_MYSQL || db == DB_MYSQL8 || db == DB_Oracle || db == DB_DMbase) {
            rs = metaData.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
        } else if (db == DB_ONE) {
            rs = metaData.getTables(catalog, "public", null, new String[]{"TABLE", "VIEW"});
        } else if (db == DB_PostSQL || db == DB_Kingbase || db == DB_Kingbase8) {
            if(StringUtils.isBlank(originSchema)){
                rs = metaData.getTables(catalog, null, null, new String[]{"TABLE", "VIEW"});
            }else {
                rs = metaData.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
            }
        } else if (db == DB_MsSQL) {
            rs = metaData.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
        } else if (db == DB_CLICKHOUSE) {
            rs = metaData.getTables(catalog, schema, null, null);
        } else if (db == DB_HIVE) {
            rs = metaData.getTables(null, conn.getSchema(), "%", new String[]{"TABLE", "VIEW"});
        } else if (db == DB_OSCAR) {
            rs = metaData.getTables(null, schema, null, new String[]{"TABLE", "VIEW"});
        } else if (db == DB_DB2) {
            rs = metaData.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
        } else if (db == DB_Oceanbase) {
            rs = metaData.getTables(null, schema, null, new String[]{"TABLE", "VIEW"});
        } else if (db == DB_Gaussdb) {
            rs = metaData.getTables(null, schema, null, new String[]{"TABLE", "VIEW"});
        } else
            rs = metaData.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});

        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }

        rs.close();
        long end = System.currentTimeMillis();
        log.info("static getTableList() takes " + (end - start) / 1000 + " seconds to fetch metadata table list");
        return tables;
    }

    public static List<String> getTableColumnList(Connection conn, DBConf db, String user, String table, String catalog, String schema) throws SQLException {
        long start = System.currentTimeMillis();
        DatabaseMetaData metaData = conn.getMetaData();
        if (StringUtils.isBlank(catalog)) {
            catalog = conn.getCatalog();
        }
        if (StringUtils.isBlank(schema)) {
            schema = user.toUpperCase();
        }
        ResultSet rs = null;
        List<String> columns = new ArrayList<>();
        if (db == DB_MYSQL || db == DB_MYSQL8 || db == DB_Oracle) {
            rs = metaData.getColumns(catalog, schema, table, null);
        } else if (db == DB_ONE) {
            rs = metaData.getColumns(catalog, "public", table, null);
        } else if (db == DB_PostSQL || db == DB_CLICKHOUSE || db == DB_Kingbase || db == DB_Kingbase8 || db == DB_DMbase || db == DB_HIVE) {
            rs = metaData.getColumns(catalog, null, table, null);
        } else if (db == DB_OSCAR) {
            rs = metaData.getColumns(catalog, schema, table, null);
        } else if (db == DB_MsSQL) {
            rs = metaData.getColumns(catalog, schema, table, null);
        } else
            rs = metaData.getColumns(catalog, schema, table, null);

        // refer to
        // https://docs.oracle.com/javase/8/docs/api/java/sql/DatabaseMetaData.html#getColumns-java.lang.String-java.lang.String-java.lang.String-java.lang.String-
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
        long end = System.currentTimeMillis();
        log.info("static getTableColumnList() takes " + (end - start) / 1000 + " seconds to fetch metadata column list");
        return columns;
    }

    public static Map<String, Object> getTableData(Connection conn, DBConf db, String user, String sql, Map<String, Object> paraMap) throws SQLException {
        long start = System.currentTimeMillis();

        Integer limit = 100;
        if (paraMap.containsKey("limit")) {
            limit = Math.max(limit, Integer.parseInt((String) paraMap.get("limit")));
        }
        Integer offset = 0;
        if (paraMap.containsKey("offset")) {
            offset = Integer.parseInt((String) paraMap.get("offset"));
        }

        String table = null;
        if (paraMap.containsKey("table")) {
            table = (String) paraMap.get("table");
        }

        String primeKey = "ID";
        if (paraMap.containsKey("primeKey")) {
            primeKey = (String) paraMap.get("primeKey");
        }

        if (db == DB_MYSQL || db == DB_MYSQL8 || db == DB_PostSQL || db == DB_Kingbase || db == DB_DMbase) {
            sql = sql + " limit " + limit;
        } else if (db == DB_Oracle) {
            sql = "select * from (" + sql + ") where rownum <= " + limit;
        } else if (db == DB_DB2) {
            String select = sql.replace("*", "*, ROW_NUMBER() OVER() AS RN");
            sql = "select * from ( " + select + " ) where RN >= 0 and RN < " + limit;
        } else if (db == DB_MsSQL) {
            sql = "select top " + limit + " * from " + table + " where " + primeKey + " not in (select top 0 " + primeKey + " from " + table + ")";
        }

        // Object rowCount = paraMap.get("rowCount");
        Map<String, Object> map = new HashMap<>();
        List<List<String>> rows = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<String> typeList = new ArrayList<>();
        map.put("names", nameList);
        map.put("types", typeList);
        map.put("rows", rows);

        try (Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
            stmt.setFetchSize(limit);
            try (ResultSet rs = stmt.executeQuery(sql)) {
                long end = System.currentTimeMillis();
                log.info("static getTableData() takes " + (end - start) / 1000 + " seconds to fetch table data");

                int count = rs.getMetaData().getColumnCount();
                for (int i = 1; i <= count; i++) {
                    nameList.add(rs.getMetaData().getColumnName(i));

                    String type = rs.getMetaData().getColumnTypeName(i);
                    if (type.equalsIgnoreCase("NUMBER")) {
                        int scale = rs.getMetaData().getScale(i);
                        int precision = rs.getMetaData().getPrecision(i);
                        typeList.add(type + "(" + precision + "," + scale + ")");
                    } else {
                        typeList.add(rs.getMetaData().getColumnTypeName(i));
                    }
                }

                int index = 0;
                while (rs.next()) {
                    if (index++ < offset)
                        continue;
                    if (rows.size() >= limit)
                        break;
                    List<String> list = new ArrayList<>();
                    for (int i = 1; i <= count; i++) {
                        try {
                            String v = rs.getString(i);
                            if (v != null && v.length() > 30)
                                v = v.substring(0, 30) + "...";
                            list.add(v);
                        } catch (Exception e) {
                            list.add("?");
                        }
                    }
                    rows.add(list);
                }
            }
        }

        long end = System.currentTimeMillis();
        log.info("static getTableData() takes " + (end - start) / 1000 + " seconds to complete getTableData()");
        return map;
    }

    public static boolean tableExist(Connection connection, String tableName) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException("Unable to determine whether the table exists.", e);
        }
    }

    public static Connection geConnetion(String driver, String url, String user, String password, Map<String, Object> properties)
            throws ClassNotFoundException, SQLException, UnsupportedEncodingException {
        Class.forName(driver);
        if (url.contains("?")) {
            if (!url.endsWith("?"))
                url += "&";
            url += "user=" + user + (StringUtils.isNotEmpty(password) ? ("&password=" + password) : "");
        } else
            url += "?" + "user=" + user + (StringUtils.isNotEmpty(password) ? ("&password=" + password) : "");
        log.trace("geConnetion: url => {}", url);
        return DriverManager.getConnection(url);
    }

    private static Connection getGenericConnection(String driver, String url, String user, String password, Map<Object, Object> properties)
            throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        if (url.contains("?")) {
            if (!url.endsWith("?"))
                url += "&";
            url += (StringUtils.isNotEmpty(user) ? ("user=" + user) : "") + (StringUtils.isNotEmpty(password) ? ("&password=" + password) : "");
        } else
            url += (StringUtils.isNotEmpty(user) ? ("?user=" + user) : "") + (StringUtils.isNotEmpty(password) ? ("&password=" + password) : "");
        log.trace("getGenericConnection: url => {}", url);
        return DriverManager.getConnection(url);
    }

    public static Connection getCommonConnection(String url, String user, String password, Properties properties, DBConf db, String jarPath)
            throws ClassNotFoundException, SQLException {
        if (db.propUrl && db.userInUrl) {
            if (url.contains(db.propHeader)) {
                if (!url.endsWith(db.propSep))
                    url += db.propSep;
                url += "user=" + user + (StringUtils.isNotEmpty(password) ? (db.propSep + "password=" + password) : "");
            } else
                url += db.propHeader + "user=" + user + (StringUtils.isNotEmpty(password) ? (db.propSep + "password=" + password) : "");
        }

        if (db.propUrl && properties != null) {
            if ("dm.jdbc.driver.DmDriver".equalsIgnoreCase(db.driver)){
                Matcher matcher = dmPattern.matcher(url);
                if (matcher.matches()){
                    url = url+"/";
                }
            }
            for (Map.Entry<Object, Object> e : properties.entrySet()) {
                if (!url.contains(db.propHeader)){
                    url += db.propHeader + e.getKey() + "=" + e.getValue();
                }else{
                    url += db.propSep + e.getKey() + "=" + e.getValue();
                }
            }
        }

        if (StringUtils.isNotBlank(jarPath)) {
            try {
                return dynamicLoadJdbc(db.driver, jarPath, url, user, password);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            Class.forName(db.driver);
        }

        if (db.needProperties) {
            if (properties == null) {
                properties = new Properties();
            }

            String userStr = properties.getProperty("user");
            if (StringUtils.isBlank(userStr)) {
                properties.put("user", user);
            }
            String passwordStr = properties.getProperty("password");
            if (StringUtils.isBlank(passwordStr)) {
                properties.put("password", password);
            }
            log.trace("getCommonConnection: url => {}, properties => {}", url, JsonUtils.toJson(properties));
            return DriverManager.getConnection(url, properties);
        }

        if (!db.userInUrl) {
            if (user == null)
                user = "";
            if (password == null)
                password = "";
            log.trace("getCommonConnection: url => {}, user => {}, password => {}", url, user, password);
            return DriverManager.getConnection(url, user, password);
        }

        log.trace("getCommonConnection: url => {}", url);
        return DriverManager.getConnection(url);
    }

    // 动态加载jdbc驱动
    private static Connection dynamicLoadJdbc(String driver, String jdbcFile, String url, String user, String password) throws Exception {
        log.info("dynamic load jdbc connection url:{}", url);
        Properties properties = new Properties();
        properties.put("user", user);
        properties.put("password", password);
        String driverKey = jdbcFile + "." + driver;
        if (tmpDriverMap.containsKey(driverKey)) {
            return tmpDriverMap.get(driverKey).connect(url, properties);
        }
        synchronized (DataBaseUtil.class) {
            if (tmpDriverMap.containsKey(driverKey)) {
                return tmpDriverMap.get(driverKey).connect(url, properties);
            }
            WelkinUrlClassloader ucl = null;
            if (jdbcFile.endsWith(".jar")) {
                URL u = new URL("jar:file:" + jdbcFile + "!/");
                ucl = new WelkinUrlClassloader(new URL[]{u});
            } else {
                File f = new File(jdbcFile);
                if (f.isDirectory()) {
                    List<URL> urls = new ArrayList<>();
                    File[] flies = f.listFiles();
                    if (flies == null) {
                        return null;
                    }
                    for (File file : flies) {
                        if (file.getName().endsWith(".jar")) {
                            URL u = new URL("jar:file:" + file.getAbsolutePath() + "!/");
                            urls.add(u);
                        }
                    }
                    ucl = new WelkinUrlClassloader(urls.toArray(new URL[0]));
                }
            }
            Driver driveObj = (Driver) Class.forName(driver, true, ucl).newInstance();
            tmpDriverMap.put(driverKey, driveObj);
            return driveObj.connect(url, properties);
        }
    }

    static class DBConf {
        String driver;
        boolean propUrl;
        String propHeader;
        String propSep;
        boolean needProperties;
        boolean userInUrl;
        boolean userInProp;
        boolean userParam;

        public DBConf(boolean propUrl, String propHeader, String propSep, boolean needProperties, boolean userInUrl, boolean userInProp, boolean userParam,
                      String driver) {
            this.driver = driver;
            this.propUrl = propUrl;
            this.propHeader = propHeader;
            this.propSep = propSep;
            this.needProperties = needProperties;
            this.userInUrl = userInUrl;
            this.userInProp = userInProp;
            this.userParam = userParam;
        }

        @Override
        public String toString() {
            return "DBConf{" + "driver='" + driver + '\'' + ", propUrl=" + propUrl + ", propHeader='" + propHeader + '\'' + ", propSep='" + propSep + '\''
                    + ", needProperties=" + needProperties + ", userInUrl=" + userInUrl + ", userInProp=" + userInProp + ", userParam=" + userParam + '}';
        }
    }

    public static Connection getConn(ConnConf connConf) throws ClassNotFoundException, SQLException {
        return getConnection(connConf.getDriver(), connConf.getUrl(), connConf.getUser(), connConf.getPassword(), connConf.convertProperties(),
                connConf.getJarPath());
    }

    @Getter
    @Setter
    public static class TablePage {
        @ApiModelProperty("总数据量")
        protected long total = 0L;

        @ApiModelProperty("总页数")
        protected long totalPages = 0L;

        @ApiModelProperty("数据列表")
        protected List<String> content;

        @ApiModelProperty("是否第一页")
        protected boolean first = false;

        @ApiModelProperty("是否最后一页")
        protected boolean last = false;
    }

    @Getter
    @Setter
    public static class ConnConf implements Serializable {
        private static final long serialVersionUID = -8980179341534613497L;

        private String DBType;
        private String name;
        private String chineseName;
        private String driver;
        private String host;
        private int port;
        private String url;
        private String catalog;
        private String schema;
        private int batchsize;
        private String database;
        private String user;
        private String password;
        private String defaultUrl;
        List<Map<String, String>> properties;
        private boolean dateToTimestamp;
        private boolean useSystemStore;
        private String version;
        private String jarPath;

        public Properties convertProperties() {
            Properties p = null;
            if (this.properties != null) {
                p = new Properties();
                for (Map<String, String> e : properties) {
                    Map.Entry<String, String> m = e.entrySet().iterator().next();
                    if (StringUtils.isNotEmpty(m.getKey()) && StringUtils.isNotEmpty(m.getValue())) {
                        p.setProperty(m.getKey(), m.getValue());
                    }
                }
            }
            return p;
        }
    }
}
