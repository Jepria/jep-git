-- script: Install/Schema/Last/run.sql
-- Выполняет установку последней версии объектов схемы.


-- Определяем табличное пространство для индексов
@oms-set-indexTablespace.sql


-- Таблицы

@oms-run jrs_feature.tab
@oms-run jrs_goods.tab
@oms-run jrs_goods_catalog.tab
@oms-run jrs_goods_catalog_link.tab
@oms-run jrs_goods_segment.tab
@oms-run jrs_goods_segment_link.tab
@oms-run jrs_goods_type.tab
@oms-run jrs_motivation_type.tab
@oms-run jrs_request.tab
@oms-run jrs_request_process.tab
@oms-run jrs_request_status.tab
@oms-run jrs_shop.tab
@oms-run jrs_shop_goods.tab
@oms-run jrs_supplier.tab
@oms-run jrs_unit.tab


-- Outline-ограничения целостности

@oms-run jrs_feature.con
@oms-run jrs_goods.con
@oms-run jrs_goods_catalog.con
@oms-run jrs_goods_catalog_link.con
@oms-run jrs_goods_segment.con
@oms-run jrs_goods_segment_link.con
@oms-run jrs_goods_type.con
@oms-run jrs_motivation_type.con
@oms-run jrs_request.con
@oms-run jrs_request_process.con
@oms-run jrs_request_status.con
@oms-run jrs_shop.con
@oms-run jrs_shop_goods.con
@oms-run jrs_supplier.con
@oms-run jrs_unit.con


-- Последовательности

@oms-run jrs_feature_seq.sqs
@oms-run jrs_goods_catalog_seq.sqs
@oms-run jrs_goods_seq.sqs
@oms-run jrs_request_process_seq.sqs
@oms-run jrs_request_seq.sqs
@oms-run jrs_shop_goods_seq.sqs
@oms-run jrs_shop_seq.sqs
@oms-run jrs_supplier_seq.sqs
