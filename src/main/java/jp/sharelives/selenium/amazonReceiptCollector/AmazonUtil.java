package jp.sharelives.selenium.amazonReceiptCollector;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;

public class AmazonUtil {

	private final FirefoxDriver driver;

	private static final String screenshotFolder = "/home/you/Pictures/amazon/";
	private static final String accountID = "@gmail.com";
	private static final String password = "pass";
	private static final int pageStartIndex = 29;
	private static final int pageEndIndex = 35;
	private static final String orderFilter = "year-2015"; // months-6

	public AmazonUtil(FirefoxDriver driver){
		this.driver = driver;
	}


	public String navigate(String path) throws Exception {
		driver.get("https://www.amazon.co.jp"+path);
		String html=driver.getPageSource();
		Thread.sleep(500);

		//we will encounter two type of login form
		if (html.contains("ap_signin_form")) {
			signIn();
		}else{
			signIn2();
		}
		html=driver.getPageSource();
		return html;
	}

	private void signIn() throws Exception {
		WebElement emailField=driver.findElement(By.id("ap_email"));
		emailField.sendKeys(accountID);
		WebElement passwordField=driver.findElement(By.id("ap_password"));
		passwordField.sendKeys(password);

		WebElement signinButton=driver.findElement(By.id("signInSubmit-input"));
		Thread.sleep(500);
		signinButton.click();

		exec();
	}

	private void signIn2() throws InterruptedException, IOException {
		WebElement emailField=driver.findElement(By.id("ap_email"));
		emailField.sendKeys(accountID);
		WebElement passwordField=driver.findElement(By.id("ap_password"));
		passwordField.sendKeys(password);

		WebElement signinButton=driver.findElement(By.id("signInSubmit"));
		Thread.sleep(500);
		signinButton.click();

		exec();
	}

	private void exec() throws InterruptedException, IOException {
		Thread.sleep(500);

		for(int i=pageStartIndex; i < pageEndIndex; i++){
			driver.navigate().to("https://www.amazon.co.jp/gp/your-account/order-history/ref=oh_aui_pagination_1_2?ie=UTF8&orderFilter=" + orderFilter + "&search=&startIndex=" + i * 10);

			for(int j=2; j < 12; j++){
				takeOrderDetail(i, j);
			}
		}
	}

	private void takeOrderDetail(int pageIndex, int index) throws InterruptedException, IOException {

		Thread.sleep(1000);

		try{
			WebElement targetElement = driver.findElement(By.xpath("//*[@id=\"ordersContainer\"]/div[" + index + "]/div[1]/div/div/div/div[2]/div[2]/ul/span[1]/a"));
			targetElement.click();
		}catch (NoSuchElementException e){
			WebElement targetElement = driver.findElement(By.xpath("//*[@id=\"ordersContainer\"]/div[" + index + "]/div[1]/div/div/div/div[2]/div[2]/ul/span[1]/span/a"));
			targetElement.click();

			Thread.sleep(1000);
			WebElement detail;
			try{
				detail = driver.findElement(By.xpath("//*[@id=\"a-popover-content-1\"]/ul/li[2]/span/a"));
			}catch (NoSuchElementException e1){
				detail = driver.findElement(By.xpath("//*[@id=\"a-popover-content-1\"]/ul/li/span/a"));
			}
			detail.click();
		}

		Thread.sleep(2000);
		File scrFile = driver.getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(screenshotFolder+ orderFilter + "/"  + "screenshot" + (pageIndex +1) + "_"+ (index -1) +".png"));
		driver.navigate().back();
	}

}
