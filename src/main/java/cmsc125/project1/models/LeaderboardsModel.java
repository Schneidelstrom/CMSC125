package cmsc125.project1.models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardsModel {
    private static final String FILE_PATH = "leaderboards.txt";

    public static void saveScore(String username, String difficulty, int score) {
        try (FileWriter fw = new FileWriter(FILE_PATH, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(username + "," + difficulty + "," + score);
        } catch (IOException e) {
            System.err.println("Could not save score: " + e.getMessage());
        }
    }

    public List<String[]> getTopScores() {
        List<String[]> scores = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return scores;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) scores.add(parts);
            }
        } catch (IOException e) {
            System.err.println("Could not load scores: " + e.getMessage());
        }

        // Sort by score descending
        scores.sort((a, b) -> {
            try { return Integer.compare(Integer.parseInt(b[2]), Integer.parseInt(a[2])); } 
            catch (NumberFormatException e) { return 0; }
        });
        return scores;
    }
}
