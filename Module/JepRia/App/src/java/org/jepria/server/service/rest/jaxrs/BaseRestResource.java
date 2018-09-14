package org.jepria.server.service.rest.jaxrs;

import java.util.function.Supplier;

import com.technology.jep.jepria.shared.record.JepRecordDefinition;

/**
 * Общий предок REST-ресурсов, использующих RecordDefinition и Dao 
 *
 * @param <D> интерфейс Dao
 */
public class BaseRestResource<D> {
  
  /**
   * RecordDefinition
   */
  protected final JepRecordDefinition recordDefinition;
  
  /**
   * Supplier интерфейса Dao
   */
  protected final Supplier<D> daoSupplier;
  
  /**
   * Имя сущности, с которой работает REST-ресурс. Потенциально может использоваться для DI
   */
  protected final String entityName;
  
  protected BaseRestResource(JepRecordDefinition recordDefinition, Supplier<D> daoSupplier, String entityName) {
    this.recordDefinition = recordDefinition;
    this.daoSupplier = daoSupplier;
    this.entityName = entityName;
  }
  
}
