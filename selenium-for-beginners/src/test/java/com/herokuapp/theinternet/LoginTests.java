package com.herokuapp.theinternet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class LoginTests {
	WebDriver driver;

	@Parameters({ "browser" })
	@BeforeMethod(alwaysRun = true)
	private void setUp(@Optional("chrome") String browser) {
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

		// Create driver

		// maximise browser window

		driver.manage().window().maximize();
	}

	@Test(priority = 1, groups = { "positiveTests", "smokeTests" })
	public void positiveLoginTest() {

		System.out.println("Starting loginTest");

		// open test page

		String url = "https://the-internet.herokuapp.com/login";
		driver.get(url);

		System.out.println("page is opened.");

		// enter username

		WebElement username = driver.findElement(By.id("username"));
		username.sendKeys("tomsmith");

		// enter password
		WebElement password = driver.findElement(By.id("password"));
		password.sendKeys("SuperSecretPassword!");

		// click login
		WebElement logInButton = driver.findElement(By.tagName("button"));
		logInButton.click();

		// sleep(3000);
		// verificaion;
		// new url

		// check correct url

		String expectedUrl = "https://the-internet.herokuapp.com/secure";
		String actualUrl = driver.getCurrentUrl();
		Assert.assertEquals(actualUrl, expectedUrl, "Actual page url is not the same");

		// logout button is visible
		WebElement logOutButton = driver.findElement(By.xpath("//a[@class='button secondary radius']"));
		Assert.assertTrue(logOutButton.isDisplayed(), "logOut button is not visible");

		// succesful login message
		// WebElement SuccessMessage = driver.findElement(By.cssSelector("#flash"));

		WebElement SuccessMessage = driver.findElement(By.xpath("//div[@id='flash']"));
		String expectedMessage = "You logged into a secure area!";
		String actualMessage = SuccessMessage.getText();
		// Assert.assertEquals(actualMessage, expectedMessage, "Actual message is not
		// the same");
		Assert.assertTrue(actualMessage.contains(expectedMessage),
				"Actual message doest not contain expected message.\n Actual message:" + actualMessage);

	}

	@Parameters({ "username", "password", "expectedMessage" })

	@Test(priority = 2, groups = { "negativeTests", "smokeTests" })
	public void negativeLoginTest(String username, String password, String expectedErrorMessage) {

		System.out.println("Starting negativeLoginTest with " + username + " and " + password);

		// open test page

		String url = "https://the-internet.herokuapp.com/login";
		driver.get(url);

		System.out.println("page is opened.");

		// enter username

		WebElement usernameElement = driver.findElement(By.id("username"));
		usernameElement.sendKeys(username);
		// username.sendKeys("tomsmith");

		// enter password
		WebElement passwordElement = driver.findElement(By.id("password"));
		passwordElement.sendKeys(password);

		// click login
		WebElement logInButton = driver.findElement(By.tagName("button"));
		logInButton.click();

		sleep(3000);

		// Verification
		WebElement errorMessage = driver.findElement(By.id("flash"));
		// String expectedErrorMessage = "Your username is invalid!";
		String actualErrorMessage = errorMessage.getText();

		Assert.assertTrue(actualErrorMessage.contains(expectedErrorMessage),
				"Actual error message does not contain expected. \nActual: " + actualErrorMessage + "\nExpected: "
						+ expectedErrorMessage);
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
