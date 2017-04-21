package com.technology.jep.jepria.server.test;

import static com.technology.jep.jepria.server.JepRiaServerConstant.DEFAULT_DATA_SOURCE_JNDI_NAME;
import static com.technology.jep.jepria.server.JepRiaServerConstant.LOGIN_SUFFIX_FOR_HASH_AUTHORIZATION;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.technology.jep.jepcommon.security.pkg_Operator;
import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.server.db.Db;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.record.JepRecord;

/**
 * Общая функциональность тестов DAO JepRia
 */
abstract public class DaoTestBase<D extends JepDataStandard> extends Assert {
  private static Logger logger = Logger.getLogger(DaoTestBase.class.getName());

  // TODO при параллельном запуске тестов разных DAO нужно будет избавляться от static
  // TODO поддержать primaryKey общего вида (когда он определяется множеством полей)
  private static String recordIdKey;

  protected D dao;
  protected static Integer operatorId;
  private List<JepRecord> clearAfterTest = new ArrayList<JepRecord>();

  public void after() {
    clearRecords(dao, operatorId);
  }

  protected static void beforeClass(String username, String password, String recordId) {
    recordIdKey = recordId;
    Db db = new Db(DEFAULT_DATA_SOURCE_JNDI_NAME);
    try {
	  boolean withHash = username.endsWith(LOGIN_SUFFIX_FOR_HASH_AUTHORIZATION);
	  if (withHash) {
		username = username.split(LOGIN_SUFFIX_FOR_HASH_AUTHORIZATION)[0];
	  }
      operatorId = pkg_Operator.logon(db, username, withHash ? null : password, withHash ? password : null);
    } catch (SQLException ex) {
      logger.error("login error", ex);
    } finally {
      db.closeAll();
    }
  }
  
  public void clearRecords(JepDataStandard dao, Integer operatorId) {
    for (JepRecord record : clearAfterTest) {
      try {
        dao.delete(record, operatorId);
      } catch (ApplicationException ex) {
        logger.error("Record deletion error", ex);
      }
    }
  }

  protected void clearRecordsAfterTest(JepRecord recordForDelete) {
    clearAfterTest.add(recordForDelete);
  }

  
  /**
   * Создание записи с заданными полями в БД
   * 
   * @param clearAfterTest удалить запись после теста
   * @param fieldMap поля создаваемой записи
   * @return Идентификатор записи - recordId
   */
  protected Object createRecordInDb(boolean clearAfterTest, Map<String, Object> fieldMap) {
    JepRecord record = new JepRecord();
    for(String fieldName: fieldMap.keySet()) {
      record.set(fieldName, fieldMap.get(fieldName));
    }
    
    Object recordId = null;
    try {
      recordId = dao.create(record, operatorId);
      if(clearAfterTest) {
        clearAfterTest(recordId);
      }
    } catch (ApplicationException ex) {
      fail("Create record error:" + ex.getMessage());
    }
    
    return recordId;
  }

  /**
   * Создание записи с заданными полями в БД с указанием последующей очистки базы
   * 
   * @param fieldMap поля создаваемой записи
   * @return Идентификатор записи - recordId
   */
  protected Object createRecordInDb(Map<String, Object> fieldMap) {
    return createRecordInDb(true, fieldMap);
  }
  
  protected void clearAfterTest(Object recordId) {
    JepRecord forDeleteById = new JepRecord();
    forDeleteById.put(recordIdKey, recordId);
    clearRecordsAfterTest(forDeleteById);
  }


  protected List<JepRecord> findById(Object recordId) throws ApplicationException {
    JepRecord templateRecord = new JepRecord();
    templateRecord.set(recordIdKey, recordId);
    return dao.find(templateRecord, null, 100, operatorId);
  }

}