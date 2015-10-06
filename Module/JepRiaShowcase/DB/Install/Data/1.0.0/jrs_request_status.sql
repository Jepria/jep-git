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
    , '����� ������' as request_status_name
  from dual
  union all select
    'PROCESSING', '���� � ���������'
  from dual
  union all select
    'CHOICE_SUP', '����� ����������'
  from dual
  union all select
    'ORDER', '����� � ����������'
  from dual
  union all select
    'DELIVERY', '��������'
  from dual
  union all select
    'COMPLETED', '������ ��������'
  from dual
  union all select
    'REJECTED', '������ ��������'
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
