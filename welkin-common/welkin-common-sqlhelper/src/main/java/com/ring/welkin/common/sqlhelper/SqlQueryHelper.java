package com.ring.welkin.common.sqlhelper;

import com.ring.welkin.common.core.page.IPage;
import com.ring.welkin.common.core.page.IPageable;
import com.ring.welkin.common.core.result.MapEntity;
import com.ring.welkin.common.queryapi.query.sql.SqlQuery;
import com.ring.welkin.common.queryapi.sql.AbstractQuerySql;
import com.ring.welkin.common.queryapi.sql.SqlQuerySql;
import com.ring.welkin.common.utils.DataBaseUtil;
import com.ring.welkin.common.utils.ICollections;
import com.ring.welkin.common.utils.JsonUtils;
import com.jn.sqlhelper.apachedbutils.QueryRunner;
import com.jn.sqlhelper.dialect.pagination.PagingRequest;
import com.jn.sqlhelper.dialect.pagination.PagingResult;
import com.jn.sqlhelper.dialect.pagination.SqlPaginations;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
public class SqlQueryHelper {
    private static final QueryRunner runner = new QueryRunner();
    private static final HiveQueryRunner hiveRunner = new HiveQueryRunner();
    private static final MapEntityListHandler rsh = new MapEntityListHandler();

    public static Connection getConn(JdbcConf conf) throws Exception {
        return getConn(conf.getDriver(), conf.getUrl(), conf.getUsername(), conf.getPassword(), conf.getProps(), conf.getJarPath());
    }

    public static Connection getConn(String driver, String url, String username, String password) throws Exception {
        return getConn(driver, url, username, password);
    }

    public static Connection getConn(String driver, String url, String username, String password, Properties params, String jarPath) throws Exception {
        try {
            log.debug("get conn from conf: {driver:{}, url:{}, username:{}, password:*****, params:{}, jarPath:{}}", driver, url, username, params, jarPath);
            return DataBaseUtil.getConnection(driver, url, username, password, params, jarPath);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    public static IPage<MapEntity> executeQuery(JdbcConf conf, String selectSql, int limit) throws Exception {
        return executeQuery(conf, selectSql, IPageable.limit(limit));
    }

    public static IPage<MapEntity> executeQuery(JdbcConf conf, String selectSql, IPageable pageable) throws Exception {
        return executeQuery(conf, SqlQuerySql.builder(SqlQuery.builder().sqlTemplate(selectSql)), pageable);
    }

    public static IPage<MapEntity> executeQuery(JdbcConf conf, AbstractQuerySql<?> selectSql, int limit) throws Exception {
        return executeQuery(conf, selectSql, IPageable.limit(limit));
    }

    public static IPage<MapEntity> executeQuery(JdbcConf conf, AbstractQuerySql<?> selectSql, IPageable pageable) throws Exception {
        return executeQuery(getConn(conf), selectSql, pageable);
    }

    public static IPage<MapEntity> executeQuery(String driver, String url, String username, String password, Properties params, String jarPath,
                                                AbstractQuerySql<?> selectSql, IPageable pageable) throws Exception {
        return executeQuery(getConn(driver, url, username, password, params, jarPath), selectSql, pageable);
    }

    public static IPage<MapEntity> executeQuery(Connection conn, AbstractQuerySql<?> selectSql, IPageable pageable) throws Exception {
        PagingRequest<?, MapEntity> request = null;
        try {
            log.debug("===>>start a sql query <<=== ");

            if (pageable != null && !pageable.isPageable()) {
                pageable.setPageSize(Integer.MAX_VALUE);
            }

            debug(selectSql, pageable);
            if (selectSql.isValid() && conn != null) {
                request = SqlPaginations.preparePagination(pageable.getPageNum(), pageable.getPageSize());
                List<MapEntity> list = runner.query(conn, selectSql.getPlaceholderSql(), rsh, selectSql.getParamValues());
                log.debug("===>>query result size:" + list.size());
                PagingResult<MapEntity> result = request.getResult();
                if (result != null) {
                    return IPage.<MapEntity>of(pageable, result.getTotal(), result.getItems());
                }else {
                    if (ICollections.hasElements(list)){
                        return IPage.<MapEntity>of(pageable, list.size(), list);
                    }
                }
            }
            log.debug("===>>end a sql query <<=== ");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
            if (request != null) {
                request.clear(true);
            }
        }
        return IPage.<MapEntity>of(pageable, 0, null);
    }

    public static <T extends Number> IPage<MapEntity> executeQuery(String driver, String url, String username, String password, Properties params,
                                                                   String jarPath, AbstractQuerySql<?> selectSql, IPageable pageable, ScalarHandler<T> scalarHandler, String limit) throws Exception {
        return executeQuery(getConn(driver, url, username, password, params, jarPath), selectSql, pageable, scalarHandler, limit);
    }

    public static <T extends Number> IPage<MapEntity> executeQueryHive(String driver, String url, String username, String password, Properties params,
                                                                       String jarPath, AbstractQuerySql<?> selectSql, IPageable pageable, ScalarHandler<T> scalarHandler) throws Exception {
        return executeQueryHive(getConn(driver, url, username, password, params, jarPath), selectSql, pageable, scalarHandler);
    }

    public static <T extends Number> IPage<MapEntity> executeQueryHive(Connection conn, AbstractQuerySql<?> selectSql, IPageable pageable,
                                                                       ScalarHandler<T> scalarHandler) throws Exception {
        try {
            log.debug("===>>start a sql query <<=== ");
            debug(selectSql, pageable);

            if (selectSql.isValid() && conn != null) {
//                if (pageable.getPageable() ) {
//                String countSql = selectSql.getPlaceholderCountSql();
//                //countSql = getCountSql(countSql);
//                long totalCount = hiveRunner.queryHive(conn, countSql, scalarHandler, selectSql.getParamValues()) == null ? (0L): hiveRunner.queryHive(conn, countSql, scalarHandler, selectSql.getParamValues()).longValue();
//                log.debug("===>>query count size:" + totalCount);
//                if (totalCount > 0) {
                List<MapEntity> list = hiveRunner.queryHive(conn,
                        formatSql(selectSql.getPlaceholderSql(), pageable.getOffset(), pageable.getPageSize()), rsh, selectSql.getParamValues());
                if (list == null) {
                    list = new ArrayList<>();
                }
                log.debug("===>>query result size:" + list.size());
                return IPage.<MapEntity>of(pageable, list.size(), list);
                //}
//                } else {
//                    List<MapEntity> list = hiveRunner.queryHive(conn, selectSql.getPlaceholderSql(), rsh, selectSql.getParamValues());
//                    log.debug("===>>query result size:" + list.size());
//                    return IPage.<MapEntity>of(pageable, list.size(), list);
//                }
            }
            log.debug("===>>end a sql query <<=== ");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return IPage.<MapEntity>of(pageable, 0, null);
    }

    /**
     * 格式化sql，转换为hive支持的分页查询形式
     *
     * @param sql
     * @return
     */
    private static String getCountSql(String sql){
        StringBuilder strBuilder = new StringBuilder();
        if(sql != null && sql.indexOf("HAVING") > -1){

            String[] array =sql.split("HAVING");
            strBuilder.append(array[0]);
            strBuilder.append(" HAVING ");
            strBuilder.append(array[1].replaceAll("_avg","").replaceAll("_sum","").replaceAll("_max","").replaceAll("_min","").replaceAll("_count",""));
            sql = strBuilder.toString();
        }

        return sql;
    }
    /**
     * 格式化sql，转换为hive支持的分页查询形式
     *
     * @param sql
     * @return
     */
    private static String formatSql(String sql, long offset, int pageSize) {

        if (sql == null || "".equals(sql)) {
            return "";
        }
//        String where = "";
//        String groupby = "";
//        String orderby = "";

        //sql = getCountSql(sql);

        String groupBy = "";
        String[] array = sql.split("GROUP BY");
        String[] array1 = sql.split("SELECT");
        StringBuilder sb = new StringBuilder();
        if(sql.indexOf("AVG(") > -1 || sql.indexOf("SUM(") > -1
                || sql.indexOf("MAX(") > -1 || sql.indexOf("MIN(") > -1 || sql.indexOf("COUNT(") > -1){
            sb.append("SELECT *  FROM (SELECT row_number() over (ORDER BY ");
            // String sql = " test_string1 , SUM(test_int) test_int_avg";
//            String selectSql = array1[1].split("FROM")[0];
//            String[] selectArray = selectSql.split(",");
//            sb.append("SELECT ");
//            for(int i=0;i<selectArray.length ;i++){
//                String str = selectArray[i];
//                String[] arr = str.split(" ");
//
//                for(String s : arr){
//                    if(s == null || "".equals(s) || s.indexOf("(") > -1){
//                        continue;
//                    }
//
//                    sb.append( s);
//                    if(i != selectArray.length -1){
//                        sb.append( ",");
//                    }
//                }
//            }
//            sb.append(" FROM (SELECT row_number() over (ORDER BY ");
        }else{
            sb.append("SELECT ");
            sb.append(array1[1].split("FROM")[0]);
            sb.append(" FROM (SELECT row_number() over (ORDER BY ");
        }
//        sb.append("SELECT *  FROM (SELECT row_number() over (ORDER BY ");
        if(array != null && array.length == 2){
            if(array[1].indexOf("ORDER BY") > -1){
                groupBy = array[1].split("ORDER BY")[1];
               // groupBy = array[1].split("ORDER BY")[1].split(" ")[1];
            }else if(array[1].indexOf("HAVING") > -1){
                groupBy = array[1].split("HAVING")[0];
            } else{
                groupBy = array[1];
            }

            sb.append(groupBy);
        }else{
            if(array1[1].indexOf("ORDER BY") > -1){
                sb.append(array1[1].split("ORDER BY")[1]);
            }else{
                sb.append(array1[1].split("FROM")[0].split(",")[0]);
            }
        }
        sb.append(" ) AS rownum, ");

        if("".equals(groupBy)){
            sb.append(array1[1]);
        }else{
            //groupBy = array1[1].split("ORDER BY")[1].split(" ")[1];
            String params = array[1].split("HAVING")[0];
            sb.append(array1[1].replaceAll("\\*",params));
        }
        long size = offset + pageSize;
        sb.append(")t WHERE t.rownum BETWEEN "+ offset + " and " + size);
        log.debug("===>>execute sql  <<=== {}",sb.toString());
        return sb.toString();
//        if(sql.indexOf("AVG(") > -1 || sql.indexOf("SUM(") > -1
//                || sql.indexOf("MAX(") > -1 || sql.indexOf("MIN(") > -1 || sql.indexOf("COUNT(") > -1){
//            return "SELECT mm. *  FROM (SELECT row_number() over (ORDER BY test_string1 ) AS rownum, u.test_string1 , AVG(u.test_int) test_int_avg FROM test_hive_parquet u GROUP BY test_string1 )mm WHERE mm.rownum BETWEEN 0 AND 10";
//        }else{
//            String[] temp = sql.split(" FROM");
//            String select = temp[0];
//            String[] temp1 = temp[1].split(" WHERE");
//            String table = temp1[0];
//            // from (select row_number() over (order by * ) as rownum, u. * , u. AVG(time1)
//            // time1_avg from hive05181530 GROUP BY time1 u )mm where mm.rownum between 0
//            // and 10
//            if (temp1.length > 1) {
//                where = temp1[1];
//            } else {
//                String[] temp2 = table.split("GROUP BY");
//                if (temp2.length > 1) {
//                    table = temp2[0];
//                    groupby = temp2[1];
//                }
//
//                String[] temp3 = table.split("ORDER BY");
//                if (temp3.length > 1) {
//                    table = temp3[0];
//                    orderby = temp3[1];
//                }
//            }
//
//            // select * from (select row_number() over (order by create_time desc) as
//            // rownum,u.* from user u) mm where mm.rownum between 10 and 15;
//            String[] array = select.split("SELECT ");
//            if (array.length != 2) {
//                //
//            }
//            StringBuilder sb = new StringBuilder();
//            StringBuilder sbrow = new StringBuilder();
//            sbrow.append(" from (select row_number() over (order by ");
//
//            String[] field = array[1].split(",");
//            // sb.append(field[0]);
//
//            sb.append(" ) as rownum,");
//
//            StringBuilder sbField = new StringBuilder();
//            for (int i = 0; i < field.length; i++) {
//                if (i == field.length - 1) {
//                    sb.append(" u." + field[i]);
//                    sbField.append(" mm." + field[i]);
//                } else {
//                    sb.append(" u." + field[i] + " ,");
//                    sbField.append(" mm." + field[i] + " ,");
//                }
//            }
//
//            sb.append(" from " + table + " u ");
//            if (!"".equals(where)) {
//                sb.append(" where " + where);
//            }
//            if (!"".equals(groupby)) {
//                sb.append(" group by " + groupby);
//            }
//            if (!"".equals(orderby)) {
//                sb.append(" order by " + orderby);
//                sbrow.append(orderby);
//            } else {
//                sbrow.append(field[0]);
//            }
//            long size = offset + pageSize;
//            sb.append(" )mm where mm.rownum between " + offset + " and " + size);
//            return "select " + sbField.toString() + sbrow.toString() + sb.toString();
//        }
    }

    public static void main(String[] args) {
        String str = "  *, AVG(time1) time1_avg ";
        if (str.indexOf("AVG(") > 0) {
            String a = str.substring(str.indexOf("AVG(") + 4, str.indexOf(")"));
            System.out.println(a);
        }
    }

    public static <T extends Number> IPage<MapEntity> executeQuery(Connection conn, AbstractQuerySql<?> selectSql, IPageable pageable,
                                                                   ScalarHandler<T> scalarHandler, String limit) throws Exception {
        try {
            log.debug("===>>start a sql query <<=== ");
            debug(selectSql, pageable);
            if (selectSql.isValid() && conn != null) {
                String countSql = selectSql.getPlaceholderCountSql();
                long totalCount = runner.query(conn, countSql, scalarHandler, selectSql.getParamValues()).longValue();
                // long totalCount = 5L;
                log.debug("===>>query count size:" + totalCount);
                if (totalCount > 0) {
//                    List<MapEntity> list = runner.query(conn, selectSql.getPlaceholderSql() + " " + limit, rsh,
//                        selectSql.getParamValues());
                    List<MapEntity> list = runner.query(conn, selectSql.getPlaceholderSql() + " " + limit, rsh, selectSql.getParamValues());
                    log.debug("===>>query result size:" + list.size());
                    return IPage.<MapEntity>of(pageable, totalCount, list);
                }
            }
            log.debug("===>>end a sql query <<=== ");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return IPage.<MapEntity>of(pageable, 0, null);
    }

    private static void debug(AbstractQuerySql<?> selectSql, IPageable pageable) {
        log.debug("===>>PlaceholderCountSql:" + selectSql.getPlaceholderCountSql());
        log.debug("===>>PlaceholderSql:" + selectSql.getPlaceholderSql());
        log.debug("===>>CountSql:" + selectSql.getCountSql());
        log.debug("===>>ExecuteSql:" + selectSql.getExecuteSql());
        log.debug("===>>ParamValues:" + Arrays.toString(selectSql.getParamValues()));
        log.debug("===>>Pageable:" + JsonUtils.toJson(pageable));
    }
}
