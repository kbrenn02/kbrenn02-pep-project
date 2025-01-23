package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    AccountDAO accountDAO;

    // Constructor to instantiate an accountDAO object
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    // Overloaded constructor in case this class is used for mocking and an accountDAO is passed in
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    // Service that implements the account registration
    public Account registerUser(Account acct){
        // Registration parameters: username is not blank and the password is at least 4 characters long
        if(acct.username != null && acct.username != "" && acct.getPassword().length() >= 4){
            // Checks if this user already exists in the DB
            if(accountDAO.loginUser(acct.getUsername()) == null){
                return accountDAO.registerUser(acct);
            }
        }
        return null;
    }

    // - The registration will be successful if and only 
    // if the username is not blank, the password is at least 
    // 4 characters long, and an Account with that username does not 
    // already exist. If all these conditions are met, the response body 
    // should contain a JSON of the Account, including its account_id. 
    // The response status should be 200 OK, which is the default. 
    // The new account should be persisted to the database.

}
// if(loggingInUser.getPassword() == acct.getPassword()){
//     return loggingInUser;
// } else {}