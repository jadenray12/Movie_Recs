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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
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
                	String username = userText.getText();
                    String password = new String(passwordText.getPassword());
                    accountService.createAccount(username, password);
                    JOptionPane.showMessageDialog(null, "Account Created");
                    frame.dispose();
                    LoginPage.createAndShowGUI();
                    
                }
            });
        }
    }

    static class MainPage {
    	static void createAndShowGUI() {
            JFrame frame = new JFrame("Main Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1600, 800); // Adjust the size to make the frame larger

            JPanel panel = new JPanel(new BorderLayout());
            frame.add(panel);

            // Create split pane for ratings and recommendations
            JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            mainSplitPane.setDividerLocation(800); // Adjust the divider location for the larger frame
            panel.add(mainSplitPane, BorderLayout.CENTER);

            // Create panel for ratings and all movies tables with vertical split
            JPanel leftPanel = new JPanel(new BorderLayout());

            // Create search bar for global search
            JPanel searchPanel = new JPanel(new BorderLayout());
            JTextField searchBar = new JTextField("Search Your Ratings");
            searchBar.setFont(new Font("Arial", Font.PLAIN, 16));
            searchPanel.add(searchBar, BorderLayout.CENTER);
            leftPanel.add(searchPanel, BorderLayout.NORTH); // Add search bar to the top of the left panel

            // Create panel for ratings table with heading
            JPanel ratingsPanel = new JPanel(new BorderLayout());
            JLabel ratingsLabel = new JLabel("Your Ratings", SwingConstants.CENTER);
            ratingsLabel.setFont(new Font("Arial", Font.BOLD, 16));
            ratingsPanel.add(ratingsLabel, BorderLayout.NORTH);

            // Create ratings table
            DefaultTableModel ratingsModel = new DefaultTableModel(new String[]{"Title", "Rating"}, 0) {
            	/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
                public boolean isCellEditable(int row, int column) {
                    if (column == 1) {
                    	return true;
                    }
                    return false;
                }
            };
            
            ratingsModel.addTableModelListener(new TableModelListener() {
            	@Override
            	public void tableChanged(TableModelEvent e) {
            		if (e.getType() == TableModelEvent.UPDATE) {
            			int row = e.getFirstRow();
            			int column = e.getColumn();
            			Object changedValue = ratingsModel.getValueAt(row, column);
            			Double changedVal = ((Number)changedValue).doubleValue();
            			Object movieTitle = ratingsModel.getValueAt(row, column);
            			int movieId = movieService.getMovieIdByMovie(movieTitle.toString());
            			ratingService.updateRating(user.getUser_id(), movieId, changedVal);
            		}
            		
            	}
            });
            JTable ratingsTable = new JTable(ratingsModel);
            ratingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            TableRowSorter<DefaultTableModel> ratingsSorter = new TableRowSorter<>(ratingsModel);
            ratingsTable.setRowSorter(ratingsSorter);
            JScrollPane ratingsScrollPane = new JScrollPane(ratingsTable);
            ratingsPanel.add(ratingsScrollPane, BorderLayout.CENTER);

            // Create vertical split pane for ratings and all movies tables
            JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            verticalSplitPane.setTopComponent(ratingsPanel);

            // Create panel for all movies table with heading and search bar
            JPanel allMoviesPanel = new JPanel(new BorderLayout());

            // Create panel for all movies table with heading
            JPanel allMoviesHeadingPanel = new JPanel(new BorderLayout());
            JLabel allMoviesHeadingLabel = new JLabel("All Movies", SwingConstants.CENTER);
            allMoviesHeadingLabel.setFont(new Font("Arial", Font.BOLD, 16));
            allMoviesHeadingPanel.add(allMoviesHeadingLabel, BorderLayout.CENTER);
            allMoviesPanel.add(allMoviesHeadingPanel, BorderLayout.NORTH);

            // Create search bar for all movies
            JPanel allMoviesSearchPanel = new JPanel(new BorderLayout());
            JTextField allMoviesSearchBar = new JTextField();
            allMoviesSearchBar.setFont(new Font("Arial", Font.PLAIN, 16));
            allMoviesSearchPanel.add(allMoviesSearchBar, BorderLayout.CENTER);
            allMoviesPanel.add(allMoviesSearchPanel, BorderLayout.NORTH);

            // Create all movies table
            DefaultTableModel allMoviesModel = new DefaultTableModel(new String[]{"ALL MOVIE TITLES"}, 0) {
            	/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
                public boolean isCellEditable(int row, int column) {
                    // All cells are uneditable
                    return false;
                }
            };
            JTable allMoviesTable = new JTable(allMoviesModel);
            allMoviesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            TableRowSorter<DefaultTableModel> allMoviesSorter = new TableRowSorter<>(allMoviesModel);
            allMoviesTable.setRowSorter(allMoviesSorter);
            JScrollPane allMoviesScrollPane = new JScrollPane(allMoviesTable);
            allMoviesPanel.add(allMoviesScrollPane, BorderLayout.CENTER);

            verticalSplitPane.setBottomComponent(allMoviesPanel);
            verticalSplitPane.setDividerLocation(400); // Adjust height as needed

            leftPanel.add(verticalSplitPane, BorderLayout.CENTER);
            mainSplitPane.setLeftComponent(leftPanel);

            // Create panel for recommendations table with heading
            JPanel recommendationsPanel = new JPanel(new BorderLayout());
            JLabel recommendationsLabel = new JLabel("Your Recommendations", SwingConstants.CENTER);
            recommendationsLabel.setFont(new Font("Arial", Font.BOLD, 16));
            recommendationsPanel.add(recommendationsLabel, BorderLayout.NORTH);

            // Create recommendations table
            DefaultTableModel recommendationsModel = new DefaultTableModel(new String[]{"Title", "Expected Rating"}, 0) {
            	/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
                public boolean isCellEditable(int row, int column) {
                    // All cells are uneditable
                    return false;
                }
            };
            JTable recommendationsTable = new JTable(recommendationsModel);
            recommendationsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane recommendationsScrollPane = new JScrollPane(recommendationsTable);
            recommendationsPanel.add(recommendationsScrollPane, BorderLayout.CENTER);

            mainSplitPane.setRightComponent(recommendationsPanel);

            // Fetch ratings and populate the ratings table
            Map<String, Double> ratings = ratingService.getRatingsByUserId(user.getUser_id());

            if (ratings != null) {
                for (Map.Entry<String, Double> rating : ratings.entrySet()) {
                    ratingsModel.addRow(new Object[]{rating.getKey(), rating.getValue()});
                }

            } else {
                ratingsModel.addRow(new Object[]{"No Ratings", "Search Table in the Bottom Left to Find and Add Movies you have Seen"});
            }

            // Fetch recommendations and populate the recommendations table
            Map<String, Double> recommendations = ratingService.recommendMoviesFromNeighborhood(user.getUser_id());
            for (Map.Entry<String, Double> recommendation : recommendations.entrySet()) {
                recommendationsModel.addRow(new Object[]{recommendation.getKey(), recommendation.getValue()});
            }

            // Fetch all movies and populate the all movies table
            List<Movie> allMovies = movieService.getAllMovies();
            for (Movie movie : allMovies) {
                allMoviesModel.addRow(new Object[]{movie.getTitle()});
            }

            // Add search functionality for the global search bar
            searchBar.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    search(searchBar.getText());
                }

                public void removeUpdate(DocumentEvent e) {
                    search(searchBar.getText());
                }

                public void changedUpdate(DocumentEvent e) {
                    search(searchBar.getText());
                }

                private void search(String str) {
                    if (str.length() == 0) {
                        ratingsSorter.setRowFilter(null);
                    } else {
                        ratingsSorter.setRowFilter(RowFilter.regexFilter("(?i)" + str, 0)); // Filter by the "Title" column (index 0)
                    }
                }
            });

            // Add search functionality for the all movies search bar
            allMoviesSearchBar.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) {
                    searchAllMovies(allMoviesSearchBar.getText());
                }

                public void removeUpdate(DocumentEvent e) {
                    searchAllMovies(allMoviesSearchBar.getText());
                }

                public void changedUpdate(DocumentEvent e) {
                    searchAllMovies(allMoviesSearchBar.getText());
                }

                private void searchAllMovies(String str) {
                    if (str.length() == 0) {
                        allMoviesSorter.setRowFilter(null);
                    } else {
                        allMoviesSorter.setRowFilter(RowFilter.regexFilter("(?i)" + str, 0)); // Filter by the "Title" column (index 0)
                    }
                }
            });

            // Add buttons
            JPanel buttonPanel = new JPanel();
            JButton addButton = new JButton("Add New Rating");
            JButton deleteButton = new JButton("Delete");
            JButton getRecommendationsButton = new JButton("Refresh Recommendations");

            buttonPanel.add(addButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(getRecommendationsButton);
            panel.add(buttonPanel, BorderLayout.SOUTH);

            // Button actions
            addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = allMoviesTable.getSelectedRow();
                    if (selectedRow != -1) {
                    	boolean valid = false;
                    	double rating = 0;
                    	String stringRating = null;
                    	
                    	while (!valid) {
                    		stringRating = JOptionPane.showInputDialog(null, "Enter a Rating for the Movie (0-5).",JOptionPane.QUESTION_MESSAGE);
                        	
                    		if (stringRating != null && !stringRating.isEmpty()) {
                    			
                    			try {
                    				if (Double.parseDouble(stringRating) < 0  || Double.parseDouble(stringRating) > 5) {
                    					throw new NumberFormatException();
                    				}
                    				rating = Double.parseDouble(stringRating);
                    				valid = true;
                    			} catch (NumberFormatException f) {                    				
                    				// Display an error message if the input is not a valid double
                                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number value between 0 and 5.", "Error", JOptionPane.ERROR_MESSAGE);
                    				
                    			}
                    			
                    		} else if(stringRating != null) {
                    			 JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid double value between between 0 and 5.", "Error", JOptionPane.ERROR_MESSAGE);
                    		} else {
                    			break;
                    		}
                    		
                    	}
                    	
                    	if (stringRating != null) {
                    		int column = allMoviesTable.getSelectedColumn();
	                    	int row = allMoviesTable.getSelectedRow();
	                    	Object movieObj = allMoviesTable.getValueAt(row, column);
	                    	
	                    	String movieTitle = movieObj.toString();
	                    	
	                    	ratingService.addRating(user.getUser_id(), movieService.getMovieIdByMovie(movieTitle), rating);
	                    	
	                    	// Fetch ratings and populate the ratings table
	                        Map<String, Double> ratings = ratingService.getRatingsByUserId(user.getUser_id());

	                        if (ratings != null) {
	                        	ratingsModel.addRow(new Object[]{movieTitle, rating});
	                            

	                        } else {
	                            ratingsModel.addRow(new Object[]{"No Ratings", "Search Table in the Bottom Left to Find and Add Movies you have Seen"});
	                        }
	                    	
                    		
                    	}	                    	
                    	
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a Movie to Rate.");
                    }
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = ratingsTable.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = ratingsTable.convertRowIndexToModel(selectedRow);
                        String title = (String) ratingsModel.getValueAt(modelRow, 0);
                        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the rating for " + title + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            ratingService.deleteRating(user.getUser_id(), movieService.getMovieIdByMovie(title));
                            ratingsModel.removeRow(modelRow);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select a rating to delete.");
                    }
                }
            });

            getRecommendationsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    recommendationsModel.setRowCount(0);  // Clear the existing recommendations
                    Map<String, Double> recommendations = ratingService.recommendMoviesFromNeighborhood(user.getUser_id());
                    for (Map.Entry<String, Double> recommendation : recommendations.entrySet()) {
                        recommendationsModel.addRow(new Object[]{recommendation.getKey(), recommendation.getValue()});
                    }
                }
            });

            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
    }


}
