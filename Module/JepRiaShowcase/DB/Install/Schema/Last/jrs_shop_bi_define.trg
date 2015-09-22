-- trigger: jrs_shop_bi_define
-- ������������� ����� ������� <jrs_shop> ��� ������� ������.
create or replace trigger jrs_shop_bi_define
  before insert
  on jrs_shop
  for each row
begin

  -- ���������� �������� ���������� �����
  if :new.shop_id is null then
    :new.shop_id := jrs_shop_seq.nextval;
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
