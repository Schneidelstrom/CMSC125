package cmsc125.project1.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameModel {
    private final int initialPayloads;
    private final int lives;
    private final List<String> sessionWords;
    private int currentWordIndex;

    private static final List<String> WORD_BANK = Arrays.asList(
            "KERNEL", "SEMAPHORE", "DEADLOCK", "THREAD", "PROCESS",
            "VIRTUAL", "MEMORY", "SCHEDULER", "FILESYSTEM", "INTERRUPT",
            "MUTEX", "PAGING", "SEGMENTATION", "CONCURRENCY", "SHELL",
            "DRIVER", "BUFFER", "CACHE", "SOCKET", "FIREWALL"
    );

    public GameModel(int payloads) {
        this.initialPayloads = payloads;
        this.lives = 7; // Default lives
        this.sessionWords = selectSessionWords(payloads);
        this.currentWordIndex = 0;

        // Print the selected words to the console for debugging
        System.out.println("DEBUG: Session words are: " + this.sessionWords);
    }

    private List<String> selectSessionWords(int count) {
        List<String> shuffledBank = new ArrayList<>(WORD_BANK);
        Collections.shuffle(shuffledBank);
        // Ensure we don't request more words than available
        int numWords = Math.min(count, shuffledBank.size());
        return new ArrayList<>(shuffledBank.subList(0, numWords));
    }

    public String getCurrentWord() {
        if (currentWordIndex < sessionWords.size()) {
            return sessionWords.get(currentWordIndex);
        }
        return null; // No more words
    }

    public void proceedToNextWord() {
        currentWordIndex++;
    }

    public boolean hasMoreWords() {
        return currentWordIndex < sessionWords.size() - 1;
    }

    public int getInitialPayloads() {
        return initialPayloads;
    }

    public int getLives() {
        return lives;
    }
}
