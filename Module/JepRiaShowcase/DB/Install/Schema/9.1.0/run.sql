-- script: Install/Schema/9.1.0/run.sql
-- Обновление объектов схемы до версии 9.1.0.
--
-- Основные изменения:
--  - добавление <jrs_feature>, <jrs_feature_seq>;
--

-- Определяем табличное пространство для индексов
@oms-set-indexTablespace.sql

@oms-run Install/Schema/Last/jrs_feature.tab
@oms-run Install/Schema/Last/jrs_feature.con
@oms-run Install/Schema/Last/jrs_feature_seq.sqs
