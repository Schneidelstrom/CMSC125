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
        setMinimumSize(new Dimension(1125, 600));
        setSize(1125, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initLayout() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // Left Side (60% width)
        gbc.gridx = 0;
        gbc.weightx = 0.6;
        mainPanel.add(createLeftPanel(), gbc);

        // Right Side (40% width)
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        mainPanel.add(createRightPanel(), gbc);

        // Top and Bottom still use BorderLayout logic but added to the JFrame
        getContentPane().add(createTopPanel(), BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(createBottomPanel(), BorderLayout.SOUTH);
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

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // --- 1. Word Guess Box ---
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(20, 50, 20, 50);
        gbc.anchor = GridBagConstraints.NORTH;
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
        gbc.gridy = 1;
        gbc.weighty = 1.0; // Give it the remaining space
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 10, 20, 10);

        JPanel keyboardWrapper = new JPanel(new GridBagLayout());
        keyboardWrapper.setOpaque(false);
        GridBagConstraints kbc = new GridBagConstraints();
        kbc.gridx = 0;
        kbc.fill = GridBagConstraints.BOTH;
        kbc.weightx = 1.0;
        kbc.weighty = 1.0;

        kbc.gridy = 0;
        keyboardWrapper.add(createKeyboardRow("QWERTYUIOP"), kbc);
        kbc.gridy = 1;
        keyboardWrapper.add(createKeyboardRow("ASDFGHJKL"), kbc);
        kbc.gridy = 2;
        keyboardWrapper.add(createKeyboardRow("ZXCVBNM"), kbc);

        leftPanel.add(keyboardWrapper, gbc);

        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.DARK_GRAY));
        rightPanel.setPreferredSize(new Dimension(400, 0));

        securityRingPanel = new SecurityRingPanel();
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

    private JPanel createKeyboardRow(String letters) {
        // Standardizing the gaps
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        row.setOpaque(false);

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

        // Ensure the button is large enough to show the letter AND the 3D bevel
        btn.setPreferredSize(new Dimension(60, 60));

        // Match your reference image: Dark keys with White text
        btn.setBackground(new Color(45, 45, 45));
        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true); // Ensures background color shows up
        btn.setOpaque(true);

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