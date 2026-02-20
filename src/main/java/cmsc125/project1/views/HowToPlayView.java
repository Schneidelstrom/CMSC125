package cmsc125.project1.views;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class HowToPlayView extends JInternalFrame {
    private JTextArea instructionArea;
    private ImagePanel imagePanel;
    private JButton prevButton, nextButton;

    public HowToPlayView() {
        super("How to Play", true, true, true, true);
        setSize(800, 600); // Increased default size slightly
        setLayout(new BorderLayout());

        instructionArea = new JTextArea();
        instructionArea.setEditable(false);
        instructionArea.setLineWrap(true);
        instructionArea.setWrapStyleWord(true);
        instructionArea.setFont(new Font("Monospaced", Font.BOLD, 16));
        instructionArea.setBackground(new Color(30, 30, 30));
        instructionArea.setForeground(Color.GREEN);
        instructionArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(new JScrollPane(instructionArea), BorderLayout.NORTH);

        imagePanel = new ImagePanel();
        imagePanel.setBackground(Color.BLACK);
        add(imagePanel, BorderLayout.CENTER);

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

        try {
            URL imgUrl = getClass().getResource(imagePath);
            if (imgUrl != null) {
                Image img = ImageIO.read(imgUrl);
                imagePanel.setImage(img);
            } else {
                System.err.println("Image not found: " + imagePath);
                imagePanel.setImage(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateButtonStates(boolean isFirst, boolean isLast) {
        prevButton.setEnabled(!isFirst);
        nextButton.setEnabled(!isLast);
    }

    public JButton getPrevButton() { return prevButton; }
    public JButton getNextButton() { return nextButton; }

    private class ImagePanel extends JPanel {
        private Image currentImage;

        public void setImage(Image img) {
            this.currentImage = img;
            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (currentImage != null) {
                g.drawImage(currentImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }
}