/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author Binit
 */
@Path("message")
public class Message {

    public static String msg;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Message
     */
    public Message() {
    }

    @GET
    @Path("/users/{login}/{password}")
    @Produces("application/json")
    public String getUserById(@PathParam("login") String login, @PathParam("password") String password) {

        int count = 0;

        String url = "jdbc:mysql://localhost:3306/test";
        
        String driver = "com.mysql.jdbc.Driver";
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url);

            String query = "select * from user where username = ? and password = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, login);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            String u = "";
            String p = "";

            while (rs.next()) {
                count++;
                u = rs.getString("username");
                p = rs.getString("password");
            }

        } catch (Exception e) {
            return e.getMessage();//e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        }
        if (count == 0) {
            return "Invalid user";
        }
        return "Valid user";
    }

    @GET
    @Path("/messages/{from}/{to}/{msg}/")
    @Produces("application/json")
    public String putMsg(@PathParam("from") String from, @PathParam("to") String to, @PathParam("msg") String message) {

        int count = 0;

        String url = "jdbc:mysql://localhost:3306/test";
        
        String driver = "com.mysql.jdbc.Driver";
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url);

            String query = "insert into messages values (?,?,?)";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(from));
            stmt.setInt(2, Integer.parseInt(to));
            stmt.setString(3, message);

            stmt.execute();
            
        } catch (Exception e) {
            return e.getMessage();//e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        }
        if (count == 0) {
            return "0";
        }
        return "1";
    }
    
    @GET
    @Path("/getmsg/{uId}/{myId}")
    @Produces("application/json")
    public String getMsg(@PathParam("uId") String uId, @PathParam("myId") String myId) {

        String messages = "";

        String url = "jdbc:mysql://localhost:3306/test";
        
        String driver = "com.mysql.jdbc.Driver";
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url);

            String query = "select * from messages where (fromId = ? and toId = ?) or (toId = ? and fromId = ?)";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(uId));
            stmt.setInt(2, Integer.parseInt(myId));
            stmt.setInt(3, Integer.parseInt(uId));
            stmt.setInt(4, Integer.parseInt(myId));

            ResultSet rs = stmt.executeQuery();
            int from = 0;
            int to = 0;
            String msg = "";
            while (rs.next()) {
                from = rs.getInt("fromId");
                to = rs.getInt("toId");
                msg = rs.getString("message");
                messages = messages+from +" " + to + " "+msg+"\n";
            }
            
            
        } catch (Exception e) {
            return e.getMessage();//e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        }

        return messages;
    }
}
