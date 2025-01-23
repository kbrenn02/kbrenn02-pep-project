package Service;

import Model.Message;
import Model.Account;
import DAO.AccountDAO;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;
    AccountDAO accountDAO;

    // See comments in AccountService.java for explanation of overloaded constructor
    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }
    
    // Service that posts a message to the DB
    public Message postMessage(Message msg){
        // Message text is not blank and not over 255 characters
        if(msg.getMessage_text().length() > 0 && msg.getMessage_text().length() <= 255){
            // posted_by refers to a real, existing user
            Account existingUser = accountDAO.getUser(msg.getPosted_by());
            if(existingUser != null){
                return messageDAO.postMessage(msg);
            }
        }
        
        return null;
    }

    // Get list of all messages
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    // Get a single message by message ID
    public Message getMessageByID(int id){
        return messageDAO.getMessageByID(id);
    }

    public Message deleteMessage(int id){
        return messageDAO.deleteMessage(id);
    }

}
