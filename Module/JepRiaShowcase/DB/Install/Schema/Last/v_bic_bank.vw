-- view: v_bic_bank
-- ���������� ���.
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
  '���������� ��� [ SVN root: JEP/Module/JepRiaShowcase]'
/
comment on column v_bic_bank.bic is
  '���������� ����������������� ��� ( ���)'
/
comment on column v_bic_bank.bankname is
  '������������ �����'
/
comment on column v_bic_bank.ks is
  '�������'
/
