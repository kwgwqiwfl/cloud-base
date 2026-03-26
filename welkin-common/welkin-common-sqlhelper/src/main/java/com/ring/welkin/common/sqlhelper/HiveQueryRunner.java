package com.ring.welkin.common.sqlhelper;


import com.jn.sqlhelper.apachedbutils.QueryRunner;
import com.jn.sqlhelper.common.utils.SQLs;
import com.jn.sqlhelper.dialect.pagination.PagingRequestContextHolder;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.StatementConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HiveQueryRunner extends QueryRunner {
    private static final PagingRequestContextHolder PAGING_CONTEXT = PagingRequestContextHolder.getContext();

    /**
     * Constructor for QueryRunner.
     */
    public HiveQueryRunner() {
        super();
    }

    /**
     * Constructor for QueryRunner that controls the use of <code>ParameterMetaData</code>.
     *
     * @param pmdKnownBroken Some drivers don't support {@link java.sql.ParameterMetaData#getParameterType(int) };
     *                       if <code>pmdKnownBroken</code> is set to true, we won't even try it; if false, we'll try it,
     *                       and if it breaks, we'll remember not to use it again.
     */
    public HiveQueryRunner(boolean pmdKnownBroken) {
        super(pmdKnownBroken);
    }

    /**
     * Constructor for QueryRunner that takes a <code>DataSource</code> to use.
     * <p>
     * Methods that do not take a <code>Connection</code> parameter will retrieve connections from this
     * <code>DataSource</code>.
     *
     * @param ds The <code>DataSource</code> to retrieve connections from.
     */
    public HiveQueryRunner(DataSource ds) {
        super(ds);
    }

    /**
     * Constructor for QueryRunner that takes a <code>StatementConfiguration</code> to configure statements when
     * preparing them.
     *
     * @param stmtConfig The configuration to apply to statements when they are prepared.
     */
    public HiveQueryRunner(StatementConfiguration stmtConfig) {
        super(stmtConfig);
    }

    /**
     * Constructor for QueryRunner that takes a <code>DataSource</code> and controls the use of <code>ParameterMetaData</code>.
     * Methods that do not take a <code>Connection</code> parameter will retrieve connections from this
     * <code>DataSource</code>.
     *
     * @param ds             The <code>DataSource</code> to retrieve connections from.
     * @param pmdKnownBroken Some drivers don't support {@link java.sql.ParameterMetaData#getParameterType(int) };
     *                       if <code>pmdKnownBroken</code> is set to true, we won't even try it; if false, we'll try it,
     *                       and if it breaks, we'll remember not to use it again.
     */
    public HiveQueryRunner(DataSource ds, boolean pmdKnownBroken) {
        super(ds, pmdKnownBroken);
    }

    /**
     * Constructor for QueryRunner that takes a <code>DataSource</code> to use and a <code>StatementConfiguration</code>.
     * <p>
     * Methods that do not take a <code>Connection</code> parameter will retrieve connections from this
     * <code>DataSource</code>.
     *
     * @param ds         The <code>DataSource</code> to retrieve connections from.
     * @param stmtConfig The configuration to apply to statements when they are prepared.
     */
    public HiveQueryRunner(DataSource ds, StatementConfiguration stmtConfig) {
        super(ds, stmtConfig);
    }

    /**
     * Constructor for QueryRunner that takes a <code>DataSource</code>, a <code>StatementConfiguration</code>, and
     * controls the use of <code>ParameterMetaData</code>.  Methods that do not take a <code>Connection</code> parameter
     * will retrieve connections from this <code>DataSource</code>.
     *
     * @param ds             The <code>DataSource</code> to retrieve connections from.
     * @param pmdKnownBroken Some drivers don't support {@link java.sql.ParameterMetaData#getParameterType(int) };
     *                       if <code>pmdKnownBroken</code> is set to true, we won't even try it; if false, we'll try it,
     *                       and if it breaks, we'll remember not to use it again.
     * @param stmtConfig     The configuration to apply to statements when they are prepared.
     */
    public HiveQueryRunner(DataSource ds, boolean pmdKnownBroken, StatementConfiguration stmtConfig) {
        super(ds, pmdKnownBroken, stmtConfig);
    }

    /**
     * Calls query after checking the parameters to ensure nothing is null.
     *
     * @param conn      The connection to use for the query call.
     * @param closeConn True if the connection should be closed, false otherwise.
     * @param sql       The SQL statement to execute.
     * @param params    An array of query replacement parameters.  Each row in
     *                  this array is one set of batch replacement values.
     * @return The results of the query.
     * @throws SQLException If there are database or parameter errors.
     */
    public <T> T queryHive(Connection conn, String sql, ResultSetHandler<T> rsh, Object... params)
            throws SQLException {
        boolean closeConn = false;
        if (conn == null) {
            throw new SQLException("Null connection");
        }

        if (sql == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null SQL statement");
        }

        if (rsh == null) {
            if (closeConn) {
                close(conn);
            }
            throw new SQLException("Null ResultSetHandler");
        }

        PreparedStatement stmt = null;
        ResultSet rs = null;
        T r = null;

        try {
            if (!PAGING_CONTEXT.isPagingRequest() || !SQLs.isSelectStatement(sql) || SQLs.isSelectCountStatement(sql)) {
                stmt = this.prepareStatement(conn, sql);
                this.fillStatement(stmt, params);
                rs = this.wrap(stmt.executeQuery());
                r = rsh.handle(rs);
            } else {
                //r = doPagingQuery(conn, sql, rsh, params);
            }
        } catch (SQLException e) {
            this.rethrow(e, sql, params);

        } finally {
            try {
                close(rs);
            } finally {
                close(stmt);
                if (closeConn) {
                    close(conn);
                }
            }
        }

        return r;
    }

    /**
     * Fill the <code>PreparedStatement</code> replacement parameters with the
     * given objects.
     *
     * @param stmt
     *            PreparedStatement to fill
     * @param params
     *            Query replacement parameters; <code>null</code> is a valid
     *            value to pass in.
     * @throws SQLException
     *             if a database access error occurs
     */
    @Override
    public void fillStatement(PreparedStatement stmt, Object... params)
            throws SQLException {

        if(params == null || params.length == 0) return;
        int length = params.length;
        int sqlParameterIndex;
        for (int i = 0; i < length; i++){

            Object param = params[i];
            sqlParameterIndex = i+1;
            if(param instanceof Number) {
                stmt.setInt(sqlParameterIndex,((Number)param).intValue());
            }else if(param instanceof String){
                stmt.setString(sqlParameterIndex,(String)param);
            }else if (param instanceof Boolean){
                stmt.setBoolean(sqlParameterIndex,(Boolean)param);
            }else{
                StringBuilder errStrBuilder = new StringBuilder();
                errStrBuilder.append("No mapping available for param type: ")
                        .append(param.getClass().getSimpleName())
                        .append(" value: ")
                        .append(param);
                throw new RuntimeException(errStrBuilder.toString());
            }
        }
    }
}
