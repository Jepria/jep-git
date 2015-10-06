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
    , '������� ���������' as motivation_type_name
    , '����������� ������������� ����� �����������' as motivation_type_comment
  from dual
  union all select
    'QUARTER'
    , '����������� ���������'
    , '������ �� ����������� ������� ������ �� �������'
  from dual
  union all select
    'MONTH'
    , '������� ������ � �����'
    , '����� �� ���������� ������ ������ ������ � �����'
  from dual
  union all select
    'PERCENT'
    , '������� � ������'
    , '������� �������� � ������� �� �����'
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
