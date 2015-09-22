-- trigger: jrs_goods_segment_link_bi_def
-- Инициализация полей таблицы <jrs_goods_segment_link> при вставке записи.
create or replace trigger jrs_goods_segment_link_bi_def
  before insert
  on jrs_goods_segment_link
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
