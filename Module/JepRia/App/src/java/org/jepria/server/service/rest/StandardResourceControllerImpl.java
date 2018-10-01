package org.jepria.server.service.rest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class StandardResourceControllerImpl implements StandardResourceController {

  protected final JepRecordDefinition recordDefinition;
  
  protected final JepDataStandard dao;
  
  public StandardResourceControllerImpl(JepRecordDefinition recordDefinition, 
      JepDataStandard dao) {
    this.recordDefinition = recordDefinition;
    this.dao = dao;
  }
  
  /**
   * Converts url-styled resource name to dao-styled resource name:
   * <br/>
   * {@code feature-status} -> {@code FeatureStatus}
   * @param resourceName url-styled resource name
   * @return dao-styled resource name; {@code null} if the input is {@code null} 
   */
  protected String normalizeResourceName(String resourceName) {
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

  @Override
  public Object getResourceById(String recordId, Integer operatorId) {

    String[] primaryKey = recordDefinition.getPrimaryKey();
    
    // check primary key is of length 1
    if (primaryKey == null) {
      throw new NullPointerException("Primary key must not be null");
    } else if (primaryKey.length != 1) {
      // TODO add support
      throw new UnsupportedOperationException("Only primary key of length 1 is supported (actual length: " + primaryKey.length + ")");
    }
    

    String primaryKey0 = primaryKey[0];

    JepTypeEnum primaryKeyType = recordDefinition.getTypeMap().get(primaryKey0);
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
      // TODO add support
      throw new UnsupportedOperationException("Only primary key of type INTEGER or STRING is supported (actual type: " + primaryKeyType + ")");
    }}

    Map<String, Object> primaryKeyMap = new HashMap<>();
    primaryKeyMap.put(primaryKey0, primaryKeyValueTyped);



    try {
      List<JepRecord> result = dao.find(
          Compat.mapToRecord(primaryKeyMap), 
          new Mutable<Boolean>(false), 
          1, 
          operatorId);

      
      // check find result is of size 1
      if (result == null || result.size() == 0) {
        // return 404 (empty result)
        return null;
      } else if (result.size() != 1) {
        // TODO 
        throw new IllegalStateException("Expected find result of size 1 (actual size: " + result.size() + ")");
      }
      
      
      // return a single-record result
      return result.get(0);

    } catch (ApplicationException e) {

      // TODO Auto-generated catch block
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public Object create(Map<String, Object> instance, Integer operatorId) {
    // TODO
    throw new NotImplementedYetException();
  }
  
  @Override
  public void deleteResourceById(String recordId, Integer operatorId) {
    // TODO
    throw new NotImplementedYetException();
  }
  
  @Override
  public void update(String recordId, Map<String, Object> fields, Integer operatorId) {
    // TODO
    throw new NotImplementedYetException();
  }
  
  /////////////////////////// OPTIONS RESOURCE //////////////////////////
  
  @Override
  public List<?> listOptions(String optionResourceName, Integer operatorId) {

    try {
      String optionResourceNameNormalized = normalizeResourceName(optionResourceName);
      
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
  
  @Override
  public void postSearch(SearchParamsDto searchParamsDto, SearchState searchState, Integer operatorId) {
    
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
      
      List<Map<String, Object>> resultRecords = Compat.recListToMapList(
          dao.find(
              Compat.mapToRecord(template),
              autoRefreshFlag,
              maxRowCount,
              operatorId));
      
      
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
  
  /**
   * Create search template from a search model
   * @param model
   * @return {@code null} if the input is {@code null}
   */
  protected Map<String, Object> createTemplate(Map<String, Object> model) {
    if (model == null) {
      return null;
    }
    
    Map<String, Object> template = new HashMap<>(model);
    
    Map<String, JepLikeEnum> matchMap = recordDefinition.getLikeMap();
    Map<String, JepTypeEnum> typeMap = recordDefinition.getTypeMap();
    
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
            // the value is already EXACT
            break;
          }
        }
      }
    });
    
    return template;
  }
  
  @Override
  public SearchEntity getSearchEntity(SearchState searchState, Integer operatorId) {
    return searchState.getSearchEntity();
  }

  private static final int DEFAULT_PAGE_SIZE = 25;
  

  @Override
  public List<?> fetchData(SearchState searchState,
      Integer pageSize, 
      Integer page, 
      String sortField,
      String sortOrder,
      Integer operatorId) {
    
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
