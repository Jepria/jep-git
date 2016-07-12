package com.technology.jep.jepria.server.download.excel;

/**
 * Константы для формирования Excel-документа.
 */
public class ExcelReportConstant {
  /**
   * Заголовок документа.
   */
  public static final String XML_HEADER =     
    "<?xml version=\"1.0\"?>\r\n" +
    "<?mso-application progid=\"Excel.Sheet\"?>\r\n" +
    "<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"\r\n" +
    "  xmlns:o=\"urn:schemas-microsoft-com:office:office\"\r\n" +
    "  xmlns:x=\"urn:schemas-microsoft-com:office:excel\"\r\n" +
    "  xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"\r\n" +
    "  xmlns:html=\"http://www.w3.org/TR/REC-html40\">\r\n";
  
  /**
   * Заголовок списка стилей.
   */
  public static final String STYLES_HEADER = "  <Styles>\r\n";
  
  /**
   * Стиль ячеек по умолчанию.
   */
  public static final String DEFAULT_STYLE = 
    "    <Style ss:ID=\"Default\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Left\" />\r\n" +
    "    </Style>";
  
  /**
   * Стиль по умолчанию для шапки таблицы.
   */
  public static final String HEADER_STYLE = 
    "    <Style ss:ID=\"Header\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Center\" ss:WrapText=\"1\"/>\r\n" +
    "      <Font ss:Bold=\"1\"/>" +
    "    </Style>";
  
  /**
   * Стиль по умолчанию для ячеек с датой.
   */
  public static final String DATE_STYLE = 
    "    <Style ss:ID=\"DefaultDate\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Left\" />\r\n" +
    "      <NumberFormat ss:Format=\"Short Date\" />\r\n" +
    "    </Style>";

  /**
   * Стиль по умолчанию для ячеек со временем.
   */
  public static final String TIME_STYLE = 
    "    <Style ss:ID=\"DefaultTime\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Left\" />\r\n" +
    "      <NumberFormat ss:Format=\"[$-F400]h:mm:ss\\ AM/PM\"/> />\r\n" +
    "    </Style>";
  
  /**
   * Стиль по умолчанию для ячеек с датой и временем.
   */
  public static final String DATETIME_STYLE = 
    "    <Style ss:ID=\"DefaultDateTime\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Left\" />\r\n" +
    "      <NumberFormat ss:Format=\"dd/mm/yy\\ h:mm;@\" />\r\n" +
    "    </Style>";
  
  /**
   * Стиль по умолчанию для ячеек с данными целочисленного типа.
   */
  public static final String INTEGER_STYLE =
    "    <Style ss:ID=\"DefaultInteger\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Left\" />\r\n" +
    "      <NumberFormat ss:Format=\"0\" />\r\n" +
    "    </Style>";
  
  /**
   * Стиль по умолчанию для ячеек с десятичными данными.
   */
  public static final String DECIMAL_STYLE =
    "    <Style ss:ID=\"DefaultDecimal\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Left\" />\r\n" +
    "      <NumberFormat ss:Format=\"Fixed\" />\r\n" +
    "    </Style>";
  
  /**
   * Стили по умолчанию.
   */
  public static final String DEFAULT_STYLES =
    "    <Style ss:ID=\"Default\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Left\" />\r\n" +
    "    </Style>\r\n" +
    "    <Style ss:ID=\"Header\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Center\" ss:WrapText=\"1\"/>\r\n" +
    "      <Font ss:Bold=\"1\"/>\r\n" +
    "    </Style>\r\n" +
    "    <Style ss:ID=\"DefaultDate\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Left\" />\r\n" +
    "      <NumberFormat ss:Format=\"Short Date\" />\r\n" +
    "    </Style>\r\n" +
    "    <Style ss:ID=\"DefaultTime\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Left\" />\r\n" +
    "      <NumberFormat ss:Format=\"[$-F400]h:mm:ss\\ AM/PM\"/> />\r\n" +
    "    </Style>\r\n" +
    "    <Style ss:ID=\"DefaultDateTime\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Left\" />\r\n" +
    "      <NumberFormat ss:Format=\"dd/mm/yy\\ h:mm;@\" />\r\n" +
    "    </Style>\r\n" +
    "    <Style ss:ID=\"DefaultInteger\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Left\" />\r\n" +
    "      <NumberFormat ss:Format=\"0\" />\r\n" +
    "    </Style>\r\n" +
    "    <Style ss:ID=\"DefaultDecimal\">\r\n" +
    "      <Alignment ss:Vertical=\"Top\" ss:Horizontal=\"Left\" />\r\n" +
    "      <NumberFormat ss:Format=\"Fixed\" />\r\n" +
    "    </Style>\r\n";

  /**
   * Закрывающий тег для списка стилей.
   */
  public static final String STYLES_FOOTER =
    "  </Styles>\r\n";

  /**
   * Заголовок таблицы.
   */
  public static final String TABLE_HEADER =
    "  <Worksheet ss:Name=\"ExcelReport\">" + 
    "    <Table>";

  /**
   * Закрывающий тег таблицы.
   */
  public static final String TABLE_FOOTER = 
    "   </Table>\r\n";

  /**
   * Префикс автофильтра.
   */
  public static final String AUTO_FILTER_PREFIX = 
    "<AutoFilter x:Range=\"";
  
  /**
   * Суффикс автофильтра.
   */
  public static final String AUTO_FILTER_SUFFIX =
    "\" xmlns=\"urn:schemas-microsoft-com:office:excel\"/>>\r\n";
  
  /**
   * Конец документа.
   */
  public static final String XML_FOOTER = 
    " <WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">\r\n" +
    "   <FrozenNoSplit/>\r\n" +
    "   <SplitHorizontal>1</SplitHorizontal>\r\n" +
    "   <TopRowBottomPane>1</TopRowBottomPane>\r\n" +
    "   <ActivePane>2</ActivePane>\n" +
    " </WorksheetOptions>\r\n" +
    "  </Worksheet>\r\n"+
    "</Workbook>\r\n";
  
  /**
   * Тег для столбца.
   */
  public static final String COLUMN_PREFIX =
    "      <Column ss:Width=\"150\"/>";
  
  /**
   * Открывающие теги для ячеек шапки таблицы.
   */
  public static final String HEADER_CELL_PREFIX =
    "        <Cell ss:StyleID=\"Header\"><Data ss:Type=\"String\">";
  
  /**
   * Открывающие теги для ячеек со строковыми данными
   */
  public static final String STRING_CELL_PREFIX =
    "        <Cell ss:StyleID=\"Default\"><Data ss:Type=\"String\">";
  
  /**
   * Теги для пустой ячейки.
   */
  public static final String EMPTY_CELL = 
    "        <Cell ss:StyleID=\"Default\"><Data ss:Type=\"String\" /></Cell>";    
  
  /**
   * Открывающие теги для ячеек с датой.
   */
  public static final String DATE_CELL_PREFIX =
    "        <Cell ss:StyleID=\"DefaultDate\"><Data ss:Type=\"DateTime\">";
  
  /**
   * Открывающие теги для ячеек с временем.
   */
  public static final String TIME_CELL_PREFIX =
    "        <Cell ss:StyleID=\"DefaultTime\"><Data ss:Type=\"DateTime\">";
  
  /**
   * Открывающие теги для ячеек с датой и временем.
   */
  public static final String DATETIME_CELL_PREFIX =
    "        <Cell ss:StyleID=\"DefaultDateTime\"><Data ss:Type=\"DateTime\">";
  
  /**
   * Открывающие теги для ячеек с целочисленными данными.
   */
  public static final String INTEGER_CELL_PREFIX =
    "        <Cell ss:StyleID=\"DefaultInteger\"><Data ss:Type=\"Number\">";
  
  /**
   * Открывающие теги для ячейки с десятичными данными.
   */
  public static final String DECIMAL_CELL_PREFIX =
    "        <Cell ss:StyleID=\"DefaultDecimal\"><Data ss:Type=\"Number\">";
    
  /**
   * Закрывающие теги для ячейки с данными.
   */
  public static final String CELL_POSTFIX =
    "</Data></Cell>";
  
  /**
   * Открывающий тег для строки таблицы.
   */
  public static final String ROW_PREFIX =
    "      <Row>";
  
  /**
   * Закрывающий тег для строки таблицы.
   */
  public static final String ROW_POSTFIX =
    "      </Row>";
  
  /**
   * Начало секции CDATA.
   */
  public static final String CDATA_PREFIX =
    "<![CDATA[";
  
  /**
   * Конец секции CDATA.
   */
  public static final String CDATA_POSTFIX = 
    "]]>";
  
  /**
   * Формат даты, воспринимаемый Excel.
   */
  public static final String EXCEL_DATE_FORMAT = "yyyy-MM-dd";
  
  /**
   * Формат даты и времени, воспринимаемый Excel.
   */
  public static final String EXCEL_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  
  /**
   * Десятичный формат по умолчанию.
   */
  public static final String EXCEL_DECIMAL_FORMAT = "########0.00";
  
}
