package com.ab.hr.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UploadSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("url", table, columnPrefix + "_url"));
        columns.add(Column.aliased("type", table, columnPrefix + "_type"));
        columns.add(Column.aliased("extension", table, columnPrefix + "_extension"));
        columns.add(Column.aliased("upload_date", table, columnPrefix + "_upload_date"));

        columns.add(Column.aliased("user_profile_id", table, columnPrefix + "_user_profile_id"));
        return columns;
    }
}
