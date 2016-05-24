-- view: v_jrs_feature_job
-- Запрос функционала: lob-поля.
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
  'Запрос функционала: lob-поля [ SVN root: JEP/Module/JepRiaShowcase]'
/
comment on column v_jrs_feature_job.feature_id is
  'Идентификатор запроса функционала'
/
comment on column v_jrs_feature_job.description is
  'Описание запроса функционала'
/

