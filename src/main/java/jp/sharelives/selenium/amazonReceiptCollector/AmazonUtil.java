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

	private static final String screenshotFolder = "/your/pucture/folder";
	private static final String accountID = "hoge@gmail.com";
	private static final String password = "hogehoge";
	private static final int pageIndex = 10;

	public AmazonUtil(FirefoxDriver driver){
		this.driver = driver;
	}


	public String navigate(String path) throws Exception {
		driver.get("https://www.amazon.co.jp"+path);
		String html=driver.getPageSource();
		Thread.sleep(500);

		//we will encounter two type of login form
		if (html.contains("ap_signin_form")) {
			signIn(pageIndex);
		}else{
			signIn2(pageIndex);
		}
		html=driver.getPageSource();
		return html;
	}

	private void signIn(int index) throws Exception {
		WebElement emailField=driver.findElement(By.id("ap_email"));
		emailField.sendKeys(accountID);
		WebElement passwordField=driver.findElement(By.id("ap_password"));
		passwordField.sendKeys(password);

		WebElement signinButton=driver.findElement(By.id("signInSubmit-input"));
		Thread.sleep(500);
		signinButton.click();

		exec(index);
	}

	private void signIn2(int pageIndex) throws InterruptedException, IOException {
		WebElement emailField=driver.findElement(By.id("ap_email"));
		emailField.sendKeys(accountID);
		WebElement passwordField=driver.findElement(By.id("ap_password"));
		passwordField.sendKeys(password);

		WebElement signinButton=driver.findElement(By.id("signInSubmit"));
		Thread.sleep(500);
		signinButton.click();

		exec(pageIndex);
	}

	private void exec(int pageIndex) throws InterruptedException, IOException {
		Thread.sleep(500);

		for(int i=0; i < pageIndex; i++){
			driver.navigate().to("https://www.amazon.co.jp/gp/your-account/order-history/ref=oh_aui_pagination_1_2?ie=UTF8&orderFilter=months-6&search=&startIndex=" + i * 10);

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
		FileUtils.copyFile(scrFile, new File(screenshotFolder + "screenshot" + (pageIndex +1) + "_"+ (index -1) +".png"));
		driver.navigate().back();
	}

}