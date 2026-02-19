package cmsc125.project1.controllers;

import cmsc125.project1.models.SettingsModel;
import cmsc125.project1.services.AudioManager;
import cmsc125.project1.views.SettingsView;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SettingsController {
    private final SettingsModel model;
    private final SettingsView view;

    public SettingsController(SettingsModel model, SettingsView view) {
        this.model = model;
        this.view = view;
        initListeners();
    }

    private void initListeners() {
        view.getBgmCheck().addActionListener(e -> {
            boolean enabled = view.getBgmCheck().isSelected();
            AudioManager.bgmEnabled = enabled;
            if (enabled) AudioManager.playBGM(AudioManager.currentBGM);
            else AudioManager.stopBGM();
        });

        view.getSfxCheck().addActionListener(e -> {
            AudioManager.sfxEnabled = view.getSfxCheck().isSelected();
        });

        view.getBgmSlider().addChangeListener(e -> {
            AudioManager.updateBGMVolume(view.getBgmSlider().getValue());
        });

        view.getSfxSlider().addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                AudioManager.sfxVolume = view.getSfxSlider().getValue();

                if (!view.getSfxSlider().getValueIsAdjusting()) AudioManager.playSound("icon_click.wav");
            }
        });

        view.getSaveButton().addActionListener(e -> {
            model.saveSettings();
            view.doDefaultCloseAction();
        });
    }
}