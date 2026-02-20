package cmsc125.project1.controllers;

import cmsc125.project1.models.GameModel;
import cmsc125.project1.views.GameView;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class GameController {
    private final GameModel model;
    private GameView view;
    private Set<Character> guessedLetters = new HashSet<>();
    private int lives;
    private int currentPayloads;
    private String secretWord;
    private boolean gameOver = false;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        if (view != null) {
            setView(view);
        }
    }

    public void setView(GameView view) {
        this.view = view;
        initGame();
        addListeners();
    }

    private void initGame() {
        this.lives = model.getLives();
        this.currentPayloads = model.getInitialPayloads();
        startNextRound();
    }

    private void startNextRound() {
        secretWord = model.getCurrentWord();
        if (secretWord == null) {
            return;
        }
        guessedLetters.clear();
        gameOver = false;

        view.updatePayloads(currentPayloads, model.getInitialPayloads());
        view.getSecurityRingPanel().updateStatus(lives);
        view.updateWordDisplay(secretWord, "");
        view.resetKeyboard();
    }

    private void addListeners() {
        view.getAlphabetButtons().forEach((character, button) -> {
            button.addActionListener(e -> processGuess(character));
        });
    }

    public void handleKeypress(char letter) {
        JButton button = view.getAlphabetButtons().get(Character.toUpperCase(letter));
        if (button != null && button.isEnabled()) {
            button.doClick();
        }
    }

    private void processGuess(char letter) {
        if (gameOver || guessedLetters.contains(Character.toUpperCase(letter))) {
            return;
        }

        char upperCaseLetter = Character.toUpperCase(letter);
        guessedLetters.add(upperCaseLetter);
        boolean isCorrect = secretWord.indexOf(upperCaseLetter) != -1;

        view.markLetterAsUsed(upperCaseLetter, isCorrect);

        if (isCorrect) {
            view.updateWordDisplay(secretWord, guessedLetters.toString());
            checkWinCondition();
        } else {
            lives--;
            view.getSecurityRingPanel().updateStatus(lives);
            checkLossCondition();
        }
    }

    private void checkWinCondition() {
        for (char c : secretWord.toCharArray()) {
            if (c != ' ' && !guessedLetters.contains(c)) {
                return; // Word not fully guessed yet
            }
        }

        // Word is complete
        currentPayloads--;
        view.updatePayloads(currentPayloads, model.getInitialPayloads());

        if (model.hasMoreWords()) {
            JOptionPane.showMessageDialog(view, "Payload decrypted! Moving to the next...", "Success", JOptionPane.INFORMATION_MESSAGE);
            model.proceedToNextWord();
            SwingUtilities.invokeLater(this::startNextRound);
        } else {
            // Last word guessed, player wins the game
            gameOver = true;
            view.getAlphabetButtons().values().forEach(b -> b.setEnabled(false));

            // Score Calculation: (lives * 150) + (initial_payloads * 50)
            int score = (lives * 150) + (model.getInitialPayloads() * 50);
            String message = String.format("All payloads decrypted. System Secured!\n\nFinal Score: %d", score);
            JOptionPane.showMessageDialog(view, message, "Victory", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void checkLossCondition() {
        if (lives <= 0) {
            gameOver = true;
            view.getSecurityRingPanel().triggerSystemFailure();
            view.getAlphabetButtons().values().forEach(b -> b.setEnabled(false));
            JOptionPane.showMessageDialog(view, "Root access compromised! The word was: " + secretWord, "System Failure", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dispose() {
        System.out.println("GameController disposed.");
    }
}
