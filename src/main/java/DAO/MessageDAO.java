package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Message> getAllMessages(){
        Connection conn = ConnectionUtil.getConnection();
        List<Message> msgs = new ArrayList<>();

        try {
            // Define SQL statement
            String sql = "SELECT * FROM message";
            
            // Create prepared statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            // Loop through results. Create a message object and add to the msgs list
            while(rs.next()){
                Message msg = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                msgs.add(msg);
            }
            // return the list of all messages
            return msgs;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message getMessageByID(int id){
        Connection conn = ConnectionUtil.getConnection();

        try {
            // Define SQL statement
            String sql = "SELECT * FROM message WHERE message_id = ?";
            
            // Create prepared statement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Message msg = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                return msg;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message deleteMessage(int id){
        Connection conn = ConnectionUtil.getConnection();
        Message deletedMsg = null;

        try {
            // Get message before deleting
            deletedMsg = getMessageByID(id);

            if(deletedMsg != null){
                // Define SQL statement
                String sql = "DELETE FROM message WHERE message_id = ?";
                // Create preparedStatement
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            return deletedMsg;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Message updateMessage(int id, Message msg){
        Connection conn = ConnectionUtil.getConnection();

        try {
            // Define SQL statement
            String sql = "UPDATE message SET posted_by = ?, message_text = ?, time_posted_epoch = ? WHERE message_id = ?";

            // Create preparedStatement
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, msg.getPosted_by());
            ps.setString(2, msg.getMessage_text());
            ps.setLong(3, msg.getTime_posted_epoch());
            ps.setInt(4, id);

            ps.executeUpdate();

            return getMessageByID(id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
