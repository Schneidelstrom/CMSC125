package cmsc125.project1.controllers;

import cmsc125.project1.models.*;
import cmsc125.project1.services.AudioManager;
import cmsc125.project1.views.*;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DashboardController {
    private final DashboardModel model;
    private final DashboardView view;
    private final Runnable logoutHandler;

    private final Set<String> activeBGApps = new HashSet<>();
    private static final Set<String> BG_APPS = Set.of("Play", "About", "How to Play");
    private final Map<String, Point> lastAppPositions = new HashMap<>();
    private final String currentUser;

    // Update constructor
    public DashboardController(DashboardModel model, DashboardView view, Runnable logoutHandler, String currentUser) {
        this.model = model;
        this.view = view;
        this.logoutHandler = logoutHandler;
        this.currentUser = currentUser; // Save it!

        initDesktop();
        initSystemMenu();
    }

    private void initDesktop() {
        for (String appName : model.getApps()) {
            view.addDesktopIcon(appName, e -> {
                AudioManager.playSound("icon_click.wav");
                if ("Play".equals(appName)) {
                    showDifficultyChooser();
                } else {
                    launchApp(appName);
                }
            });
        }
    }

    private void initSystemMenu() {
        view.addPlayListener(e -> showDifficultyChooser());
        view.addLogoutListener(e -> {
            if (view.showConfirm("Are you sure you want to logout?") == 0) {
                AudioManager.playSound("logged_out.wav");
                new Thread(AudioManager::stopBGM).start();
                if (logoutHandler != null) logoutHandler.run();
            }
        });

        view.addShutdownListener(e -> performShutdown());
    }

    private void showDifficultyChooser() {
        String[] difficulties = {"Script Kiddie", "System Breach", "Root Override"};
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel label = new JLabel("Select a difficulty level:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
        panel.add(Box.createVerticalStrut(15));

        for (int i = 0; i < difficulties.length; i++) {
            JButton button = new JButton(difficulties[i]);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            final int choice = i;
            button.addActionListener(e -> {
                int payloads = (choice == 0) ? 3 : (choice == 1) ? 5 : 7;
                launchApp("Play", payloads, difficulties[choice]);
                SwingUtilities.getWindowAncestor(panel).dispose();
            });
            panel.add(button);
            if (i < difficulties.length - 1) {
                panel.add(Box.createVerticalStrut(10));
            }
        }

        JOptionPane.showOptionDialog(
                view,
                panel,
                "Difficulty Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{},
                null
        );
    }

    private void performShutdown() {
        if (view.showConfirm("Shut down system?") == 0) {
            new Thread(AudioManager::stopBGM).start();
            System.exit(0);
        }
    }

    private void launchApp(String appName) {
        launchApp(appName, 0, "");
    }

    private void launchApp(String appName, int payloads, String difficulty) {
        JInternalFrame frame = createFrameInstance(appName, payloads, difficulty);

        if (lastAppPositions.containsKey(appName)) {
            frame.setLocation(lastAppPositions.get(appName));
        } else {
            centerInternalFrame(frame);
        }

        JToggleButton taskButton = new JToggleButton(appName);
        taskButton.setSelected(true);
        taskButton.addActionListener(e -> handleTaskbarClick(frame, taskButton));

        if (BG_APPS.contains(appName)) {
            activeBGApps.add(appName);
            AudioManager.playBGM("game_bgm.wav");
        }

        frame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                lastAppPositions.put(appName, frame.getLocation());
                view.removeTaskbarButton(taskButton);

                if (BG_APPS.contains(appName)) {
                    activeBGApps.remove(appName);
                    if (activeBGApps.isEmpty()) {
                        new Thread(AudioManager::stopBGM).start();
                    }
                }
            }
            @Override public void internalFrameActivated(InternalFrameEvent e) { taskButton.setSelected(true); }
            @Override public void internalFrameDeactivated(InternalFrameEvent e) { taskButton.setSelected(false); }
        });

        view.addInternalFrame(frame);
        view.addTaskbarButton(taskButton);
    }

    private void centerInternalFrame(JInternalFrame frame) {
        Dimension desktopSize = view.getSize();
        Dimension frameSize = frame.getSize();
        int x = (desktopSize.width - frameSize.width) / 2;
        int y = (desktopSize.height - frameSize.height) / 2;
        frame.setLocation(x, Math.max(0, y - 40));
    }

private JInternalFrame createFrameInstance(String appName, int payloads, String difficulty) {
        switch (appName) {
            case "About": return new AboutView();
            case "Leaderboards": 
                cmsc125.project1.models.LeaderboardsModel lm = new cmsc125.project1.models.LeaderboardsModel();
                cmsc125.project1.views.LeaderboardsView lv = new cmsc125.project1.views.LeaderboardsView();
                new cmsc125.project1.controllers.LeaderboardsController(lm, lv);
                return lv;
            case "Settings":
                SettingsModel sm = new SettingsModel();
                SettingsView sv = new SettingsView();
                new SettingsController(sm, sv);
                return sv;
            case "Play":
                GameModel gm = new GameModel(payloads);
                // UPDATE HERE: Pass currentUser and difficulty as the 3rd and 4th arguments
                GameController gc = new GameController(gm, null, currentUser, difficulty); 
                GameView gv = new GameView(gc::handleKeypress); 
                gc.setView(gv); 
                return gv;
            default:
                JInternalFrame f = new JInternalFrame(appName, true, true, true, true);
                f.setSize(400, 300);
                return f;
        }
    }

    private void handleTaskbarClick(JInternalFrame frame, JToggleButton btn) {
        try {
            if (frame.isIcon()) {
                frame.setIcon(false);
                frame.setSelected(true);
            } else if (frame.isSelected()) {
                frame.setIcon(true);
            } else {
                frame.setSelected(true);
            }
        } catch (java.beans.PropertyVetoException pve) { pve.printStackTrace(); }
    }
}
