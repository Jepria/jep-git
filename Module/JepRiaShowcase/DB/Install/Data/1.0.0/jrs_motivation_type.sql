insert into
  jrs_motivation_type
(
  motivation_type_code
  , motivation_type_name
  , motivation_type_comment
)
select
  s.motivation_type_code
  , s.motivation_type_name
  , s.motivation_type_comment
from
  (
  select
    'USUAL' as motivation_type_code
    , 'Обычная мотивация' as motivation_type_name
    , 'Специальная мотивационная схема отсутствует' as motivation_type_comment
  from dual
  union all select
    'QUARTER'
    , 'Квартальная мотивация'
    , 'Бонусы по результатам продажи товара за квартал'
  from dual
  union all select
    'MONTH'
    , 'Уровень продаж в месяц'
    , 'Бонус за превышение некого уровня продаж в месяц'
  from dual
  union all select
    'PERCENT'
    , 'Процент с дохода'
    , 'Выплата процента с прибыли за товар'
  from dual
  ) s
where
  not exists
    (
    select
      null
    from
      jrs_motivation_type t
    where
      t.motivation_type_code = s.motivation_type_code
    )
/

commit
/
