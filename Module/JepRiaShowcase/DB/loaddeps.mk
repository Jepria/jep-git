#
# Зависимости при загрузке файлов в БД.
#
# Файлы в зависимостях должны указываться с дополнительным суффиксом:
# .$(lu)      - загрузка под первым пользователем
# .$(lu2)     - загрузка под вторым пользователем
# .$(lu3)     - загрузка под третьим пользователем
# ...         - ...
#
# Пример ( зависимость тела пакета pkg_TestModule от собственной спецификации
# и спецификации пакета pkg_TestModule2 при загрузке под первым пользователем):
#
# pkg_TestModule.pkb.$(lu): \
#   pkg_TestModule.pks.$(lu) \
#   pkg_TestModule2.pks.$(lu)
#
#
# Замечания:
# - в данном файле не должен использоваться символ табуляции ( вместо него для
#   форматирования нужно использовать пробелы), т.к. символ табуляции имеет
#   специальное значение для make и его случайное появление может привести к
#   труднообнаруживаемым ошибкам;
# - в случае, если последняя строка зависимости также завершается символом
#   экранирования ( обратной косой чертой), то после зависимости
#   должна идти как минимум одна пустая строка, иначе при загрузке будет
#   возникать ошибка "*** No rule to make target ` ', needed by ...";
# - файлы в зависимости должны указываться с путем относительно каталога DB
#   с учетом регистра, например "Install/Schema/Last/test_view.vw.$(lu): ...";
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


