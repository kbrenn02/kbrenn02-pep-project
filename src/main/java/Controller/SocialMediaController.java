package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import static org.mockito.ArgumentMatchers.nullable;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        accountService = new AccountService();
        messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // GET paths 
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getSingleMessageHandler);
        // app.get("/accounts/{account_id}/messages", this::exampleHandler);

        // POST paths
        app.post("/register", this::registerANewUserHandler);
        app.post("/login", this::logInAnExistingUserHandler);
        app.post("/messages", this::postAMessageHandler);

        // PATCH paths
        // app.patch("/messages/{message_id}", this::exampleHandler);

        // DELETE paths
        app.delete("/messages/{message_id}", this::deleteAMessageHandler);

        return app;
    }


    /* ----------------------- ALL HANDLERS ----------------------- */


    /* ~~~~~~~~~~~~~~~ GET ~~~~~~~~~~~~~~~ */

    // /messages
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();

        List<Message> recievedMsgs = messageService.getAllMessages();
        
        if(recievedMsgs == null){
            recievedMsgs = Collections.emptyList();
            ctx.json(recievedMsgs);
        } else {
            ctx.json(om.writeValueAsString(recievedMsgs));
        }
        ctx.status(200);
    }

    // /messages/{message_id}
    private void getSingleMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        int msg_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message recievedMsg = messageService.getMessageByID(msg_id);
        
        if(recievedMsg == null){
            ctx.result("");
        } else {
            ctx.json(om.writeValueAsString(recievedMsg));
        }
        ctx.status(200);
    }


    /* ~~~~~~~~~~~~~~~ POST ~~~~~~~~~~~~~~~ */

    // /register
    private void registerANewUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account acct = om.readValue(ctx.body(), Account.class);

        Account registeredUser = accountService.registerUser(acct);
        if(registeredUser == null){
            ctx.status(400);
        } else {
            ctx.json(om.writeValueAsString(registeredUser));
            ctx.status(200);
        }
    }

    // /login
    private void logInAnExistingUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Account acct = om.readValue(ctx.body(), Account.class);

        Account loggedInUser = accountService.loginUser(acct);
        if(loggedInUser == null){
            ctx.status(401);
        } else {
            ctx.json(om.writeValueAsString(loggedInUser));
            ctx.status(200);
        }
    }

    // /messages
    private void postAMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        Message msg = om.readValue(ctx.body(), Message.class);

        Message postedMessage = messageService.postMessage(msg);
        if(postedMessage == null){
            ctx.status(400);
        } else {
            ctx.json(om.writeValueAsString(postedMessage));
            ctx.status(200);
        }
    }


    /* ~~~~~~~~~~~~~~~ DELETE ~~~~~~~~~~~~~~~ */

    // /messages/{message_id}
    private void deleteAMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        int msg_id = Integer.parseInt(ctx.pathParam("message_id"));

        Message deletedMessage = messageService.deleteMessage(msg_id);

        if(deletedMessage == null){
            ctx.result("");
        } else {
            ctx.json(om.writeValueAsString(deletedMessage));
        }
        ctx.status(200);
    }

}