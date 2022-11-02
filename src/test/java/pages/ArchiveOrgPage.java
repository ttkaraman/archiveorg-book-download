package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.Driver;

import java.util.List;

public class ArchiveOrgPage {

    public ArchiveOrgPage(){
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(xpath = "//input[@type='email']")
    public WebElement emailField;
    @FindBy(xpath = "//input[@type='password']")
    public WebElement passwordField;
    @FindBy(id = "search-input")
    public WebElement searchField;
    @FindBy(className = "BRpageimage")
    public WebElement coverImg;
    @FindBy(xpath = "(//img[@class='BRpageimage'])[1]")
    public WebElement img2;
    @FindBy(xpath = "(//img[@class='BRpageimage'])[2]")
    public WebElement img1;
    @FindBy(xpath = "//button[@title='Flip right']")
    public WebElement nextPageButton;
    @FindBy(xpath = "//span[@class='BRcurrentpage']")
    public WebElement numOfPages;
    @FindBy(className = "item-img")
    public List<WebElement> itemImages;

}
