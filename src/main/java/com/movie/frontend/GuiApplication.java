package com.movie.frontend;

import com.movie.backend.entity.Account;
import com.movie.backend.service.AccountService;
import com.movie.backend.service.MovieService;
import com.movie.backend.service.RatingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@ComponentScan("com.movie.backend")
public class GuiApplication {
    private static AccountService accountService;
    private static RatingService ratingService;
    private static MovieService movieService;
    private static Account user;

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ApplicationContext context = SpringApplication.run(GuiApplication.class, args);
        accountService = context.getBean(AccountService.class);
        movieService = context.getBean(MovieService.class);
        ratingService = context.getBean(RatingService.class);

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
            frame.setSize(400, 300);

            JPanel panel = new JPanel(new GridBagLayout());
            frame.add(panel);
            placeComponents(frame, panel);

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }

        private static void placeComponents(JFrame frame, JPanel panel) {
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(10, 10, 10, 10);

            JLabel userLabel = new JLabel("User");
            userLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.LINE_END;
            panel.add(userLabel, constraints);

            JTextField userText = new JTextField();
            userText.setFont(new Font("Arial", Font.PLAIN, 16));
            userText.setPreferredSize(new Dimension(200, 30));
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.LINE_START;
            panel.add(userText, constraints);

            JLabel passwordLabel = new JLabel("Password");
            passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.LINE_END;
            panel.add(passwordLabel, constraints);

            JPasswordField passwordText = new JPasswordField();
            passwordText.setFont(new Font("Arial", Font.PLAIN, 16));
            passwordText.setPreferredSize(new Dimension(200, 30));
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.LINE_START;
            panel.add(passwordText, constraints);

            JButton loginButton = new JButton("Login");
            loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.CENTER;
            panel.add(loginButton, constraints);

            JButton registerButton = new JButton("Register");
            registerButton.setFont(new Font("Arial", Font.PLAIN, 16));
            constraints.gridx = 1;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.CENTER;
            panel.add(registerButton, constraints);

            loginButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String username = userText.getText();
                    String password = new String(passwordText.getPassword());
                    Account account = accountService.login(username, password);
                    if (account != null) {
                        JOptionPane.showMessageDialog(null, "Login successful!");
                        frame.dispose();
                        user = account;
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

            JPanel panel = new JPanel(new BorderLayout());
            frame.add(panel);

            JPanel ratingsPanel = new JPanel();
            ratingsPanel.setLayout(new BoxLayout(ratingsPanel, BoxLayout.Y_AXIS));
            JScrollPane scrollPane = new JScrollPane(ratingsPanel);
            panel.add(scrollPane, BorderLayout.CENTER);

            Map<String, Double> ratings = ratingService.getRatingsByUserId(user.getUser_id());
            for (Map.Entry<String, Double> rating : ratings.entrySet()) {
                JPanel ratingPanel = new JPanel(new GridLayout(1, 2));
                JLabel titleLabel = new JLabel(rating.getKey().toString());
                JLabel ratingLabel = new JLabel(rating.getValue().toString());
                ratingPanel.add(titleLabel);
                ratingPanel.add(ratingLabel);
                ratingsPanel.add(ratingPanel);

                ratingPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        selectedRatingId =  rating.getValue();
                    }
                });
            }

            JPanel buttonPanel = new JPanel();
            JButton editButton = new JButton("Edit");
            JButton deleteButton = new JButton("Delete");
            JButton getRecommendationsButton = new JButton("Get Recommendations");

            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(getRecommendationsButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            editButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (selectedRatingId != null) {
                        String newRating = JOptionPane.showInputDialog("Enter new rating:");
                        if (newRating != null) {
                            ratingService.editRating(selectedRatingId, Integer.parseInt(newRating));
                            frame.dispose();
                            MainPage.createAndShowGUI();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a rating to edit.");
                    }
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (selectedRatingId != null) {
                        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this rating?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            ratingService.deleteRating(selectedRatingId);
                            frame.dispose();
                            MainPage.createAndShowGUI();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a rating to delete.");
                    }
                }
            });

            getRecommendationsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    RecommendationsPage.createAndShowGUI();
                }
            });

            frame.setLocationRelativeTo(null);
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

            List<String> recommendations = accountService.getRecommendations(user.getUser_id());
            for (String recommendation : recommendations) {
                JLabel recommendationLabel = new JLabel(recommendation);
                panel.add(recommendationLabel);
            }

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }

    private static Double selectedRatingId = null;
}
