package cmsc125.project1.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class HowToPlayView extends JInternalFrame {
    private JTextArea instructionArea;
    private JLabel imageLabel;
    private JButton prevButton, nextButton;

    public HowToPlayView() {
        super("How to Play", true, true, true, true);
        setSize(600, 500);
        setLayout(new BorderLayout());

        instructionArea = new JTextArea();
        instructionArea.setEditable(false);
        instructionArea.setLineWrap(true);
        instructionArea.setWrapStyleWord(true);
        instructionArea.setFont(new Font("Monospaced", Font.BOLD, 16));
        instructionArea.setBackground(new Color(30, 30, 30));
        instructionArea.setForeground(Color.GREEN);
        instructionArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(instructionArea, BorderLayout.NORTH);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setBackground(Color.BLACK);
        imageLabel.setOpaque(true);
        add(imageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(30, 30, 30));

        prevButton = new JButton("< Previous");
        nextButton = new JButton("Next >");

        styleButton(prevButton);
        styleButton(nextButton);

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Monospaced", Font.PLAIN, 14));
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    public void updateContent(String text, String imagePath) {
        instructionArea.setText(text);

        URL imgUrl = getClass().getResource(imagePath);
        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            Image img = icon.getImage().getScaledInstance(1280, 720, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
            imageLabel.setText("");
        } else {
            imageLabel.setIcon(null);
            imageLabel.setForeground(Color.RED);
            imageLabel.setText("Image not found: " + imagePath);
        }
    }

    public void updateButtonStates(boolean isFirst, boolean isLast) {
        prevButton.setEnabled(!isFirst);
        nextButton.setEnabled(!isLast);
    }

    public JButton getPrevButton() { return prevButton; }
    public JButton getNextButton() { return nextButton; }
}