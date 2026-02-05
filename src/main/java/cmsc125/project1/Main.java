package cmsc125.project1;

import cmsc125.project1.models.*;
import cmsc125.project1.views.*;
import cmsc125.project1.controllers.*;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::showLoginScreen);
    }

    public static void showLoginScreen() {
        UserModel model = new UserModel();
        LogInView view = new LogInView();

        new LogInController(model, view, () -> showDashboard(model.getUsername()));

        view.setVisible(true);
    }

    public static void showDashboard(String username) {
        DashboardModel model = new DashboardModel();
        DashboardView view = new DashboardView(username);
        new DashboardController(model, view);

        view.setVisible(true);
    }
}