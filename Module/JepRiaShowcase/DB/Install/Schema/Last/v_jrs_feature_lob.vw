-- view: v_jrs_feature_lob
-- ������ �����������: lob-����.
--
create or replace force view
  v_jrs_feature_lob
as
select
  -- SVN root: JEP/Module/JepRiaShowcase
  feature_id
  , description
from
  jrs_feature
/


comment on table v_jrs_feature_lob is
  '������ �����������: lob-���� [ SVN root: JEP/Module/JepRiaShowcase]'
/
comment on column v_jrs_feature_lob.feature_id is
  '������������� ������� �����������'
/
comment on column v_jrs_feature_lob.description is
  '�������� ������� �����������'
/

