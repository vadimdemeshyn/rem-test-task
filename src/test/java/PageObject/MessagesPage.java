package PageObject;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.*;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.concurrent.TimeUnit;
import static org.testng.Assert.*;

public class MessagesPage {

    WebDriver browser;
    String URL_MESSAGES_PAGE = "http://staging.remedly.com/patient-messages/inbox";

    public MessagesPage(WebDriver browser){
        this.browser = browser;
        PageFactory.initElements(browser, this);
    }

    //Page Object Elements
    @FindBy(how = How.XPATH, using = "//a[@data-target=\"#js-modal-compose\"]")
    private WebElement composeMessageButton;
    @FindBy(how = How.XPATH, using = "//div[.//span[contains(., \"more information\")] and @class=\"check-list__item compose__list-item\"]//span[@class=\"check__radio\"]")
    private WebElement moreInformationRadioButton;
    @FindBy(how = How.XPATH, using = "//div[.//span[contains(., \"unexpected outcome\")] and @class=\"check-list__item compose__list-item\"]//span[@class=\"check__radio\"]")
    private WebElement unexpectedOutcomeOrConcernRadioButton;
    @FindBy(how = How.XPATH, using = "//button[@title=\"Select appointment\"]")
    private WebElement selectAppointmentButton;
    @FindBy(how = How.XPATH, using = "//button[@title=\"Your Care Team\"]")
    private WebElement sendToButton;
    @FindBy(how = How.XPATH, using = "//input[@placeholder=\"Type subject line\"]")
    private WebElement subjectInput;
    @FindBy(how = How.ID, using = "compose-message")
    private WebElement messageInput;
    @FindBy(how = How.XPATH, using = "//input[@name=\"qqfile\"]")
    private WebElement fileInput;
    @FindBy(how = How.XPATH, using = "//div[@class=\"qq-upload-button-selector qq-upload-button fu-custom__area\"]")
    private WebElement addFileButton;
    @FindBy(how = How.XPATH, using = "//a[@class=\"btn btn-success compose__send\"]")
    private WebElement sendMessageButton;


    //Methods

    public boolean waitUntilElementNotDisplayed(WebDriver driver, final By by) {
        try {
            driver.manage().timeouts()
                    .implicitlyWait(0, TimeUnit.SECONDS);

            WebDriverWait wait = new WebDriverWait(browser,
                    15);

            boolean present = wait
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(NoSuchElementException.class)
                    .until(ExpectedConditions.invisibilityOfElementLocated(by));

            return present;
        } catch (Exception e) {
            return false;
        } finally {
            driver.manage().timeouts()
                    .implicitlyWait(15, TimeUnit.SECONDS);
        }
    }

    public MessagesPage navigateToMessagesPage(){
        browser.get(URL_MESSAGES_PAGE);
        return this;
    }

    public MessagesPage composeMessageWithReason(String reason, String appointment, String sendTo, String subject, String message, String file, boolean dragOrClick) {
        composeMessageButton.click();

        //Choose reason
        if (reason.contains("more") || reason.contains("information")) {
            moreInformationRadioButton.click();
        } else if (reason.contains("unexpected") || reason.contains("outcome")) {
            unexpectedOutcomeOrConcernRadioButton.click();
        }

        // Choose Appointment
        if (appointment != null) {
            selectAppointmentButton.click();
            browser.findElement(By.xpath("//div[@class=\"dropdown-menu open\"]//a[.//span[contains(., \"" + appointment + "\")]]")).click();
        }

        //Choose Send To
        sendToButton.click();
        browser.findElement(By.xpath("//div[@class=\"dropdown-menu open\"]//a[.//span[contains(., \"" + sendTo + "\")]]")).click();

        //Choose Subject
        if (subject != null) {
            subjectInput.sendKeys(subject);
        }

        if (message != null) {
            messageInput.sendKeys(message);
        }

        if (file != null) {
            //drag = true, upload via click = false
            dragOrClickFile(dragOrClick, file);
            sendMessageButton.click();

            //check email is sent - modal disappeared
            waitUntilElementNotDisplayed(browser, By.xpath("//div[@class=\"modal-dialog modal-freeze\" and .//div[contains(., \"What is the reason for your message\")]]"));

        }
        return this;
    }

    public MessagesPage composeMessageWithoutReason( String appointment, String sendTo, String subject, String message, String file, boolean dragOrClick){
        composeMessageButton.click();
        // Choose Appointment
        if (appointment != null) {
            selectAppointmentButton.click();
            browser.findElement(By.xpath("//div[@class=\"dropdown-menu open\"]//a[.//span[contains(., \"" + appointment + "\")]]")).click();
        }

        //Choose Send To
        sendToButton.click();
        browser.findElement(By.xpath("//div[@class=\"dropdown-menu open\"]//a[.//span[contains(., \"" + sendTo + "\")]]")).click();

        //Choose Subject
        if (subject != null) {
            subjectInput.sendKeys(subject);
        }

        if (message != null) {
            messageInput.sendKeys(message);
        }

        if (file != null) {
            //drag = true, upload via click = false
            dragOrClickFile(dragOrClick, file);
            sendMessageButton.click();

            //check email is sent - modal disappeared
            waitUntilElementNotDisplayed(browser, By.xpath("//div[@class=\"modal-dialog modal-freeze\" and .//div[contains(., \"What is the reason for your message\")]]"));

        }
        return this;

    }

    private void dragOrClickFile(boolean dragOrClick, String file){
        if (dragOrClick){
            fileInput.sendKeys(System.getProperty("user.dir") + "/src/test/resources/"+file);
        }
        else if (!dragOrClick){

            addAttachmentViaAddButton(file);
        }
        WebElement attachedFile = new WebDriverWait(browser, 15).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class=\"text-attach__file-name\"]")));
        assertTrue(attachedFile.isDisplayed());
    }

    private void addAttachmentViaAddButton(String filename){

        String os = System.getProperty("os.name").toLowerCase();
        addFileButton.click();

        if(os.contains("mac")){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//File Need to be imported

            File file = new File(System.getProperty("user.dir") + "/src/test/resources/"+filename);

            StringSelection stringSelection= new StringSelection(file.getAbsolutePath());

//Copy to clipboard
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

            Robot robot = null; try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }


// Cmd + Tab is needed since it launches a Java app and the browser looses focus

            robot.keyPress(KeyEvent.VK_META);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_META);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.delay(500);

//Open Goto window

            robot.keyPress(KeyEvent.VK_META);
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_G);
            robot.keyRelease(KeyEvent.VK_META);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.keyRelease(KeyEvent.VK_G);

//Paste the clipboard value

            robot.keyPress(KeyEvent.VK_META);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_META);
            robot.keyRelease(KeyEvent.VK_V);

//Press Enter key to close the Goto window and Upload window

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(5000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

        }


        else if(os.contains("win")){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//File Need to be imported

            File file = new File(System.getProperty("user.dir") + "/src/test/resources/"+filename);

            StringSelection stringSelection= new StringSelection(file.getAbsolutePath());
            System.out.println(file.getAbsolutePath());

//Copy to clipboard
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

            Robot robot = null; try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }

//Paste the clipboard value

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

//Press Enter key to close the Goto window and Upload window

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(5000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

        }

        else if(os.contains("nix") || os.contains("nux") || os.contains("aix") || os.contains("linux")){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//File Need to be imported

            File file = new File(System.getProperty("user.dir") + "/src/test/resources/"+filename);

            StringSelection stringSelection= new StringSelection(file.getAbsolutePath());
            System.out.println(file.getAbsolutePath());

//Copy to clipboard
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

            Robot robot = null; try {
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }

//Open Goto window
            robot.keyPress(KeyEvent.VK_SLASH);
            robot.keyRelease(KeyEvent.VK_SLASH);
            robot.keyPress(KeyEvent.VK_BACK_SPACE);
            robot.keyRelease(KeyEvent.VK_BACK_SPACE);

//Paste the clipboard value

            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_V);

//Press Enter key to close the Goto window and Upload window

            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(5000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);

        }

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
