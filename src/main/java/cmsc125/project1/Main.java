package cmsc125.project1;

import cmsc125.project1.models.UserModel;
import cmsc125.project1.views.LogInView;
import cmsc125.project1.controllers.LogInController;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserModel model = new UserModel();
            LogInView view = new LogInView();

            new LogInController(model, view);

            view.setVisible(true);
        });
    }
}