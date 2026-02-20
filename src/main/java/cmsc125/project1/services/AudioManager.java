package cmsc125.project1.services;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AudioManager {
    private static Clip bgmClip;
    private static final String ASSETS_PATH = "/assets/sounds/";
    private static final ExecutorService soundPool = Executors.newCachedThreadPool();

    public static int sfxVolume = 100, bgmVolume = 100;
    public static boolean sfxEnabled = true, bgmEnabled = true;
    public static String currentBGM = "";

    public static void playSound(String fileName) {
        if (!sfxEnabled) return;

        soundPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Clip clip = loadClip(fileName);
                    if (clip != null) {
                        setClipVolume(clip, sfxVolume);
                        clip.start();
                    }
                } catch (Exception e) {
                    System.out.println("Error playing SFX [" + fileName + "]: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public static void playBGM(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            if (currentBGM.isEmpty()) return;
            fileName = currentBGM;
        } else {
            currentBGM = fileName;
        }

        if (!bgmEnabled) {
            stopBGM();
            return;
        }

        if (bgmClip != null && bgmClip.isRunning()) {
            System.out.println(bgmClip);
            return;
        }

        String finalFileName = fileName;
        soundPool.submit(new Runnable() {
            @Override
            public void run() {
                stopBGM();
                try {
                    bgmClip = loadClip(finalFileName);
                    if (bgmClip != null) {
                        setClipVolume(bgmClip, bgmVolume);
                        bgmClip.loop(Clip.LOOP_CONTINUOUSLY);
                        bgmClip.start();
                    }
                } catch (Exception e) {
                    System.out.println("Error playing BGM [" + finalFileName + "]: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    public static void stopBGM() {
        if (bgmClip != null) {
            if (bgmClip.isRunning()) {
                bgmClip.stop();
            }
            bgmClip.close();
            bgmClip = null;
        }
    }

    public static void updateBGMVolume(int newVolume) {
        bgmVolume = newVolume;
        if (bgmClip != null && bgmClip.isOpen()) {
            setClipVolume(bgmClip, bgmVolume);
        }
    }

    private static Clip loadClip(String fileName) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        InputStream is = AudioManager.class.getResourceAsStream(ASSETS_PATH + fileName);

        if (is == null) {
            System.out.println("CRITICAL ERROR: Audio file not found: " + ASSETS_PATH + fileName);
            return null;
        }

        InputStream bufferedIn = new BufferedInputStream(is);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);

        return clip;
    }

    private static void setClipVolume(Clip clip, int volumePercent) {
        if (clip == null) return;
        try {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * (volumePercent / 100.0f)) + gainControl.getMinimum();

            if (volumePercent <= 0) {
                gain = gainControl.getMinimum();
            }
            if (volumePercent >= 100) {
                gain = gainControl.getMaximum();
            }

            gainControl.setValue(gain);
        } catch (IllegalArgumentException e) {
            System.out.println("Volume control not supported for this clip.");
        }
    }
}