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
	
	//create a new method called notVisibleTest
	@Test(priority = 1)
    public void notVisibleTest() {
		//open webpage https://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");
		
		//then find the locator for start button and click on it
		WebElement startButton = driver.findElement(By.xpath("//div[@id = 'start']/button"));
		startButton.click();
		
		//get finish element text
		WebElement finishElement = driver.findElement(By.id("finish"));
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(finishElement));
		
		String finishText = finishElement.getText();
		
		//compare actual finish element text "Hello World!" Using testNg assert class
		Assert.assertTrue(finishText.contains("Hello World!"), "finish Text: " + finishText);
    }
	
	@Test(priority = 2)
    public void timeOutTest() {
		//open webpage https://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("https://the-internet.herokuapp.com/dynamic_loading/1");
		
		//then find the locator for start button and click on it
		WebElement startButton = driver.findElement(By.xpath("//div[@id = 'start']/button"));
		startButton.click();
		
		//get finish element text
		WebElement finishElement = driver.findElement(By.id("finish"));
		WebDriverWait wait = new WebDriverWait(driver, 2);
		try {
			wait.until(ExpectedConditions.visibilityOf(finishElement));
		} catch (TimeoutException exception) {
			System.out.println("Exception Catched: " + exception.getMessage());
			sleep(3000);
		}
		
		String finishText = finishElement.getText();
		
		//compare actual finish element text "Hello World!" Using testNg assert class
		Assert.assertTrue(finishText.contains("Hello World!"), "finish Text: " + finishText);
    }
	
	@Test(priority = 3)
    public void noSuchElementTest() {
		//open webpage https://the-internet.herokuapp.com/dynamic_loading/1
		driver.get("https://the-internet.herokuapp.com/dynamic_loading/2");
		
		//then find the locator for start button and click on it
		WebElement startButton = driver.findElement(By.xpath("//div[@id = 'start']/button"));
		startButton.click();
		
		//get finish element text
		WebElement finishElement = driver.findElement(By.id("finish"));
		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOf(finishElement));
		
		String finishText = finishElement.getText();
		
		//compare actual finish element text "Hello World!" Using testNg assert class
		Assert.assertTrue(finishText.contains("Hello World!"), "finish Text: " + finishText);
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
