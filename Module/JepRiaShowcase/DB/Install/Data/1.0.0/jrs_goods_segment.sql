insert into
  jrs_goods_segment
(
  goods_segment_code
  , goods_segment_name
)
select
  s.goods_segment_code
  , s.goods_segment_name
from
  (
  select
    'EVERYDAY' as goods_segment_code
    , '������������� ������' as goods_segment_name
  from dual
  union all select
    'REST', '������ � �����'
  from dual
  union all select
    'HOME', '��� ���� � ����'
  from dual
  union all select
    'AUTOMOBILE', '������������'
  from dual
  union all select
    'TOY', '�������'
  from dual
  ) s
where
  not exists
    (
    select
      null
    from
      jrs_goods_segment t
    where
      t.goods_segment_code = s.goods_segment_code
    )
/

commit
/
