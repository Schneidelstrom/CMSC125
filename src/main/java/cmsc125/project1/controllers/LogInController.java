package cmsc125.project1.controllers;

import cmsc125.project1.models.UserModel;
import cmsc125.project1.views.LogInView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LogInController {
    private final UserModel model;
    private final LogInView view;

    public LogInController(UserModel model, LogInView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        view.addLoginListener(new LoginActionListener());
        view.addPowerListener(new PowerActionListener());

        view.addWindowCloseListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleExit();
            }
        });
    }

    private void handleExit() {
        int choice = view.showConfirmation("Are you sure you to exit session?", "System Exit");
        if (choice == 0) {
            System.exit(0);
        }
    }

    class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = view.getUsernameInput();

            boolean isPlaceholder = view.isPlaceholderActive();

            if (isPlaceholder) {
                view.showErrorMessage("Please enter a valid username.");
                return;
            }

            if (model.validateUsername(input, LogInView.LOGIN_PLACEHOLDER)) {
                model.setUsername(input);
                System.out.println("Login Successful for: " + model.getUsername());
            } else {
                view.showErrorMessage("Please enter a valid username.");
            }
        }
    }

    class PowerActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            handleExit();
        }
    }
}
