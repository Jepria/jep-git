package com.technology.jep.jepriashowcase.goods.auto;

import java.util.Set;

import com.technology.jep.jepria.auto.JepRiaModuleAuto;

public interface GoodsAuto extends JepRiaModuleAuto {

  /**
   * Заполнение поля <I>Наименование товара</I>
   * 
   * @param goodsName значение поля <I>Наименование товара</I> 
   */
  void setGoodsName(String goodsName);

  /**
   * Получение значения поля <I>Наименование товара</I>
   * @return значение поля <I>Наименование товара</I>
   */
  String getGoodsName();

  /**
   * Заполнение поля <I>Закупочная цена</I>
   * 
   * @param purchasingPrice значение поля <I>Закупочная цена</I>
   */
  void setPurchasingPrice(String purchasingPrice);

  /**
   * Получение значения поля <I>Закупочная цена</I>
   * 
   * @return значение поля <I>Закупочная цена</I>
   */
  String getPurchasingPrice();

  /**
   * Заполнение поля <I>Тип товара</I>
   * 
   * @param goodsType тип товара
   */
  void setGoodsType(String goodsType);

  /**
   * Получение значения поля <I>Тип товара</I>
   * 
   * @return значение поля <I>Тип товара</I>
   */
  String getGoodsType();

  /**
   * @param segment
   */
  void setSegment(String segment);

  /**
   * Установить принадлежность к разделам каталога товаров, пометив узлы дерева 
   * 
   * @param catalogSections - разделы каталога
   */
  void setCatalogSections(Set<String> catalogSections);

  /**
   * Заполнение поля <I>Мотивация</I>
   * 
   * @param motivation тип товара
   */
  void setMotivation(String motivation);

  /**
   * Получение значения поля <I>Мотивация</I>
   * 
   * @return значение поля <I>Мотивация</I>
   */
  String getMotivation();

  /**
   * Заполнение поля <I>Единицы измерения</I>
   * 
   * @param motivation единицы измерения
   */
  void setUnit(String unit);

  /**
   * Получение значения поля <I>Единицы измерения</I>
   * 
   * @return значение поля <I>Единицы измерения</I>
   */
  String getUnit();

  /**
   * Получение значения поля <I>Сегмент товара</I>
   * 
   * @return значение поля <I>Сегмент товара</I>
   */
  String getSegment();

  /**
   * Получение значения поля <I>Принадлежность к каталогу</I> - множества помеченных узлов дерева
   * 
   * @return значение поля <I>Принадлежность к каталогу</I> - множество помеченных узлов дерева
   */
  Set<String> getCatalogSections();

  /**
   * Заполнение поля <I>Фото товара</I> файлом
   * 
   * @param pathToFile локальный путь на машине до нужного файла
   */
  void setPhoto(String pathToFile);

  /**
   * Заполнение поля <I>Спецификация товара</I> файлом
   * 
   * @param pathToFile локальный путь на машине до нужного файла
   */
  void setPortfolio(String pathToFile);

  
  /**
   * Заполнение формы поиска
   * 
   * @param goodsName Наименование товара
   * @param goodsType Тип товара
   * @param unit Единица измерения
   * @param segment Сегмент товара
   * @param catalogSections Принадлежность к каталогу
   */
  void fillSearchForm(
      String goodsName,
      String goodsType,
      String unit,
      String segment,
      Set<String> catalogSections);

  /**
   * Заполнение формы создания
   * 
   * @param goodsName Наименование товара
   * @param goodsType Тип товара
   * @param unit Единица измерения
   * @param motivation Мотивация
   * @param purchasingPrice закупочная цена
   * @param photo фото товара
   * @param specification спецификация товара
   */
  void fillCreateForm(
      String goodsName,
      String goodsType,
      String unit,
      String motivation,
      String purchasingPrice,
      String photo,
      String specification);

  /**
   * Заполнение формы редактирования
   * 
   * @param goodsName Наименование товара
   * @param goodsType Тип товара
   * @param unit Единица измерения
   * @param motivation Мотивация
   * @param purchasingPrice закупочная цена
   * @param photo фото товара
   * @param specification спецификация товара
   */
  void fillEditForm(
      String goodsName,
      String goodsType,
      String unit,
      String motivation,
      String purchasingPrice,
      String photo,
      String specification);
}
