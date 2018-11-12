package PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    WebDriver browser;
    String URL_HOME_PAGE = "http://staging.remedly.com/user-login";

    public HomePage(WebDriver browser){
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    //Page Object Elements
    @FindBy(how = How.XPATH, using = "//a[.//span[contains(., \"Messages\")]]")
    private WebElement messagesButton;
    @FindBy(how = How.XPATH, using = "txtLoginPassword")
    private WebElement pwdInput;
    @FindBy(how = How.XPATH, using = "//button[@onclick=\"login()\"]")
    private WebElement loginButton;


    //Methods
    public HomePage navigateToHomePage(){
        browser.get(URL_HOME_PAGE);
        return this;
    }

    public HomePage proceedToMessagesPage(){
        messagesButton.click();
        return this;
    }


}
