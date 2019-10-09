package com.technology.jep.jepria.server.dao;

import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import oracle.jdbc.OracleTypes;

import org.apache.log4j.Logger;

import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.record.lob.JepClob;
import com.technology.jep.jepria.shared.util.JepRiaUtil;

/**
 * Класс предназначен для облегчения работы с jdbc.<br/>
 * 
 * <pre>
 * Примеры использования:
 * 
 * 1. Пример использования метода create.
 * 
 *     Integer recordId = DaoSupport.<Integer>create(sqlQuery,
 *       divisionId, branchId, employeeId, periodTypeCode, periodNumber, periodYear,
 *       operatorId);
 *
 * 2. Пример использования метода execute.
 * 
 *     DaoSupport.execute(sqlQuery,
 *       divisionId, branchId, employeeId, periodTypeCode, periodNumber, periodYear,
 *       operatorId);
 * 
 * 3. Пример использования метода executeAndReturn.
 * 
 *     String result = DaoSupport.executeAndReturn(sqlQuery,
 *       String.class, divisionId, branchId, employeeId, periodTypeCode, periodNumber, periodYear,
 *       operatorId);
 * 
 * 3. Пример использования метода find.
 * 
 *     List<ExportTaskDto> exportTaskList = DaoSupport.find(sqlQuery,
 *        new ResultSetMapper<ExportTaskDto>() {
 *        public void map(ResultSet rs, ExportTaskDto dto) throws SQLException {
 *          dto.setTaskId(getInteger(rs, "task_id")); // Обратите внимание на то, как достается значение типа Integer из ResultSet
 *          dto.setExportTaskTypeId(getInteger(rs, "export_task_type_id"));
 *          dto.setProcessName(rs.getString("process_name"));
 *          dto.setFormNameRus(rs.getString("form_name_rus"));
 *          dto.setTaskStatusCode(rs.getString("task_status_code"));
 *          dto.setTaskStatusName(rs.getString("task_status_name"));
 *          dto.setTaskCreateDate(getDate(rs, "task_create_date")); // Обратите внимание на то, как достается значение типа Timestamp из ResultSet
 *          dto.setTaskOperatorId(getInteger(rs, "task_operator_id"));
 *          dto.setTaskOperatorName(rs.getString("task_operator_name"));
 *          dto.setTaskParamsStr(rs.getString("task_params_str"));
 *          dto.setTaskResultCode(rs.getString("task_result_code"));
 *          dto.setTaskResultName(rs.getString("task_result_name"));
 *        }
 *      }, ExportTaskDto.class, taskId, exportTaskTypeId, processName, beginDate, endDate, operatorId);
 *      
 *    Использование методов getInteger и getDate необходимо для корректного получения данных из ResultSet (см. JavaDoc к данным методам).
 *
 * 4. Пример использования метода select.
 * 
 *     List<PositionTypeDto> positionTypeList = DaoSupport.<PositionTypeDto>select(sqlQuery,
 *      new ResultSetMapper<PositionTypeDto>() {
 *        public void map(ResultSet rs, PositionTypeDto dto)
 *            throws SQLException {
 *          dto.setPositionTypeId(getInteger(rs, "position_type_id"));
 *          dto.setPositionTypeNameRus(rs.getString("position_type_name_rus"));
 *          dto.setPositionTypeNameEng(rs.getString("position_type_name_eng"));
 *        }
 *      }, PositionTypeDto.class);
 * 
 * 5. Пример использования метода update.
 * 
 *     DaoSupport.update(sqlQuery, taskId, operatorId);
 *        
 * 6. Пример использования метода delete.
 * 
 *     DaoSupport.delete(sqlQuery, taskId, operatorId);
 * </pre>
 *     
 * ВАЖНО: Перед использованием методов необходимо предварительно
 * вызвать {@link CallContext#begin(String, String)} для старта транзакции, далее
 * {@link CallContext#commit()} либо {@link CallContext#rollback()}. После завершения
 * необходимо освободить ресурсы с помощью {@link CallContext#end()}.
 * @deprecated for Rest use {@link org.jepria.server.data.DaoSupport} instead
 */
@Deprecated
public class DaoSupport {
  protected static Logger logger = Logger.getLogger(DaoSupport.class.getName());  
  
  /**
   * Возможные типы выполнения запроса в методе {@link DaoSupport#setParamsAndExecute}.
   */
  private enum ExecutionType {
    /**
     * SQL-запрос.
     */
    QUERY, 
    
    /**
     * SQL-выражение (специфичный для Oracle тип, результат исполнения &mdash; курсор).
     */
    CALLABLE_STATEMENT
  }
  
  /**
   * Данный метод выполняет sql-выражение, создающее запись в БД.
   * 
   * @param <T>                 тип возвращаемого значения
   * @param query               текст запроса
   * @param params              параметры sql-выражения
   * @param resultTypeClass     Тип возвращаемого значения
   * @return идентификатор (первичный ключ) созданной записи
   * @throws ApplicationException
   */
  @SuppressWarnings("unchecked")
  public static <T> T create(
      String query
      , Class<? super T> resultTypeClass
      , Object... params)
      throws ApplicationException {
    
    T result = null;

    try {
      Db db = CallContext.getDb();
      
      CallableStatement callableStatement = db.prepare(query);
      
      setInputParamsToStatement(callableStatement, 2, params);

      if (resultTypeClass.equals(Integer.class)) {
        callableStatement.registerOutParameter(1, Types.INTEGER);
      } else if (resultTypeClass.equals(String.class)) {
        callableStatement.registerOutParameter(1, Types.VARCHAR);
      } else if (resultTypeClass.equals(Timestamp.class)) {
        callableStatement.registerOutParameter(1, Types.TIMESTAMP);
      } else if (resultTypeClass.equals(BigDecimal.class)) {
        callableStatement.registerOutParameter(1, Types.NUMERIC);
      } else {
        throw new ApplicationException("Unknown result type", null);
      }
      
      setApplicationInfo(query);
      // Выполнение запроса    
      callableStatement.execute();

      result = (T)callableStatement.getObject(1);
      if (callableStatement.wasNull()) {
        result = null;
      }

    } catch (Throwable th) {
      throw new ApplicationException(th.getMessage(), th);
    }
    
    return result;
  }
  
  /**
   * Данный метод выполняет sql-выражение без возвращаемого значения.
   * 
   * @param query               текст запроса
   * @param params              параметры sql-выражения
   * @throws ApplicationException
   */
  public static void execute(
      String query
      , Object... params)
      throws ApplicationException {

    try {
      Db db = CallContext.getDb();
      
      CallableStatement callableStatement = db.prepare(query);
      
      setInputParamsToStatement(callableStatement, 1, params);

      setApplicationInfo(query);
      callableStatement.execute();

    } catch (Throwable th) {
      throw new ApplicationException(th.getMessage(), th);
    }
  }
  
  /**
   * Данный метод выполняет sql-выражение, возвращающее курсор.
   * 
   * @param <T>          тип возвращаемого значения
   * @param query          текст запроса
   * @param mapper        экземпляр класса, осуществляющего мэппинг полей dto и ResultSet
   * @param recordClass        класс dto
   * @param params        параметры sql-выражения
   * @return            список объектов в виде List&lt;T&gt;
   * @throws ApplicationException
   */
  public static <T> List<T> find(
      String query
      , ResultSetMapper<? super T> mapper
      , Class<? super T> recordClass
      , Object... params) 
      throws ApplicationException {
      
    return findOrSelect(query, mapper,
        recordClass, ExecutionType.CALLABLE_STATEMENT, params);
  }
  
  /**
   * Данный метод выполняет sql-выражение, возвращающее объект типа T.
   * 
   * @param <T>          тип возвращаемого значения
   * @param query          текст запроса
   * @param resultTypeClass    тип возвращаемого значения; для возврата нескольких значений используется массив типов - Object[].
   *                Пример параметра, передаваемого при вызове: <code>new Object[] {Integer.class, String.class, Float.class}</code> 
   * @param params        параметры sql-выражения
   * @return объект типа T
   * @throws ApplicationException
   */
  public static <T> T executeAndReturn(
      String query
      , Class<? super T> resultTypeClass
      , Object... params) 
      throws ApplicationException {

    T result = null;

    try {
      Db db = CallContext.getDb();
      
      CallableStatement callableStatement = db.prepare(query);
      
      setInputParamsToStatement(
          callableStatement,
          resultTypeClass.isArray() ? 1 : 2,
          params);
      
      setOutputParamsToStatement(
          callableStatement,
          resultTypeClass,
          params);
  
      setApplicationInfo(query);
      callableStatement.execute();

      result = getResult(callableStatement, resultTypeClass, params);

    } catch (Throwable th) {
      throw new ApplicationException(th.getMessage(), th);
    }
    
    return result;
  }

  /**
   * Служебный метод, устанавливающий выходные параметры в statement.
   * @param callableStatement шаблон SQL-инструкции
   * @param resultTypeClass тип параметра
   * @param params массив параметров
   * @throws SQLException при возникновении ошибки JDBC
   */
  private static <T> void setOutputParamsToStatement(
      CallableStatement callableStatement,
      Class<? super T> resultTypeClass,
      Object[] params) throws SQLException {
    if(resultTypeClass.isArray()) {
      Object[] outputParamTypes = (Object[]) params[0];
      for(int i = 0; i < outputParamTypes.length; i++) {
        registerParameter(callableStatement, i + params.length, (Class<? super T>) outputParamTypes[i]);
      }
    } else {
      registerParameter(callableStatement, 1, resultTypeClass);
    }
  }

  /**
   * Служебный метод, устанавливающий выходной параметр в statement
   * @param callableStatement шаблон SQL-инструкции
   * @param paramNumber номер параметра
   * @param resultTypeClass тип параметра
   * @throws SQLException при возникновении ошибки JDBC
   */
  private static <T> void registerParameter(
      CallableStatement callableStatement,
      int paramNumber,
      Class<? super T> resultTypeClass) throws SQLException {
    if (resultTypeClass.equals(Integer.class)) {
      callableStatement.registerOutParameter(paramNumber, Types.INTEGER);
    } else if (resultTypeClass.equals(String.class)) {
      callableStatement.registerOutParameter(paramNumber, Types.VARCHAR);
    } else if (resultTypeClass.equals(Timestamp.class)) {
      callableStatement.registerOutParameter(paramNumber, Types.TIMESTAMP);
    } else if (resultTypeClass.equals(BigDecimal.class)) {
      callableStatement.registerOutParameter(paramNumber, Types.NUMERIC);
    } else if (resultTypeClass.equals(Clob.class)) {
      callableStatement.registerOutParameter(paramNumber, Types.CLOB);
    } else {
      throw new IllegalArgumentException("Unknown result type");
    }
  }

  /**
   * Служебный метод, осушествляющий извлечение выходных параметров.<br/>
   * Поддерживается как извлечение единственного параметра, так и нескольких параметров.
   * @param callableStatement SQL-выражение
   * @param resultTypeClass тип возвращаемого значения или массив типов
   * @param params параметры вызова
   * @return выходной параметр (или массив выходных параметров)
   * @throws SQLException
   */
  private static <T> T getResult(
      CallableStatement callableStatement,
      Class<? super T> resultTypeClass,
      Object[] params) throws SQLException {
    T result = null;
    
    if(resultTypeClass.isArray()) {
      Object[] outputParamTypes = (Object[]) params[0];
      Object[] results = new Object[outputParamTypes.length];
      for(int i = 0; i < outputParamTypes.length; i++) {
        results[i] = callableStatement.getObject(i + params.length);
        if (callableStatement.wasNull()) results[i] = null;
      }
      result = (T) results;
    } else {
      result = (T)callableStatement.getObject(1);
      if (callableStatement.wasNull())result = null;
    }
    
    return result;
  }
  
  /**
   * Данный метод выполняет sql-запрос.
   * 
   * @param <T>          тип возвращаемого значения
   * @param query          текст запроса
   * @param mapper        экземпляр класса, осуществляющего мэппинг полей dto и ResultSet
   * @param modelClass            класс dto
   * @param params        параметры sql-запрос
   * @return            список объектов в виде List&lt;T&gt;
   * @throws ApplicationException
   */
  public static <T> List<T> select(
      String query
      , ResultSetMapper<? super T> mapper
      , Class<? super T> modelClass
      , Object... params) 
      throws ApplicationException {
      
    return findOrSelect(query, mapper,
        modelClass, ExecutionType.QUERY, params);
  }
  
  /**
   * Данный метод выполняет sql-выражение, изменяющее запись в БД.
   * 
   * @param query               текст запроса
   * @param params              параметры sql-выражения
   * @throws ApplicationException
   */
  public static void update(
      String query
      , Object... params)
      throws ApplicationException {
      
    execute(query, params);
    
  }
  
  /**
   * Данный метод выполняет sql-выражение, удаляющее запись из БД.
   * 
   * @param query               текст запроса
   * @param params              параметры sql-выражения
   * @throws ApplicationException
   */
  public static void delete(
      String query
      , Object... params)
      throws ApplicationException {
    
    execute(query, params);

  }
  
  /**
   * Вспомогательный метод, объединящий в себе логику работы методов find и select.
   * 
   * @param <T>                 тип возвращаемого значения
   * @param query               текст запроса
   * @param mapper              экземпляр класса, осуществляющего мэппинг полей dto и ResultSet
   * @param recordClass класс записи
   * @param params параметры sql-запроса или sql-выражения
   * @return список объектов в виде List&lt;T&gt;
   * @throws ApplicationException
   */
  private static <T> List<T> findOrSelect(
      String query
      , ResultSetMapper<? super T> mapper
      , Class<? super T> recordClass
      , ExecutionType executionType
      , Object... params)
      throws ApplicationException {
      
    List<T> result = new ArrayList<T>();
    
    ResultSet resultSet = null;
    
    try {
      Db db = CallContext.getDb();
      
      CallableStatement callableStatement = db.prepare(query);
    
      setApplicationInfo(query);
      resultSet = setParamsAndExecute(callableStatement, executionType, params);
      
      while (resultSet.next()) {
        T resultModel = (T)recordClass.newInstance();
        
        mapper.map(resultSet, resultModel);
        
        result.add(resultModel);
      }
      
    } catch (Throwable th) {
      throw new ApplicationException(th.getMessage(), th);
    } finally {
      try {
        if (resultSet != null) {
          resultSet.close();
        }
      } catch (SQLException e) {
        throw new ApplicationException(e.getMessage(), e);
      }
    }
    
    return result;
  }
  
  /**
   * Установка информации о вызывающем модуле.
   * Использует встроенный функционал Oracle для установки трёх параметров сессии.
   * Значение module_name (имя модуля) извлекается из {@link CallContext}, action_name
   * (название действия) - из шаблона SQL-выражения. client_info заполняется пустым значением.
   * @param queryToExecute шаблон запроса
   * @throws SQLException при ошибке взаимодействия с базой
   */
  private static void setApplicationInfo(String queryToExecute) throws SQLException {
    setModule(CallContext.getModuleName(), getAction(queryToExecute));
  }
  
  /**
   * Установка имени модуля (module name) и названия действия (action_name).
   * Кроме того, метод сбрасывает установленное значение client_info.
   * @param moduleName имя модуля (Oracle обрезает значение до 48 байт)
   * @param actionName название действия (Oracle обрезает значение до 32 байт)
   * @throws SQLException при ошибке взаимодействия с базой
   */
  public static void setModule(String moduleName, String actionName) throws SQLException {
    /*
     * TODO: Найти способ передать в client_info полезную информацию
     * (например, id или логин вызывающего оператора).
     */
    String query = 
        "begin  " 
          +  "dbms_application_info.set_module(" 
              + "module_name => ? " 
              + ", action_name => ? " 
          + ");" 
          +  "dbms_application_info.set_client_info(" 
              + "client_info => null "
          + ");"
       + " end;";
    Db db = CallContext.getDb();
    CallableStatement callableStatement = db.prepare(query);
    setInputParamsToStatement(callableStatement, 1, moduleName, actionName);
    callableStatement.execute();
  }
  
  /**
   * Получение названия действия (action_name) из шаблона SQL-выражения.
   * Если запрос содержит имя функции или процедуры, то метод возвращает данное имя.
   * В противном случае (например, если это SQL-запрос) в качестве названия действия
   * возвращается сам шаблон запроса.
   * @param query шаблон запроса
   * @return название действия
   */
  private static String getAction(String query) {
    int leftDelimiterIndex = query.indexOf('.');
    int rightDelimiterIndex = query.indexOf('(');
    if (rightDelimiterIndex == -1) {
      rightDelimiterIndex = query.indexOf(';');
    }
    if (leftDelimiterIndex != -1 && rightDelimiterIndex > leftDelimiterIndex) {
      return query.substring(leftDelimiterIndex + 1, rightDelimiterIndex);
    }
    else {
      return query;
    }
  }
  
  /**
   * Вспомогательный метод, выставляющий параметры callableStatement и выполняющий запрос.
   * 
   * @param callableStatement    экземпляр callableStatement
   * @param executionType        данная переменная используется для определения того,
   *                              работаем ли мы с sql-выражением, или sql-запросом
   * @param params               параметры sql-запроса или sql-выражения
   * @return экземпляр ResultSet
   * @throws SQLException
   */
  private static ResultSet setParamsAndExecute(
      CallableStatement callableStatement, ExecutionType executionType, Object... params)
      throws SQLException {
    
    logger.trace("setParamsAndExecute(...)");
    
    ResultSet resultSet = null;
    if (executionType == ExecutionType.CALLABLE_STATEMENT) {
      callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
  
      setInputParamsToStatement(callableStatement, 2, params);
      
      callableStatement.execute();
  
      //Получим набор.
      resultSet = ResultSetWrapper.wrap((ResultSet) callableStatement.getObject(1));
    } else if (executionType == ExecutionType.QUERY) {
      setInputParamsToStatement(callableStatement, 1, params);
      
      //Получим набор.
      resultSet = callableStatement.executeQuery();
    }
    return resultSet;
  }

  /**
   * Данный вспомогательный метод присваивает параметры запроса объекту callableStatement.
   * 
   * @param callableStatement   экзепляр callableStatement
   * @param i                   номер с, с которого начинаем выставлять параметры
   * @param params              параметры запроса
   * @throws SQLException
   */
  public static void setInputParamsToStatement(
      CallableStatement callableStatement,
      int i,
      Object... params) throws SQLException {
    if (params.length > 0) {
      for (int index = ((params[0] != null && params[0].getClass().isArray()) ? 1 : 0); index < params.length; index++) {
        Object param = params[index];
  
        if (param != null) {
          Class<?> clazz = param.getClass();
          if (clazz.equals(Integer.class)) {
            setIntegerParameter(callableStatement, (Integer)param, i);
          } else if (clazz.equals(String.class)) {
            setStringParameter(callableStatement, (String)param, i);
          } else if (clazz.equals(Boolean.class)) {
            setBooleanParameter(callableStatement, (Boolean)param, i);
          } else if (clazz.equals(BigDecimal.class)) {
            setBigDecimalParameter(callableStatement, (BigDecimal)param, i);
          } else if (clazz.equals(java.util.Date.class)) {
            setDateParameter(callableStatement, (java.util.Date)param, i);
          } else if (clazz.equals(JepClob.class)) {
            setClobParameter(callableStatement, (JepClob)param, i);
          } else {
            callableStatement.setObject(i, param);
          }
        } else {
          callableStatement.setNull(i, Types.NULL);
        }
        i++;
      }
    }
  }

  /**
   * Вспомогательный метод. Используется для задания строкового параметра объекту callableStatement.
   * 
   * @param callableStatement    экзепляр callableStatement
   * @param parameter            параметр
   * @param place                место вставки параметра
   * @throws SQLException
   */
  private static void setStringParameter(
    CallableStatement callableStatement
    , String parameter
    , int place) 
    throws SQLException {

    if(JepRiaUtil.isEmpty(parameter)) {
      callableStatement.setNull(place, Types.VARCHAR);
    } else {
      callableStatement.setString(place, parameter);
    }
  }
  
  /**
   * Вспомогательный метод. Используется для задания целочисленного параметра объекту callableStatement.
   * 
   * @param callableStatement    экзепляр callableStatement
   * @param parameter            параметр
   * @param place                место вставки параметра
   * @throws SQLException
   */
  private static void setIntegerParameter(
    CallableStatement callableStatement
    , Integer parameter
    , int place) 
    throws SQLException {

    if(JepRiaUtil.isEmpty(parameter)) {
      callableStatement.setNull(place, Types.INTEGER);
    } else {
      callableStatement.setInt(place, parameter);
    }
  }
  
  /**
   * Вспомогательный метод. Используется для задания булевого параметра объекту callableStatement.
   * 
   * @param callableStatement    экзепляр callableStatement
   * @param parameter            параметр
   * @param place                место вставки параметра
   * @throws SQLException
   */
  private static void setBooleanParameter(
    CallableStatement callableStatement
    , Boolean parameter
    , int place) 
    throws SQLException {

    if(parameter) {
      callableStatement.setInt(place, 1);
    } else {
      callableStatement.setInt(place, 0);
    }
  }
  
  /**
   * Вспомогательный метод. Используется для задания параметра типа BigDecimal объекту callableStatement. 
   * 
   * @param callableStatement    экзепляр callableStatement
   * @param parameter            параметр
   * @param place                место вставки параметра
   * @throws SQLException
   */
  private static void setBigDecimalParameter(
    CallableStatement callableStatement
    , BigDecimal parameter
    , int place) 
    throws SQLException {

    if(parameter == null) {
      callableStatement.setNull(place, Types.NUMERIC);
    } else {
      callableStatement.setBigDecimal(place, parameter);
    }
  }

  /**
   * Вспомогательный метод. Используется для задания параметра типа Date объекту callableStatement. 
   * 
   * @param callableStatement    экземпляр callableStatement
   * @param parameter            параметр
   * @param place                место вставки параметра
   * @throws SQLException
   */
  private static void setDateParameter(CallableStatement callableStatement, java.util.Date parameter, int place)
      throws SQLException {
    if (parameter == null) {
      callableStatement.setNull(place, Types.TIMESTAMP);
    } else {
      callableStatement.setTimestamp(place, new java.sql.Timestamp(parameter.getTime()));
    }
  }
  
  /**
   * Вспомогательный метод. Используется для задания параметра типа Clob объекту callableStatement. 
   * 
   * @param callableStatement    экземпляр callableStatement
   * @param parameter            параметр
   * @param place                место вставки параметра
   * @throws SQLException
   */
  private static void setClobParameter(CallableStatement callableStatement, JepClob parameter, int place) 
      throws SQLException {
    if (JepRiaUtil.isEmpty(parameter)) {
      callableStatement.setNull(place, Types.CLOB);
    } else {
      callableStatement.setClob(place, new StringReader(parameter.getBigText()));
    }
  }
}
