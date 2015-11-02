alter table
  jrs_supplier
add (
  privilege_supplier_flag       number(1)           default 0       not null
  , constraint jrs_supplier_ck_priv_supp_flag check
    ( privilege_supplier_flag in ( 0, 1))
)
/


comment on column jrs_supplier.privilege_supplier_flag is
  'Привилегированный поставщик ( 1 да, 0 нет)'
/
