package com.may.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnectOracle {

    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet result = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("准备连接数据库...");
            String url = "jdbc:oracle:thin:@10.10.10.7:1521/yjppdb";
            String user = "dingnuo";
            String pass = "dingnuo";
            conn = DriverManager.getConnection(url,user,pass);
            System.out.println("连接成功...");
            String sql = "select table_name from all_tab_columns where column_name = 'ID_CARD'";
            pre = conn.prepareStatement(sql);
            result = pre.executeQuery();
            while (result.next()){
                System.out.println(result.getString("TABLE_NAME"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (result != null){
                    result.close();
                }
                if (pre != null){
                    pre.close();
                }
                if (conn != null){
                    conn.close();
                }
                System.out.println("数据库已关闭连接...");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
