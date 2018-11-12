package PageObject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeTest;

public class UserLoginPage {

    WebDriver browser;
    String URL_LOGIN_PAGE = "http://staging.remedly.com/user-login";

    public UserLoginPage(WebDriver browser){
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    //Page Object Elements
    @FindBy(how = How.ID, using = "txtLoginEmail")
    private WebElement emailInput;
    @FindBy(how = How.ID, using = "txtLoginPassword")
    private WebElement pwdInput;
    @FindBy(how = How.XPATH, using = "//button[@onclick=\"login()\"]")
    private WebElement loginButton;


    //Methods
    public UserLoginPage navigateToLoginPage(){
        browser.get(URL_LOGIN_PAGE);
        return this;
    }

    public UserLoginPage login(String email, String pwd){
        emailInput.sendKeys(email);
        pwdInput.sendKeys(pwd);
        loginButton.click();
        return this;
    }

}
