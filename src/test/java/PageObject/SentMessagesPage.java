package PageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.testng.Assert.*;

public class SentMessagesPage {

    WebDriver browser;
    String URL_SENT_MESSAGES_PAGE = "http://staging.remedly.com/patient-messages/sent";

    public SentMessagesPage(WebDriver browser){
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    //Page Object Elements
    @FindBy(how = How.XPATH, using = "//a[@data-target=\"#js-modal-compose\"]")
    private WebElement composeMessageButton;


    //Methods


    private int getTableIndex(String neededItem){
        int item = 0;
        List<WebElement> listOfEelementsInTableHeader = browser.findElements(By.xpath("//table[@id=\"table-inbox\"]//thead//th"));
        for (int i = 0; i <listOfEelementsInTableHeader.size() ; i++) {
            if (listOfEelementsInTableHeader.get(i).getText().equals(neededItem)){
                item = i;
            }
        }
        return item+1;
    }

    public SentMessagesPage navigateToMessagesPage(){
        browser.get(URL_SENT_MESSAGES_PAGE);
        return this;
    }

    public SentMessagesPage verifyMessageContainsNeededInfo(String to, String subject, String relatedTo, String reason){

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Calendar cal = Calendar.getInstance();
        String neededRow = "//table[@id=\"table-inbox\"]//tr[.//a[text()=\""+to+"\"] and .//a[text()=\""+subject+"\"] and .//td[contains(., \""+format.format(new Date())+"\")]]";

        //Assert To is right
        assertEquals(browser.findElement(By.xpath(neededRow.toString()+"//td["+getTableIndex("To")+"]"+"//a")).getText(), to);

        //Assert Subject is right
        assertEquals(browser.findElement(By.xpath(neededRow.toString()+"//td["+getTableIndex("Subject")+"]"+"//a")).getText(), subject);

        //Assert Date is right
        assertTrue(browser.findElement(By.xpath(neededRow.toString()+"//td["+getTableIndex("Date")+"]"+"")).getText().contains(format.format(new Date())));

        //proceed into message
        browser.findElement(By.xpath(neededRow.toString()+"//td["+getTableIndex("Subject")+"]"+"//a")).click();

        //Assert from is correct
        WebElement messageText = browser.findElement(By.xpath("//div[@id=\"original-message\"]//strong"));
        assertTrue(messageText.getText().toLowerCase().contains("alisa dorochov"));

        //Assert Subject is correct
        assertTrue(messageText.getText().toLowerCase().contains(subject.toLowerCase()));

        //Assert reason is correct
        if (reason != null) assertTrue(messageText.getText().toLowerCase().contains(reason.toLowerCase()));

        //Assert Related to Appointment is correct
        //assertTrue(messageText.getText().toLowerCase().contains(relatedTo.toLowerCase()));


        //Assert Date is correct
        assertTrue(messageText.getText().toLowerCase().contains(Integer.toString(new Date().getDay()).toLowerCase()));

        return this;
    }

}
