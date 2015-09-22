#
# ����������� ��� �������� ������ � ��.
#
# ����� � ������������ ������ ����������� � �������������� ���������:
# .$(lu)      - �������� ��� ������ �������������
# .$(lu2)     - �������� ��� ������ �������������
# .$(lu3)     - �������� ��� ������� �������������
# ...         - ...
#
# ������ ( ����������� ���� ������ pkg_TestModule �� ����������� ������������
# � ������������ ������ pkg_TestModule2 ��� �������� ��� ������ �������������):
#
# pkg_TestModule.pkb.$(lu): \
#   pkg_TestModule.pks.$(lu) \
#   pkg_TestModule2.pks.$(lu)
#
#
# ���������:
# - � ������ ����� �� ������ �������������� ������ ��������� ( ������ ���� ���
#   �������������� ����� ������������ �������), �.�. ������ ��������� �����
#   ����������� �������� ��� make � ��� ��������� ��������� ����� �������� �
#   �������������������� �������;
# - � ������, ���� ��������� ������ ����������� ����� ����������� ��������
#   ������������� ( �������� ����� ������), �� ����� �����������
#   ������ ���� ��� ������� ���� ������ ������, ����� ��� �������� �����
#   ��������� ������ "*** No rule to make target ` ', needed by ...";
# - ����� � ����������� ������ ����������� � ����� ������������ �������� DB
#   � ������ ��������, �������� "Install/Schema/Last/test_view.vw.$(lu): ...";
#

pkg_JepRiaShowcase.pkb.$(lu): \
  pkg_JepRiaShowcase.pks.$(lu) \


Install/Data/1.0.0/jrs_goods.sql.$(lu): \
  Install/Data/1.0.0/jrs_supplier.sql.$(lu) \
  Install/Data/1.0.0/jrs_goods_type.sql.$(lu) \
  Install/Data/1.0.0/jrs_motivation_type.sql.$(lu) \
  Install/Data/1.0.0/jrs_unit.sql.$(lu) \


Install/Data/1.0.0/jrs_goods_catalog_link.sql.$(lu): \
  Install/Data/1.0.0/jrs_goods.sql.$(lu) \
  Install/Data/1.0.0/jrs_goods_catalog.sql.$(lu) \


Install/Data/1.0.0/jrs_goods_segment_link.sql.$(lu): \
  Install/Data/1.0.0/jrs_goods.sql.$(lu) \
  Install/Data/1.0.0/jrs_goods_segment.sql.$(lu) \


Install/Data/1.0.0/jrs_request.sql.$(lu): \
  Install/Data/1.0.0/jrs_shop.sql.$(lu) \
  Install/Data/1.0.0/jrs_request_status.sql.$(lu) \
  Install/Data/1.0.0/jrs_goods.sql.$(lu) \


Install/Data/1.0.0/jrs_request_process.sql.$(lu): \
  Install/Data/1.0.0/jrs_request.sql.$(lu) \


Install/Data/1.0.0/jrs_shop_goods.sql.$(lu): \
  Install/Data/1.0.0/jrs_shop.sql.$(lu) \
  Install/Data/1.0.0/jrs_goods.sql.$(lu) \


