alter table
  jrs_request
drop constraint
  jrs_request_fk_goods_id
/

alter table
  jrs_request
add constraint
  jrs_request_fk_goods_id
foreign key
  ( goods_id)
references
  jrs_goods ( goods_id)
on delete cascade
/



alter table
  jrs_goods
drop constraint
  jrs_goods_fk_supplier_id
/

alter table
  jrs_goods
add constraint
  jrs_goods_fk_supplier_id
foreign key
  ( supplier_id)
references
  jrs_supplier ( supplier_id)
on delete cascade
/
