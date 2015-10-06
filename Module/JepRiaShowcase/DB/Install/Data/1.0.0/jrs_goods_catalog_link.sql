insert into
  jrs_goods_catalog_link
(
  goods_id
  , goods_catalog_id
)
select
  gd.goods_id
  , s.goods_catalog_id
from
  (
select
  b.*
  , (
    select
      t.goods_catalog_id
    from
      (
      select
        gc.goods_catalog_id
        , ltrim( sys_connect_by_path( gc.goods_catalog_name, ' / '), ' /')
          as catalog_path
      from
        jrs_goods_catalog gc
      where
        level = 3
      connect by
        prior gc.goods_catalog_id = gc.parent_goods_catalog_id
      start with
        gc.parent_goods_catalog_id is null
      ) t
    where
      t.catalog_path = bt.column_value
    )
    as goods_catalog_id
from
  (
  select
    'Новая заря' as supplier_name
    , 'Прихожая "Ассоль"' as goods_name
    , cmn_string_table_t(
        'Все для дома / Мебель / Прихожая'
      )
      as catalog_path_list
  from dual
  union all select
    'Новая заря'
    , 'Стенка "Витязь"'
    , cmn_string_table_t(
        'Все для дома / Мебель / Гостиная'
      )
  from dual
  union all select
    'Новая заря'
    , 'Молоко "Буренка", 1 л.'
    , cmn_string_table_t(
        'Продукты питания / Молочные продукты / Молоко'
      )
  from dual
  union all select
    'Издательство "Слово"'
    , 'Иванов В.В. "Книга о вкусной и здоровой пище"'
    , cmn_string_table_t(
        'Пора на дачу / Книги / Готовим на природе'
      )
  from dual
  union all select
    'Издательство "Слово"'
    , 'Петров Ф.В. "Как поймать язя"'
    , cmn_string_table_t(
        'Активный отдых / Книги, кино, музыка / Книги о спорте'
        , 'Активный отдых / Отдых на природе / Товары для туризма'
        , 'Книги / Нехудожественная литература / Домашний круг'
      )
  from dual
  union all select
    'ООО "Иностранная книга"'
    , 'Diane Hopkins, Pauline Cullen "Cambridge Gram for IELTS"'
    , cmn_string_table_t(
        'Книги / Нехудожественная литература / Изучение языков мира'
        , 'Книги / Учебная литература / Школьникам и абитуриентам'
        , 'Книги / Учебная литература / Студентам и аспирантам'
        , 'Книги / Литература на иностранных языках / Нехудожественная литература'
      )
  from dual
  union all select
    'ООО "Иностранная книга"'
    , 'Рэймонд Мерфи "English Grammar in Use with Answers"'
    , cmn_string_table_t(
        'Книги / Нехудожественная литература / Изучение языков мира'
        , 'Книги / Учебная литература / Школьникам и абитуриентам'
        , 'Книги / Литература на иностранных языках / Нехудожественная литература'
      )
  from dual
  union all select
    'ООО "Иностранная книга"'
    , 'Liz and John Soars "New Headway: Student''s Book"'
    , cmn_string_table_t(
        'Книги / Нехудожественная литература / Изучение языков мира'
        , 'Книги / Учебная литература / Школьникам и абитуриентам'
        , 'Книги / Литература на иностранных языках / Нехудожественная литература'
      )
  from dual
  union all select
    '"Продукты питания", ОАО'
    , 'Молоко "Деревенька", 1 л.'
    , cmn_string_table_t(
        'Продукты питания / Молочные продукты / Молоко'
      )
  from dual
  union all select
    '"Продукты питания", ОАО'
    , 'Молоко разливное'
    , cmn_string_table_t(
        'Продукты питания / Молочные продукты / Молоко'
      )
  from dual
  union all select
    '"Продукты питания", ОАО'
    , 'Рис'
    , null
  from dual
  union all select
    'Электромир'
    , 'Электровеник'
    , cmn_string_table_t(
        'Пора на дачу / Кухонные принадлежности / Кухонные принадлежности'
      )
  from dual
  union all select
    'Электромир'
    , 'Электровеник с подсветкой'
    , cmn_string_table_t(
        'Пора на дачу / Кухонные принадлежности / Кухонные принадлежности'
      )
  from dual
  union all select
    'Сатурн'
    , 'Электромухобойка'
    , cmn_string_table_t(
        'Пора на дачу / Кухонные принадлежности / Кухонные принадлежности'
        , 'Активный отдых / Пикник / Защита от насекомых'
      )
  from dual
  union all select
    'Сатурн'
    , 'Электромухобойка с лазерным прицелом'
    , cmn_string_table_t(
        'Пора на дачу / Кухонные принадлежности / Кухонные принадлежности'
        , 'Активный отдых / Пикник / Защита от насекомых'
      )
  from dual
  union all select
    'Сатурн'
    , 'Электромухобойка с лазерным прицелом на солнечных батареях'
    , cmn_string_table_t(
        'Пора на дачу / Кухонные принадлежности / Кухонные принадлежности'
        , 'Активный отдых / Пикник / Защита от насекомых'
      )
  from dual
  union all select
    'Профессиональный инструмент'
    , 'Дрель'
    , cmn_string_table_t(
        'Пора на дачу / Инструменты / Электроинструмент'
      )
  from dual
  union all select
    'Профессиональный инструмент'
    , 'Дрель аккумуляторная'
    , cmn_string_table_t(
        'Пора на дачу / Инструменты / Электроинструмент'
      )
  from dual
  union all select
    'Профессиональный инструмент'
    , 'Набор инструментов'
    , cmn_string_table_t(
        'Пора на дачу / Пикник / Инструменты'
      )
  from dual
  union all select
    'Мебель из Франции'
    , 'Гарнитур "Версаль"'
    , cmn_string_table_t(
        'Все для дома / Мебель / Гостиная'
      )
  from dual
  union all select
    'Мебель из Франции'
    , 'Гарнитур "Париж"'
    , cmn_string_table_t(
        'Все для дома / Мебель / Гостиная'
      )
  from dual
  union all select
    'Мебель из Франции'
    , 'Спальня "Людовик XIV"'
    , cmn_string_table_t(
        'Все для дома / Мебель / Гостиная'
      )
  from dual
  union all select
    'ООО "Все для дома"'
    , 'Мыло хозяйственное'
    , cmn_string_table_t(
        'Пора на дачу / Косметика / Гигиена'
        , 'Активный отдых / Всё для путешествий / Уход за лицом и телом'
      )
  from dual
  union all select
    'Компьютерный мир'
    , 'Компьютер для дома'
    , cmn_string_table_t(
        'Активный отдых / Активный отдых с детьми / Электроника для детей'
      )
  from dual
  union all select
    'Компьютерный мир'
    , 'Компьютер для офиса'
    , null
  from dual
  ) b
  , table( b.catalog_path_list) bt
  ) s
  left outer join jrs_supplier sp
    on sp.supplier_name = s.supplier_name
  left outer join jrs_goods gd
    on gd.supplier_id = sp.supplier_id
      and gd.goods_name = s.goods_name
where
  not exists
    (
    select
      null
    from
      jrs_goods_catalog_link t
    where
      t.goods_id = gd.goods_id
      and t.goods_catalog_id = s.goods_catalog_id
    )
/

commit
/
