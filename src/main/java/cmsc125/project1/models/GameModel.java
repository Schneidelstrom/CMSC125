package cmsc125.project1.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class GameModel {
    private final int initialPayloads;
    private final int lives;
    private final List<String> sessionWords;
    private int currentWordIndex;

// Initialize the list using the resource path
    private static final List<String> WORD_BANK = loadWords("/assets/word_bank.txt");

    private static List<String> loadWords(String resourcePath) {
        // 1. Get the file from the classpath as an InputStream
        try (InputStream is = GameModel.class.getResourceAsStream(resourcePath)) {
            
            // 2. Check if the file was actually found
            if (is == null) {
                System.err.println("Resource not found: " + resourcePath + ". Loading defaults.");
                return fallbackWords();
            }

            // 3. Read the stream line by line
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.toList());
            }

        } catch (IOException e) {
            System.err.println("Error reading " + resourcePath + ". Loading defaults.");
            return fallbackWords();
        }
    }

    // A safe fallback just in case the file is missing from the built jar
    private static List<String> fallbackWords() {
        return Arrays.asList("KERNEL", "SEMAPHORE", "DEADLOCK", "THREAD", "PROCESS");
    }    public GameModel(int payloads) {
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
