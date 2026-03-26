package com.ring.welkin.common.persistence.jpa.support.dialect;

import org.hibernate.dialect.Oracle12cDialect;

import java.sql.Types;

/**
 * @author EDY
 */
public class OceanBaseForOracleDialect extends Oracle12cDialect {

    public OceanBaseForOracleDialect(){
        super();
    }

    @Override
    protected void registerLargeObjectTypeMappings() {
        registerColumnType( Types.DOUBLE, "number($p,$s)" );

        registerColumnType( Types.VARCHAR,"varchar2($l char)" );
        registerColumnType( Types.VARCHAR, 4000, "varchar2($l char)" );

        registerColumnType( Types.BINARY, 2000, "blob" );
        registerColumnType( Types.BINARY, "blob" );

        registerColumnType( Types.VARBINARY, 2000, "blob" );
        registerColumnType( Types.VARBINARY, "blob" );

        registerColumnType( Types.BLOB, "blob" );
        registerColumnType( Types.CLOB, "clob" );

        registerColumnType( Types.LONGVARCHAR, "clob" );
        registerColumnType( Types.LONGVARBINARY, "blob" );

    }

}
