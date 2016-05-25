-- script: Install/Grant/Last/run.sql
-- ������ ����� �� ������������� ������.
--
-- ���������:
-- toUserName                  - ��� ������������, �������� �������� �����
--
-- ���������:
--  - ������ ����������� ��� �������������, �������� ����������� ������� ������
--   ;
--

define toUserName = "&1"



grant
  select, update
on
  v_jrs_feature_job
to
  &toUserName
/
create or replace synonym
  &toUserName..v_jrs_feature_job
for
  v_jrs_feature_job
/

grant execute on
  pkg_JepRiaShowcase
to
  &toUserName
/
create or replace synonym
  &toUserName..pkg_JepRiaShowcase
for
  pkg_JepRiaShowcase
/



undefine toUserName
