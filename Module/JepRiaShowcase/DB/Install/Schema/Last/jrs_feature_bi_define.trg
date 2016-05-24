-- trigger: jrs_feature_bi_define
-- ������������� ����� ������� <jrs_feature> ��� ������� ������.
create or replace trigger jrs_feature_bi_define
  before insert
  on jrs_feature
  for each row
begin

  -- ���������� �������� ���������� �����
  if :new.feature_id is null then
    select
      jrs_feature_seq.nextval
    into :new.feature_id
    from
      dual
    ;
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
