package cmsc125.project1.views;

import cmsc125.project1.services.AppInfo;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.text.*;

public class GameView extends JInternalFrame {

    private SecurityRingPanel securityRingPanel;
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

        getContentPane().add(createTopPanel(), BorderLayout.NORTH);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        getContentPane().add(createBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(0, 0, 51));
        topPanel.setBorder(BorderFactory.createLineBorder(Color.CYAN, 1));

        JLabel title = new JLabel(AppInfo.getAppName());
        title.setForeground(Color.CYAN);
        title.setFont(new Font("Monospaced", Font.BOLD, 48));
        topPanel.add(title);
        return topPanel;
    }

    private JPanel createLeftPanel() {
        // The main container for the left side still uses GridBag to split
        // the "Word Box" (top) and "Keyboard" (bottom).
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

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

        wordDisplay = new JTextPane();
        wordDisplay.setEditable(false);
        wordDisplay.setFocusable(false);
        wordDisplay.setOpaque(false);
        wordDisplay.setFont(new Font("Monospaced", Font.BOLD, 36));
        wordDisplay.setForeground(Color.WHITE);

        StyledDocument doc = wordDisplay.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        wordContainer.add(wordDisplay, BorderLayout.CENTER);
        leftPanel.add(wordContainer, gbc);

        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 20, 10);

        // Create wrapper panel to hold the rows and BoxLayout.Y_AXIS stacks components vertically (top to bottom) for simple stacking.
        JPanel keyboardWrapper = new JPanel();
        keyboardWrapper.setLayout(new BoxLayout(keyboardWrapper, BoxLayout.Y_AXIS));
        keyboardWrapper.setOpaque(false);

        keyboardWrapper.add(createKeyboardRow("QWERTYUIOP"));
        keyboardWrapper.add(Box.createVerticalStrut(15));
        keyboardWrapper.add(createKeyboardRow("ASDFGHJKL"));
        keyboardWrapper.add(Box.createVerticalStrut(15));
        keyboardWrapper.add(createKeyboardRow("ZXCVBNM"));

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
        label.setForeground(new Color(0, 255, 204));
        label.setFont(new Font("Monospaced", Font.BOLD, 18));
        bottomPanel.add(label);

        payloadBoxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        payloadBoxPanel.setOpaque(false);
        bottomPanel.add(payloadBoxPanel);

        return bottomPanel;
    }

    private JPanel createKeyboardRow(String letters) {
        // FlowLayout.CENTER automatically centers the buttons in the row, and naturally look keyboard staggered
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        row.setOpaque(false);

        char[] chars = letters.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            JButton btn = createStyledButton(String.valueOf(c));
            alphabetButtons.put(c, btn);
            row.add(btn);
        }
        return row;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Monospaced", Font.BOLD, 18));
        btn.setPreferredSize(new Dimension(60, 60));
        btn.setBackground(new Color(45, 45, 45));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
        return btn;
    }

    public void updatePayloads(int remaining, int total) {
        payloadBoxPanel.removeAll();
        for (int i = 0; i < total; i++) {
            JPanel box = new JPanel();
            box.setPreferredSize(new Dimension(35, 35));
            if (i < remaining) {
                box.setBackground(new Color(255, 51, 153));
            } else {
                box.setBackground(new Color(0, 51, 102));
            }
            box.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            payloadBoxPanel.add(box);
        }
        payloadBoxPanel.revalidate();
        payloadBoxPanel.repaint();
    }

    public void updateWordDisplay(String secretPhrase, String guessedLetters) {
        StringBuilder displayString = new StringBuilder();
        char[] phraseChars = secretPhrase.toUpperCase().toCharArray();

        for (int i = 0; i < phraseChars.length; i++) {
            char c = phraseChars[i];
            if (c == ' ') {
                displayString.append("   ");
            } else {
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

    public void markLetterAsUsed(char letter, boolean isCorrect) {
        JButton btn = alphabetButtons.get(letter);
        if (btn != null) {
            btn.setEnabled(false);
            if (isCorrect) {
                btn.setBackground(new Color(100, 255, 100));
            } else {
                btn.setBackground(Color.GRAY);
            }
        }
    }

    public void resetKeyboard() {
        for (Character key : alphabetButtons.keySet()) {
            JButton btn = alphabetButtons.get(key);
            btn.setEnabled(true);
            btn.setBackground(new Color(45, 45, 45)); // Reset to original dark color
        }
    }
}