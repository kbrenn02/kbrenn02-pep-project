package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {

    public Account registerUser(Account acct){
        Connection conn = ConnectionUtil.getConnection();

        try {
            // Prepare SQL string for insertion
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";

            // Prepare the prepared statement and insert the values from acct
            PreparedStatement ps = conn.prepareStatement(sql);
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

    public Account loginUser(String username){
        Connection conn = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT * FROM account WHERE username = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

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
    
}
