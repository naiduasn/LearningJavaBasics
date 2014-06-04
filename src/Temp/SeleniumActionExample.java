package Temp;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SeleniumActionExample {
  private static WebDriver driver;

  @BeforeClass
  public void setUp() {
    driver = new FirefoxDriver();
    driver.manage().window().maximize();

  }

  @AfterClass
  public void tearDown() {
    driver.close();
    driver.quit();
  }

  @Test
  public void draggable() throws InterruptedException {

    driver.get("http://jqueryui.com/draggable");
    // driver.get("http://jqueryui.com/resources/demos/draggable/default.html");
    WebElement iframe = driver.findElement(By.cssSelector(".demo-frame"));
    // driver.switchTo().frame(1);
    driver.switchTo().frame(iframe);
    // driver.switchTo().defaultContent();
    System.out.println("Going to click on draggable");
    // driver.findElement(By.linkText("Draggable")).click();
    // System.out.println("Clicked on draggable");
    WebDriverWait wait = new WebDriverWait(driver, 20);

    System.out.println("Searching draggable");
    // driver.wait(10000);
    WebElement draggable =
        wait.until(ExpectedConditions.visibilityOfElementLocated((By
            .xpath("//div[contains(@class,'draggable')]"))));
    // wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("draggable"))));
    // driver.wait(10000);
    System.out.println("Found.. GOing to move element");
    new Actions(driver).dragAndDropBy(draggable, 120, 120).build().perform();
    System.out.println("Moved element to 120, 120. Please have a look");
    Thread.sleep(60000);
  }

  @Test
  public void droppable() throws InterruptedException {
    driver.get("http://jqueryui.com/demos/droppable/");
    WebElement iframe = driver.findElement(By.cssSelector(".demo-frame"));
    driver.switchTo().frame(iframe);
    WebElement draggable = driver.findElement(By.id("draggable"));
    WebElement droppable = driver.findElement(By.id("droppable"));

    new Actions(driver).dragAndDrop(draggable, droppable).build().perform();
    Thread.sleep(10000);
  }

  @Test
  public void selectMultiple() throws InterruptedException {
    driver.get("http://jqueryui.com/demos/selectable/");
    WebElement iframe = driver.findElement(By.cssSelector(".demo-frame"));
    driver.switchTo().frame(iframe);
    List<WebElement> listItems = driver.findElements(By.cssSelector("ol#selectable *"));
    for (WebElement webEle : listItems) {
      System.out.println("ITEM::>> " + webEle.getText());
    }
    Actions builder = new Actions(driver);
    System.out.println("Going to perform first");


    builder.clickAndHold(listItems.get(1)).moveToElement((listItems.get(2))).release();


    /* builder.clickAndHold(listItems.get(1)).build().perform();
     System.out.println("Going to perform Second");
     builder.moveToElement(listItems.get(2)).build().perform();
     System.out.println("Going to perform 3");
     builder.release(listItems.get(2)).build().perform();*/
    Action selectMultiple = builder.build();
    selectMultiple.perform();
    System.out.println("Done");

    Thread.sleep(10000);
  }

  @Test
  public void sliding() throws Exception {
    driver.get("http://jqueryui.com/demos/slider/");

    WebElement iframe = driver.findElement(By.cssSelector(".demo-frame"));
    driver.switchTo().frame(iframe);
    WebElement draggable = driver.findElement(By.className("ui-slider-handle"));
    new Actions(driver).dragAndDropBy(draggable, 120, 3).build().perform();
    takeScreenshot(driver);
    Thread.sleep(10000);
  }


  private void takeScreenshot(final WebDriver webDriver) throws Exception {
    System.out.println("inside takeScreenshot");
    File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);

    // ((TakesScreenshot) (((WrapsDriver) (selenium)).getWrappedDriver()))
    // .getScreenshotAs(OutputType.FILE);
    FileUtils.copyFile(screenshot, new File("target" + File.separator + "tmp" + File.separator
        + "image_" + String.valueOf(System.currentTimeMillis()) + ".png"));
  }

}
