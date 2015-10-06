-- trigger: jrs_request_process_bi_define
-- ������������� ����� ������� <jrs_request_process> ��� ������� ������.
create or replace trigger jrs_request_process_bi_define
  before insert
  on jrs_request_process
  for each row
begin

  -- ���������� �������� ���������� �����
  if :new.request_process_id is null then
    :new.request_process_id := jrs_request_process_seq.nextval;
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
