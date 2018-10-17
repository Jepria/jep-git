package org.jepria.server.service.rest;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jepria.server.load.rest.Compat;
import org.jepria.server.load.rest.FieldSortConfigDto;
import org.jepria.server.load.rest.ListSorter;
import org.jepria.server.load.rest.SearchEntity;
import org.jepria.server.load.rest.SearchParamsDto;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.util.Mutable;

public class StandardSearchResourceControllerImpl implements StandardSearchResourceController {

  protected final JepRecordDefinition recordDefinition;
  
  protected final JepDataStandard dao;
  
  public StandardSearchResourceControllerImpl(JepRecordDefinition recordDefinition, 
      JepDataStandard dao) {
    this.recordDefinition = recordDefinition;
    this.dao = dao;
  }
  
  @Override
  public void postSearch(SearchParamsDto searchParamsDto, SearchState searchState, Credential credential) {
    
    Map<String, Object> template = searchParamsDto.getTemplate();
    if (template == null) {
      // search with no params
      template = Collections.emptyMap();
    }
    
    Integer maxRowCount = searchParamsDto.getMaxRowCount();
    if (maxRowCount == null) {     
      maxRowCount = JepRiaConstant.DEFAULT_MAX_ROW_COUNT;
    }    
    
    try {
      
      Map<String, Object> model = createModel(template);
      
      Mutable<Boolean> autoRefreshFlag = new Mutable<Boolean>(false);
      
      List<Map<String, Object>> resultRecords = Compat.recListToMapList(
          dao.find(
              Compat.mapToRecord(model),
              autoRefreshFlag,
              maxRowCount,
              credential.getOperatorId()));
      
      
      // Statefulness:


      // sorting
      List<FieldSortConfigDto> fieldSortConfigs = searchParamsDto.getFieldSearchConfigs();
      
      if (fieldSortConfigs != null && fieldSortConfigs.size() > 0) {
        ListSorter listSorter = new ListSorter(fieldSortConfigs, getFieldComparators());
        
        synchronized (resultRecords) {
          Collections.sort(resultRecords, listSorter);
        }
      }
      
      
      // Сохраним результаты поиска для возможного повторного использования в приложении 
      // (например, для сортировки или выгрузки отчета в Excel).
      searchState.setSearchResultRecords(resultRecords);
      
      // Сформируем поисковую сущность
      SearchEntity searchEntity = new SearchEntity();
      searchEntity.setResultSize(resultRecords.size());
      searchEntity.setTemplate(template);
      searchEntity.setModel(model);
      
      // Сохраним информацию по поисковому запросу
      searchState.setSearchEntity(searchEntity);
      
      // Запишем в сессию флаг автообновления.
      searchState.setRefreshFlag(autoRefreshFlag.get());
      
    } catch (ApplicationException e) {
      
      // TODO Auto-generated catch block
      throw new RuntimeException(e);
    }
  }
  
  protected Map<String, Comparator<Object>> getFieldComparators() {
    // TODO build from recordDefinition
    return null;
  }
  
  /**
   * Create search model from a search template
   * @param template
   * @return {@code null} if the input is {@code null}
   */
  protected Map<String, Object> createModel(Map<String, Object> template) {
    if (template == null) {
      return null;
    }
    
    Map<String, Object> model = new HashMap<>(template);
    
    Map<String, JepLikeEnum> matchMap = recordDefinition.getLikeMap();
    Map<String, JepTypeEnum> typeMap = recordDefinition.getTypeMap();
    
    if (matchMap == null || matchMap.size() == 0 || typeMap == null || typeMap.size() == 0) {
      return model;
    }

    model.entrySet().forEach(entry -> {

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
    
    return model;
  }
  
  @Override
  public SearchEntity getSearchEntity(SearchState searchState, Credential credential) {
    return searchState.getSearchEntity();
  }

  private static final int DEFAULT_PAGE_SIZE = 25;
  

  @Override
  public List<?> fetchData(SearchState searchState,
      Integer pageSize, 
      Integer page, 
      Credential credential) {
    
    List<?> records = (List<?>)searchState.getSearchResultRecords();

    if (records == null) {
      return null;
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
