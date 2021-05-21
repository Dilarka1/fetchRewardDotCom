package com.fetch.tests;

import com.fetch.utilities.BrowserFactory;
import com.fetch.utilities.ConfigurationReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.List;

public class positiveTest {

    private WebDriver driver;
    Logger logger = LoggerFactory.getLogger(positiveTest.class);

    @BeforeMethod
    public void setup() {
        driver = BrowserFactory.getDriver("chrome");
        driver.get(ConfigurationReader.getProperty("url"));
        driver.manage().window().maximize();
    }

    @Test(description = "Verify page title")
    public void test1() {
        String expected = "React App";
        String actual = driver.getTitle();
        Assert.assertEquals(actual, expected);
        logger.info("actual title is : " + actual);
    }

    @Test(description = "Finding fake gold bar")
    public void test2() throws InterruptedException {
        logger.info("Test started!");
        List<WebElement> coins = driver.findElements(By.xpath("//button[@class='square']"));
        WebElement leftBowl = driver.findElement(By.xpath("//input[@id='left_0']"));
        WebElement rightBowl = driver.findElement(By.xpath("//input[@id='right_0']"));

        for (int i = 0; i < coins.size(); i++) {

            int num1 = i;
            int num2 = i + 1;

            wait2(500L);
            leftBowl.sendKeys((num1 + ""));
            wait2(500L);
            rightBowl.sendKeys((num2 + ""));
            wait2(500L);

            WebElement weigh = driver.findElement(By.id("weigh"));
            weigh.click();
            wait2(2000L);

            WebElement resultButton = driver.findElement(By.xpath("//body/div[@id='root']/div[1]/div[1]/div[2]/button[1]"));
            WebElement left = driver.findElement(By.xpath("//button[@id='coin_" + i + "']"));
            WebElement right = driver.findElement(By.xpath("//button[@id='coin_" + (i + 1) + "']"));

            List<WebElement> resultInfo = driver.findElements(By.tagName("li"));
            if (resultButton.getText().contains(">")) {
                logger.info("FAKE GOLD BAR : {}", num2);

                right.click();
                wait2(1000L);

                //alert message assertion
                String text = driver.switchTo().alert().getText();
                String actual= text;
                String expected="Yay! You find it!";
                Assert.assertEquals(actual,expected);

                logger.info(text);
                driver.switchTo().alert().accept();
                logger.info((i + 1) + ". " + resultInfo.get(i).getText());
                wait2(8000L);
                break;
            } else if (resultButton.getText().contains("<")) {
                logger.info("FAKE GOLD BAR :" + num1);

                left.click();
                wait2(1000L);

                String text = driver.switchTo().alert().getText();
                logger.info(text);
                driver.switchTo().alert().accept();
                logger.info((i + 1) + ". " + resultInfo.get(i).getText());
                wait2(8000L);
                break;
            }
            WebElement resetButton = driver.findElement(By.xpath("//button[contains(text(),'Reset')]"));
            resetButton.click();
            wait2(1000L);

            logger.info((i + 1) + ". " + resultInfo.get(i).getText());
        }
    }

    public void wait2(Long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}