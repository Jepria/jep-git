-- script: Install/Schema/9.1.0/run.sql
-- ���������� �������� ����� �� ������ 9.1.0.
--
-- �������� ���������:
--  - ���������� <jrs_feature>, <jrs_feature_seq>;
--

-- ���������� ��������� ������������ ��� ��������
@oms-set-indexTablespace.sql

@oms-run Install/Schema/Last/jrs_feature.tab
@oms-run Install/Schema/Last/jrs_feature.con
@oms-run Install/Schema/Last/jrs_feature_seq.sqs
