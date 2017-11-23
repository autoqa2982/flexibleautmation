package pageobjects.amazon;

import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import lib.Web;
import util.Log;
import util.Log.Priority;

@SuppressWarnings("unused")
public class CartPage {

	@FindBy(xpath=".//div[@data-name='Active Items']//span[@class='a-list-item']/a")
	private List<WebElement> cartList;
	
	private WebDriver driver = null;
	private Log log = null;
	
	public CartPage(WebDriver driver) {
		this.driver = driver;
		log = new Log(SearchResultPage.class);
		PageFactory.initElements(driver, this);
		if(!Web.isWebElementDisplayed(cartList.get(0),true)) {
			throw new IllegalStateException("This is not Amazon Product Details page");
		}
	}
	
	public boolean verifyAdded(String prod) {
		log.Report(Priority.INFO, "Verifying " + prod + " is added successfully in cart page");
		if(cartList.get(0).getText().contains(prod)) return true;
		return false;
	}

}
