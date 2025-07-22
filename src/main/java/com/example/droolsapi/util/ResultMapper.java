package com.example.droolsapi.util;

import com.example.droolsapi.model.CommonDataModel;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultMapper {

    public static CommonDataModel mapRowToFact(ResultSet rs) throws SQLException {
        CommonDataModel fact = new CommonDataModel();
        fact.setId(rs.getString("id"));
        fact.setElmNbr(rs.getString("elm_nbr"));
        fact.setName(rs.getString("name"));
        return fact;
    }
}
