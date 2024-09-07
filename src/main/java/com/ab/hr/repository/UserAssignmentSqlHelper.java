package com.ab.hr.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.relational.core.sql.Column;
import org.springframework.data.relational.core.sql.Expression;
import org.springframework.data.relational.core.sql.Table;

public class UserAssignmentSqlHelper {

    public static List<Expression> getColumns(Table table, String columnPrefix) {
        List<Expression> columns = new ArrayList<>();
        columns.add(Column.aliased("id", table, columnPrefix + "_id"));
        columns.add(Column.aliased("order_of_questions", table, columnPrefix + "_order_of_questions"));
        columns.add(Column.aliased("total_duration_in_mins", table, columnPrefix + "_total_duration_in_mins"));
        columns.add(Column.aliased("access_url", table, columnPrefix + "_access_url"));
        columns.add(Column.aliased("access_expiry_date", table, columnPrefix + "_access_expiry_date"));
        columns.add(Column.aliased("user_assignment_status", table, columnPrefix + "_user_assignment_status"));
        columns.add(Column.aliased("assigned_at", table, columnPrefix + "_assigned_at"));
        columns.add(Column.aliased("joined_at", table, columnPrefix + "_joined_at"));
        columns.add(Column.aliased("finished_at", table, columnPrefix + "_finished_at"));

        columns.add(Column.aliased("user_id", table, columnPrefix + "_user_id"));
        columns.add(Column.aliased("assignment_id", table, columnPrefix + "_assignment_id"));
        return columns;
    }
}
