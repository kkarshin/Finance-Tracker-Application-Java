/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * User define the structure of the user object, holding user data, without logic
 * @author Lee Chia Ying
 */
package model;
import database.UserDAO;
        

public class User {
    private String username;
    private String password; 
    private String securityQuestion;
    private String securityAnswer;
    UserDAO dao = new UserDAO();
    
    // User Constructor
    public User(String username, String password, String securityQuestion, String securityAnswer){
        this.username = username;
        this.password = password;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
    }
    
    // Setter method to reset username and password
    public void setUsername(String currentUser){
        this.username = currentUser;
    }
    
    public void setPassword(String newPassword){
        this.password = newPassword;
    }
    
    // Getters
    public String getUsername(){
        dao.getUser(username);
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getSecurityQuestion(){
        return securityQuestion;
    }
    
    public String getSecurityAnswer(){
        return securityAnswer;
    }
}
