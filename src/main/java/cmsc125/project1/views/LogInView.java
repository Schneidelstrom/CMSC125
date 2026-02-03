package cmsc125.project1.views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowListener;

public class LogInView extends JFrame {
    private JTextField usernameField;
    private JButton loginButton, powerButton;

    private static final Font HEADER_FONT = new Font("Monospaced", Font.BOLD, 64);
    private static final Font INPUT_FONT = new Font("SansSerif", Font.PLAIN, 17);
    private static final Color BG_COLOR = Color.WHITE;
    private static final Color USER_INPUT_COLOR = Color.BLACK;
    private static final Color PLACEHOLDER_INPUT_COLOR = Color.GRAY;
    private static final String APP_HEADER = "DE_CRYPT OS";

    // Public constant so Controller can reference it if needed, though ideally, the controller shouldn't care about the specific string.
    public static final String LOGIN_PLACEHOLDER = "Enter Username";

    public LogInView() {
        initializeFrame();
        initializeComponents();
        layoutComponents();
    }

    private void initializeFrame() {
        setTitle("De_crypt Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(BG_COLOR);
        setContentPane(contentPane);
    }

    private void initializeComponents() {
        usernameField = new JTextField(LOGIN_PLACEHOLDER, 23);
        usernameField.setFont(INPUT_FONT);
        usernameField.setForeground(PLACEHOLDER_INPUT_COLOR);
        usernameField.addFocusListener(createPlaceholderFocusListener());

        loginButton = new JButton("→");

        powerButton = new JButton("⏻");
        powerButton.setFont(new Font("SansSerif", Font.BOLD, 64));
        powerButton.setForeground(Color.RED);
    }

    private void layoutComponents() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BG_COLOR);

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel(APP_HEADER);
        titleLabel.setFont(HEADER_FONT);

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        centerPanel.add(titleLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        centerPanel.add(usernameField, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        centerPanel.add(loginButton, gbc);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.add(powerButton);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public String getUsernameInput() {
        return usernameField.getText();
    }

    public boolean isPlaceholderActive() {
        return usernameField.getForeground() == PLACEHOLDER_INPUT_COLOR;
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
        usernameField.addActionListener(listener);
    }

    public void addPowerListener(ActionListener listener) {
        powerButton.addActionListener(listener);
    }

    // Helper to attach window listener (for the 'X' button)
    public void addWindowCloseListener(WindowListener listener) {
        addWindowListener(listener);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Login Error", JOptionPane.WARNING_MESSAGE);
    }

    public int showConfirmation(String message, String title) {
        Object[] options = {"Confirm", "Cancel"};
        return JOptionPane.showOptionDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);
    }

    public void close() {
        dispose();
    }

    private FocusListener createPlaceholderFocusListener() {
        return new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals(LOGIN_PLACEHOLDER)) {
                    usernameField.setText("");
                    usernameField.setForeground(USER_INPUT_COLOR);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setForeground(PLACEHOLDER_INPUT_COLOR);
                    usernameField.setText(LOGIN_PLACEHOLDER);
                }
            }
        };
    }
}