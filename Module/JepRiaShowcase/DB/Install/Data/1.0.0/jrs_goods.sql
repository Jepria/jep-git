insert into
  jrs_goods
(
  supplier_id
  , goods_name
  , goods_type_code
  , motivation_type_code
  , unit_code
  , purchasing_price
)
select
  sp.supplier_id
  , s.goods_name
  , s.goods_type_code
  , s.motivation_type_code
  , s.unit_code
  , s.purchasing_price
from
  (
  select
    'Новая заря' as supplier_name
    , 'Прихожая "Ассоль"' as goods_name
    , 'INDUSTRIAL' as goods_type_code
    , 'USUAL' as motivation_type_code
    , 'ITEM' as unit_code
    , 45500.00 as purchasing_price
  from dual
  union all select
    'Новая заря'
    , 'Стенка "Витязь"'
    , 'INDUSTRIAL'
    , 'USUAL'
    , 'ITEM'
    , 95500.59 as purchasing_price
  from dual
  union all select
    'Новая заря'
    , 'Молоко "Буренка", 1 л.'
    , 'FOOD'
    , 'USUAL'
    , 'ITEM'
    , 20.11
  from dual
  union all select
    'Издательство "Слово"'
    , 'Иванов В.В. "Книга о вкусной и здоровой пище"'
    , 'BOOK'
    , 'USUAL'
    , 'ITEM'
    , 890.00
  from dual
  union all select
    'Издательство "Слово"'
    , 'Петров Ф.В. "Как поймать язя"'
    , 'BOOK'
    , 'USUAL'
    , 'ITEM'
    , 40.10
  from dual
  union all select
    'ООО "Иностранная книга"'
    , 'Diane Hopkins, Pauline Cullen "Cambridge Gram for IELTS"'
    , 'BOOK'
    , 'MONTH'
    , 'ITEM'
    , 1807.00
  from dual
  union all select
    'ООО "Иностранная книга"'
    , 'Рэймонд Мерфи "English Grammar in Use with Answers"'
    , 'BOOK'
    , 'PERCENT'
    , 'ITEM'
    , 1460.00
  from dual
  union all select
    'ООО "Иностранная книга"'
    , 'Liz and John Soars "New Headway: Student''s Book"'
    , 'BOOK'
    , 'PERCENT'
    , 'ITEM'
    , 1280.00
  from dual
  union all select
    '"Продукты питания", ОАО'
    , 'Молоко "Деревенька", 1 л.'
    , 'FOOD'
    , 'USUAL'
    , 'ITEM'
    , 15.05
  from dual
  union all select
    '"Продукты питания", ОАО'
    , 'Молоко разливное'
    , 'FOOD'
    , 'USUAL'
    , 'L'
    , 15.05
  from dual
  union all select
    '"Продукты питания", ОАО'
    , 'Рис'
    , 'FOOD'
    , 'USUAL'
    , 'KG'
    , 35.05
  from dual
  union all select
    'Электромир'
    , 'Электровеник'
    , 'INDUSTRIAL'
    , 'QUARTER'
    , 'ITEM'
    , 500.00
  from dual
  union all select
    'Электромир'
    , 'Электровеник с подсветкой'
    , 'INDUSTRIAL'
    , 'QUARTER'
    , 'ITEM'
    , 550.00
  from dual
  union all select
    'Сатурн'
    , 'Электромухобойка'
    , 'INDUSTRIAL'
    , 'MONTH'
    , 'ITEM'
    , 400.00
  from dual
  union all select
    'Сатурн'
    , 'Электромухобойка с лазерным прицелом'
    , 'INDUSTRIAL'
    , 'MONTH'
    , 'ITEM'
    , 450.00
  from dual
  union all select
    'Сатурн'
    , 'Электромухобойка с лазерным прицелом на солнечных батареях'
    , 'INDUSTRIAL'
    , 'MONTH'
    , 'ITEM'
    , 550.00
  from dual
  union all select
    'Сатурн'
    , 'Молоко разливное'
    , 'FOOD'
    , 'USUAL'
    , 'L'
    , 18.15
  from dual
  union all select
    'Профессиональный инструмент'
    , 'Дрель'
    , 'INDUSTRIAL'
    , 'USUAL'
    , 'ITEM'
    , 1500.00
  from dual
  union all select
    'Профессиональный инструмент'
    , 'Дрель аккумуляторная'
    , 'INDUSTRIAL'
    , 'USUAL'
    , 'ITEM'
    , 3500.00
  from dual
  union all select
    'Профессиональный инструмент'
    , 'Набор инструментов'
    , 'INDUSTRIAL'
    , 'USUAL'
    , 'ITEM'
    , 9500.00
  from dual
  union all select
    'Мебель из Франции'
    , 'Гарнитур "Версаль"'
    , 'INDUSTRIAL'
    , 'QUARTER'
    , 'ITEM'
    , 250000.00
  from dual
  union all select
    'Мебель из Франции'
    , 'Гарнитур "Париж"'
    , 'INDUSTRIAL'
    , 'QUARTER'
    , 'ITEM'
    , 350000.00
  from dual
  union all select
    'Мебель из Франции'
    , 'Спальня "Людовик XIV"'
    , 'INDUSTRIAL'
    , 'QUARTER'
    , 'ITEM'
    , 510000.00
  from dual
  union all select
    'ООО "Все для дома"'
    , 'Мыло хозяйственное'
    , 'INDUSTRIAL'
    , 'USUAL'
    , 'ITEM'
    , 5.50
  from dual
  union all select
    'Компьютерный мир'
    , 'Компьютер для дома'
    , 'INDUSTRIAL'
    , 'MONTH'
    , 'ITEM'
    , 30000.00
  from dual
  union all select
    'Компьютерный мир'
    , 'Компьютер для офиса'
    , 'INDUSTRIAL'
    , 'MONTH'
    , 'ITEM'
    , 20000.00
  from dual
  ) s
  left outer join jrs_supplier sp
    on sp.supplier_name = s.supplier_name
where
  not exists
    (
    select
      null
    from
      jrs_goods t
    where
      t.supplier_id = sp.supplier_id
      and t.goods_name = s.goods_name
    )
/

commit
/
