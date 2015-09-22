insert into
  jrs_goods_segment
(
  goods_segment_code
  , goods_segment_name
)
select
  s.goods_segment_code
  , s.goods_segment_name
from
  (
  select
    'EVERYDAY' as goods_segment_code
    , 'Повседневного спроса' as goods_segment_name
  from dual
  union all select
    'REST', 'Туризм и отдых'
  from dual
  union all select
    'HOME', 'Для дома и дачи'
  from dual
  union all select
    'AUTOMOBILE', 'Автозапчасти'
  from dual
  union all select
    'TOY', 'Игрушки'
  from dual
  ) s
where
  not exists
    (
    select
      null
    from
      jrs_goods_segment t
    where
      t.goods_segment_code = s.goods_segment_code
    )
/

commit
/
