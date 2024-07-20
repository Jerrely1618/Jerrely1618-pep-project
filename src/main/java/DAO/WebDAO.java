// You will need to design and create your own DAO classes from scratch. 
// You should refer to prior mini-project lab examples and course material for guidance.

// Please refrain from using a 'try-with-resources' block when connecting to your database. 
// The ConnectionUtil provided uses a singleton, and using a try-with-resources will cause issues in the tests.
package DAO;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;

public class WebDAO{
    public Account insertAccount(Account act){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO account(username,password) VALUES(?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, act.getUsername());
            preparedStatement.setString(2, act.getPassword());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                int accountId = rs.getInt(1);
                return new Account(accountId, act.getUsername(), act.getPassword());
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE username=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message insertMessage(Message msg){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO message(posted_by,message_text,time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, msg.getPosted_by());
            preparedStatement.setString(2, msg.getMessage_text());
            preparedStatement.setLong(3, msg.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                return new Message(rs.getInt("message_id"), msg.getPosted_by(), msg.getMessage_text(),msg.getTime_posted_epoch());
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccountById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "SELECT * FROM account WHERE account_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            if(rs.next()){
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM message WHERE message_id=?";
            Message msg = getMessageById(id);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                return msg;
            }
            return null; 
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessageById(String message_text,int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE message SET message_text=? WHERE message_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message_text);
            preparedStatement.setInt(2, id);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows > 0){
                return getMessageById(id);
            }
            return null; 
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    public List<Message> getAllMessagesByAccoundId(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message where posted_by = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}