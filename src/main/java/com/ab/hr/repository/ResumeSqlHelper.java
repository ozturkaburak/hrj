package com.ab.hr.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class ResumeSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("file", table, columnPrefix + "_file"));
        columns.add(Column.aliased("file_content_type", table, columnPrefix + "_file_content_type"));
        columns.add(Column.aliased("file_type", table, columnPrefix + "_file_type"));
        columns.add(Column.aliased("upload_date", table, columnPrefix + "_upload_date"));

        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));
        return columns;
    }
}
