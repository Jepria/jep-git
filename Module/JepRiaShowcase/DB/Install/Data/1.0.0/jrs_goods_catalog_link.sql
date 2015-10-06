insert into
  jrs_goods_catalog_link
(
  goods_id
  , goods_catalog_id
)
select
  gd.goods_id
  , s.goods_catalog_id
from
  (
select
  b.*
  , (
    select
      t.goods_catalog_id
    from
      (
      select
        gc.goods_catalog_id
        , ltrim( sys_connect_by_path( gc.goods_catalog_name, ' / '), ' /')
          as catalog_path
      from
        jrs_goods_catalog gc
      where
        level = 3
      connect by
        prior gc.goods_catalog_id = gc.parent_goods_catalog_id
      start with
        gc.parent_goods_catalog_id is null
      ) t
    where
      t.catalog_path = bt.column_value
    )
    as goods_catalog_id
from
  (
  select
    '����� ����' as supplier_name
    , '�������� "������"' as goods_name
    , cmn_string_table_t(
        '��� ��� ���� / ������ / ��������'
      )
      as catalog_path_list
  from dual
  union all select
    '����� ����'
    , '������ "������"'
    , cmn_string_table_t(
        '��� ��� ���� / ������ / ��������'
      )
  from dual
  union all select
    '����� ����'
    , '������ "�������", 1 �.'
    , cmn_string_table_t(
        '�������� ������� / �������� �������� / ������'
      )
  from dual
  union all select
    '������������ "�����"'
    , '������ �.�. "����� � ������� � �������� ����"'
    , cmn_string_table_t(
        '���� �� ���� / ����� / ������� �� �������'
      )
  from dual
  union all select
    '������������ "�����"'
    , '������ �.�. "��� ������� ���"'
    , cmn_string_table_t(
        '�������� ����� / �����, ����, ������ / ����� � ������'
        , '�������� ����� / ����� �� ������� / ������ ��� �������'
        , '����� / ���������������� ���������� / �������� ����'
      )
  from dual
  union all select
    '��� "����������� �����"'
    , 'Diane Hopkins, Pauline Cullen "Cambridge Gram for IELTS"'
    , cmn_string_table_t(
        '����� / ���������������� ���������� / �������� ������ ����'
        , '����� / ������� ���������� / ���������� � ������������'
        , '����� / ������� ���������� / ��������� � ����������'
        , '����� / ���������� �� ����������� ������ / ���������������� ����������'
      )
  from dual
  union all select
    '��� "����������� �����"'
    , '������� ����� "English Grammar in Use with Answers"'
    , cmn_string_table_t(
        '����� / ���������������� ���������� / �������� ������ ����'
        , '����� / ������� ���������� / ���������� � ������������'
        , '����� / ���������� �� ����������� ������ / ���������������� ����������'
      )
  from dual
  union all select
    '��� "����������� �����"'
    , 'Liz and John Soars "New Headway: Student''s Book"'
    , cmn_string_table_t(
        '����� / ���������������� ���������� / �������� ������ ����'
        , '����� / ������� ���������� / ���������� � ������������'
        , '����� / ���������� �� ����������� ������ / ���������������� ����������'
      )
  from dual
  union all select
    '"�������� �������", ���'
    , '������ "����������", 1 �.'
    , cmn_string_table_t(
        '�������� ������� / �������� �������� / ������'
      )
  from dual
  union all select
    '"�������� �������", ���'
    , '������ ���������'
    , cmn_string_table_t(
        '�������� ������� / �������� �������� / ������'
      )
  from dual
  union all select
    '"�������� �������", ���'
    , '���'
    , null
  from dual
  union all select
    '����������'
    , '������������'
    , cmn_string_table_t(
        '���� �� ���� / �������� �������������� / �������� ��������������'
      )
  from dual
  union all select
    '����������'
    , '������������ � ����������'
    , cmn_string_table_t(
        '���� �� ���� / �������� �������������� / �������� ��������������'
      )
  from dual
  union all select
    '������'
    , '����������������'
    , cmn_string_table_t(
        '���� �� ���� / �������� �������������� / �������� ��������������'
        , '�������� ����� / ������ / ������ �� ���������'
      )
  from dual
  union all select
    '������'
    , '���������������� � �������� ��������'
    , cmn_string_table_t(
        '���� �� ���� / �������� �������������� / �������� ��������������'
        , '�������� ����� / ������ / ������ �� ���������'
      )
  from dual
  union all select
    '������'
    , '���������������� � �������� �������� �� ��������� ��������'
    , cmn_string_table_t(
        '���� �� ���� / �������� �������������� / �������� ��������������'
        , '�������� ����� / ������ / ������ �� ���������'
      )
  from dual
  union all select
    '���������������� ����������'
    , '�����'
    , cmn_string_table_t(
        '���� �� ���� / ����������� / �����������������'
      )
  from dual
  union all select
    '���������������� ����������'
    , '����� ��������������'
    , cmn_string_table_t(
        '���� �� ���� / ����������� / �����������������'
      )
  from dual
  union all select
    '���������������� ����������'
    , '����� ������������'
    , cmn_string_table_t(
        '���� �� ���� / ������ / �����������'
      )
  from dual
  union all select
    '������ �� �������'
    , '�������� "�������"'
    , cmn_string_table_t(
        '��� ��� ���� / ������ / ��������'
      )
  from dual
  union all select
    '������ �� �������'
    , '�������� "�����"'
    , cmn_string_table_t(
        '��� ��� ���� / ������ / ��������'
      )
  from dual
  union all select
    '������ �� �������'
    , '������� "������� XIV"'
    , cmn_string_table_t(
        '��� ��� ���� / ������ / ��������'
      )
  from dual
  union all select
    '��� "��� ��� ����"'
    , '���� �������������'
    , cmn_string_table_t(
        '���� �� ���� / ��������� / �������'
        , '�������� ����� / �� ��� ����������� / ���� �� ����� � �����'
      )
  from dual
  union all select
    '������������ ���'
    , '��������� ��� ����'
    , cmn_string_table_t(
        '�������� ����� / �������� ����� � ������ / ����������� ��� �����'
      )
  from dual
  union all select
    '������������ ���'
    , '��������� ��� �����'
    , null
  from dual
  ) b
  , table( b.catalog_path_list) bt
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
      jrs_goods_catalog_link t
    where
      t.goods_id = gd.goods_id
      and t.goods_catalog_id = s.goods_catalog_id
    )
/

commit
/
