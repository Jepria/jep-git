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
  v_jrs_feature_lob
to
  &toUserName
/
create or replace synonym
  &toUserName..v_jrs_feature_lob
for
  v_jrs_feature_lob
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
