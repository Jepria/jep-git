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
    , '��.' as unit_short_name
    , '�����' as unit_name
  from dual
  union all select
    'KG', 'Kg', '����������'
  from dual
  union all select
    'L', 'L', '�����'
  from dual
  union all select
    'M', 'M', '�����'
  from dual
  union all select
    'M2', '��.M', '���������� �����'
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
