package jp.sharelives.selenium.amazonReceiptCollector;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Amazon {

	public static void  main(String[] args) throws Exception {

		FirefoxDriver driver = new FirefoxDriver();
		driver.manage().window().setSize(new Dimension(1200, 900));

		AmazonUtil amazon = new AmazonUtil(driver);
		amazon.navigate("/gp/css/order-history/ref=oh_aui_menu_open?ie=UTF8&orderFilter=open");

		driver.close();
		driver.quit();
	}

}