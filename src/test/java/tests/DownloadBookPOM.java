package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;
import pages.ArchiveOrgPage;
import utilities.ConfigReader;
import utilities.Driver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class DownloadBookPOM {

    ArchiveOrgPage archiveOrgPage= new ArchiveOrgPage();

    @Test
    public void downloadBook() throws InterruptedException, AWTException {
        Driver.getDriver().get(ConfigReader.getProperty("archiveOrgUrl"));

        archiveOrgPage.emailField.sendKeys(ConfigReader.getProperty("archiveOrgEmail"));
        archiveOrgPage.passwordField.sendKeys(ConfigReader.getProperty("archiveOrgPassword"), Keys.ENTER);

        Thread.sleep(10000); // implicitlyWait is not sufficient here
        String jSPath= "return document.querySelector(\"body > app-root\").shadowRoot.querySelector(\"#maincontent > div > router-slot > home-page\").shadowRoot.querySelector(\"#page-content-container > home-page-hero-block\").shadowRoot.querySelector(\"#collection-search-container > collection-search-input\").shadowRoot.querySelector(\"#search-input\").shadowRoot.querySelector(\"#text-input\")";


        // We have to get the javascript path here since this WebElement is under #shadow-root(open)
        JavascriptExecutor js =(JavascriptExecutor) Driver.getDriver();
        WebElement searchField= (WebElement) js.executeScript(jSPath);
        searchField.sendKeys(ConfigReader.getProperty("archiveOrgBookName"), Keys.ENTER);

        archiveOrgPage.itemImages.get(0).click();
        // As you can see the code will only work as intended if the first result is the desired one



        int numOfPages= Integer.parseInt(archiveOrgPage.numOfPages.getText().substring(6, 9));
        String initialPage= archiveOrgPage.numOfPages.getText();
        String maxPage="("+numOfPages+" of "+numOfPages+")";
        // We need these values to know when to stop the loop


        Robot robot = new Robot();
        Actions action = new Actions(Driver.getDriver());


        for (int i = 0; i < numOfPages; i++) {

            if (archiveOrgPage.numOfPages.getText().equals(initialPage)){
                Thread.sleep(2000); // without thread.sleep we find the elements before image loads
                action.contextClick(archiveOrgPage.coverImg).build().perform();

                robot.keyPress(KeyEvent.VK_V);
                Thread.sleep(2000);
                robot.keyPress(KeyEvent.VK_ENTER);
                // Action class does not send a value to System but rather to Driver so we must use Robot class instead

                archiveOrgPage.nextPageButton.click();
            }else if(!archiveOrgPage.numOfPages.getText().equals(maxPage)) {

                action.contextClick(archiveOrgPage.img1).build().perform();
                robot.keyPress(KeyEvent.VK_V);
                Thread.sleep(2000);
                robot.keyPress(KeyEvent.VK_ENTER);

                archiveOrgPage.nextPageButton.click();
                Thread.sleep(2000);
                action.contextClick(archiveOrgPage.img2).build().perform();
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
