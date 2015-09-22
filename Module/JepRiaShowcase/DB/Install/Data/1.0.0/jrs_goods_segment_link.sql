insert into
  jrs_goods_segment_link
(
  goods_id
  , goods_segment_code
)
select
  gd.goods_id
  , s.goods_segment_code
from
  (
select
  b.*
  , bt.column_value as goods_segment_code
from
  (
  select
    '����� ����' as supplier_name
    , '�������� "������"' as goods_name
    , 'HOME' as goods_segment_code_list
  from dual
  union all select
    '����� ����'
    , '������ "������"'
    , 'HOME'
  from dual
  union all select
    '����� ����'
    , '������ "�������", 1 �.'
    , 'EVERYDAY'
  from dual
  union all select
    '������������ "�����"'
    , '������ �.�. "����� � ������� � �������� ����"'
    , 'HOME,REST'
  from dual
  union all select
    '������������ "�����"'
    , '������ �.�. "��� ������� ���"'
    , 'HOME,REST'
  from dual
  union all select
    '��� "����������� �����"'
    , 'Diane Hopkins, Pauline Cullen "Cambridge Gram for IELTS"'
    , 'HOME,REST'
  from dual
  union all select
    '��� "����������� �����"'
    , '������� ����� "English Grammar in Use with Answers"'
    , 'HOME,REST'
  from dual
  union all select
    '��� "����������� �����"'
    , 'Liz and John Soars "New Headway: Student''s Book"'
    , 'HOME,REST'
  from dual
  union all select
    '"�������� �������", ���'
    , '������ "����������", 1 �.'
    , 'EVERYDAY'
  from dual
  union all select
    '"�������� �������", ���'
    , '������ ���������'
    , 'EVERYDAY'
  from dual
  union all select
    '"�������� �������", ���'
    , '���'
    , 'EVERYDAY'
  from dual
  union all select
    '����������'
    , '������������'
    , 'EVERYDAY,HOME,TOY'
  from dual
  union all select
    '����������'
    , '������������ � ����������'
    , 'EVERYDAY,HOME,TOY'
  from dual
  union all select
    '������'
    , '����������������'
    , 'EVERYDAY,HOME,TOY'
  from dual
  union all select
    '������'
    , '���������������� � �������� ��������'
    , 'EVERYDAY,HOME,TOY'
  from dual
  union all select
    '������'
    , '���������������� � �������� �������� �� ��������� ��������'
    , 'EVERYDAY,HOME,TOY'
  from dual
  union all select
    '���������������� ����������'
    , '�����'
    , 'HOME'
  from dual
  union all select
    '���������������� ����������'
    , '����� ��������������'
    , 'HOME'
  from dual
  union all select
    '���������������� ����������'
    , '����� ������������'
    , 'HOME'
  from dual
  union all select
    '������ �� �������'
    , '�������� "�������"'
    , 'HOME'
  from dual
  union all select
    '������ �� �������'
    , '�������� "�����"'
    , 'HOME'
  from dual
  union all select
    '������ �� �������'
    , '������� "������� XIV"'
    , 'HOME'
  from dual
  union all select
    '��� "��� ��� ����"'
    , '���� �������������'
    , 'EVERYDAY,HOME'
  from dual
  union all select
    '������������ ���'
    , '��������� ��� ����'
    , 'HOME,REST,TOY'
  from dual
  union all select
    '������������ ���'
    , '��������� ��� �����'
    , 'EVERYDAY'
  from dual
  ) b
  , table( pkg_Common.split( b.goods_segment_code_list, ',')) bt
  ) s
  left outer join jrs_supplier sp
    on sp.supplier_name = s.supplier_name
  left outer join jrs_goods gd
    on gd.supplier_id = sp.supplier_id
      and gd.goods_name = s.goods_name
where
  not exists
    (
    select
      null
    from
      jrs_goods_segment_link t
    where
      t.goods_id = gd.goods_id
      and t.goods_segment_code = s.goods_segment_code
    )
/

commit
/
