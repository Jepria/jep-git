package org.jepria.server.service.rest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jepria.server.load.rest.Compat;
import org.jepria.server.load.rest.ListSorter;
import org.jepria.server.load.rest.SearchEntity;
import org.jepria.server.load.rest.SearchParamsDto;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.exceptions.NotImplementedYetException;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.util.Mutable;

public class StandardResourceController {

  /**
   * Supplier because it may not have been initialized in constructor 
   */
  private final Supplier<JepRecordDefinition> recordDefinitionSupplier;
  
  /**
   * Supplier because it may not have been initialized in constructor 
   */
  private final Supplier<JepDataStandard> daoSupplier;
  
  public StandardResourceController(Supplier<JepRecordDefinition> recordDefinitionSupplier, 
      Supplier<JepDataStandard> daoSupplier) {
    this.recordDefinitionSupplier = recordDefinitionSupplier;
    this.daoSupplier = daoSupplier;
  }
  
  private static String normalizeResourceName(String resourceName) {
    if (resourceName == null) {
      return null;
    }
 
    String ret = resourceName;
    
    while (true) {
      Matcher m = Pattern.compile("(^|[-_]+)([a-z])").matcher(ret);
      if (!m.find()) {
        break;
      } else {
        ret = ret.substring(0, m.start()) + Character.toUpperCase(m.group(2).charAt(0)) + ret.substring(m.end(), ret.length());
      }
    }
    
    return ret;
  }
  
  //////////// CRUD ///////////////////

  /**
   * @param recordId
   * @return instance or null
   */
  public Object getResourceById(String recordId) {

    String[] primaryKey = recordDefinitionSupplier.get().getPrimaryKey();
    if (primaryKey != null && primaryKey.length == 1) {

      String primaryKey0 = primaryKey[0];

      JepTypeEnum primaryKeyType = recordDefinitionSupplier.get().getTypeMap().get(primaryKey0);
      final Object primaryKeyValueTyped;

      switch (primaryKeyType) {
      case INTEGER: {
        primaryKeyValueTyped = Integer.parseInt(recordId); 
        break;
      }
      case STRING: {
        primaryKeyValueTyped = recordId;
        break;
      }
      default: {
        // TODO
        throw new UnsupportedOperationException();
      }}

      Map<String, Object> primaryKeyMap = new HashMap<>();
      primaryKeyMap.put(primaryKey0, primaryKeyValueTyped);



      final JepDataStandard dao = daoSupplier.get();

      try {
        List<JepRecord> result = dao.find(
            Compat.mapToRecord(primaryKeyMap), 
            new Mutable<Boolean>(false), 
            1, 
            1/*TODO operatorId*/);

        if (result == null || result.size() == 0) {

          // return 404 (empty result)
          return null;

        } else if (result.size() == 1) {

          // return normal result (single-record)
          return result.get(0);

        } else {

          // TODO 
          throw new IllegalStateException("result size > 1");

        }

      } catch (ApplicationException e) {

        // TODO Auto-generated catch block
        throw new RuntimeException(e);
      }

    } else {
      // TODO
      throw new IllegalStateException();
    }
  }
  
  /**
   * @param instance
   * @return created instance ID, not null
   */
  public Object create(Map<String, Object> instance) {
    // TODO
    throw new NotImplementedYetException();
  }
  
  public void deleteResourceById(String recordId) {
    // TODO
    throw new NotImplementedYetException();
  }
  
  public void update(String recordId, Map<String, Object> fields) {
    // TODO
    throw new NotImplementedYetException();
  }
  
  /////////////////////////// OPTIONS RESOURCE //////////////////////////
  
  /**
   * 
   * @param optionResourceName
   * @return non-empty list (null or empty list means 'not found' status)
   */
  public List<?> listOptions(String optionResourceName) {

    try {
      String optionResourceNameNormalized = normalizeResourceName(optionResourceName);
      
      final JepDataStandard dao = daoSupplier.get();
  
      final Method getOptionsMethod;
  
      try {
  
        String methodName = "get" + optionResourceNameNormalized;
        Class<?> daoClass = dao.getClass();
        getOptionsMethod = daoClass.getMethod(methodName);
  
      } catch (NoSuchMethodException e) {
        // Only NoSuchMethodException equals 'not found' status
        e.printStackTrace();
        return null;
      }
  
  
      final Object result = getOptionsMethod.invoke(dao);
  
      return (List<?>)result;
      
    } catch (Throwable e) {
      // TODO
      throw new RuntimeException(e);
    }
  }

  
  
  /////////////////////////// SEARCH RESOURCE ///////////////////////////
  
  /**
   * @param searchParamsDto
   * @param searchState not null
   */
  public void postSearch(SearchParamsDto searchParamsDto, SearchState searchState) {
    
    Map<String, Object> model = searchParamsDto.getModel();
    if (model == null) {
      // search with no params
      model = Collections.emptyMap();
    }
    
    Integer maxRowCount = searchParamsDto.getMaxRowCount();
    if (maxRowCount == null) {     
      maxRowCount = JepRiaConstant.DEFAULT_MAX_ROW_COUNT;
    }    
    
    try {
      
      Map<String, Object> template = createTemplate(model);
      
      Mutable<Boolean> autoRefreshFlag = new Mutable<Boolean>(false);
      
      
      final JepDataStandard dao = daoSupplier.get();
      
      List<Map<String, Object>> resultRecords = Compat.recListToMapList(
          dao.find(
              Compat.mapToRecord(template),
              autoRefreshFlag,
              maxRowCount,
              1/*TODO operatorId*/));
      
      
      // Statefulness:


      // Сохраним результаты поиска для возможного повторного использования в приложении 
      // (например, для сортировки или выгрузки отчета в Excel).
      searchState.setSearchResultRecords(resultRecords);
      
      // Сформируем поисковую сущность
      SearchEntity searchEntity = new SearchEntity();
      searchEntity.setResultSize(resultRecords.size());
      searchEntity.setModel(model);
      searchEntity.setTemplate(template);
      
      // Сохраним информацию по поисковому запросу
      searchState.setSearchEntity(searchEntity);
      
      // Запишем в сессию флаг автообновления.
      searchState.setRefreshFlag(autoRefreshFlag.get());
      
    } catch (ApplicationException e) {
      
      // TODO Auto-generated catch block
      throw new RuntimeException(e);
    }
  }
  
  private Map<String, Object> createTemplate(Map<String, Object> model) {
    if (model == null) {
      return null;
    }
    
    Map<String, Object> template = new HashMap<>(model);
    
    Map<String, JepLikeEnum> matchMap = recordDefinitionSupplier.get().getLikeMap();
    Map<String, JepTypeEnum> typeMap = recordDefinitionSupplier.get().getTypeMap();
    
    if (matchMap == null || matchMap.size() == 0 || typeMap == null || typeMap.size() == 0) {
      return template;
    }

    template.entrySet().forEach(entry -> {

      final String key = entry.getKey();
      final Object value = entry.getValue();

      JepTypeEnum valueType = typeMap.get(key);
      JepLikeEnum matchType = matchMap.get(key);

      if (valueType == JepTypeEnum.STRING && value instanceof String) {
        String valueStr = (String)value;
        if (matchType != null) {
          switch(matchType) {
          case FIRST:
            entry.setValue(valueStr + "%");
            break;
          case CONTAINS:
            entry.setValue("%" + valueStr + "%");
            break;
          case LAST:
            entry.setValue("%" + valueStr);
            break;
          case EXACT:
            break;
          }
        }
      }
    });
    
    return template;
  }
  
  /**
   * 
   * @param searchState not null
   * @return null means 'not found' status
   */
  public SearchEntity getSearchEntity(SearchState searchState) {
    return searchState.getSearchEntity();
  }

  private static final int DEFAULT_PAGE_SIZE = 25;
  
  /**
   * @param searchState not null
   * @param pageSize
   * @param page
   * @param sortField
   * @param sortOrder
   * @return non-empty list (null or empty list means 'not found' status)
   */
  public List<?> fetchData(SearchState searchState,
      Integer pageSize, 
      Integer page, 
      String sortField,
      String sortOrder) {
    
    List<?> records = (List<?>)searchState.getSearchResultRecords();

    if (records == null) {
      return null;
    }
    
    
    
    // sorting
    if (sortField != null) {
      
      @SuppressWarnings("unchecked")
      List<Map<String, Object>> sortRecords = new ArrayList<>((List<Map<String, Object>>)records);// typed copy of the list 
      
      final int sortOrder0 = "desc".equals(sortOrder) ? -1 : 1;
      
      synchronized (sortRecords) {
        Collections.sort(sortRecords, new ListSorter(sortField, sortOrder0));
      }
      
      records = sortRecords;
    }
    
    
    
    // paging
    pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;

    page = page == null ? 1 : page;
    
    final int fromIndex = pageSize * (page - 1);
    
    if (fromIndex >= records.size()) {
      return null;
    }
    
    final int toIndex = Math.min(records.size(), fromIndex + pageSize);
    
    if (fromIndex < toIndex) {
      
      List<?> pageRecords = Collections.unmodifiableList(records.subList(fromIndex, toIndex));
      return pageRecords;
      
    } else {
      return null;
    }
    
  }
}
