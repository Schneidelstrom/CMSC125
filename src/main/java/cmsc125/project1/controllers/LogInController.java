package cmsc125.project1.controllers;

import cmsc125.project1.models.UserModel;
import cmsc125.project1.views.LogInView;
import cmsc125.project1.services.AudioManager;

import java.awt.event.ActionListener;

public class LogInController {
    private UserModel model;
    private LogInView view;
    private Runnable onSuccess; // Callback for Main to switch screens

    public LogInController(UserModel model, LogInView view, Runnable onSuccess) {
        this.model = model;
        this.view = view;
        this.onSuccess = onSuccess;
        initListeners();
    }

    private void initListeners() {
        ActionListener loginAction = e -> attemptLogin();
        view.addLoginListener(loginAction);
        view.addPowerListener(e -> confirmExit());
    }

    private void attemptLogin() {
        if (view.isPlaceholder()) {
            view.showError("Please enter a valid username.");
            return;
        }

        if (model.validateUsername(view.getUsername(), LogInView.LOGIN_PLACEHOLDER)) {
            model.setUsername(view.getUsername());
            cmsc125.project1.services.AudioManager.playSound("logged_in.wav");
            view.close();
            if (onSuccess != null) onSuccess.run();
        } else {
            view.showError("Please enter a valid username.");
        }
    }

    private void confirmExit() {
        int choice = view.showConfirm("Are you sure you want to exit?");
        if (choice == 0) System.exit(0);
    }
}