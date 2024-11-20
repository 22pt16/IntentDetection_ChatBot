package org.example;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class ChatbotGUI {
    private JFrame frame;
    private JTextArea chatArea;
    private ChatbotModel chatbotModel;
    private JComboBox<String> moodSelector;
    private String userName;
    private Map<String, Integer> moodCount;
    private JProgressBar moodProgressBar;
    private JTextField inputField;

    public ChatbotGUI() throws Exception {
        chatbotModel = new ChatbotModel();
        moodCount = new HashMap<>();

        showSplashScreen();
    }

    private void showSplashScreen() {
        JFrame splashFrame = new JFrame("Welcome to ChatCare");
        splashFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Fullscreen
        splashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splashFrame.setLayout(new BorderLayout());

        // Background panel with gradient
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(173, 216, 230); // Light blue
                Color color2 = new Color(255, 228, 225); // Light pink
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        backgroundPanel.setLayout(new GridBagLayout());
        splashFrame.add(backgroundPanel, BorderLayout.CENTER);

        // Title label
        JLabel titleLabel = new JLabel("Welcome to Mental Support Bot: ChatCare", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Increased font size
        titleLabel.setForeground(new Color(0, 102, 204));
        backgroundPanel.add(titleLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(20, 20, 10, 20), 0, 0));

        // Tagline label
        JLabel taglineLabel = new JLabel("An immersive experience towards your mental wellbeing", SwingConstants.CENTER);
        taglineLabel.setFont(new Font("Arial", Font.ITALIC, 18)); // Italic style
        taglineLabel.setForeground(Color.WHITE); // White color
        backgroundPanel.add(taglineLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 20, 10, 20), 0, 0));

        // Logo/Image
        JLabel logoLabel = new JLabel(new ImageIcon("C:\\Users\\Admin\\IdeaProjects\\Chat\\src\\main\\resources\\logo.png")); // Update with actual logo path
        backgroundPanel.add(logoLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.3,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL, new Insets(10, 20, 10, 20), 0, 0));

        // Input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new FlowLayout());
        JTextField nameField = new JTextField(15);
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setPreferredSize(new Dimension(200, 30));
        inputPanel.add(new JLabel("Enter your name:"));
        inputPanel.add(nameField);
        backgroundPanel.add(inputPanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 20, 10, 20), 0, 0));

        // Loading Tips
        JLabel loadingTip = new JLabel("Tip: Remember to take a deep breath!", SwingConstants.CENTER);
        loadingTip.setForeground(new Color(0, 102, 204));
        backgroundPanel.add(loadingTip, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(10, 20, 10, 20), 0, 0));

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        okButton.setBackground(new Color(200, 200, 255));
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> {
            userName = nameField.getText().trim();
            if (userName.isEmpty()) {
                userName = "User"; // Default name
            }
            splashFrame.dispose(); // Close splash screen
            createGUI(); // Create main GUI
        });
        buttonPanel.add(okButton);
        backgroundPanel.add(buttonPanel, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(20, 20, 20, 20), 0, 0));

        splashFrame.setVisible(true);
    }






    private void createGUI() {
        frame = new JFrame("ChatCare: Mental Support Bot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Set desired width and height
        frame.setLocationRelativeTo(null); // Center the frame on the screen

        // Create the custom GradientPanel
        GradientPanel gradientPanel = new GradientPanel();
        gradientPanel.setLayout(new BorderLayout()); // Set the layout of the GradientPanel
        frame.setContentPane(gradientPanel);

        // Mood selector
        String[] moods = {"Happy", "Sad", "Anxious", "Neutral", "Angry"};
        moodSelector = new JComboBox<>(moods);
        moodSelector.setPreferredSize(new Dimension(120, 30)); // Set preferred size
        moodSelector.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Input field setup
        inputField = new JTextField("Type your message here...");
        inputField.setForeground(Color.GRAY);
        inputField.setPreferredSize(new Dimension(400, 30)); // Set preferred size
        inputField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        inputField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (inputField.getText().equals("Type your message here...")) {
                    inputField.setText("");
                    inputField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (inputField.getText().isEmpty()) {
                    inputField.setForeground(Color.GRAY);
                    inputField.setText("Type your message here...");
                }
            }
        });

        inputField.addActionListener(this::handleUserInput);

        // Mood progress bar panel
        JPanel progressPanel = new JPanel();
        progressPanel.setBorder(createCenteredTitledBorder("Happy Mood Progress Bar"));

        moodProgressBar = new JProgressBar(0, 100);
        moodProgressBar.setStringPainted(true);
        moodProgressBar.setForeground(Color.GREEN);
        moodProgressBar.setPreferredSize(new Dimension(780, 30)); // Set preferred size

        progressPanel.add(moodProgressBar);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10)); // Horizontal layout with gaps

        JButton helpButton = createButton("Help", e -> showHelpDialog());
        JButton randomQuoteButton = createButton("Random Quote", e -> showRandomQuote());
        JButton quickTipsButton = createButton("Quick Tips", e -> showQuickTips());

        buttonPanel.add(helpButton);
        buttonPanel.add(randomQuoteButton);
        buttonPanel.add(quickTipsButton);

        // Chat area setup
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        chatArea.setBackground(new Color(245, 245, 245));
        chatArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scrollPane.setBorder(createCenteredTitledBorder("Chat History"));
        scrollPane.setPreferredSize(new Dimension(780, 400)); // Set preferred size for scroll pane

        // Center the chat area
        JPanel chatPaddingPanel = new JPanel();
        chatPaddingPanel.setLayout(new BorderLayout());
        chatPaddingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding around the chat area
        chatPaddingPanel.add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the chat area with a different background color
        JPanel chatAreaPanel = new JPanel();
        chatAreaPanel.setBackground(new Color(245, 245, 245));
        chatAreaPanel.setLayout(new BorderLayout());
        chatAreaPanel.add(chatPaddingPanel, BorderLayout.CENTER);

        // Main panel to arrange components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(progressPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between progress bar and chat area
        mainPanel.add(chatPaddingPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between chat area and button panel
        mainPanel.add(buttonPanel);


        // Input panel with mood selector
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.add(moodSelector);
        inputPanel.add(inputField);

        // Add the main panel and input panel to the GradientPanel
        gradientPanel.add(mainPanel, BorderLayout.CENTER);
        gradientPanel.add(inputPanel, BorderLayout.SOUTH);

        showOpeningMessage();

        frame.setVisible(true);
    }
    private Border createCenteredTitledBorder(String title) {
        TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
        titledBorder.setTitlePosition(TitledBorder.ABOVE_TOP);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        return titledBorder;
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setFocusPainted(false);
        button.setBackground(new Color(200, 200, 255));
        button.setPreferredSize(new Dimension(100, 20)); // Increase height here
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }


    private void showOpeningMessage() {
        chatArea.append("Welcome to Mental Support Bot, " + userName + "!\n");
        chatArea.append("I'm here to listen and offer support.\n");
        chatArea.append("Please select your mood and type your message below.\n\n");
    }

    private void handleUserInput(ActionEvent e) {
        try {
            String input = inputField.getText();
            if (input.equals("Type your message here...") || input.trim().isEmpty()) {
                return; // Ignore placeholder or empty input
            }

            String mood = (String) moodSelector.getSelectedItem();
            moodCount.putIfAbsent(mood, 0);
            moodCount.put(mood, moodCount.get(mood) + 1);
            updateMoodProgressBar();

            String response = chatbotModel.processInput(input, mood);
            chatArea.append(userName + ": " + input + "\n");
            chatArea.append("Bot: " + response + "\n\n");
            inputField.setText("Type your message here...");
            inputField.setForeground(Color.GRAY);
        } catch (Exception ex) {
            chatArea.append("Error: " + ex.getMessage() + "\n\n");
        }
    }

    private void updateMoodProgressBar() {
        int totalInputs = moodCount.values().stream().mapToInt(Integer::intValue).sum();
        if (totalInputs > 0) {
            int happyCount = moodCount.getOrDefault("Happy", 0); // Use default value
            moodProgressBar.setValue((happyCount * 100) / totalInputs);
            moodProgressBar.setString("Happy Mood Percentage: " + (happyCount * 100) / totalInputs + "%");
        } else {
            moodProgressBar.setValue(0);
            moodProgressBar.setString("No mood entries yet.");
        }
    }


    private void showHelpDialog() {
        String helpMessage = "Welcome to the Mental Support Bot!\n\n" +
                "How to Use:\n" +
                "- Select your mood from the dropdown.\n" +
                "- Type your message in the input box and press Enter.\n" +
                "- Click 'Help' for more information.\n\n" +
                "Examples of what to type:\n" +
                "- \"I'm feeling stressed.\"\n" +
                "- \"Can you give me some advice?\"\n" +
                "- \"What should I do when I feel anxious?\"";
        JOptionPane.showMessageDialog(frame, helpMessage, "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showRandomQuote() {
        String randomQuote;
        try {
            randomQuote = chatbotModel.getRandomQuote();
        } catch (Exception ex) {
            randomQuote = "Error fetching quote.";
        }
        chatArea.append("Bot: " + randomQuote + "\n\n");
    }

    private void showQuickTips() {
        String quickTipsMessage = "Quick Tips:\n" +
                "- Take a deep breath in for 4 seconds, hold for 4 seconds, and breathe out for 4 seconds.\n" +
                "- Try to focus on something positive.\n" +
                "- Remember, it's okay to ask for help.\n";
        JOptionPane.showMessageDialog(frame, quickTipsMessage, "Quick Tips", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ChatbotGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Custom panel for gradient background
    static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            Color color1 = new Color(173, 216, 230); // Light blue
            Color color2 = new Color(255, 228, 225); // Light pink
            GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}