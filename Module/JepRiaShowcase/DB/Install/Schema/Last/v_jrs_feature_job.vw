-- view: v_jrs_feature_job
-- ������ �����������: lob-����.
--
create or replace force view
  v_jrs_feature_job
as
select
  -- SVN root: JEP/Module/JepRiaShowcase
  to_number( null) as feature_id
  , to_clob( null) as description
from
  dual
/


comment on table v_jrs_feature_job is
  '������ �����������: lob-���� [ SVN root: JEP/Module/JepRiaShowcase]'
/
comment on column v_jrs_feature_job.feature_id is
  '������������� ������� �����������'
/
comment on column v_jrs_feature_job.description is
  '�������� ������� �����������'
/

