insert into
  jrs_request_process
(
  request_id
  , process_comment
  , date_ins
)
select
  s.request_id
  , s.process_comment
  , s.date_ins
from
  (
  select
    rq.request_id
    , 'Комментарий №' || tt.column_value as process_comment
    , rq.request_date
      + ( to_number( tt.column_value) + mod( rq.request_id / 10, 11)) * 1.1
      as date_ins
  from
    jrs_request rq
    cross join table( cmn_string_table_t( '1','2','3')) tt
  where
    to_number( tt.column_value) <= mod( rq.request_id, 3) + 1
  ) s
where
  not exists
    (
    select
      null
    from
      jrs_request_process t
    where
      t.request_id = s.request_id
      and t.date_ins = s.date_ins
    )
order by
  s.date_ins
/

commit
/
