package cmsc125.project1.views;

import cmsc125.project1.services.AppInfo;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.*;

public class GameView extends JInternalFrame {

//    private JLabel wordLabel;
    private SecurityRingPanel securityRingPanel; // New primary visual field
    private final Map<Character, JButton> alphabetButtons = new HashMap<>();
    private JPanel payloadBoxPanel;
    private JTextPane wordDisplay;

    public GameView() {
        super("Game", true, true, true, true);
        initLayout();
        setSize(900, 600); // Increased default size slightly for better ring visibility
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createTopPanel(), BorderLayout.NORTH);
        mainPanel.add(createLeftPanel(), BorderLayout.CENTER);
        mainPanel.add(createRightPanel(), BorderLayout.EAST);
        mainPanel.add(createBottomPanel(), BorderLayout.SOUTH);
        getContentPane().add(mainPanel);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(0, 0, 51)); // Dark navy
        topPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 1));

        JLabel title = new JLabel(AppInfo.getAppName());
        title.setForeground(Color.CYAN);
        title.setFont(new Font("Monospaced", Font.BOLD, 48));
        topPanel.add(title);
        return topPanel;
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // --- Word Guess Box ---
        gbc.insets = new Insets(10, 40, 30, 40);
        JPanel wordContainer = new JPanel(new BorderLayout());
        wordContainer.setBackground(new Color(5, 5, 40));
        wordContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 255), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Initialize JTextPane
        wordDisplay = new JTextPane();
        wordDisplay.setEditable(false);
        wordDisplay.setFocusable(false);
        wordDisplay.setOpaque(false);
        wordDisplay.setFont(new Font("Monospaced", Font.BOLD, 36));
        wordDisplay.setForeground(Color.WHITE);

        // Center the text within the TextPane
        StyledDocument doc = wordDisplay.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        wordContainer.add(wordDisplay, BorderLayout.CENTER);
        leftPanel.add(wordContainer, gbc);

        // --- The Keyboard Layout ---
        gbc.insets = new Insets(0, 10, 10, 10);
        JPanel keyboardWrapper = new JPanel();
        keyboardWrapper.setLayout(new BoxLayout(keyboardWrapper, BoxLayout.Y_AXIS));
        keyboardWrapper.setOpaque(false);

        keyboardWrapper.add(createKeyboardRow("QWERTYUIOP", 0));
        keyboardWrapper.add(Box.createVerticalStrut(15));

        keyboardWrapper.add(createKeyboardRow("ASDFGHJKL", 30));
        keyboardWrapper.add(Box.createVerticalStrut(15));

        keyboardWrapper.add(createKeyboardRow("ZXCVBNM", 60));

        leftPanel.add(keyboardWrapper, gbc);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        // We use BorderLayout here so the SecurityRingPanel can fill the space
        // while maintaining its own internal centering logic.
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("System Integrity"));
        rightPanel.setPreferredSize(new Dimension(350, 0)); // Set a preferred width for the sidebar

        // Initialize the Ring Panel
        securityRingPanel = new SecurityRingPanel();

        // Adding it to CENTER allows it to expand/shrink with the window
        rightPanel.add(securityRingPanel, BorderLayout.CENTER);

        return rightPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottomPanel.setBackground(new Color(0, 0, 51));
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

        JLabel label = new JLabel("Payloads Remaining:");
        label.setForeground(new Color(0, 255, 204)); // Neon Cyan
        label.setFont(new Font("Monospaced", Font.BOLD, 18));
        bottomPanel.add(label);

        // This is the dynamic part
        payloadBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        payloadBoxPanel.setOpaque(false);
        bottomPanel.add(payloadBoxPanel);

        return bottomPanel;
    }

    private JPanel createKeyboardRow(String letters, int leftPadding) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        row.setOpaque(false);

        row.setBorder(BorderFactory.createEmptyBorder(0, leftPadding, 0, 0));

        for (char c : letters.toCharArray()) {
            JButton btn = createStyledButton(String.valueOf(c));
            alphabetButtons.put(c, btn);
            row.add(btn);
        }
        return row;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 18));

        // Force a square shape so they don't squish to fit the letter
        btn.setPreferredSize(new Dimension(55, 55));

        // Add internal breathing room
        btn.setMargin(new Insets(2, 2, 2, 2));

        // BevelBorder creates that 3D "key" look
        btn.setBorder(BorderFactory.createRaisedBevelBorder());

        return btn;
    }

    /**
     * Controller calls this to refresh the payload boxes
     */
    public void updatePayloads(int remaining, int total) {
        payloadBoxPanel.removeAll(); // Clear existing boxes

        for (int i = 0; i < total; i++) {
            JPanel box = new JPanel();
            box.setPreferredSize(new Dimension(35, 35));

            // i < remaining means the payload is active (Pink)
            // i >= remaining means it's spent (Dark Blue)
            if (i < remaining) {
                box.setBackground(new Color(255, 51, 153)); // Neon Pink
            } else {
                box.setBackground(new Color(0, 51, 102)); // Dark Navy/Spent
            }

            box.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            payloadBoxPanel.add(box);
        }

        payloadBoxPanel.revalidate();
        payloadBoxPanel.repaint();
    }

    /**
     * Formats a phrase for display.
     * Example: "HELLO WORLD" with 'H' and 'E' guessed becomes "H E _ _ _   _ _ _ _ _"
     */
    public void updateWordDisplay(String secretPhrase, String guessedLetters) {
        StringBuilder displayString = new StringBuilder();

        for (char c : secretPhrase.toUpperCase().toCharArray()) {
            if (c == ' ') {
                // Use 3 spaces to create a clear visual break between words
                displayString.append("   ");
            } else {
                // Check if letter was guessed
                if (guessedLetters.indexOf(c) != -1) {
                    displayString.append(c).append(" ");
                } else {
                    displayString.append("_ ");
                }
            }
        }

        wordDisplay.setText(displayString.toString().trim());
    }

    public SecurityRingPanel getSecurityRingPanel() {
        return securityRingPanel;
    }

    public Map<Character, JButton> getAlphabetButtons() {
        return alphabetButtons;
    }

    /**
     * Disables a button and changes its color to show it was used.
     */
    public void markLetterAsUsed(char letter, boolean isCorrect) {
        JButton btn = alphabetButtons.get(letter);
        if (btn != null) {
            btn.setEnabled(false);
            // Turn red if wrong, green if correct, or just gray out
            btn.setBackground(isCorrect ? new Color(100, 255, 100) : Color.GRAY);
        }
    }

    /**
     * Resets all buttons for a new game.
     */
    public void resetKeyboard() {
        for (JButton btn : alphabetButtons.values()) {
            btn.setEnabled(true);
            btn.setBackground(new Color(245, 245, 240));
        }
    }
}