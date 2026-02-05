package cmsc125.project1.controllers;

import cmsc125.project1.Main;
import cmsc125.project1.models.DashboardModel;
import cmsc125.project1.views.DashboardView;

import javax.swing.JInternalFrame;
import javax.swing.JToggleButton;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DashboardController {
    private final DashboardModel model;
    private final DashboardView view;

    public DashboardController(DashboardModel model, DashboardView view) {
        this.model = model;
        this.view = view;
        initDesktop();
        initSystemMenu();
        initWindowExit();
    }

    private void initDesktop() {
        for (String appName : model.getApps()) {
            view.addDesktopIcon(appName, e -> launchApp(appName));
        }
    }

    private void initSystemMenu() {
        view.addLogoutListener(e -> {
            if (view.showConfirm("Are you sure you want to logout?") == 0) {
                view.dispose();
                Main.showLoginScreen();
            }
        });

        view.addShutdownListener(e -> performShutdown());
    }

    private void initWindowExit() {
        view.addWindowCloseListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                performShutdown();
            }
        });
    }

    private void performShutdown() {
        if (view.showConfirm("Shut down system?") == 0) {
            System.exit(0);
        }
    }

    private void launchApp(String appName) {
        JInternalFrame frame = new JInternalFrame(appName, true, true, true, true);
        frame.setSize(400, 300);
        frame.setLocation(50, 50);

        JToggleButton taskButton = new JToggleButton(appName);
        taskButton.setSelected(true);

        taskButton.addActionListener(e -> {
            try {
                if (frame.isIcon()) {
                    frame.setIcon(false);
                    frame.setSelected(true);
                    taskButton.setSelected(true);
                } else if (frame.isSelected()) {
                    frame.setIcon(true);
                    taskButton.setSelected(false);
                } else {
                    frame.setSelected(true);
                    taskButton.setSelected(true);
                }
            } catch (java.beans.PropertyVetoException pve) {
                pve.printStackTrace();
            }
        });

        frame.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                view.removeTaskbarButton(taskButton);
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
                taskButton.setSelected(false);
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
                taskButton.setSelected(true);
            }

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                taskButton.setSelected(true);
            }
        });

        view.addInternalFrame(frame);
        view.addTaskbarButton(taskButton);
    }
}
