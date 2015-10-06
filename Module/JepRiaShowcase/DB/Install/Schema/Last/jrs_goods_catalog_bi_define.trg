-- trigger: jrs_goods_catalog_bi_define
-- ������������� ����� ������� <jrs_goods_catalog> ��� ������� ������.
create or replace trigger jrs_goods_catalog_bi_define
  before insert
  on jrs_goods_catalog
  for each row
begin

  -- ���������� �������� ���������� �����
  if :new.goods_catalog_id is null then
    :new.goods_catalog_id := jrs_goods_catalog_seq.nextval;
  end if;

  -- Id ���������, ����������� ������
  if :new.operator_id is null then
    :new.operator_id := pkg_Operator.getCurrentUserId();
  end if;

  -- ���������� ���� ���������� ������
  if :new.date_ins is null then
    :new.date_ins := sysdate;
  end if;
end;
/
