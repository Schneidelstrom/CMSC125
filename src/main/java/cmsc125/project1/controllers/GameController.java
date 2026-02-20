package cmsc125.project1.controllers;

import cmsc125.project1.models.GameModel;
import cmsc125.project1.views.GameView;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

public class GameController {
    private final GameModel model;
    private GameView view; // View can be null initially
    private final Set<Character> guessedLetters = new HashSet<>();
    private int lives;
    private int payloads;
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
        this.payloads = model.getPayloads();
        this.secretWord = model.getCurrentWord().toUpperCase();

        view.updatePayloads(payloads, payloads);
        view.getSecurityRingPanel().updateStatus(lives);
        view.updateWordDisplay(secretWord, "");
        view.resetKeyboard();
    }

    private void addListeners() {
        // Clicks on the on-screen buttons will trigger the game logic
        view.getAlphabetButtons().forEach((character, button) -> {
            button.addActionListener(e -> processGuess(character));
        });
    }

    /**
     * Handles a key press from the physical keyboard.
     * Finds the corresponding on-screen button and simulates a click.
     */
    public void handleKeypress(char letter) {
        JButton button = view.getAlphabetButtons().get(Character.toUpperCase(letter));
        if (button != null && button.isEnabled()) {
            button.doClick();
        }
    }

    /**
     * Processes the game logic for a given letter guess.
     */
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
        boolean allGuessed = true;
        for (char c : secretWord.toCharArray()) {
            if (c != ' ' && !guessedLetters.contains(c)) {
                allGuessed = false;
                break;
            }
        }
        if (allGuessed) {
            gameOver = true;
            JOptionPane.showMessageDialog(view, "System Secured! You win!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
