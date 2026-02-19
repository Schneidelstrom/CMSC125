package cmsc125.project1.views;

import cmsc125.project1.services.AppInfo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

public class AboutView extends JInternalFrame {
    private static final String GITHUB_URL = "https://github.com/Schneidelstrom/CMSC125.git";
    private static final Color MATRIX_GREEN = new Color(0, 255, 65), DARK_BG = Color.BLACK;
    private static final Font TERMINAL_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 14);
    private static final int SCROLL_SPEED_MS = 30;
    private static final int MIN_WIDTH = 500;
    private static final int MIN_HEIGHT = 400;

    private JScrollPane scrollPane;
    private JPanel contentPanel;
    private Timer scrollTimer;

    public AboutView() {
        super("About", true, true, false, true);
        initFrame();
        initComponents();
        startAutoScroll();
        setSize(1500, 750);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    @Override
    public void dispose() {
        if (scrollTimer != null) {
            scrollTimer.stop();
        }
        super.dispose();
    }

    private void initFrame() {
        getContentPane().setBackground(DARK_BG);
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(MATRIX_GREEN, 2));
    }

    private void initComponents() {
        contentPanel = new JPanel();
        contentPanel.setBackground(DARK_BG);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        addCreditsContent();

        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(DARK_BG);
        scrollPane.getViewport().setBackground(DARK_BG);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { scrollTimer.stop(); }
            @Override
            public void mouseExited(MouseEvent e) { scrollTimer.start(); }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(DARK_BG);

        JButton closeBtn = new JButton("TERMINATE_SESSION");
        closeBtn.setFont(TERMINAL_FONT);
        closeBtn.addActionListener(e -> dispose());

        bottomPanel.add(closeBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addCreditsContent() {
        addLabel("DE_CRYPT OS", new Font(Font.MONOSPACED, Font.BOLD, 36), 50);
        addLabel("Version: " + AppInfo.getAppVersion(), TERMINAL_FONT, 30);

        addHeader("SYSTEM_PURPOSE");
        addCenteredTextPane("A cybersecurity simulation game where players defend system Protection Rings by decrypting OS-related terms. Secure the Kernel before Root Access is compromised.");

        addHeader("PROTECTION_RINGS");
        addLabel("> Ring 3: User Applications", TERMINAL_FONT, 5);
        addLabel("> Ring 2: System Libraries", TERMINAL_FONT, 5);
        addLabel("> Ring 1: Device Drivers", TERMINAL_FONT, 5);
        addLabel("> Ring 0: Secure Kernel", TERMINAL_FONT, 30);

        addHeader("CORE_DEVELOPERS");
        addLabel("ali1x3", TERMINAL_FONT, 5);
        addLabel("ddrhckrzz", TERMINAL_FONT, 5);
        addLabel("Schneidelstrom", TERMINAL_FONT, 30);

        addHeader("SOURCE_REPOSITORY");
        JLabel linkLabel = new JLabel("<html><div style='text-align: center;'><u>" + GITHUB_URL + "</u></div></html>");
        linkLabel.setFont(TERMINAL_FONT);
        linkLabel.setForeground(MATRIX_GREEN);
        linkLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        linkLabel.setHorizontalAlignment(SwingConstants.CENTER);
        linkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { openBrowser(); }
        });
        contentPanel.add(linkLabel);
        contentPanel.add(Box.createVerticalStrut(40));

        addHeader("LEGAL_NOTICE");
        addCenteredTextPane("Academic Submission Only. Developed for CMSC 125: Operating Systems. Utilizes royalty-free assets and terminal-emulation GUI components.");
    }

    private void addLabel(String text, Font font, int spacing) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(MATRIX_GREEN);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(label);
        contentPanel.add(Box.createVerticalStrut(spacing));
    }

    private void addHeader(String text) {
        JLabel header = new JLabel("--- " + text + " ---");
        header.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        header.setForeground(Color.WHITE);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(header);
        contentPanel.add(Box.createVerticalStrut(10));
    }

    private void addCenteredTextPane(String text) {
        JTextPane pane = new JTextPane();
        pane.setText(text);
        pane.setFont(TERMINAL_FONT);
        pane.setForeground(MATRIX_GREEN);
        pane.setBackground(DARK_BG);
        pane.setEditable(false);
        pane.setFocusable(false);
        pane.setOpaque(false);

        StyledDocument doc = pane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        pane.setAlignmentX(Component.CENTER_ALIGNMENT);
        pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        contentPanel.add(pane);
        contentPanel.add(Box.createVerticalStrut(30));
    }

    private void startAutoScroll() {
        scrollTimer = new Timer(SCROLL_SPEED_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
                int maxValue = verticalBar.getMaximum() - verticalBar.getVisibleAmount();
                int currentValue = verticalBar.getValue();

                if (currentValue < maxValue) {
                    verticalBar.setValue(currentValue + 1);
                } else {
                    scrollTimer.stop();
                }
            }
        });

        Timer initialDelay = new Timer(1500, e -> scrollTimer.start());
        initialDelay.setRepeats(false);
        initialDelay.start();
    }

    private void openBrowser() {
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(AboutView.GITHUB_URL));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Could not open browser: " + e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
