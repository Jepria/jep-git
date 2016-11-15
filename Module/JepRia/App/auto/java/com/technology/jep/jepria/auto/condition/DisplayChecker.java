package com.technology.jep.jepria.auto.condition;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Проверка отображения Web-элемента, заданного id
 */
public class DisplayChecker implements ConditionChecker {
  
  private String displayedElementId;
  private WebDriver wd;

  public DisplayChecker(WebDriver wd, String displayedElementId) {
    this.wd = wd;
    this.displayedElementId = displayedElementId;
  }
  
  @Override
  public boolean isSatisfied() {
    WebElement el;
    try {
      el = wd.findElement(By.id(displayedElementId));
    } catch (Exception r) {
      return false;
    }
    
    return el.isDisplayed();
  }
}
