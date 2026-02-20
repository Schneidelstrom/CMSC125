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

public class LogInView extends JPanel {
    private JTextField usernameField;
    private JButton loginButton, powerButton;
    public static final String LOGIN_PLACEHOLDER = "Enter Username";
    private static final Color PLACEHOLDER_COLOR = Color.GRAY, TEXT_COLOR = Color.BLACK;

    public LogInView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initUI();
        layoutUI();
    }

    private void initUI() {
        usernameField = new JTextField(LOGIN_PLACEHOLDER, 23);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 17));
        usernameField.setForeground(PLACEHOLDER_COLOR);

        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals(LOGIN_PLACEHOLDER)) {
                    usernameField.setText("");
                    usernameField.setForeground(TEXT_COLOR);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setForeground(PLACEHOLDER_COLOR);
                    usernameField.setText(LOGIN_PLACEHOLDER);
                }
            }
        });

        loginButton = new JButton("→");
        powerButton = new JButton("⏻");
        powerButton.setFont(new Font("SansSerif", Font.BOLD, 32));
        powerButton.setForeground(Color.RED);
    }

    private void layoutUI() {
        JPanel loginManager = new JPanel(new GridBagLayout());
        loginManager.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("DE_CRYPT OS");
        title.setFont(new Font("Monospaced", Font.BOLD, 64));

        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2;
        loginManager.add(title, gbc);

        gbc.gridy=1; gbc.gridwidth=1;
        loginManager.add(usernameField, gbc);

        gbc.gridx=1;
        loginManager.add(loginButton, gbc);

        JPanel buttonsManager = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsManager.setBackground(Color.WHITE);
        buttonsManager.add(powerButton);

        add(loginManager, BorderLayout.CENTER);
        add(buttonsManager, BorderLayout.SOUTH);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public boolean isPlaceholder() {
        return usernameField.getForeground() == PLACEHOLDER_COLOR;
    }

    public void close() {
        usernameField.setText(LOGIN_PLACEHOLDER);
        usernameField.setForeground(PLACEHOLDER_COLOR);
    }

    public void addLoginListener(ActionListener l) {
        loginButton.addActionListener(l);
        usernameField.addActionListener(l);
    }

    public void addPowerListener(ActionListener l) {
        powerButton.addActionListener(l);
    }

    public void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.WARNING_MESSAGE);
    }
    public int showConfirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Confirm", JOptionPane.YES_NO_OPTION);
    }
}
