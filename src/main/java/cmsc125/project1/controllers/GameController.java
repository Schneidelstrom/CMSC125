package cmsc125.project1.controllers;

import cmsc125.project1.models.GameModel;
import cmsc125.project1.views.GameView;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;

public class GameController {
    private final GameModel model;
    private final GameView view;
    private final Set<Character> guessedLetters = new HashSet<>();
    private int lives;
    private int payloads;
    private String secretWord;
    private boolean gameOver = false;

    public GameController(GameModel model, GameView view) {
        this.model = model;
        this.view = view;

        initGame();
        addListeners();
        setupKeyBindings();
    }

    private void initGame() {
        this.lives = model.getLives();
        this.payloads = model.getPayloads();
        this.secretWord = model.getCurrentWord().toUpperCase();

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

        view.getAlphabetButtons().forEach((character, button) -> {
            button.addActionListener(e -> handleLetterGuess(character));
        });
    }

    private void setupKeyBindings() {
        InputMap inputMap = view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = view.getRootPane().getActionMap();

        for (char c = 'A'; c <= 'Z'; c++) {
            String actionName = "guess" + c;
            inputMap.put(KeyStroke.getKeyStroke(c), actionName);
            char finalC = c;
            actionMap.put(actionName, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleLetterGuess(finalC);
                }
            });
        }
    }

    private void handleLetterGuess(char letter) {
        if (gameOver || guessedLetters.contains(letter)) {
            return; // Don't process input if game is over or letter was already guessed
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
            JOptionPane.showMessageDialog(view, "System has been Compromised! The word was: " + secretWord, "System " +
                    "Compromised",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void dispose() {
        // Cleanup logic
        System.out.println("GameController disposed.");
    }
}
