package cmsc125.project1.controllers;

import cmsc125.project1.models.GameModel;
import cmsc125.project1.views.GameView;

import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.util.HashSet;
import java.util.Set;

public class GameController {
    private final GameModel model;
    private final GameView view;
    private final Set<Character> guessedLetters = new HashSet<>();
    private int lives;
    private int payloads;
    private String secretWord;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        initGame();
        addListeners();
    }

    private void initGame() {
        this.lives = model.getLives();
        this.payloads = model.getPayloads();
        this.secretWord = model.getCurrentWord();

        // Initialize the view
        view.updatePayloads(payloads, payloads);
        view.getSecurityRingPanel().updateStatus(lives);
        view.updateWordDisplay(secretWord, "");
        view.resetKeyboard();
    }

    private void addListeners() {
        view.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                dispose();
            }
        });

        // Add action listeners for alphabet buttons
        view.getAlphabetButtons().forEach((character, button) -> {
            button.addActionListener(e -> handleLetterGuess(character));
        });
    }

    private void handleLetterGuess(char letter) {
        if (guessedLetters.contains(letter)) {
            return; // Letter already guessed
        }

        guessedLetters.add(letter);
        boolean isCorrect = secretWord.indexOf(letter) != -1;

        view.markLetterAsUsed(letter, isCorrect);

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
            if (!guessedLetters.contains(c)) {
                allGuessed = false;
                break;
            }
        }
        if (allGuessed) {
            // Handle win logic (e.g., show message, start new round)
            System.out.println("You win!");
            view.resetKeyboard(); // Example: reset for a new word
        }
    }

    private void checkLossCondition() {
        if (lives <= 0) {
            // Handle loss logic (e.g., show message, end game)
            System.out.println("You lose! The word was: " + secretWord);
            view.getAlphabetButtons().values().forEach(b -> b.setEnabled(false)); // Disable all buttons
        }
    }

    public void dispose() {
        // Cleanup logic
        System.out.println("GameController disposed.");
    }
}
