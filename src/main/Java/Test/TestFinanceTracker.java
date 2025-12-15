/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author Hp
 */
import manager.UserManager;

public class TestFinanceTracker {
    public static void main(String[] args) {
        try {
            UserManager userManager = UserManager.getInstance();
            
            // Test 1: Registration
            System.out.println("=== Testing Registration ===");
            testRegistration(userManager);

            // Test 2: Login
            System.out.println("\n=== Testing Login ===");
            testLogin(userManager);

            // Test 3: Password Reset
            System.out.println("\n=== Testing Password Reset ===");
            testPasswordReset(userManager);

            // Test 4: Edge Cases
            System.out.println("\n=== Testing Edge Cases ===");
            testEdgeCases(userManager);
            
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testRegistration(UserManager userManager) {
        // Test valid registration
        String result1 = userManager.register("testUser1", "password123", 
            "What is your favorite color?", "blue");
        System.out.println("Register new user: " + result1);

        // Test duplicate username
        String result2 = userManager.register("testUser1", "password456", 
            "What is your favorite color?", "red");
        System.out.println("Register duplicate username: " + result2);

        // Test empty fields
        String result3 = userManager.register("", "", "", "");
        System.out.println("Register with empty fields: " + result3);
    }

    private static void testLogin(UserManager userManager) {
        // Test valid login
        String result1 = userManager.login("testUser1", "password123");
        System.out.println("Login with correct credentials: " + result1);

        // Test wrong password
        String result2 = userManager.login("testUser1", "wrongpassword");
        System.out.println("Login with wrong password: " + result2);

        // Test non-existent user
        String result3 = userManager.login("nonexistentUser", "password123");
        System.out.println("Login with non-existent user: " + result3);
    }

    private static void testPasswordReset(UserManager userManager) {
        // Test valid password reset
        String result1 = userManager.resetPassword("testUser1", "blue", "newpassword123");
        System.out.println("Reset password with correct answer: " + result1);

        // Test wrong security answer
        String result2 = userManager.resetPassword("testUser1", "red", "newpassword123");
        System.out.println("Reset password with wrong answer: " + result2);

        // Test login with new password
        String result3 = userManager.login("testUser1", "newpassword123");
        System.out.println("Login with new password: " + result3);
    }

    private static void testEdgeCases(UserManager userManager) {
        // Test null values
        String result1 = userManager.register(null, null, null, null);
        System.out.println("Register with null values: " + result1);

        // Test very long inputs
        String longString = "a".repeat(100);
        String result2 = userManager.register(longString, longString, longString, longString);
        System.out.println("Register with very long inputs: " + result2);
        
        String result3 = userManager.register("test@#$%","pass@#$%","question?","answer!");
        System.out.println("Register with special characters: " + result3);
    }    
}
