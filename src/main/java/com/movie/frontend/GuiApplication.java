package com.movie.frontend;

import com.movie.backend.service.AccountService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

@SpringBootApplication
@ComponentScan("com.movie.backend") // Scan all components in the com.movie package and its sub-packages
public class GuiApplication {
    private static AccountService userService;

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ApplicationContext context = SpringApplication.run(GuiApplication.class, args);
        userService = context.getBean(AccountService.class);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                LoginPage.createAndShowGUI();
            }
        });
    }

    static class LoginPage {
        static void createAndShowGUI() {
            JFrame frame = new JFrame("Login");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300); // Increase the size of the frame

            JPanel panel = new JPanel(new GridBagLayout());
            frame.add(panel);
            placeComponents(frame, panel);

            frame.setLocationRelativeTo(null); // Center the frame on the screen
            frame.setVisible(true);
        }

        private static void placeComponents(JFrame frame, JPanel panel) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(10, 10, 10, 10); // Padding around components

            JLabel userLabel = new JLabel("User");
            userLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.LINE_END;
            panel.add(userLabel, constraints);

            JTextField userText = new JTextField();
            userText.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
            userText.setPreferredSize(new Dimension(200, 30)); // Set preferred size
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.LINE_START;
            panel.add(userText, constraints);

            JLabel passwordLabel = new JLabel("Password");
            passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.LINE_END;
            panel.add(passwordLabel, constraints);

            JPasswordField passwordText = new JPasswordField();
            passwordText.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
            passwordText.setPreferredSize(new Dimension(200, 30)); // Set preferred size
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.LINE_START;
            panel.add(passwordText, constraints);

            JButton loginButton = new JButton("Login");
            loginButton.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.CENTER;
            panel.add(loginButton, constraints);

            JButton registerButton = new JButton("Register");
            registerButton.setFont(new Font("Arial", Font.PLAIN, 16)); // Increase font size
            constraints.gridx = 1;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.CENTER;
            panel.add(registerButton, constraints);

            loginButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String username = userText.getText();
                    String password = new String(passwordText.getPassword());
                    boolean loginSuccessful = userService.login(username, password);
                    if (loginSuccessful) {
                        JOptionPane.showMessageDialog(null, "Login successful!");
                        frame.dispose();
                        MainPage.createAndShowGUI();
                    } else {
                        JOptionPane.showMessageDialog(null, "Login failed. Try again.");
                    }
                }
            });

            registerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Handle the register action
                }
            });
        }
    }

    static class MainPage {
        static void createAndShowGUI() {
            JFrame frame = new JFrame("Main Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            frame.add(panel);

            // Ratings Panel
            JPanel ratingsPanel = new JPanel();
            ratingsPanel.setLayout(new BoxLayout(ratingsPanel, BoxLayout.Y_AXIS));
            JScrollPane scrollPane = new JScrollPane(ratingsPanel);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Fetch and display ratings
            List<String> ratings = userService.getCurrentRatings(); // Assume this method exists
            for (String rating : ratings) {
                JPanel ratingPanel = new JPanel(new FlowLayout());
                JLabel ratingLabel = new JLabel(rating);
                JButton editButton = new JButton("Edit");
                JButton deleteButton = new JButton("Delete");
                ratingPanel.add(ratingLabel);
                ratingPanel.add(editButton);
                ratingPanel.add(deleteButton);
                ratingsPanel.add(ratingPanel);

                // Add action listeners for edit and delete buttons
                editButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Handle edit action
                    }
                });

                deleteButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Handle delete action
                    }
                });
            }

            // Get Recommendations Button
            JButton getRecommendationsButton = new JButton("Get Recommendations");
            getRecommendationsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    RecommendationsPage.createAndShowGUI();
                }
            });
            panel.add(getRecommendationsButton, BorderLayout.SOUTH);

            frame.setVisible(true);
        }
    }

    static class RecommendationsPage {
        static void createAndShowGUI() {
            JFrame frame = new JFrame("Recommendations");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JScrollPane scrollPane = new JScrollPane(panel);
            frame.add(scrollPane);

            // Fetch and display recommendations
            List<String> recommendations = userService.getRecommendations(); // Assume this method exists
            for (String recommendation : recommendations) {
                JLabel recommendationLabel = new JLabel(recommendation);
                panel.add(recommendationLabel);
            }

            frame.setVisible(true);
        }
    }
}
