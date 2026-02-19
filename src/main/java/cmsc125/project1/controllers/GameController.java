package cmsc125.project1.controllers;

import cmsc125.project1.models.GameModel;
import cmsc125.project1.views.GameView;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class GameController {
    private GameModel model;
    private GameView view;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        this.view.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                dispose();
            }
        });
    }

    public void wrongGuess() {
        model.reduceLives();

        // update the rings
        view.getSecurityRingPanel().updateStatus(model.getLives());

        if (model.getLives() <= 0) gameOver();
    }

    public void dispose() {
        // In the future, you can add logic here to stop any game timers,
        // save game state, or release other resources.
    }

    public void gameOver() {
        view.getSecurityRingPanel().triggerSystemFailure();
        // Disable keyboard so no more input is accepted
        view.getAlphabetButtons().values().forEach(btn -> btn.setEnabled(false));
    };
}
