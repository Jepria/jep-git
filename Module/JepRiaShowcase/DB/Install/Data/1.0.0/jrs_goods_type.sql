insert into
  jrs_goods_type
(
  goods_type_code
  , goods_type_name
)
select
  s.goods_type_code
  , s.goods_type_name
from
  (
  select
    'FOOD' as goods_type_code
    , 'Продукты питания' as goods_type_name
  from dual
  union all select
    'INDUSTRIAL', 'Промышленные товары'
  from dual
  union all select
    'BOOK', 'Печатная продукция'
  from dual
  ) s
where
  not exists
    (
    select
      null
    from
      jrs_goods_type t
    where
      t.goods_type_code = s.goods_type_code
    )
/

commit
/
