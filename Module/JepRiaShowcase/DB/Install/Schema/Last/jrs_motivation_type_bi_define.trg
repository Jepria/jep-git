-- trigger: jrs_motivation_type_bi_define
-- ������������� ����� ������� <jrs_motivation_type> ��� ������� ������.
create or replace trigger jrs_motivation_type_bi_define
  before insert
  on jrs_motivation_type
  for each row
begin

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
