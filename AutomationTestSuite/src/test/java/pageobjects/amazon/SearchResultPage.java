package pageobjects.amazon;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import lib.Web;
import util.Log;
import util.Log.Priority;

public class SearchResultPage {
   
	@FindBy(xpath=".//li[@class='s-result-item celwidget ']//a[@title]")
	private WebElement searchedResult;
	
	private WebDriver driver = null;
	private Log log = null;
	
	public SearchResultPage(WebDriver driver){
		this.driver = driver;
		log = new Log(SearchResultPage.class);
		PageFactory.initElements(driver, this);
		if(!Web.isWebElementDisplayed(searchedResult,true)) {
			throw new IllegalStateException("This is not Amazon Search Result page");
		}
	}
	
	public boolean verifySearchResult(String expectedRes) {
		log.Report(Priority.INFO, "verify searched product "+expectedRes+" is found under search result");
		if(searchedResult.getText().contains(expectedRes)) return true;
		return false;
	}
	
	public ProductDetailPage selectResult() {
		log.Report(Priority.INFO, "selecting searched result ");
		Web.clickOnElement(searchedResult);
		return new ProductDetailPage(driver);
	}
	
}
