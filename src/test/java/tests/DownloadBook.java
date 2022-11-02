package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utilities.ConfigReader;
import utilities.Driver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;

public class DownloadBook {
    // standalone version not using POM

    WebDriver driver;
    @BeforeClass
    public void setup(){
        WebDriverManager.chromedriver().setup();
        driver= new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().window().maximize();
    }
    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void test() throws InterruptedException, AWTException {
        driver.get("https://www.archive.org/account/login");

        WebElement emailField = driver.findElement(By.xpath("//input[@type='email']"));
        WebElement passwordField = driver.findElement(By.xpath("//input[@type='password']"));
        emailField.sendKeys("email"); // YOU MUST ENTER YOUR USERNAME/EMAIL HERE
        passwordField.sendKeys("password", Keys.ENTER); // YOU MUST REPLACE WITH YOUR PASSWORD

        Thread.sleep(5000); // implicitlyWait is not sufficient here
        String jSPath= "return document.querySelector(\"body > app-root\").shadowRoot.querySelector(\"#maincontent > div > router-slot > home-page\").shadowRoot.querySelector(\"#page-content-container > home-page-hero-block\").shadowRoot.querySelector(\"#collection-search-container > collection-search-input\").shadowRoot.querySelector(\"#search-input\").shadowRoot.querySelector(\"#text-input\")";
        // We have to get the javascript path here since this WebElement is under #shadow-root(open)
        JavascriptExecutor js =(JavascriptExecutor) driver;
        WebElement searchField= (WebElement) js.executeScript(jSPath);
        searchField.sendKeys("the social anthropology of radcliffe brown", Keys.ENTER);

        List<WebElement> itemImages= driver.findElements(By.className("item-img")) ;
        itemImages.get(0).click();
        // As you can see the code will only work as intended if the first result is the desired one


        WebElement numOfPages= driver.findElement(By.xpath("//span[@class='BRcurrentpage']"));
        WebElement coverImg= driver.findElement(By.className("BRpageimage"));
        WebElement nextPageButton=driver.findElement(By.xpath("//button[@title='Flip right']"));


        int numOfPagesInt= Integer.parseInt(numOfPages.getText().substring(6, 9));
        String initialPage= numOfPages.getText();
        String maxPage="("+numOfPagesInt+" of "+numOfPagesInt+")";
        // We need these values to know when to stop the loop


        Robot robot = new Robot();
        Actions action = new Actions(driver);


        for (int i = 0; i < numOfPagesInt; i++) {

            if (numOfPages.getText().equals(initialPage)){
                Thread.sleep(2000); // without thread.sleep we find the elements before image loads
                action.contextClick(coverImg).build().perform();

                robot.keyPress(KeyEvent.VK_V);
                Thread.sleep(2000);
                robot.keyPress(KeyEvent.VK_ENTER);
                // Action class does not send a value to System but rather to Driver so we must use Robot class instead

                nextPageButton.click();
            }else if(!numOfPages.getText().equals(maxPage)) {

                WebElement img1=driver.findElement(By.xpath("(//img[@class='BRpageimage'])[2]"));
                WebElement img2=driver.findElement(By.xpath("(//img[@class='BRpageimage'])[1]"));

                action.contextClick(img1).build().perform();
                robot.keyPress(KeyEvent.VK_V);
                Thread.sleep(2000);
                robot.keyPress(KeyEvent.VK_ENTER);

                nextPageButton.click();
                Thread.sleep(2000);
                action.contextClick(img2).build().perform();
                robot.keyPress(KeyEvent.VK_V);
                Thread.sleep(2000);
                robot.keyPress(KeyEvent.VK_ENTER);
                Thread.sleep(1000);

            } else {
                break;
            }

        }
    }
}
