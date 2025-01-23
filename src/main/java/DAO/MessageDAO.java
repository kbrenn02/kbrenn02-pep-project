package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;

public class MessageDAO {

    public Message postMessage(Message msg){
        // Create connection
        Connection conn = ConnectionUtil.getConnection();

        try {
            // Define SQL statement
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

            // Create prepared statement and input data
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, msg.getPosted_by());
            ps.setString(2, msg.getMessage_text());
            ps.setLong(3, msg.getTime_posted_epoch());

            ps.executeUpdate();

            ResultSet returnMsgsRS = ps.getGeneratedKeys();

            if(returnMsgsRS.next()){
                int generated_msg_id = (int) returnMsgsRS.getLong(1);
                return new Message(generated_msg_id, msg.getPosted_by(), msg.getMessage_text(), msg.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
    
}
