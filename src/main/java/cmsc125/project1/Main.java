package cmsc125.project1;

import cmsc125.project1.models.*;
import cmsc125.project1.services.AppInfo;
import cmsc125.project1.views.*;
import cmsc125.project1.controllers.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.awt.CardLayout;

public class Main extends JFrame {
    private static Main instance;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private static final String LOGIN_CARD = "LOGIN", DASHBOARD_CARD = "DASHBOARD";

    private Object currentController;

    public Main() {
        super("De_crypt Login");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        AppInfo.getInfo();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                confirmExit();
            }
        });

        initLayout();
    }

    private void initLayout() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);
    }

    public static void main(String[] args) {
        new cmsc125.project1.models.SettingsModel().loadSettings();

        SwingUtilities.invokeLater(() -> {
            instance = new Main();
            instance.showLoginScreen();
            instance.setVisible(true);
        });
    }

    public void showLoginScreen() {
        setTitle("De_crypt Login");

        UserModel model = new UserModel();
        LogInView view = new LogInView();

        LogInController controller = new LogInController(model, view,
                () -> transitionToDashboard(model.getUsername())
        );

        currentController = controller;

        addCard(view, LOGIN_CARD);
        cardLayout.show(mainPanel, LOGIN_CARD);
    }

    public void transitionToDashboard(String username) {
        setTitle("De_crypt OS - User: " + username);

        DashboardModel model = new DashboardModel();
        DashboardView view = new DashboardView(username);

        Runnable onLogout = this::showLoginScreen;

        currentController = new DashboardController(model, view, onLogout);

        addCard(view, DASHBOARD_CARD);
        cardLayout.show(mainPanel, DASHBOARD_CARD);
    }

    private void addCard(JPanel panel, String name) {
        mainPanel.add(panel, name);
    }

    public static void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(instance, "Force System Shutdown?", "Power Off", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) System.exit(0);
    }
}