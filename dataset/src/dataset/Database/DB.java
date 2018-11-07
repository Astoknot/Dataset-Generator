/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataset.Database;

import java.io.FileWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

/**
 *
 * @author lekhika
 */
public class DB {

    public void insert(int counter, String src, String des, String ipsrc, String ipdes, String protocol, String blob) {
        try {
            // create a mysql database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/soe",
                    "root",
                    "lekhika");

            // create a sql date object so we can use it in our INSERT statement
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            String timestamp = sdf.format(calendar.getTime());

            // the mysql insert statement
            String query = " insert into packets(timestamp,eth_src,eth_dest,ip_src,ip_dest,protocol,data)"
                    + " values (?, ?, ?, ?, ?,?,?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStmt.setString(1, timestamp);
            preparedStmt.setString(2, src);
            preparedStmt.setString(3, des);
            preparedStmt.setString(4, ipsrc);
            preparedStmt.setString(5, ipdes);
            preparedStmt.setString(6, protocol);
            preparedStmt.setString(7, blob);
            
            //Save to log file
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("select count(*) from meta");
            String fileName="File";
            int count=0;
            
            while (resultSet.next()) {
                count = resultSet.getInt(1)+1;
            }
            fileName+= String.valueOf(count);
            
            FileWriter fw = new FileWriter(fileName,true);
            
            Scanner sc=new Scanner(fileName);
            fw.write("S.No.: "+counter+" || Timestamp: "+timestamp+" || MAC Src Addr: "+src+" || MAC Dest Addr: "+des
                    + " || IP Src Addr: "+ipsrc+" || IP Dest Addr: "+ipdes
                    + " || Protocol: "+protocol+" || Details: "+blob+"\n\n");
            fw.close();

            // execute the preparedstatement
            preparedStmt.execute();

            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
        }
    }

    public void insertMeta(String startTime, String endTime, long duration) {
        try {
            

            // create a mysql database connection
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/soe", "root", "lekhika");

            // the mysql insert statement
            String query = " insert into meta(start_time,end_time,duration)"
                    + " values (?, ?, ?)";
            

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStmt.setString(1, startTime);
            preparedStmt.setString(2, endTime);
            preparedStmt.setLong(3, duration);

            // execute the preparedstatement
            preparedStmt.execute();

            conn.close();
        } catch (Exception e) {
            System.err.println("Got an exception!!!");
            System.err.println(e.getMessage());
        }
    }

}
