package cmsc125.project1.controllers;

import cmsc125.project1.models.LeaderboardsModel;
import cmsc125.project1.views.LeaderboardsView;

public class LeaderboardsController {
    public LeaderboardsController(LeaderboardsModel model, LeaderboardsView view) {
        // Fetch top scores and send them straight to the view
        view.updateScores(model.getTopScores());
    }
}
