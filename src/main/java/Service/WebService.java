package Service;
import java.util.List;

import DAO.WebDAO;
import Model.Account;
import Model.Message;
public class WebService{
    WebDAO webDAO;

    public WebService(){
        webDAO = new WebDAO();
    }

    public WebService(WebDAO webDAO){
        this.webDAO = webDAO;
    }

    public Account addAccount(Account act){
        return webDAO.insertAccount(act);
    }

    public Account getAccountByUsername(String username){
        return webDAO.getAccountByUsername(username);
    }

    public Account getAccountById(int id){
        return webDAO.getAccountById(id);
    }

    public Message addMessage(Message msg){
        return webDAO.insertMessage(msg);
    }

    public List<Message> getAllMessages(){
        return webDAO.getAllMessages();
    }

    public Message getMessageById(int id){
        return webDAO.getMessageById(id);
    }

    public Message removeMessageById(int id){
        return webDAO.deleteMessageById(id);
    }

    public Message updateMessageById(String message_text, int id){
        return webDAO.updateMessageById(message_text, id);
    }

    public List<Message> getAllMessagesByAccoundId(int id){
        return webDAO.getAllMessagesByAccoundId(id);
    }
}