-- trigger: jrs_supplier_bi_define
-- Инициализация полей таблицы <jrs_supplier> при вставке записи.
create or replace trigger jrs_supplier_bi_define
  before insert
  on jrs_supplier
  for each row
begin

  -- Определяем значение первичного ключа
  if :new.supplier_id is null then
    :new.supplier_id := jrs_supplier_seq.nextval;
  end if;

  -- Id оператора, добавившего запись
  if :new.operator_id is null then
    :new.operator_id := pkg_Operator.getCurrentUserId();
  end if;

  -- Определяем дату добавления записи
  if :new.date_ins is null then
    :new.date_ins := sysdate;
  end if;

  -- Значения по умолчанию
  if :new.exclusive_supplier_flag is null then
    :new.exclusive_supplier_flag := 0;
  end if;
end;
/
