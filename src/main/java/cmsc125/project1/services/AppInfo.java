package cmsc125.project1.services;
import java.util.Properties;
import java.io.InputStream;

public class AppInfo {
    private static String appVersion;
    private static String appName;

    public static void getInfo() {
        String fileName = "project.properties";
        // Using the Thread context class loader is often more reliable
        try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + fileName);
                return;
            }
            Properties prop = new Properties();
            prop.load(input);
            appVersion = prop.getProperty("app.version");
            appName = prop.getProperty("app.name");
        } catch (Exception ex) {
            IO.println("Could not get app info!");
            IO.println("Cause: " + ex.getMessage());
        }
    }

    public static String getAppName() {
        if (appName == null) getInfo();
        return appName;
    }

    public static String getAppVersion() {
        if (appVersion == null) getInfo();
        return appVersion;
    }
}