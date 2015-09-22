-- trigger: jrs_request_status_bi_define
-- ������������� ����� ������� <jrs_request_status> ��� ������� ������.
create or replace trigger jrs_request_status_bi_define
  before insert
  on jrs_request_status
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
