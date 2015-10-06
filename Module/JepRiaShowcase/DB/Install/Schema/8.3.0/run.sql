-- script: Install/Schema/8.3.0/run.sql
-- Обновление объектов схемы до версии 8.3.0.
--
-- Основные изменения:
--  - в таблицу <jrs_supplier> добавлены поля phone_number, fax_number,
--    bank_bic, recipient_name, settlement_account;
--  - внешние ключи jrs_request_fk_goods_id и jrs_goods_fk_supplier_id
--    пересозданы с опцией "on delete cascade";
--

-- Определяем табличное пространство для индексов
@oms-set-indexTablespace.sql

@oms-run recreate-fk.sql
@oms-run jrs_supplier.sql
