package cmsc125.project1.controllers;

import cmsc125.project1.models.HowToPlayModel;
import cmsc125.project1.views.HowToPlayView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HowToPlayController {
    private final HowToPlayModel model;
    private final HowToPlayView view;

    public HowToPlayController(HowToPlayModel model, HowToPlayView view) {
        this.model = model;
        this.view = view;

        refreshView();

        view.getPrevButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.previousStep();
                refreshView();
            }
        });

        view.getNextButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.nextStep();
                refreshView();
            }
        });
    }

    private void refreshView() {
        view.updateContent(model.getCurrentInstruction(), model.getCurrentImagePath());
        view.updateButtonStates(model.isFirstStep(), model.isLastStep());
    }
}