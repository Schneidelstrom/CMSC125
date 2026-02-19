package cmsc125.project1.views;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/* This class is the "desktop screen" of the game
 * TODO: remove one of the JInternalFrames, there are two which makes it redundant
 * The JDesktopPane automatically generates a jinternal frame, however there is also an implementation of JInternalFrame which 
 * was added to the borderlayout.south at line 36
 *
 *
 * */

public class DashboardView extends JPanel {
    private final JDesktopPane desktopPane;
    private final JPanel taskbarPanel, iconsPanel;
    private JPanel selectedIcon = null;
    private JToggleButton startButton;
    private JPopupMenu startMenu;
    private JMenuItem logoutItem, shutdownItem, playItem;
    private static final Color SELECTION_COLOR = new Color(0, 120, 215, 75), HOVER_COLOR = new Color(255, 255, 255, 50);
    private static final int ICON_SIZE = 64;
    private static final String ASSETS_PATH = "/assets/";

    public DashboardView(String username) {
        setLayout(new BorderLayout());

        desktopPane = new JDesktopPane() {
            private Image wallpaper;
            {
                wallpaper = loadAssetImage("wallpaper.png");
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (wallpaper != null) {
                    g.drawImage(wallpaper, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(30, 30, 30));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        iconsPanel = new JPanel();
        iconsPanel.setLayout(new BoxLayout(iconsPanel, BoxLayout.Y_AXIS));
        iconsPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        iconsPanel.setOpaque(false);
        desktopPane.add(iconsPanel, JLayeredPane.FRAME_CONTENT_LAYER);

        desktopPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                iconsPanel.setBounds(0, 0, desktopPane.getWidth(), desktopPane.getHeight());
                iconsPanel.revalidate();
            }
        });

        add(desktopPane, BorderLayout.CENTER);

        taskbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        taskbarPanel.setBackground(new Color(236, 240, 241));
        taskbarPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));

        createStartMenu();
        taskbarPanel.add(startButton);
        add(taskbarPanel, BorderLayout.SOUTH);
    }

    private void createStartMenu() {
        startButton = new JToggleButton("Start");
        startMenu = new JPopupMenu();
        playItem = new JMenuItem("Play");
        logoutItem = new JMenuItem("Logout");
        shutdownItem = new JMenuItem("Shutdown");
        startMenu.add(playItem);
        startMenu.add(logoutItem);
        startMenu.add(shutdownItem);

        startButton.addActionListener(e -> {
            if (startButton.isSelected()) {
                startMenu.show(startButton, 0, -startMenu.getPreferredSize().height);
            }
        });

        startMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                startButton.setSelected(false);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                startButton.setSelected(false);
            }
        });
    }

    public void addDesktopIcon(String name, ActionListener onClick) {
        JPanel iconWrapper = new JPanel();
        iconWrapper.setLayout(new BoxLayout(iconWrapper, BoxLayout.Y_AXIS));
        iconWrapper.setOpaque(false);
        iconWrapper.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel iconGraphic = new JLabel(getIconPlaceholder(name));
        iconGraphic.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(name);
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        iconWrapper.add(iconGraphic);
        iconWrapper.add(Box.createVerticalStrut(5));
        iconWrapper.add(textLabel);

        JPanel gridCell = new JPanel(new GridBagLayout());
        gridCell.setOpaque(false);
        gridCell.setPreferredSize(new Dimension(100, 120));
        gridCell.setMaximumSize(new Dimension(100, 120));
        gridCell.setAlignmentX(Component.LEFT_ALIGNMENT);
        gridCell.add(iconWrapper);

        iconWrapper.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleSingleClick(iconWrapper);
                if (e.getClickCount() == 2) {
                    onClick.actionPerformed(null);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if (selectedIcon != iconWrapper) {
                    iconWrapper.setOpaque(true);
                    iconWrapper.setBackground(HOVER_COLOR);
                    iconWrapper.repaint();
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (selectedIcon != iconWrapper) {
                    iconWrapper.setOpaque(false);
                    iconWrapper.repaint();
                }
            }
        });

        iconsPanel.add(gridCell);
        iconsPanel.revalidate();
        iconsPanel.repaint();
    }

    public void addInternalFrame(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
        try { frame.setSelected(true); } catch (java.beans.PropertyVetoException ignored) {}
    }

    public void addTaskbarButton(JToggleButton btn) {
        taskbarPanel.add(btn);
        taskbarPanel.revalidate();
        taskbarPanel.repaint();
    }

    public void removeTaskbarButton(JToggleButton btn) {
        taskbarPanel.remove(btn);
        taskbarPanel.revalidate();
        taskbarPanel.repaint();
    }

    public void addPlayListener(ActionListener listener) {
        playItem.addActionListener(listener);
    }

    public void addLogoutListener(ActionListener listener) {
        logoutItem.addActionListener(listener);
    }

    public void addShutdownListener(ActionListener listener) {
        shutdownItem.addActionListener(listener);
    }

    public int showConfirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "System", JOptionPane.YES_NO_OPTION);
    }

    private BufferedImage loadAssetImage(String path) {
        try {
            URL url = getClass().getResource(ASSETS_PATH + path);
            return ImageIO.read(url);
        } catch (IOException e) {
            System.err.println("Could not load asset: " + path);
        }
        return null;
    }

    private Icon getIconPlaceholder(String name) {
        String fileName = "icons/" + name.toLowerCase().replace(" ", "_") + ".png";

        BufferedImage img = loadAssetImage(fileName);
        if (img != null) {
            Image scaled = img.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }

        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.CYAN);
                g2.fillRoundRect(x, y, ICON_SIZE, ICON_SIZE, 15, 15);
            }
            @Override public int getIconWidth() { return ICON_SIZE; }
            @Override public int getIconHeight() { return ICON_SIZE; }
        };
    }

    private void handleSingleClick(JPanel clickedIcon) {
        if (selectedIcon != null && selectedIcon != clickedIcon) {
            selectedIcon.setOpaque(false);
            selectedIcon.repaint();
        }

        selectedIcon = clickedIcon;
        clickedIcon.setOpaque(true);
        clickedIcon.setBackground(SELECTION_COLOR);
        clickedIcon.repaint();
        desktopPane.requestFocusInWindow();
    }
}
