package org.jepria.server.data;

import com.technology.jep.jepria.server.dao.ResultSetMapper;

import java.io.*;
import java.util.List;

public interface DaoSupport {

  // Зачем фабричный метод? Поскольку в перспективе может понадобиться, например,
  // кешировать инстанс, реализовать его одиночкой или давать ту или иную реализацию в зависимости от найденного DB-драйвера или других параметров.
  // Поэтому лучше изначально не давать повода писать в прикладном коде 'new DaoSupportOracle()' (хотя такую возможность отменять тоже не стоит).
  static DaoSupport getInstance() {
    return new DaoSupportOracle();
  }

  <T> T create(String query, Class<? super T> resultTypeClass, Object... params);

  void execute(String query, Object... params);

  <T> List<T> find(String query, ResultSetMapper<? super T> mapper, Class<? super T> recordClass, Object... params);

  <T> T executeAndReturn(String query, Class<? super T> resultTypeClass, Object... params);

  <T> List<T> select(String query, ResultSetMapper<? super T> mapper, Class<? super T> modelClass, Object... params);

  void update(String query, Object... params);

  void delete(String query, Object... params);

  void deleteClob(String tableName, String dataFieldName, String whereClause);

  /**
   * @param tableName
   * @param dataFieldName field in the table which to upload object data to
   * @param whereClause must select a single row from the table, which to upload object data to
   * @param reader object data source. Passing {@code null} to erase the existing content is discouraged (may cause exceptions), use {@link #deleteClob(String, String, String)}
   */
  void uploadClob(String tableName, String dataFieldName, String whereClause, Reader reader);

  void deleteBlob(String tableName, String dataFieldName, String whereClause);

  /**
   *
   * @param tableName
   * @param dataFieldName field in the table which to upload object data to
   * @param whereClause must select a single row from the table, which to upload object data to
   * @param stream object data source. Passing {@code null} to erase the existing content is discouraged (may cause exceptions), use {@link #deleteBlob(String, String, String)}
   */
  void uploadBlob(String tableName, String dataFieldName, String whereClause, InputStream stream);

  /**
   * @param tableName
   * @param dataFieldName
   * @param whereClause
   * @param content object data. Passing {@code null} (as well as passing {@code ""}) erases the existing content
   */
  default void uploadClob(String tableName, String dataFieldName, String whereClause, String content) {
    if (content == null || "".equals(content)) {
      deleteClob(tableName, dataFieldName, whereClause);
    } else {
      uploadClob(tableName, dataFieldName, whereClause, new StringReader(content));
    }
  }

  /**
   *
   * @param tableName
   * @param dataFieldName
   * @param whereClause
   * @param stream object data source. Passing {@code null} to erase the existing content is discouraged (may cause exceptions), use {@link #deleteClob(String, String, String)}
   * @param charset
   */
  default void uploadClob(String tableName, String dataFieldName, String whereClause, InputStream stream, String charset) {
    final Reader reader;
    try {
      reader = new InputStreamReader(stream, charset);
    } catch (UnsupportedEncodingException e) {
      // TODO better to declare throws or wrap into a RuntimeException?
      throw new RuntimeException(e);
    }
    uploadClob(tableName, dataFieldName, whereClause, reader);
  }

  /**
   * @param tableName
   * @param dataFieldName field in the table which to upload object data to
   * @param whereClause must select a single row from the table, which to download object data from
   * @param writer object data target. If the object is {@code null} (as well as empty) in the database, no data will be written
   */
  // no charset parameter because the database already knows which charset it uses to store clob
  void downloadClob(String tableName, String dataFieldName, String whereClause, Writer writer);

  /**
   *
   * @param tableName
   * @param dataFieldName field in the table which to upload object data to
   * @param whereClause must select a single row from the table, download object data from
   * @param stream object data target
   */
  void downloadBlob(String tableName, String dataFieldName, String whereClause, OutputStream stream);

  /**
   *
   * @param tableName
   * @param dataFieldName
   * @param whereClause
   * @return either non-empty string containing object data or {@code null} if the object is {@code null} (as well as empty) in the database
   */
  // no charset parameter because the database already knows which charset it uses to store clob
  default String downloadClob(String tableName, String dataFieldName, String whereClause) {
    StringWriter stringWriter = new StringWriter();
    downloadClob(tableName, dataFieldName, whereClause, stringWriter);
    // TODO return null if there is no clob (empty clob), instead of a clob with value ""
    return stringWriter.toString();
  }
}
