package com.movie.frontend;

import com.movie.backend.entity.Account;
import com.movie.backend.entity.Movie;
import com.movie.backend.service.AccountService;
import com.movie.backend.service.MovieService;
import com.movie.backend.service.RatingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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

            // Create search bar
            JPanel searchPanel = new JPanel(new BorderLayout());
            JTextField searchBar = new JTextField();
            searchBar.setFont(new Font("Arial", Font.PLAIN, 16));
            searchPanel.add(searchBar, BorderLayout.CENTER);
            panel.add(searchPanel, BorderLayout.NORTH);

            // Create table model
            DefaultTableModel model = new DefaultTableModel(new String[]{"Title", "Rating"}, 0);
            JTable table = new JTable(model);
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(table);
            panel.add(scrollPane, BorderLayout.CENTER);

            // Create table row sorter
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            table.setRowSorter(sorter);

            // Fetch ratings
            Map<String, Double> ratings = ratingService.getRatingsByUserId(user.getUser_id());
            for (Map.Entry<String, Double> rating : ratings.entrySet()) {
                model.addRow(new Object[]{rating.getKey(), rating.getValue()});
            }

            // Add search functionality
            searchBar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    search(searchBar.getText());
                }

                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    search(searchBar.getText());
                }

                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    search(searchBar.getText());
                }

                public void search(String str) {
                    if (str.length() == 0) {
                        sorter.setRowFilter(null);
                    } else {
                        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + str));
                    }
                }
            });

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
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        String title = (String) model.getValueAt(modelRow, 0);
                        String newRating = JOptionPane.showInputDialog("Enter new rating for " + title + ":");
                        if (newRating != null) {
                            ratingService.updateRating(user.getUser_id(), movieService.getMovieIdByMovie(title), Double.parseDouble(newRating));
                            model.setValueAt(Double.parseDouble(newRating), modelRow, 1);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a rating to edit.");
                    }
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        String title = (String) model.getValueAt(modelRow, 0);
                        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the rating for " + title + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            ratingService.deleteRating(user.getUser_id(), movieService.getMovieIdByMovie(title));
                            model.removeRow(modelRow);
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

            List<Movie> recommendations = new ArrayList<Movie>();
            recommendations = ratingService.recommendMoviesFromNeighborhood(user.getUser_id());
            for (Movie recommendation : recommendations) {
                JLabel recommendationLabel = new JLabel(recommendation.getTitle());
                panel.add(recommendationLabel);
            }

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }
}
