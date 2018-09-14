package com.technology.jep.jepria.server.service.rest;

import static com.technology.jep.jepria.server.JepRiaServerConstant.IS_REFRESH_NEEDED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jepria.server.service.rest.jaxrs.BaseRestResource;

import com.technology.jep.jepria.server.dao.JepDataStandard;
import com.technology.jep.jepria.shared.JepRiaConstant;
import com.technology.jep.jepria.shared.exceptions.ApplicationException;
import com.technology.jep.jepria.shared.field.JepLikeEnum;
import com.technology.jep.jepria.shared.field.JepTypeEnum;
import com.technology.jep.jepria.shared.load.rest.SearchEntity;
import com.technology.jep.jepria.shared.load.rest.SearchParamsDto;
import com.technology.jep.jepria.shared.record.JepRecord;
import com.technology.jep.jepria.shared.record.JepRecordDefinition;
import com.technology.jep.jepria.shared.util.Mutable;


public class StandardRestResourceSearch<D extends JepDataStandard> extends BaseRestResource<D> {
  
  protected StandardRestResourceSearch(JepRecordDefinition recordDefinition, Supplier<D> daoSupplier, String entityName) {
    super(recordDefinition, daoSupplier, entityName);
  }

  protected String generateSearchID(SearchParamsDto searchParamsDto) {
    return UUID.randomUUID().toString();
  }
  
  /**
   * 
   * @param searchParamsDto
   * @param request
   */
  // TODO param 'request' needed only for session-stateful search, remove it when search becomes stateless or non-session-stateful
  public Response postSearch(SearchParamsDto searchParamsDto, HttpServletRequest request) {
    
    final String searchId = generateSearchID(searchParamsDto);
    
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
      
      
      final D dao = daoSupplier.get();
      
      List<JepRecord> resultRecords = dao.find(
          Compat.mapToRecord(template),
          autoRefreshFlag,
          maxRowCount,
          1/*TODO operatorId*/);
      
      
      // Statefulness:

      HttpSession session = request.getSession();
      // Сохраним результаты поиска для возможного повторного использования в приложении 
      // (например, для сортировки или выгрузки отчета в Excel).
      session.setAttribute(generateSearchResultRecordsSessionKey(searchId), resultRecords);
      
      // Сформируем поисковую сущность
      SearchEntity searchEntity = new SearchEntity();
      searchEntity.setResultSize(resultRecords.size());
      searchEntity.setModel(model);
      searchEntity.setTemplate(template);
      
      // Сохраним информацию по поисковому запросу
      session.setAttribute(generateSearchEntitySessionKey(searchId), searchEntity);
      
      // Запишем в сессию флаг автообновления.
      session.setAttribute(IS_REFRESH_NEEDED + searchId, autoRefreshFlag.get());
      
      return Response.status(Status.CREATED).header("Location", searchId).build();
      
    } catch (ApplicationException e) {
      
      // TODO Auto-generated catch block
      throw new RuntimeException(e);
    }
  }
  
  protected String generateSearchResultRecordsSessionKey(String searchId) {
    return this.getClass().getCanonicalName() + searchId + ".records";
  }
  
  protected String generateSearchEntitySessionKey(String searchId) {
    return this.getClass().getCanonicalName() + searchId + ".searchEntity";
  }
  
  private Map<String, Object> createTemplate(Map<String, Object> model) {
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
            break;
          }
        }
      }
    });
    
    return template;
  }
  
  public SearchEntity getSearchEntity(String searchId, HttpServletRequest request) {
    
    SearchEntity searchEntity = (SearchEntity)request.getSession().getAttribute(generateSearchEntitySessionKey(searchId));
    
    if (searchEntity == null) {
      throw new NotFoundException();
    }
    
    return searchEntity;
  }

  private static final int DEFAULT_PAGE_SIZE = 25;
  
  public Response fetchData(
      String searchId,
      Integer pageSize,
      Integer page, 
      String sortFieldName,
      String sortOrder,
      HttpServletRequest request) {
    
    List<?> records = (List<?>)request.getSession().getAttribute(generateSearchResultRecordsSessionKey(searchId));

    if (records == null) {
      throw new NotFoundException();
    }
    
    
    
    // sorting
    if (sortFieldName != null) {
      
      @SuppressWarnings("unchecked")
      List<Map<String, Object>> sortRecords = new ArrayList<>((List<Map<String, Object>>)records);// typed copy of the list 
      
      final int sortOrder0 = "desc".equals(sortOrder) ? -1 : 1;
      
      synchronized (sortRecords) {
        Collections.sort(sortRecords, new ListSorter(sortFieldName, sortOrder0));
      }
      
      records = sortRecords;
    }
    
    
    
    // paging
    pageSize = pageSize == null ? DEFAULT_PAGE_SIZE : pageSize;

    page = page == null ? 1 : page;
    
    int fromIndex = pageSize * (page - 1);
    
    int toIndex = fromIndex + pageSize;
    if (toIndex > records.size()) {
      toIndex = Math.max(fromIndex, records.size());
    }
    
    try {
      List<?> pageRecords = Collections.unmodifiableList(records.subList(fromIndex, toIndex));
      return Response.ok(pageRecords).build();
    } catch (IndexOutOfBoundsException e) {
      // TODO no need to print stack trace. Log a single-line message
      throw new NotFoundException();
    }
  }
  
  
}