package com.technology.jep.jepria.auto.condition;

import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class ExpectedConditions {
  
  /**
   * Проверка отображения всех Web-элементов, представленных списком ID
   * @param args
   * @return первый {@link By}, элемент которого найден отображенным.
   */
  public static ExpectedCondition<Boolean> allElementsLocatedVisible(By... args) {
    final List<By> byes = Arrays.asList(args);
    return new ExpectedCondition<Boolean>() {
      @Override
      public Boolean apply(WebDriver driver) {
        for (By by : byes) {
          WebElement el;
          try {
            el = driver.findElement(by);
          } catch (Exception r) {
            return false;
          }
          if (!el.isDisplayed()) {
            return false;
          }
        }

        return true;
      }
    };
  }
  
  /**
   * Проверка отображения одного из Web-элементов, представленных списком ID
   * @param args
   * @return первый {@link By}, элемент которого найден отображенным.
   */
  public static ExpectedCondition<By> oneOfElementsLocatedVisible(By... args) {
    final List<By> byes = Arrays.asList(args);
    return new ExpectedCondition<By>() {
      @Override
      public By apply(WebDriver driver) {
        for (By by : byes) {
          WebElement el;
          try {
            el = driver.findElement(by);
          } catch (Exception r) {
            continue;
          }
          if (el.isDisplayed()) {
            return by;
          }
        }

        return null;
      }
    };
  }
  
  /**
   * Проверка выполнения одного из условий, представленных списком ConditionChecker-ов 
   * @param args - список ConditionChecker-ов
   * @return первый {@link ConditionChecker}, условие которого выполнилось.
   */
  public static ExpectedCondition<ConditionChecker> atLeastOneOfConditionIsSatisfied(ConditionChecker... args) {
    final List<ConditionChecker> checkers = Arrays.asList(args);
    return new ExpectedCondition<ConditionChecker>() {
      @Override
      public ConditionChecker apply(WebDriver driver) {
        for (ConditionChecker checker : checkers) {
          if(checker.isSatisfied()) {
            return checker;
          }
        }

        return null;
      }
    };
  }
}
