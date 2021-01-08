package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ExceptionsTests {
	WebDriver driver;

	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(@Optional("chrome") String browser) {

		// Create driver
		switch (browser) {
		case "chrome":
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			break;

		case "edge":
			System.setProperty("webdriver.edge.driver", "src/main/resources/msedgedriver.exe");
			driver = new EdgeDriver();
			break;

		default:
			System.out.println("Do not know to start the : " + browser + "so instead chrome is started");
			System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
			driver = new ChromeDriver();
			break;
		}

		// maximise browser window

		driver.manage().window().maximize();
	}

	// create a new method called notVisibleTest
	@Test(priority = 1)
	public void notVisibleTest() {
		// open webpage https://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");

		// then find the locator for start button and click on it
		WebElement startButton = driver.findElement(By.xpath("//div[@id = 'start']/button"));
		startButton.click();

		// get finish element text
		WebElement finishElement = driver.findElement(By.id("finish"));
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(finishElement));

		String finishText = finishElement.getText();

		// compare actual finish element text "Hello World!" Using testNg assert class
		Assert.assertTrue(finishText.contains("Hello World!"), "finish Text: " + finishText);
	}

	@Test(priority = 2)
	public void timeOutTest() {
		// open webpage https://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");

		// then find the locator for start button and click on it
		WebElement startButton = driver.findElement(By.xpath("//div[@id = 'start']/button"));
		startButton.click();

		// get finish element text
		WebElement finishElement = driver.findElement(By.id("finish"));
		WebDriverWait wait = new WebDriverWait(driver, 2);
		try {
			wait.until(ExpectedConditions.visibilityOf(finishElement));
		} catch (TimeoutException exception) {
			System.out.println("Exception Catched: " + exception.getMessage());
			sleep(3000);
		}

		String finishText = finishElement.getText();

		// compare actual finish element text "Hello World!" Using testNg assert class
		Assert.assertTrue(finishText.contains("Hello World!"), "finish Text: " + finishText);
	}

	@Test(priority = 3)
	public void noSuchElementTest() {
		// open webpage https://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("https://the-internet.herokuapp.com/dynamic_loading/2");

		// then find the locator for start button and click on it
		WebElement startButton = driver.findElement(By.xpath("//div[@id = 'start']/button"));
		startButton.click();

		// explicite wait untill element appear
		WebDriverWait wait = new WebDriverWait(driver, 10);
		Assert.assertTrue(
				wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("finish"), "Hello World!")),
				"Could't verify Expected text 'Hello world!'");

		// Assign the value when element is available
//		WebElement finishElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("finish")));
//		
//		String finishText = finishElement.getText();
//		
//		//compare actual finish element text "Hello World!" Using testNg assert class
//		Assert.assertTrue(finishText.contains("Hello World!"), "finish Text: " + finishText);
	}

	@Test
	public void staleElementTest() {
		driver.get("http://the-internet.herokuapp.com/dynamic_controls");
		WebElement checkbox = driver.findElement(By.id("checkbox"));
		WebElement removeButton = driver.findElement(By.xpath("//button[contains(text(),'Remove')]"));

		WebDriverWait wait = new WebDriverWait(driver, 10);
		removeButton.click();
		// wait.until(ExpectedConditions.invisibilityOf(checkbox));

//		Assert.assertTrue(wait.until(ExpectedConditions.invisibilityOf(checkbox)),
//				"Checkbox is still visible, but shouldn't be");

		Assert.assertTrue(wait.until(ExpectedConditions.stalenessOf(checkbox)),
				"Checkbox is still visible, but shouldn't be");
		
		WebElement addButton = driver.findElement(By.xpath("//button[contains(text(),'Add')]"));
		addButton.click();
		
		checkbox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkbox")));
		
		Assert.assertTrue(checkbox.isDisplayed(), "Checkbox is still visible, but shouldn't be");
		
	}
	
	@Test
	public void disabledElementTest() {
		driver.get("http://the-internet.herokuapp.com/dynamic_controls");
		
		sleep(5000);
		
		WebElement enableButton = driver.findElement(By.xpath("//button[contains(text(), 'Enable')]"));
		WebElement textBox = driver.findElement(By.xpath("//form[@id='input-example']/input"));
		
		enableButton.click();
		
		WebDriverWait wait = new WebDriverWait(driver, 10);
		
		wait.until(ExpectedConditions.elementToBeClickable(textBox));
		
		textBox.click();
		
		sleep(1000);
		textBox.sendKeys("text is visible");
		
		sleep(1000);
		
		Assert.assertEquals(textBox.getAttribute("value"), "text is visible");
		sleep(1000);
		
	}

	private void sleep(long m) {
		try {
			Thread.sleep(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterMethod(alwaysRun = true)
	private void tearDown() {
		// close browser
		driver.quit();
	}

}
