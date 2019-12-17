package org.jepria.server.data;

import java.lang.reflect.Field;
import java.util.*;

/**
 * RecordDefinition built from a set of DTO classes
 */
public class RecordDefinitionDtoImpl implements RecordDefinition {

  // unmodifiable
  protected final Set<String> primaryKey;
  // unmodifiable
  protected final Map<String, Class<?>> fieldMap;

  /**
   *
   * @param dtoClasses all DTO classes used in the particular module
   */
  public RecordDefinitionDtoImpl(Class<?>... dtoClasses) {

    final Set<Class<?>> dtoClassSet = new HashSet<>(Arrays.asList(dtoClasses));

    Set<String> primaryKey = new HashSet<>();
    Map<String, Class<?>> fieldMap = new HashMap<>();

    for (Class<?> dtoClass: dtoClassSet) {
      for (Field field: dtoClass.getDeclaredFields()) {

        final String fieldName = field.getName();
        final Class<?> fieldType = field.getType();

        Class<?> fieldTypeExs = fieldMap.get(fieldName);

        if (fieldTypeExs != null) {
          // the field exists
          if (!fieldTypeExs.equals(fieldType)) {
            throw new IllegalStateException("The field " + fieldName + " has multiple conflicting types" +
                    " (" + fieldTypeExs.getCanonicalName() + " and " + fieldType.getCanonicalName() + ")" +
                    " among the DTO classes " + Arrays.toString(dtoClasses));
          }
        } else {
          fieldMap.put(fieldName, fieldType);
        }

        if (field.getAnnotation(PrimaryKey.class) != null) {
          primaryKey.add(fieldName);
        }
      }
    }

    if (primaryKey.isEmpty()) {
      throw new IllegalStateException("No primary key found among the DTO classes " + Arrays.toString(dtoClasses));
    }

    { // convert camelCase to snake_case
      Set<String> primaryKeySnake = new HashSet<>();
      for (String s : primaryKey) {
        primaryKeySnake.add(NamingUtil.camelCase2snake_case(s));
      }
      primaryKey = primaryKeySnake;
      // TODO log here at DEBUG level the original primaryKey and the primaryKeySnake map

      Map<String, Class<?>> fieldMapSnake = new HashMap<>();
      for (String s : fieldMap.keySet()) {
        fieldMapSnake.put(NamingUtil.camelCase2snake_case(s), fieldMap.get(s));
      }
      fieldMap = fieldMapSnake;
      // TODO log here at DEBUG level the original fieldMap and the fieldMapSnake map
    }


    this.primaryKey = Collections.unmodifiableSet(primaryKey);
    this.fieldMap = Collections.unmodifiableMap(fieldMap);
  }

  @Override
  public Set<String> getFieldNames() {
    return fieldMap.keySet();
  }

  @Override
  public List<String> getPrimaryKey() {
    return new ArrayList<>(primaryKey); // TODO RecordDefinition must return Set instead of List
  }

  @Override
  public Class<?> getFieldType(String fieldName) {
    return fieldMap.get(fieldName);
  }

}
