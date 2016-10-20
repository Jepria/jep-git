package com.technology.jep.jepria.auto.entrance;

import static com.technology.jep.jepria.auto.util.WebDriverFactory.getWait;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.technology.jep.jepria.auto.exceptions.AutomationException;
import com.technology.jep.jepria.auto.manager.JepRiaAuto;
import com.technology.jep.jepria.auto.pages.PageManagerBase;
import com.technology.jep.jepria.auto.HasText;
import com.technology.jep.jepria.client.JepRiaAutomationConstant;

public abstract class AutoBaseImpl<A extends JepRiaAuto, P extends PageManagerBase> implements AutoBase {
  private static Logger logger = Logger.getLogger(AutoBaseImpl.class.getName());

  protected A applicationManager;
    protected P pages;

    public AutoBaseImpl(A app, P pageManager) {
        this.applicationManager = app;
        pages = pageManager;
    }

  public void openMainPage(String url) {
    logger.info(this.getClass() + ": openMainPage() BEGIN");
    
    applicationManager.getWebDriver().get(url);

    logger.info(this.getClass() + ": openMainPage() END");
  }

  public void waitTextToBeChanged(HasText hasText, String currentWorkstateDisplayText) {
    
    getWait()
        .until(
            textToBeChangedInElementLocated(By.id(JepRiaAutomationConstant.STATUSBAR_PANEL_ID),
                currentWorkstateDisplayText));
  }
  
  public static ExpectedCondition<Boolean> textToBeChangedInElementLocated(final By locator, final String currentText) {

    return new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        try {
          String elementText = findElement(locator, driver).getText();
          if(currentText != null) {
            return !currentText.equals(elementText);
          } else {
            return elementText != null;
          }
        } catch (StaleElementReferenceException e) {
          return null;
        }
      }

      @Override
      public String toString() {
        return String.format("text ('%s') to be present in element found by %s", currentText, locator);
      }
    };
  }

  /**
   * Looks up an element. Logs and re-throws WebDriverException if thrown.
   * <p/>
   * Method exists to gather data for
   * http://code.google.com/p/selenium/issues/detail?id=1800
   */
  private static WebElement findElement(By by, WebDriver driver) {
    try {
      return driver.findElement(by);
    } catch (NoSuchElementException e) {
      throw e;
    } catch (WebDriverException e) {
//      log.log(Level.WARNING, String.format("WebDriverException thrown by findElement(%s)", by), e);
      throw e;
    }
  }
}
