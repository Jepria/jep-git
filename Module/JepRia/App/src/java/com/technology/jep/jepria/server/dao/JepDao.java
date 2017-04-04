package com.technology.jep.jepria.server.dao;

import static com.technology.jep.jepria.server.JepRiaServerConstant.JEP_RIA_RESOURCE_BUNDLE_NAME;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.option.JepOption;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Общий предок DAO, требующих расширенную работу с данными.
 */
public class JepDao extends JepDaoStandard {

  /**
   * {@inheritDoc}
   */
  public List<JepRecord> find(
      String sqlQuery,
      ResultSetMapper<JepRecord> resultSetMapper,
      Object... params) throws ApplicationException {
    return DaoSupport.find(
        sqlQuery,
        resultSetMapper,
        JepRecord.class,
        params); 
  }
  
  /**
   * {@inheritDoc}
   */
  public List<JepOption> getOptions(
      String sqlQuery,
      ResultSetMapper<JepOption> resultSetMapper,
      Object... params) throws ApplicationException {
    return DaoSupport.find(
        sqlQuery,
        resultSetMapper,
        JepOption.class,
        params); 
  }

  /**
   * {@inheritDoc}
   */
  public <T> T create(
      String sqlQuery,
      Class<T> resultTypeClass,      
      Object... params) throws ApplicationException {
    return DaoSupport.<T> create(sqlQuery,
        resultTypeClass,
        params);
  }

  /**
   * {@inheritDoc}
   */
  public void update(
      String sqlQuery,
      Object... params) throws ApplicationException {
    DaoSupport.update(
        sqlQuery,
        params);
  }

  /**
   * {@inheritDoc}
   */
  public void delete(
      String sqlQuery,
      Object... params) throws ApplicationException {
    DaoSupport.delete(
        sqlQuery,
        params);
  }

  /**
   * Обеспечивает выполнение update-запроса, не предусматривающего возвращаемого значения.
   * @param sqlQuery шаблон запроса
   * @param params входные параметры
   * @throws ApplicationException в случае возникновения ошибки при взаимодействии с базой данных
   */
  public void execute(
      String sqlQuery,
      Object... params) throws ApplicationException {
    DaoSupport.execute(
        sqlQuery,
        params);
  }

  /**
   * Обеспечивает выполнение запроса с возвращаемым значением.
   * @param sqlQuery шаблон запроса
   * @param resultTypeClass тип возвращаемого значения или массив типов
   *                   (например, <code>new Object[] {Integer.class, String.class, Float.class}</code>)
   * @param params параметры вызова (при наличии нескольких выходных параметров
   *                                 первым в этом списке идёт массив)
   * @return результат вызова
   * @throws ApplicationException в случае возникновения ошибки при взаимодействии с базой данных
   */
  public <T> T executeAndReturn(
      String sqlQuery,
      Class<T> resultTypeClass,
      Object... params) throws ApplicationException {
    return DaoSupport.executeAndReturn(
        sqlQuery,
        resultTypeClass,
        params);
  }
  
  /**
   * Вызов select-запроса (без использования курсора).
   * @param sqlQuery шаблон запроса
   * @param resultSetMapper объект, выполняющий отображение содержимого <code>ResultSet</code> на {@link JepRecord}
   * @param params параметры вызова
   * @return результат выполнения запроса
   * @throws ApplicationException в случае возникновения ошибки при взаимодействии с базой данных
   */
  public List<JepRecord> select(
      String sqlQuery,
      ResultSetMapper<JepRecord> resultSetMapper,
      Object... params) throws ApplicationException {
    return DaoSupport.select(
        sqlQuery,
        resultSetMapper,
        JepRecord.class,
        params); 
  }

  /**
   * Возвращает название месяца по его номеру.
   * @param month номер месяца (от 1 до 12)
   * @return название месяца
   */
  protected String getMonthName(int month) {
    ResourceBundle bundle = ResourceBundle.getBundle(JEP_RIA_RESOURCE_BUNDLE_NAME);
    switch(month) {
      case 1: return bundle.getString("month.january");
      case 2: return bundle.getString("month.february");
      case 3: return bundle.getString("month.march");
      case 4: return bundle.getString("month.april");
      case 5: return bundle.getString("month.may");
      case 6: return bundle.getString("month.june");
      case 7: return bundle.getString("month.july");
      case 8: return bundle.getString("month.august");
      case 9: return bundle.getString("month.september");
      case 10: return bundle.getString("month.october");
      case 11: return bundle.getString("month.november");
      case 12: return bundle.getString("month.december");
      default: throw new IllegalArgumentException(bundle.getString("errors.server.illegalMonthNumber"));
    }
  }
  
  /**
   * Возвращает название дня недели по номеру.
   * @param weekDay номер дня недели (от 1 до 7)
   * @return название дня недели
   */
  protected String getWeekDayName(int weekDay) {
    ResourceBundle bundle = ResourceBundle.getBundle(JEP_RIA_RESOURCE_BUNDLE_NAME);
    switch(weekDay) {
      case 1: return bundle.getString("weekDay.monday");
      case 2: return bundle.getString("weekDay.tuesday");
      case 3: return bundle.getString("weekDay.wednesday");
      case 4: return bundle.getString("weekDay.thursday");
      case 5: return bundle.getString("weekDay.friday");
      case 6: return bundle.getString("weekDay.saturday");
      case 7: return bundle.getString("weekDay.sunday");
      default: throw new IllegalArgumentException(bundle.getString("errors.server.illegalWeekDayNumber"));
    }
  }
  
  /**
   * Возвращает список из двух опций, "Да" и "Нет".
   * @return список опций
   */
  protected List<JepOption> getYesNo() {
    ResourceBundle bundle = ResourceBundle.getBundle(JEP_RIA_RESOURCE_BUNDLE_NAME);
    List<JepOption> result = new ArrayList<JepOption>();
    
    result.add(new JepOption(bundle.getString("yes"), true));
    result.add(new JepOption(bundle.getString("no"), false));
    
    return result;
  }

}
