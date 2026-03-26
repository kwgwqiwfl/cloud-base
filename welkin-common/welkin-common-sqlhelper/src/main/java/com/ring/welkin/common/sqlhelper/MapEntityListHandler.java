package com.ring.welkin.common.sqlhelper;

import com.ring.welkin.common.core.result.MapEntity;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.AbstractListHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapEntityListHandler extends AbstractListHandler<MapEntity> {

    /**
     * The RowProcessor implementation to use when converting rows into Maps.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of MapListHandler using a
     * <code>BasicRowProcessor</code> for conversion.
     */
    public MapEntityListHandler() {
        this(new BasicRowProcessor());
    }

    /**
     * Creates a new instance of MapListHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation to use when
     *                converting rows into Maps.
     */
    public MapEntityListHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Converts the <code>ResultSet</code> row into a <code>Map</code> object.
     *
     * @param rs <code>ResultSet</code> to process.
     * @return A <code>Map</code>, never null.
     * @throws SQLException if a database access error occurs
     * @see org.apache.commons.dbutils.handlers.AbstractListHandler#handle(ResultSet)
     */
    @Override
    protected MapEntity handleRow(ResultSet rs) throws SQLException {
        return MapEntity.from(this.convert.toMap(rs));
    }

}
