insert into
  jrs_supplier
(
  supplier_name
  , contract_finish_date
  , exclusive_supplier_flag
  , supplier_description
)
select
  s.supplier_name
  , s.contract_finish_date
  , s.exclusive_supplier_flag
  , s.supplier_description
from
  (
  select
    '����� ����' as supplier_name
    , DATE '2014-02-01' as contract_finish_date
    , 0 as exclusive_supplier_flag
    , '������������� ���������' as supplier_description
  from dual
  union all select
    '������������ "�����"'
    , DATE '2019-05-14'
    , 0
    , '����������� �������� ���������'
  from dual
  union all select
    '��� "����������� �����"'
    , DATE '2015-01-01'
    , 0
    , '�������� ��������� ���������� �����������'
  from dual
  union all select
    '"�������� �������", ���'
    , DATE '2015-01-11'
    , 1
    , '����������������� ������'
  from dual
  union all select
    '����������'
    , DATE '2014-08-01'
    , 0
    , '�������'
  from dual
  union all select
    '������'
    , DATE '2018-01-01'
    , 1
    , '������������� ���������'
  from dual
  union all select
    '���������������� ����������'
    , DATE '2015-05-01'
    , 0
    , ''
  from dual
  union all select
    '������ �� �������'
    , DATE '2017-01-01'
    , 1
    , ''
  from dual
  union all select
    '��� "��� ��� ����"'
    , DATE '2017-01-01'
    , 0
    , '�������, ��������� ������� ������'
  from dual
  union all select
    '������������ ���'
    , DATE '2018-04-01'
    , 0
    , ''
  from dual
  ) s
where
  not exists
    (
    select
      null
    from
      jrs_supplier t
    where
      t.supplier_name = s.supplier_name
    )
/

commit
/
