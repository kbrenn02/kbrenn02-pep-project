package Service;

import Model.Account;
import DAO.AccountDAO;

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

    // Service that implements user loging functionality
    public Account loginUser(Account acct){
        Account retrievedAcct = accountDAO.loginUser(acct.getUsername());
        // Check if the retrieved account is null (username does not exist in the DB)
        if(retrievedAcct == null){
            return null;
        } else if(acct.getPassword().equals(retrievedAcct.getPassword())){ // checks if the input password and the retrieved password are the same
            return retrievedAcct;
        }
        return null;
    }
}