-- trigger: jrs_request_bi_define
-- ������������� ����� ������� <jrs_request> ��� ������� ������.
create or replace trigger jrs_request_bi_define
  before insert
  on jrs_request
  for each row
begin

  -- ���������� �������� ���������� �����
  if :new.request_id is null then
    :new.request_id := jrs_request_seq.nextval;
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
