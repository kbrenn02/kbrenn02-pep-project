package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    public Account registerUser(Account acct){
        // Create connection
        Connection conn = ConnectionUtil.getConnection();

        try {
            // Prepare SQL string for insertion
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

            // Prepare the prepared statement and insert the values from acct (making sure to return generated keys)
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, acct.getUsername());
            ps.setString(2, acct.getPassword());

            // Execute prepared statement
            ps.executeUpdate();

            // Return the newly created account (with it's ID that is autogenerated)
            ResultSet returnAccts = ps.getGeneratedKeys();
            if(returnAccts.next()){
                int generated_account_id = (int) returnAccts.getLong(1);
                return new Account(generated_account_id, acct.getUsername(), acct.getPassword());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return acct;
    }

    
    // Since usernames are unique values in the DB, I decided to just return an account with the given username instead
    // of working with an Account object
    public Account loginUser(String username){
        // Create connection
        Connection conn = ConnectionUtil.getConnection();

        try {
            // Prepare SQL string for insertion
            String sql = "SELECT * FROM account WHERE username = ?";

            // Prepare the prepared statement and insert the username
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            // Execute prepared statement
            ResultSet rs = ps.executeQuery();

            // Return the logged in account (with it's ID that is autogenerated) by creating a new Account object with the ID
            while (rs.next()){
                Account loggingInUser = new Account(rs.getInt("account_id"), rs.getString("username"), 
                        rs.getString("password"));
                return loggingInUser;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public Account getUser(int id){
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Account returnedUser = new Account(rs.getInt("account_id"), rs.getString("username"), 
                        rs.getString("password"));
                return returnedUser;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
    
}
