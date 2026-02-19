package cmsc125.project1.views;

import cmsc125.project1.services.AudioManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SettingsView extends JInternalFrame {
    private final JCheckBox sfxCheck, bgmCheck;
    private final JSlider sfxSlider, bgmSlider;
    private final JButton saveButton;

    public SettingsView() {
        super("System Settings", true, true, true, true);
        setSize(400, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.BLACK);

        addHeader(panel, "SOUND_EFFECTS");
        sfxCheck = addCheckbox(panel, "Enable SFX", AudioManager.sfxEnabled);
        sfxSlider = addSlider(panel, AudioManager.sfxVolume);

        panel.add(Box.createVerticalStrut(30));

        addHeader(panel, "BACKGROUND_MUSIC");
        bgmCheck = addCheckbox(panel, "Enable BGM", AudioManager.bgmEnabled);
        bgmSlider = addSlider(panel, AudioManager.bgmVolume);

        panel.add(Box.createVerticalStrut(30));

        saveButton = new JButton("SAVE_CONFIGURATION");
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(saveButton);

        add(panel);
    }

    private void addHeader(JPanel p, String text) {
        JLabel l = new JLabel(">> " + text);
        l.setForeground(Color.GREEN);
        l.setFont(new Font("Monospaced", Font.BOLD, 14));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Box.createVerticalStrut(10));
    }

    private JCheckBox addCheckbox(JPanel p, String text, boolean selected) {
        JCheckBox cb = new JCheckBox(text);
        cb.setSelected(selected);
        cb.setOpaque(false);
        cb.setForeground(Color.WHITE);
        cb.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(cb);
        return cb;
    }

    private JSlider addSlider(JPanel p, int value) {
        JSlider s = new JSlider(0, 100, value);
        s.setOpaque(false);
        s.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(s);
        return s;
    }

    public JCheckBox getSfxCheck() { return sfxCheck; }
    public JCheckBox getBgmCheck() { return bgmCheck; }
    public JSlider getSfxSlider() { return sfxSlider; }
    public JSlider getBgmSlider() { return bgmSlider; }
    public JButton getSaveButton() { return saveButton; }
}
