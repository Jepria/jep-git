-- trigger: jrs_unit_bi_define
-- Инициализация полей таблицы <jrs_unit> при вставке записи.
create or replace trigger jrs_unit_bi_define
  before insert
  on jrs_unit
  for each row
begin

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
