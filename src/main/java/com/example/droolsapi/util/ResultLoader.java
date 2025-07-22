package com.example.droolsapi.util;

import com.example.droolsapi.model.CommonDataModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class ResultLoader {

    public static void saveResults(List<Object> facts, String id) {
        String sql = "INSERT INTO STREAMLIT_RESULTS_KIS (id, rule_name, rule_flag, is_active, audit_create_dt) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

        try (Connection conn = FactsExtractor.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Object obj : facts) {
                if (obj instanceof CommonDataModel model && model.isRuleMatched()) {
                    stmt.setString(1, model.getId());
                    stmt.setString(2, model.getRuleName());
                    stmt.setString(3, "TRUE");
                    stmt.setString(4, "Y");
                    stmt.addBatch();
                }
            }
            stmt.executeBatch();
        } catch (Exception e) {
            throw new RuntimeException("Error saving results: " + e.getMessage(), e);
        }
    }
}
