-- script: Install/Grant/Last/run.sql
-- Выдает права на использование модуля.
--
-- Параметры:
-- toUserName                  - имя пользователя, которому выдаются права
--
-- Замечания:
--  - скрипт запускается под пользователем, которому принадлежат объекты модуля
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
