-- script: Install/Schema/8.6.0/run.sql
-- Обновление объектов схемы до версии 8.6.0.
--
-- Основные изменения:
--  - в таблицу <jrs_supplier> добавлено поле privilege_supplier_flag;
--

@oms-run jrs_supplier.sql
