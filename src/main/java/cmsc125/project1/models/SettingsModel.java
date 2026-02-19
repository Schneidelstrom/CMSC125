package cmsc125.project1.models;

import cmsc125.project1.services.AudioManager;

import java.io.*;
import java.util.Properties;

public class SettingsModel {
    private final String CONFIG_FILE = "config.properties";
    private Properties props = new Properties();

    public SettingsModel() {
        loadSettings();
    }

    public void loadSettings() {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);

            AudioManager.sfxEnabled = Boolean.parseBoolean(props.getProperty("sfxEnabled", "true"));
            AudioManager.bgmEnabled = Boolean.parseBoolean(props.getProperty("bgmEnabled", "true"));

            AudioManager.sfxVolume = Integer.parseInt(props.getProperty("sfxVolume", "100"));
            AudioManager.bgmVolume = Integer.parseInt(props.getProperty("bgmVolume", "100"));

        } catch (IOException ex) { }
    }

    public void saveSettings() {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            props.setProperty("sfxEnabled", String.valueOf(AudioManager.sfxEnabled));
            props.setProperty("bgmEnabled", String.valueOf(AudioManager.bgmEnabled));
            props.setProperty("sfxVolume", String.valueOf(AudioManager.sfxVolume));
            props.setProperty("bgmVolume", String.valueOf(AudioManager.bgmVolume));

            props.store(output, "De_crypt Game Settings");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
