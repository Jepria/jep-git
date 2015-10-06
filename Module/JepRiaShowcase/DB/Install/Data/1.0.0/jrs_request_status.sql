insert into
  jrs_request_status
(
  request_status_code
  , request_status_name
)
select
  s.request_status_code
  , s.request_status_name
from
  (
  select
    'NEW' as request_status_code
    , 'Новый запрос' as request_status_name
  from dual
  union all select
    'PROCESSING', 'Взят в обработку'
  from dual
  union all select
    'CHOICE_SUP', 'Выбор поставщика'
  from dual
  union all select
    'ORDER', 'Заказ у поставщика'
  from dual
  union all select
    'DELIVERY', 'Поставка'
  from dual
  union all select
    'COMPLETED', 'Запрос выполнен'
  from dual
  union all select
    'REJECTED', 'Запрос отклонен'
  from dual
  ) s
where
  not exists
    (
    select
      null
    from
      jrs_request_status t
    where
      t.request_status_code = s.request_status_code
    )
/

commit
/
