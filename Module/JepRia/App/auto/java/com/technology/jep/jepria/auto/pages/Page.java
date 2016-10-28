package com.technology.jep.jepria.auto.pages;

import com.technology.jep.jepria.auto.util.WebDriverFactory;

public interface Page {
  /**
   * Запрос на ожидание полной загрузки страницы. Для каждой страницы "полная загрузка" определяется
   * своим набором полей, на которых навешивается ожидание загрузки методами {@link WebDriverFactory#getWait()}.
   */
  void ensurePageLoaded();
  
  /**
   * Legacy-метод, ранее находившийся в {@link FramePage} и {@link PlainPage}. TODO Осознать, зачем он нужен?
   */
  void getContent();
}
