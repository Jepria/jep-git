insert into
  jrs_goods
(
  supplier_id
  , goods_name
  , goods_type_code
  , motivation_type_code
  , unit_code
  , purchasing_price
)
select
  sp.supplier_id
  , s.goods_name
  , s.goods_type_code
  , s.motivation_type_code
  , s.unit_code
  , s.purchasing_price
from
  (
  select
    '����� ����' as supplier_name
    , '�������� "������"' as goods_name
    , 'INDUSTRIAL' as goods_type_code
    , 'USUAL' as motivation_type_code
    , 'ITEM' as unit_code
    , 45500.00 as purchasing_price
  from dual
  union all select
    '����� ����'
    , '������ "������"'
    , 'INDUSTRIAL'
    , 'USUAL'
    , 'ITEM'
    , 95500.59 as purchasing_price
  from dual
  union all select
    '����� ����'
    , '������ "�������", 1 �.'
    , 'FOOD'
    , 'USUAL'
    , 'ITEM'
    , 20.11
  from dual
  union all select
    '������������ "�����"'
    , '������ �.�. "����� � ������� � �������� ����"'
    , 'BOOK'
    , 'USUAL'
    , 'ITEM'
    , 890.00
  from dual
  union all select
    '������������ "�����"'
    , '������ �.�. "��� ������� ���"'
    , 'BOOK'
    , 'USUAL'
    , 'ITEM'
    , 40.10
  from dual
  union all select
    '��� "����������� �����"'
    , 'Diane Hopkins, Pauline Cullen "Cambridge Gram for IELTS"'
    , 'BOOK'
    , 'MONTH'
    , 'ITEM'
    , 1807.00
  from dual
  union all select
    '��� "����������� �����"'
    , '������� ����� "English Grammar in Use with Answers"'
    , 'BOOK'
    , 'PERCENT'
    , 'ITEM'
    , 1460.00
  from dual
  union all select
    '��� "����������� �����"'
    , 'Liz and John Soars "New Headway: Student''s Book"'
    , 'BOOK'
    , 'PERCENT'
    , 'ITEM'
    , 1280.00
  from dual
  union all select
    '"�������� �������", ���'
    , '������ "����������", 1 �.'
    , 'FOOD'
    , 'USUAL'
    , 'ITEM'
    , 15.05
  from dual
  union all select
    '"�������� �������", ���'
    , '������ ���������'
    , 'FOOD'
    , 'USUAL'
    , 'L'
    , 15.05
  from dual
  union all select
    '"�������� �������", ���'
    , '���'
    , 'FOOD'
    , 'USUAL'
    , 'KG'
    , 35.05
  from dual
  union all select
    '����������'
    , '������������'
    , 'INDUSTRIAL'
    , 'QUARTER'
    , 'ITEM'
    , 500.00
  from dual
  union all select
    '����������'
    , '������������ � ����������'
    , 'INDUSTRIAL'
    , 'QUARTER'
    , 'ITEM'
    , 550.00
  from dual
  union all select
    '������'
    , '����������������'
    , 'INDUSTRIAL'
    , 'MONTH'
    , 'ITEM'
    , 400.00
  from dual
  union all select
    '������'
    , '���������������� � �������� ��������'
    , 'INDUSTRIAL'
    , 'MONTH'
    , 'ITEM'
    , 450.00
  from dual
  union all select
    '������'
    , '���������������� � �������� �������� �� ��������� ��������'
    , 'INDUSTRIAL'
    , 'MONTH'
    , 'ITEM'
    , 550.00
  from dual
  union all select
    '������'
    , '������ ���������'
    , 'FOOD'
    , 'USUAL'
    , 'L'
    , 18.15
  from dual
  union all select
    '���������������� ����������'
    , '�����'
    , 'INDUSTRIAL'
    , 'USUAL'
    , 'ITEM'
    , 1500.00
  from dual
  union all select
    '���������������� ����������'
    , '����� ��������������'
    , 'INDUSTRIAL'
    , 'USUAL'
    , 'ITEM'
    , 3500.00
  from dual
  union all select
    '���������������� ����������'
    , '����� ������������'
    , 'INDUSTRIAL'
    , 'USUAL'
    , 'ITEM'
    , 9500.00
  from dual
  union all select
    '������ �� �������'
    , '�������� "�������"'
    , 'INDUSTRIAL'
    , 'QUARTER'
    , 'ITEM'
    , 250000.00
  from dual
  union all select
    '������ �� �������'
    , '�������� "�����"'
    , 'INDUSTRIAL'
    , 'QUARTER'
    , 'ITEM'
    , 350000.00
  from dual
  union all select
    '������ �� �������'
    , '������� "������� XIV"'
    , 'INDUSTRIAL'
    , 'QUARTER'
    , 'ITEM'
    , 510000.00
  from dual
  union all select
    '��� "��� ��� ����"'
    , '���� �������������'
    , 'INDUSTRIAL'
    , 'USUAL'
    , 'ITEM'
    , 5.50
  from dual
  union all select
    '������������ ���'
    , '��������� ��� ����'
    , 'INDUSTRIAL'
    , 'MONTH'
    , 'ITEM'
    , 30000.00
  from dual
  union all select
    '������������ ���'
    , '��������� ��� �����'
    , 'INDUSTRIAL'
    , 'MONTH'
    , 'ITEM'
    , 20000.00
  from dual
  ) s
  left outer join jrs_supplier sp
    on sp.supplier_name = s.supplier_name
where
  not exists
    (
    select
      null
    from
      jrs_goods t
    where
      t.supplier_id = sp.supplier_id
      and t.goods_name = s.goods_name
    )
/

commit
/
