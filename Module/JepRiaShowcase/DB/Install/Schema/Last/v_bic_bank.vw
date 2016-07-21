-- view: v_bic_bank
-- Справочник БИК.
--
create or replace force view
  v_bic_bank
as
select
  -- SVN root: JEP/Module/JepRiaShowcase
  d.bic
  , d.bankname
  , d.ks
from
  jrs_bic_bank d
/



comment on table v_bic_bank is
  'Справочник БИК [ SVN root: JEP/Module/JepRiaShowcase]'
/
comment on column v_bic_bank.bic is
  'Банковский идентификационный код ( БИК)'
/
comment on column v_bic_bank.bankname is
  'Наименование банка'
/
comment on column v_bic_bank.ks is
  'Корсчет'
/
