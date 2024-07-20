package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.WebService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    WebService webService;
    public SocialMediaController(){
        webService = new WebService();
    }
    ObjectMapper om = new ObjectMapper();

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getAccountMessages);
        app.delete("/messages/{message_id}", this::removeMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.post("/messages", this::createMessagesHandler);
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginHandler);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void loginHandler(Context context) throws JsonProcessingException {
        String jsonString = context.body();
        ObjectMapper om = new ObjectMapper();
        Account act = om.readValue(jsonString, Account.class);
        String password = act.getPassword();
        String username = act.getUsername();
        Account SearchedAccount = webService.getAccountByUsername(username);
        if(SearchedAccount==null){
            context.status(401);
            return;
        }
        if(SearchedAccount.getPassword().equals(password)){
            context.status(200);
            context.json(SearchedAccount);
            return;
        }
        context.status(401);
    }

    private void registerAccountHandler(Context context) throws JsonProcessingException {
        String jsonString = context.body();
        ObjectMapper om = new ObjectMapper();
        Account act = om.readValue(jsonString, Account.class);
        String password = act.getPassword();
        String username = act.getUsername();
        if(webService.getAccountByUsername(username)!=null){
            context.status(400);
            return;
        }
        if (username.length() > 0 && password.length() > 4) {
            Account AddedAccount = webService.addAccount(act);
            context.status(200);
            context.json(AddedAccount);
            return;
        } 
        context.status(400);
    }

    private void createMessagesHandler(Context context) throws JsonProcessingException {
        String jsonString = context.body();
        ObjectMapper om = new ObjectMapper();
        Message msg = om.readValue(jsonString, Message.class);
        String messageText = msg.getMessage_text();
        int PostedBy = msg.getPosted_by();
        if(webService.getAccountById(PostedBy)==null){
            context.status(400);
            return;
        }
        if(messageText.length() > 0 && messageText.length() < 255){
            Message AddedMessage = webService.addMessage(msg);
            context.status(200);
            context.json(AddedMessage);
            return;
        }
        context.status(400);
    }

    private void getMessagesHandler(Context context){
        context.json(webService.getAllMessages());
    }

    private void getMessageHandler(Context context){
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = webService.getMessageById(message_id);
        if(message != null){
            context.json(message);
        }
        context.status(200);
    }

    private void removeMessageHandler(Context context){
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        Message message = webService.removeMessageById(message_id);
        if(message != null){
            context.json(message);
        }
        context.status(200);
    }

    private void updateMessageHandler(Context context) throws JsonProcessingException{
        String jsonString = context.body();
        ObjectMapper om = new ObjectMapper();
        Message msg = om.readValue(jsonString, Message.class);
        String new_message = msg.getMessage_text();
        int message_id = Integer.parseInt(context.pathParam("message_id"));
        if(new_message.length() > 0 && new_message.length() < 255){
            Message message = webService.updateMessageById(new_message,message_id);
            if(message != null){
                context.json(message);
                context.status(200);
                return;
            }
        }
        context.status(400);
    }

    private void getAccountMessages(Context context) throws JsonProcessingException{
        int account_id = Integer.parseInt(context.pathParam("account_id"));
        context.json(webService.getAllMessagesByAccoundId(account_id));
    }
}