package pageobjects.amazon;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import lib.Web;
import util.Log;
import util.Log.Priority;

public class HomePage {

	@FindBy(id="searchDropdownBox")
	private WebElement selSearchBox;
	
	@FindBy(id="twotabsearchtextbox")
	private WebElement amazonGlobalSearch;
	
	@FindBy(xpath=".//input[@value='Go']")
	private WebElement btnSearch;
	
	private WebDriver driver = null;
	private Log log = null;
	
	public HomePage(WebDriver driver){
		this.driver = driver;
		log = new Log(HomePage.class);
		PageFactory.initElements(driver, this);
		if(!Web.isWebElementDisplayed(amazonGlobalSearch,true)) {
			throw new IllegalStateException("This is not Amazon Home page");
		}
	}
	
	public HomePage addProduct(String category, String product){
		Web.selectItem(selSearchBox, category);
		Web.setTextToTextBox(amazonGlobalSearch, product);
		log.Report(Priority.INFO, "searched product "+product+" for category "+category);
		return this;
	}
	
	public SearchResultPage performSearch() {
		log.Report(Priority.INFO, "navigating to  search result page by performing search");
		Web.clickOnElement(btnSearch);
		return new SearchResultPage(driver);
	}

	
	
	
	
	
}
