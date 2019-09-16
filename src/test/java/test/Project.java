package test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import junit.framework.Assert;

public class Project {
	public static WebDriver driver;
	public static String userdir = System.getProperty("user.dir");
	public String url = "https://en.wikipedia.org/wiki/Selenium";

	@BeforeClass
	public void init() {
		System.setProperty("webdriver.chrome.driver", userdir + "\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			//driver.close();
		}
	}

	@FindBy(xpath = "//a[contains(text(),'National Institutes of Health page on Selenium')]")
	public WebElement externalLink1;

	@FindBy(xpath = "//a[@title='Oxygen']")
	public WebElement oxygen;

	@FindBy(xpath = "//div[@id='mw-indicator-featured-star']")
	public List<WebElement> feature;

	@FindBy(xpath = "//table[@class='infobox']/tbody")
	public WebElement properties;

	@FindBy(xpath = "//div[@class='reflist columns references-column-width']//a[contains(@href,'pdf')]")
	public List<WebElement> Refpdfs;

	@FindBy(xpath = "//input[@type='search']")
	public WebElement searchBox;

	@Test
	public void validation() throws IOException, InterruptedException {
		// Open the page
		driver.get(url);
		PageFactory.initElements(driver, this);

		// Verify that the external links in “External links“ section work
		externalLink1.click();
		Assert.assertNotSame("External Link Section Doesn't work", url, driver.getCurrentUrl());

		// Click on the “Oxygen” link on the Periodic table at the bottom of the page
		driver.get(url);
		oxygen.click();

		// Verify that it is a “featured article”
		if (feature.size() == 1) {
			System.out.println("This is feature article");
		}

		// Take a screenshot of the right hand box that contains element properties
		FileUtils.copyFile(properties.getScreenshotAs(OutputType.FILE), new File(userdir + "\\ScreenShot\\shot.jpg"));

		// Count the number of pdf links in “References“
		System.out.println("Total Number of PDF in Page==>" + Refpdfs.size());

		// In the search bar on top right enter “pluto” and verify that the 2nd
		// suggestion is “Plutonium”
		searchBox.sendKeys("Pluto");
		Thread.sleep(5000);
		searchBox.sendKeys(Keys.ARROW_DOWN);
		Thread.sleep(4000);
		searchBox.sendKeys(Keys.ARROW_DOWN);
		Thread.sleep(2000);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String text = (String) js.executeScript("return document.getElementById('searchInput').value");
		Assert.assertEquals("Plutonium", text);

	}

}
