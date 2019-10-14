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

  /**
   * @param tableName
   * @param dataFieldName field in the table which to upload object data to
   * @param whereClause must select a single row from the table, which to upload object data to
   * @param reader object data source TODO might support uploading {@code null} reader (e.g. erasing existing content)?
   */
  void uploadClob(String tableName, String dataFieldName, String whereClause, Reader reader);

  /**
   *
   * @param tableName
   * @param dataFieldName field in the table which to upload object data to
   * @param whereClause must select a single row from the table, which to upload object data to
   * @param stream object data source TODO might support uploading {@code null} stream (e.g. erasing existing content)?
   */
  void uploadBlob(String tableName, String dataFieldName, String whereClause, InputStream stream);

  default void uploadClob(String tableName, String dataFieldName, String whereClause, String content) {
    uploadClob(tableName, dataFieldName, whereClause, content == null ? null : new StringReader(content));
  }

  default void uploadClob(String tableName, String dataFieldName, String whereClause, InputStream stream, String charset) {
    final Reader reader;
    try {
      reader = stream == null ? null : new InputStreamReader(stream, charset);
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
   * @param writer object data target
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

  // no charset parameter because the database already knows which charset it uses to store clob
  default String downloadClob(String tableName, String dataFieldName, String whereClause) {
    StringWriter stringWriter = new StringWriter();
    downloadClob(tableName, dataFieldName, whereClause, stringWriter);
    // TODO return null if there is no clob (empty clob), instead of a clob with value ""
    return stringWriter.toString();
  }
}
