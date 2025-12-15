/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

/**
 *
 * @author Hp
 */

import gui.Login;

public class TestGUIIntegration {
    public static void main(String[] args) {
        // Test GUI flow
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                // Create and show login window
                Login loginWindow = new Login();
                loginWindow.setVisible(true);

                // Print test instructions
                System.out.println("GUI Test Instructions:");
                System.out.println("1. Try registering a new user");
                System.out.println("2. Try logging in with the new user");
                System.out.println("3. Try the forgot password flow");
                System.out.println("4. Check if all windows transition correctly");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
