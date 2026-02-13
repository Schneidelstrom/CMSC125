package cmsc125.project1.views;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.JDesktopPane;
import javax.swing.JLayeredPane;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* This class is the "desktop screen" of the game
 * TODO: remove one of the JInternalFrames, there are two which makes it redundant
 * The JDesktopPane automatically generates a jinternal frame, however there is also an implementation of JInternalFrame which 
 * was added to the borderlayout.south at line 36
 *
 *
 * */

public class DashboardView extends JFrame {
    private final JDesktopPane desktopPane;
    private final JPanel taskbarPanel, iconsPanel;
    private JToggleButton startButton;
    private JPopupMenu startMenu;
    private JMenuItem logoutItem, shutdownItem;

    public DashboardView(String username) {
        setTitle("De_crypt OS - User: " + username);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout());

        desktopPane = new JDesktopPane();
        desktopPane.setBackground(Color.PINK);

        iconsPanel = new JPanel(new GridLayout(0, 1, 0, 20));
        iconsPanel.setOpaque(false);
        iconsPanel.setBounds(20, 20, 150, 1000);

        desktopPane.add(iconsPanel, JLayeredPane.DEFAULT_LAYER);
        add(desktopPane, BorderLayout.CENTER);

        taskbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        taskbarPanel.setBackground(Color.LIGHT_GRAY);
        taskbarPanel.setBorder(BorderFactory.createEtchedBorder());

        createStartMenu();
        taskbarPanel.add(startButton);

        add(taskbarPanel, BorderLayout.SOUTH);
    }

    private void createStartMenu() {
        startButton = new JToggleButton("Start");
        startMenu = new JPopupMenu();

        logoutItem = new JMenuItem("Logout");
        shutdownItem = new JMenuItem("Shutdown");

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
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.Y_AXIS));
        iconPanel.setOpaque(false);
        iconPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel graphic = new JPanel();
        graphic.setBackground(Color.GREEN);
        graphic.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        JLabel label = new JLabel(name);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 15));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        iconPanel.add(graphic);
        iconPanel.add(Box.createVerticalStrut(5));
        iconPanel.add(label);

        iconPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    onClick.actionPerformed(null);
                }
            }
        });

        iconsPanel.add(iconPanel);
    }

    public void addInternalFrame(JInternalFrame frame) {
        desktopPane.add(frame, JLayeredPane.MODAL_LAYER);
        frame.setVisible(true);
        try { frame.setSelected(true); } catch (Exception ignored) {}
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

    public void addLogoutListener(ActionListener l) {
        logoutItem.addActionListener(l);
    }

    public void addShutdownListener(ActionListener l) {
        shutdownItem.addActionListener(l);
    }

    public void addWindowCloseListener(java.awt.event.WindowListener listener) {
        this.addWindowListener(listener);
    }

    public int showConfirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "System", JOptionPane.YES_NO_OPTION);
    }
}
