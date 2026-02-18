package cmsc125.project1.services;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioManager {
    private static Clip bgmClip;
    private static final String ASSETS_PATH = "/cmsc125/project1/assets/sounds/";
    private static final ExecutorService soundPool = Executors.newFixedThreadPool(5);

    public static void playSound(String fileName) {
        soundPool.submit(() -> {
            try {
                URL url = AudioManager.class.getResource(ASSETS_PATH + fileName);
                if (url == null) {
                    System.err.println("Sound not found: " + fileName);
                    return;
                }

                AudioInputStream stream = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();

                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) clip.close();
                });

                clip.open(stream);
                clip.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void playBGM(String fileName) {
        soundPool.submit(() -> {
            stopBGM();

            try {
                URL url = AudioManager.class.getResource(ASSETS_PATH + fileName);
                if (url == null) {
                    System.err.println("BGM not found: " + fileName);
                    return;
                }

                AudioInputStream stream = AudioSystem.getAudioInputStream(url);
                bgmClip = AudioSystem.getClip();
                bgmClip.open(stream);
                bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                bgmClip.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void stopBGM() {
        if (bgmClip != null && bgmClip.isRunning()) {
            bgmClip.stop();
            bgmClip.close();
        }
    }
}