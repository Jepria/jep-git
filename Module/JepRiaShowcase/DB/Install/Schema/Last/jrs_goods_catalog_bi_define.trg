-- trigger: jrs_goods_catalog_bi_define
-- Инициализация полей таблицы <jrs_goods_catalog> при вставке записи.
create or replace trigger jrs_goods_catalog_bi_define
  before insert
  on jrs_goods_catalog
  for each row
begin

  -- Определяем значение первичного ключа
  if :new.goods_catalog_id is null then
    :new.goods_catalog_id := jrs_goods_catalog_seq.nextval;
  end if;

  -- Id оператора, добавившего запись
  if :new.operator_id is null then
    :new.operator_id := pkg_Operator.getCurrentUserId();
  end if;

  -- Определяем дату добавления записи
  if :new.date_ins is null then
    :new.date_ins := sysdate;
  end if;
end;
/
