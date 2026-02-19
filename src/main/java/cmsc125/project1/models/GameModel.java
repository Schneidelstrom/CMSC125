package cmsc125.project1.models;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameModel {
    private final int payloads;
    private final int lives;
    private final String currentWord;

    private static final List<String> WORD_BANK = Arrays.asList(
            "KERNEL", "SEMAPHORE", "DEADLOCK", "THREAD", "PROCESS",
            "VIRTUAL", "MEMORY", "SCHEDULER", "FILESYSTEM", "INTERRUPT",
            "MUTEX", "PAGING", "SEGMENTATION", "CONCURRENCY", "SHELL"
    );

    public GameModel(int payloads) {
        this.payloads = payloads;
        this.lives = 7; // Default lives
        this.currentWord = selectRandomWord();
    }

    private String selectRandomWord() {
        Random rand = new Random();
        return WORD_BANK.get(rand.nextInt(WORD_BANK.size()));
    }

    public int getPayloads() {
        return payloads;
    }

    public int getLives() {
        return lives;
    }

    public String getCurrentWord() {
        return currentWord;
    }
}
