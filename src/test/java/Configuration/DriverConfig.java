package Configuration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class DriverConfig {

    public WebDriver initFirefox() {
        System.setProperty("webdriver.gecko.driver", getFirefoxPath());
        WebDriver browser = new FirefoxDriver();
        browser.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        return browser;
    }

    public String getEnv() {
        return System.getProperty("os.name").toLowerCase();
    }

    public String getFirefoxPath(){
        String env = getEnv();
        String path = "";
        if (env.contains("mac")){
            path =  "src/bin/mac/geckodriver";
        }
        if (env.contains("win")){
            path =  "src/bin/win/geckodriver.exe";
        }
        if (env.contains("linux") || env.contains("nix") || env.contains("nux")){
            path =  "src/bin/linux/geckodriver";
        }

        return path;
    }
}
