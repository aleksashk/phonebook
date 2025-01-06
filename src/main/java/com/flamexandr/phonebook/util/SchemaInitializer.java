package com.flamexandr.phonebook.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class SchemaInitializer {
    public static void executeSchema() {
        String filePath = "src/main/resources/sql/schema.sql";

        try (Connection connection = DatabaseUtil.getConnection();
             BufferedReader br = new BufferedReader(new FileReader(filePath));
             Statement statement = connection.createStatement()) {

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            statement.execute(sb.toString());
            System.out.println("Schema executed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
