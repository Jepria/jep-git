insert into
  jrs_unit
(
  unit_code
  , unit_short_name
  , unit_name
)
select
  s.unit_code
  , s.unit_short_name
  , s.unit_name
from
  (
  select
    'ITEM' as unit_code
    , 'Шт.' as unit_short_name
    , 'Штуки' as unit_name
  from dual
  union all select
    'KG', 'Kg', 'Килограммы'
  from dual
  union all select
    'L', 'L', 'Литры'
  from dual
  union all select
    'M', 'M', 'Метры'
  from dual
  union all select
    'M2', 'Кв.M', 'Квадратные метры'
  from dual
  ) s
where
  not exists
    (
    select
      null
    from
      jrs_unit t
    where
      t.unit_code = s.unit_code
    )
/

commit
/
