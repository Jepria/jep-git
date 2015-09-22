-- trigger: jrs_shop_goods_bi_define
-- ������������� ����� ������� <jrs_shop_goods> ��� ������� ������.
create or replace trigger jrs_shop_goods_bi_define
  before insert
  on jrs_shop_goods
  for each row
begin

  -- ���������� �������� ���������� �����
  if :new.shop_goods_id is null then
    :new.shop_goods_id := jrs_shop_goods_seq.nextval;
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
