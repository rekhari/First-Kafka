package com.kafka;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.stream.IntStream;

public class Test {
    public static void main(String[] args) throws SQLException {
        String user="postgres";
        String pass="postgres";
        String url="jdbc:postgresql://pr4-centos:5432/test?currentSchema=First";
        Connection conn = DriverManager.getConnection(url,user,pass);
        System.out.println(conn);
    }
}
