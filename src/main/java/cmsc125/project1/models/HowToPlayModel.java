package cmsc125.project1.models;

public class HowToPlayModel {
    private final String[] instructions;
    private final String[] imagePaths;

    private int currentStepIndex;

    public HowToPlayModel() {
        this.currentStepIndex = 0;

        this.instructions = new String[]{
                "MISSION BRIEFING:\nWelcome to DE_CRYPT OS. Your objective is to decrypt corrupted system payloads (words) to secure the kernel.",
                "THE INTERFACE:\nThe top left shows the encrypted word. The bottom left is your virtual keyboard. The right side shows the System Protection Rings.",
                "DECRYPTION:\nClick letters on the keyboard to guess the password. Correct guesses fill in the blanks.",
                "SYSTEM INTEGRITY:\nEvery incorrect guess damages the Protection Rings (Applications -> Libraries -> Drivers -> Kernel). You have 7 lives.",
                "VICTORY CONDITION:\nDecrypt all assigned payloads (3, 5, or 7 depending on difficulty) to win. If the Kernel breaks, you lose access."
        };

        this.imagePaths = new String[]{
                "/assets/tutorials/front.png",
                "/assets/tutorials/general.png",
                "/assets/tutorials/right.png",
                "/assets/tutorials/wrong.png",
                "/assets/tutorials/win.png"
        };
    }

    public void nextStep() {
        if (currentStepIndex < instructions.length - 1) {
            currentStepIndex++;
        }
    }

    public void previousStep() {
        if (currentStepIndex > 0) {
            currentStepIndex--;
        }
    }

    public String getCurrentInstruction() {
        return instructions[currentStepIndex];
    }

    public String getCurrentImagePath() {
        return imagePaths[currentStepIndex];
    }

    public boolean isFirstStep() {
        return currentStepIndex == 0;
    }

    public boolean isLastStep() {
        return currentStepIndex == instructions.length - 1;
    }
}