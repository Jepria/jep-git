insert into
  jrs_goods_segment_link
(
  goods_id
  , goods_segment_code
)
select
  gd.goods_id
  , s.goods_segment_code
from
  (
select
  b.*
  , bt.column_value as goods_segment_code
from
  (
  select
    'Новая заря' as supplier_name
    , 'Прихожая "Ассоль"' as goods_name
    , 'HOME' as goods_segment_code_list
  from dual
  union all select
    'Новая заря'
    , 'Стенка "Витязь"'
    , 'HOME'
  from dual
  union all select
    'Новая заря'
    , 'Молоко "Буренка", 1 л.'
    , 'EVERYDAY'
  from dual
  union all select
    'Издательство "Слово"'
    , 'Иванов В.В. "Книга о вкусной и здоровой пище"'
    , 'HOME,REST'
  from dual
  union all select
    'Издательство "Слово"'
    , 'Петров Ф.В. "Как поймать язя"'
    , 'HOME,REST'
  from dual
  union all select
    'ООО "Иностранная книга"'
    , 'Diane Hopkins, Pauline Cullen "Cambridge Gram for IELTS"'
    , 'HOME,REST'
  from dual
  union all select
    'ООО "Иностранная книга"'
    , 'Рэймонд Мерфи "English Grammar in Use with Answers"'
    , 'HOME,REST'
  from dual
  union all select
    'ООО "Иностранная книга"'
    , 'Liz and John Soars "New Headway: Student''s Book"'
    , 'HOME,REST'
  from dual
  union all select
    '"Продукты питания", ОАО'
    , 'Молоко "Деревенька", 1 л.'
    , 'EVERYDAY'
  from dual
  union all select
    '"Продукты питания", ОАО'
    , 'Молоко разливное'
    , 'EVERYDAY'
  from dual
  union all select
    '"Продукты питания", ОАО'
    , 'Рис'
    , 'EVERYDAY'
  from dual
  union all select
    'Электромир'
    , 'Электровеник'
    , 'EVERYDAY,HOME,TOY'
  from dual
  union all select
    'Электромир'
    , 'Электровеник с подсветкой'
    , 'EVERYDAY,HOME,TOY'
  from dual
  union all select
    'Сатурн'
    , 'Электромухобойка'
    , 'EVERYDAY,HOME,TOY'
  from dual
  union all select
    'Сатурн'
    , 'Электромухобойка с лазерным прицелом'
    , 'EVERYDAY,HOME,TOY'
  from dual
  union all select
    'Сатурн'
    , 'Электромухобойка с лазерным прицелом на солнечных батареях'
    , 'EVERYDAY,HOME,TOY'
  from dual
  union all select
    'Профессиональный инструмент'
    , 'Дрель'
    , 'HOME'
  from dual
  union all select
    'Профессиональный инструмент'
    , 'Дрель аккумуляторная'
    , 'HOME'
  from dual
  union all select
    'Профессиональный инструмент'
    , 'Набор инструментов'
    , 'HOME'
  from dual
  union all select
    'Мебель из Франции'
    , 'Гарнитур "Версаль"'
    , 'HOME'
  from dual
  union all select
    'Мебель из Франции'
    , 'Гарнитур "Париж"'
    , 'HOME'
  from dual
  union all select
    'Мебель из Франции'
    , 'Спальня "Людовик XIV"'
    , 'HOME'
  from dual
  union all select
    'ООО "Все для дома"'
    , 'Мыло хозяйственное'
    , 'EVERYDAY,HOME'
  from dual
  union all select
    'Компьютерный мир'
    , 'Компьютер для дома'
    , 'HOME,REST,TOY'
  from dual
  union all select
    'Компьютерный мир'
    , 'Компьютер для офиса'
    , 'EVERYDAY'
  from dual
  ) b
  , table( pkg_Common.split( b.goods_segment_code_list, ',')) bt
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
      jrs_goods_segment_link t
    where
      t.goods_id = gd.goods_id
      and t.goods_segment_code = s.goods_segment_code
    )
/

commit
/
