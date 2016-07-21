package com.technology.jep.jepriareport.server;

public class JepReportConstant {

  /**
   * Режим построения отчётов в оперативной памяти
   */
  public static final int LIST_IN_MEMORY_MODE = 0;

  /**
   * Режим построения отчётов с использованием файловой системы (для больших отчётов)
   */
  public static final int LIST_IN_FILES_MODE = 1;

  /**
   * Режим построения отчётов по умолчанию
   */
  public static final int DEFAULT_MODE = LIST_IN_MEMORY_MODE;
    
  /**
   * Размер по умолчанию блока записей большого отчёта (число записей в блоке)
   */
  public static final int DEFAULT_RECORD_BLOCK_SIZE = 100;

  /**
   * Параметры отчёта.Название отчёта
   */
  public static final String TITLE_REPORT_PARAMETER = "ReportTitle";

  /**
   * Параметр http - имя файла для выгрузки
   */
  public static final String DOWNLOAD_FILE_NAME_PARAMETER = "reportFileName";

}
