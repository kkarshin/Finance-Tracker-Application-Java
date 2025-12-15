/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 * UserManager handles all user-related operations
 * @author Lee Chia Ying
 */
package manager;

import model.User;
import java.util.ArrayList;
import java.util.List;
import database.UserDAO;
import database.Username;
import database.ForgetPassword;
import database.TestDAO;
import database.PasswordUsed;
import database.AnswerUsed;

public class UserManager {
    UserDAO dao = new UserDAO();
    Username UName = new Username();
    ForgetPassword pass = new ForgetPassword();
    TestDAO tdao = new TestDAO();
    PasswordUsed passu = new PasswordUsed();
    AnswerUsed answer = new AnswerUsed();
    private static UserManager instance; // Store the single instance of UserManager
    private List<User> users; // Create a private user list to store many User objects from the user class
    private User currentUser; // Store the current logged-in user
    
    // Private constructor (no other class can create a new instance of UserManager)
    private UserManager(){
        users = new ArrayList<>(); // Initialize the users list to an empty ArrayList
    }
    
    // Can access to the one UserManager instance
    // Create a new instance of UserManager only once and returns the same instance every time
    public static UserManager getInstance(){
        if (instance == null)
            instance = new UserManager();
        return instance;
    }
    
    // Login operation
    public String login(String username, String password){
        System.out.println("Attempting to login user: " + username);
        
        
        try {
            // Check for null values
            if (username == null || password == null)
                return "Invalid input: null values are not allowed";

            // Check for empty values
            if (username.trim().isEmpty() || password.trim().isEmpty())
                return "Please fill in all fields";

        
            if (UName.isUsernameUsed(username)) {
                if (passu.isPasswordUsed(password)){
                    // Fetch the user from the DB or the list
                    User loggedInUser = dao.getUser(username); 
                    currentUser = loggedInUser; // Store in memory for the session
                    return "Login successful";
                }
                    return "Incorrect password";
            }
//            }
            return "Username not found";
            
        } catch (Exception e) {
            System.err.println("Error in login: " + e.getMessage()); // Print error messages
            return "Login failed due to system error";
        }
    }
    
    // Registration operation
    public String register(String username, String password, String securityQuestion, String securityAnswer){
        System.out.println("Attempting to register user: " + username);

        try {
            // Check for null values
            if (username == null || password == null || securityQuestion == null || securityAnswer == null) 
                return "Invalid input: null values are not allowed";

            // Check for empty values
            if (username.trim().isEmpty() || password.trim().isEmpty() || 
                securityQuestion.trim().isEmpty() || securityAnswer.trim().isEmpty())
                return "Please fill in all fields";

            // Check username length
            if (username.length() > 30)
                return "Username is too long (maximum 30 characters)";

            // Check if username contains at least one letter and one number
            if (!username.matches(".*[A-Za-z].*") || !username.matches(".*\\d.*")) 
                return "Username must contain at least one letter and one number";
            
            // Check password length
            if (password.length() <= 6)
                return "Password must be more than 6 characters long";
            
            // Check if username exists
            for (User user : users) {
                if (user.getUsername().equals(username))
                    return "Username already exists";
            }

            // Create new user
             // Create user object
//          users.add(newUser);   // Add the new user object to the list
            if (UName.isUsernameUsed(username)) {
                     System.out.println("Username is already exist");
                     return "Username already exists";
            } else { User newUser = new User(username, password, securityQuestion, securityAnswer);
                     currentUser = newUser;
                     System.out.println("Username is available");
                     users.add(newUser);
                     dao.addUser(username,password,securityQuestion,securityAnswer);
}


            return "Registration successful";
            
        } catch (Exception e) {
            System.err.println("Error in registration: " + e.getMessage());
            return "Registration failed due to system error";
        }   
    }
    
    // Password Reset Operation
    public String resetPassword(String username, String securityAnswer, String newPassword){
        System.out.println("Attempting to reset password for user: " + username);

        try {
            // Check for null values
            if (username == null || securityAnswer == null || newPassword == null) {
                return "Invalid input: null values are not allowed";
            }

            // Check for empty values
            if (username.trim().isEmpty() || securityAnswer.trim().isEmpty() || 
                newPassword.trim().isEmpty()) {
                return "Please fill in all fields";
            }
            
            // Password must be more than 6 characters
            if (newPassword.length() <= 6) {
                return "New password must be more than 6 characters long";
            }

            if (UName.isUsernameUsed(username)) {
                if (answer.isAnswerUsed(securityAnswer)){
                    dao.updatePassword(username, newPassword);
                    pass.resetPassword(username,securityAnswer, newPassword);// Set previous pass to new password
                    return "Password reset successful";

                }
                    return "Incorrect password";
            }
//            }
            return "Incorrect security answer";
            
        } catch (Exception e) {
            System.err.println("Error in password reset: " + e.getMessage());
            return "Password reset failed due to system error";
        }
    }
    
    // Get security question based on the username
    public String getSecurityQuestion(String username){
        for (User user : users){
            if (user.getUsername().equals(username))
                return user.getSecurityQuestion();
        }
        return null;
    }
    
    // Get current user
    public User getCurrentUser(){
        return currentUser;
    }
    
    // Logout - to clear the information
    public void logout(){
        // Reset the currentUser variable so it no longer points to any User object
        currentUser = null; 
    }
    
    // Method to display all users (TESTING PURPOSE)
    public void displayAllUsers() {
        System.out.println("\nCurrent Users in System:");
        for (User user : users) {
            System.out.println("Username: " + user.getUsername());
        }
    }
}
