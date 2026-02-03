package cmsc125.project1.models;

public class UserModel {
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public boolean validateUsername(String input, String placeholder) {
        if (input == null) return false;
        String trimmed = input.trim();
        return !trimmed.isEmpty() && !trimmed.equals(placeholder);
    }
}