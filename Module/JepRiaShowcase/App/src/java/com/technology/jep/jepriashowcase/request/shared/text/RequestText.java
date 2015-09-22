package com.technology.jep.jepriashowcase.request.shared.text;

/**
 * Interface to represent the constants contained in resource bundle:
 * 	'D:/JEP/Module/JepRiaShowcase/Trunk/App/src/java/com/technology/jep/jepriashowcase/request/shared/text/RequestText.properties'.
 */
public interface RequestText extends com.google.gwt.i18n.client.Constants {
  
  /**
   * Translated "ID товара".
   * 
   * @return translated "ID товара"
   */
  @DefaultStringValue("ID товара")
  @Key("request.detail.goods_id")
  String request_detail_goods_id();

  /**
   * Translated "Наименование товара".
   * 
   * @return translated "Наименование товара"
   */
  @DefaultStringValue("Наименование товара")
  @Key("request.detail.goods_name")
  String request_detail_goods_name();

  /**
   * Translated "Дата создания запроса".
   * 
   * @return translated "Дата создания запроса"
   */
  @DefaultStringValue("Дата создания запроса")
  @Key("request.detail.request_date")
  String request_detail_request_date();

  /**
   * Translated "Начальная дата должна быть раньше конечной!".
   * 
   * @return translated "Начальная дата должна быть раньше конечной!"
   */
  @DefaultStringValue("Начальная дата должна быть раньше конечной!")
  @Key("request.detail.request_date.error.beginEndDates")
  String request_detail_request_date_error_beginEndDates();

  /**
   * Translated "Дата создания запроса должна быть меньше текущей!".
   * 
   * @return translated "Дата создания запроса должна быть меньше текущей!"
   */
  @DefaultStringValue("Дата создания запроса должна быть меньше текущей!")
  @Key("request.detail.request_date.error.sysdate")
  String request_detail_request_date_error_sysdate();

  /**
   * Translated "Дата создания запроса с".
   * 
   * @return translated "Дата создания запроса с"
   */
  @DefaultStringValue("Дата создания запроса с")
  @Key("request.detail.request_date_from")
  String request_detail_request_date_from();

  /**
   * Translated "Дата создания запроса по".
   * 
   * @return translated "Дата создания запроса по"
   */
  @DefaultStringValue("Дата создания запроса по")
  @Key("request.detail.request_date_to")
  String request_detail_request_date_to();

  /**
   * Translated "ID запроса".
   * 
   * @return translated "ID запроса"
   */
  @DefaultStringValue("ID запроса")
  @Key("request.detail.request_id")
  String request_detail_request_id();

  /**
   * Translated "Код статуса запроса".
   * 
   * @return translated "Код статуса запроса"
   */
  @DefaultStringValue("Код статуса запроса")
  @Key("request.detail.request_status_code")
  String request_detail_request_status_code();

  /**
   * Translated "Количество записей".
   * 
   * @return translated "Количество записей"
   */
  @DefaultStringValue("Количество записей")
  @Key("request.detail.row_count")
  String request_detail_row_count();

  /**
   * Translated "ID магазина".
   * 
   * @return translated "ID магазина"
   */
  @DefaultStringValue("ID магазина")
  @Key("request.detail.shop_id")
  String request_detail_shop_id();

  /**
   * Translated "Наименование магазина".
   * 
   * @return translated "Наименование магазина"
   */
  @DefaultStringValue("Наименование магазина")
  @Key("request.detail.shop_name")
  String request_detail_shop_name();

  /**
   * Translated "ID товара".
   * 
   * @return translated "ID товара"
   */
  @DefaultStringValue("ID товара")
  @Key("request.list.goods_id")
  String request_list_goods_id();

  /**
   * Translated "Наименование товара".
   * 
   * @return translated "Наименование товара"
   */
  @DefaultStringValue("Наименование товара")
  @Key("request.list.goods_name")
  String request_list_goods_name();

  /**
   * Translated "Дата создания запроса".
   * 
   * @return translated "Дата создания запроса"
   */
  @DefaultStringValue("Дата создания запроса")
  @Key("request.list.request_date")
  String request_list_request_date();

  /**
   * Translated "ID запроса".
   * 
   * @return translated "ID запроса"
   */
  @DefaultStringValue("ID запроса")
  @Key("request.list.request_id")
  String request_list_request_id();

  /**
   * Translated "Наименование статуса запроса".
   * 
   * @return translated "Наименование статуса запроса"
   */
  @DefaultStringValue("Наименование статуса запроса")
  @Key("request.list.request_status_name")
  String request_list_request_status_name();

  /**
   * Translated "ID магазина".
   * 
   * @return translated "ID магазина"
   */
  @DefaultStringValue("ID магазина")
  @Key("request.list.shop_id")
  String request_list_shop_id();

  /**
   * Translated "Наименование магазина".
   * 
   * @return translated "Наименование магазина"
   */
  @DefaultStringValue("Наименование магазина")
  @Key("request.list.shop_name")
  String request_list_shop_name();

  /**
   * Translated "Запрос на закупку".
   * 
   * @return translated "Запрос на закупку"
   */
  @DefaultStringValue("Запрос на закупку")
  @Key("request.title")
  String request_title();
}
