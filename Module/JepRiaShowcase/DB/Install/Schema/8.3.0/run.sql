-- script: Install/Schema/8.3.0/run.sql
-- ���������� �������� ����� �� ������ 8.3.0.
--
-- �������� ���������:
--  - � ������� <jrs_supplier> ��������� ���� phone_number, fax_number,
--    bank_bic, recipient_name, settlement_account;
--  - ������� ����� jrs_request_fk_goods_id � jrs_goods_fk_supplier_id
--    ����������� � ������ "on delete cascade";
--

-- ���������� ��������� ������������ ��� ��������
@oms-set-indexTablespace.sql

@oms-run recreate-fk.sql
@oms-run jrs_supplier.sql
