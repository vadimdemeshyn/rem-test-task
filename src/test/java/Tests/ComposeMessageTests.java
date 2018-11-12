package Tests;

import Configuration.DriverConfig;
import PageObject.MessagesPage;
import PageObject.SentMessagesPage;
import PageObject.UserLoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ComposeMessageTests {

    WebDriver browser;
    UserLoginPage userLoginPage;
    MessagesPage messagesPage;
    SentMessagesPage sentMessagesPage;

    @BeforeTest
    public void setUp(){
        browser = new DriverConfig().initFirefox();
        userLoginPage = new UserLoginPage(browser);
        messagesPage = new MessagesPage(browser);
        sentMessagesPage = new SentMessagesPage(browser);

    }

    @Test(description = "Compose Message With Reason and drag-n-drop file", priority = 1)
    public void composeMessageWithReasonAndDragNDropFile(){

        String subject = "test subject "+Math.random();

        //Compose and Send message
        userLoginPage
                .navigateToLoginPage()
                .login("alisad@test.com", "d123456");
        messagesPage
                .navigateToMessagesPage()
                .composeMessageWithReason("more information", "May 31, 2018", "John Smith, MD",
                        subject, "test message", "testPicture.jpg", true);

        //Verify message is sent and contains appropriate info
        sentMessagesPage
                .navigateToMessagesPage()
                .verifyMessageContainsNeededInfo("John Smith", subject, "May 31 2018", "more information");


    }

    @Test(description = "Compose Message With Reason and upload file", priority = 2)
    public void composeMessageWithReasonAndUploadFileViaButton(){

        String subject = "test subject "+Math.random();

        //Compose and Send message
        userLoginPage
                .navigateToLoginPage()
                .login("alisad@test.com", "d123456");
        messagesPage
                .navigateToMessagesPage()
                .composeMessageWithReason("more information", "May 31, 2018", "John Smith, MD",
                        subject, "test message", "testPicture.jpg", false);

        //Verify message is sent and contains appropriate info
        sentMessagesPage
                .navigateToMessagesPage()
                .verifyMessageContainsNeededInfo("John Smith", subject, "May 31 2018", "more information");

    }

    @Test(description = "Compose Message Without Reason and drag-n-drop file", priority = 3)
    public void composeMessageWithoutReasonAndDragNDropFile(){

        String subject = "test subject "+Math.random();

        //Compose and Send message
        userLoginPage
                .navigateToLoginPage()
                .login("alisad@test.com", "d123456");
        messagesPage
                .navigateToMessagesPage()
                .composeMessageWithoutReason( "May 31, 2018", "John Smith, MD",
                        subject, "test message", "testPicture.jpg", true);

        //Verify message is sent and contains appropriate info
        sentMessagesPage
                .navigateToMessagesPage()
                .verifyMessageContainsNeededInfo("John Smith", subject, "May 31 2018", null);

    }

    @Test(description = "Compose Message Without Reason and upload file", priority = 4)
    public void composeMessageWithoutReasonAndUploadFileViaButton(){

        String subject = "test subject "+Math.random();

        //Compose and Send message
        userLoginPage
                .navigateToLoginPage()
                .login("alisad@test.com", "d123456");
        messagesPage
                .navigateToMessagesPage()
                .composeMessageWithoutReason( "May 31, 2018", "John Smith, MD",
                        subject, "test message", "testPicture.jpg", false);

        //Verify message is sent and contains appropriate info
        sentMessagesPage
                .navigateToMessagesPage()
                .verifyMessageContainsNeededInfo("John Smith", subject, "May 31 2018", null);

    }

    @AfterTest
    public void tearDown(){
        browser.close();
    }

}
