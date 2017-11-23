package pageobjects.amazon;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import lib.Web;
import util.Log;
import util.Log.Priority;

public class ProductDetailPage {

	@FindBy(id = "add-to-cart-button")
	private WebElement btnAddToCart;

	@FindBy(id = "productTitle")
	private WebElement prodTitle;

	@FindBy(xpath = ".//select[@name='quantity']")
	private WebElement selQty;

	@FindBy(id = "siNoCoverage-announce")
	private WebElement addedCoverageNoBtn;

	@FindBy(id = "nav-cart-count")
	private WebElement cartLink;

	private WebDriver driver = null;
	private Log log = null;

	public ProductDetailPage(WebDriver driver) {
		this.driver = driver;
		log = new Log(SearchResultPage.class);
		PageFactory.initElements(driver, this);
		if (!Web.isWebElementDisplayed(btnAddToCart, true)) {
			throw new IllegalStateException("This is not Amazon Product Details page");
		}
	}

	public boolean verifyExpectedProdDisplayed(String prod) {
		log.Report(Priority.INFO, "Verify " + prod + " is displayed in Product Details page");
		if (prodTitle.getText().contains(prod))
			return true;
		return false;
	}

	public ProductDetailPage addToCart(String qty) {
		Web.selectItem(selQty, qty);
		Web.clickOnElement(btnAddToCart);
		Web.waitForElement(addedCoverageNoBtn, 5);
		if (Web.isWebElementDisplayed(addedCoverageNoBtn)) {
			Web.clickOnElement(addedCoverageNoBtn);
		}
		log.Report(Priority.INFO, "Adding cart quantity " + qty + " for the product");
		return this;
	}

	public CartPage navigateToCart() {
		Web.clickOnElement(cartLink);
		log.Report(Priority.INFO, "Navigating to Cart ");
		return new CartPage(driver);
	}

}
