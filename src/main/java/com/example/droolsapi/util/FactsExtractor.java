package com.example.droolsapi.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FactsExtractor {

    private static final String JDBC_URL = "jdbc:snowflake://<your_account>.snowflakecomputing.com";
    private static final String USER = "<username>";
    private static final String PASSWORD = "<password>";
    private static final String DATABASE = "<db>";
    private static final String SCHEMA = "<schema>";
    private static final String WAREHOUSE = "<warehouse>";

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(
                JDBC_URL + "/?db=" + DATABASE + "&schema=" + SCHEMA + "&warehouse=" + WAREHOUSE,
                USER, PASSWORD);
    }

    public static List<String> loadUniqueIds(int offset, int limit) {
        List<String> ids = new ArrayList<>();
        String sql = "SELECT DISTINCT id FROM ELEMENT_DATA_TABLE LIMIT ? OFFSET ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ids.add(rs.getString("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load IDs: " + e.getMessage(), e);
        }

        return ids;
    }

    public static List<Object> loadFactsForId(String id) {
        List<Object> facts = new ArrayList<>();
        String sql = "SELECT * FROM ELEMENT_DATA_TABLE WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // You must map to your actual Java Fact class
                facts.add(ResultMapper.mapRowToFact(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load facts for ID " + id + ": " + e.getMessage(), e);
        }

        return facts;
    }
}
