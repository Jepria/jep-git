alter table
  jrs_supplier
add (
  phone_number                  varchar2(20)
  , fax_number                    varchar2(20)
  , bank_bic                      varchar2(9)
  , recipient_name                varchar2(150)
  , settlement_account            varchar2(20)
)
/



comment on column jrs_supplier.phone_number is
  'Телефон'
/
comment on column jrs_supplier.fax_number is
  'Факс'
/
comment on column jrs_supplier.bank_bic is
  'Банк'
/
comment on column jrs_supplier.recipient_name is
  'Получатель'
/
comment on column jrs_supplier.settlement_account is
  'Расчетный счет'
/
