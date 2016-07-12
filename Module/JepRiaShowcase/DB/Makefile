# makefile: Сборка

# Каталог сборки OMS-модуля
omsModuleDir = OmsModule

# Включаем стандартные функции сборки проекта
include $(omsModuleDir)/init.mk

# Абстрактные цели
.PHONY:                 \
  all                   \
  clean                 \
  set-version           \
  show-oms-version      \
  update-oms-version    \
  gendoc                \
  gendoc-clean          \
  gendoc-menu           \
  grant-exec            \
  grant-save-info       \
  grant                 \
  load                  \
  load-clean            \
  install               \
  install-after         \
  install-batch         \
  install-before        \
  install-data          \
  install-load          \
  install-save-info     \
  install-schema        \
  install-test          \
  test                  \
  uninstall             \
  uninstall-after       \
  uninstall-before      \
  uninstall-data        \
  uninstall-load        \
  uninstall-save-info   \
  uninstall-schema      \



#
# group: Общие цели
#

# target: all
# Целиком собирает проект.

all: all.oms



# target: clean
# Удаляет файлы, созданные при сборке.

clean: clean.oms



#
# group: Версия модуля
#

# build var: MODULE_VERSION
# Номер текущей версии модуля.
# Должен быть явно задан при использовании цели <set-version>.
# Если не задан явно, то при выполнении целей, работающих с БД, автоматически
# определяется по значению тэга version в map-файле ( Doc/map.xml). При этом
# в случае, если в <INSTALL_VERSION> указана более старшая версия, чем в
# map-файле, и она используется в модуле ( есть соответствующие подкаталоги в
# ветке DB/Install или документирована в DB/Doc/version.txt), то версия
# модуля автоматически устанавливается в значение, указанное в
# <INSTALL_VERSION> ( при этом вносятся изменения файлы модуля, аналогично
# выполнению цели <set-version>).
#
MODULE_VERSION =



# target: set-version
# Устанавливает номер текущей версии модуля.
# Новый номер версии указывается в параметре <MODULE_VERSION>. При установке
# номера обновляется значение тэга version в map-файле ( Doc/map.xml) и тэга
# SubTitle в меню документации ( DB/OmsModule/NaturalDocs/Menu.txt).  Новый
# номер будет отображаться в меню документации после ее обновления ( например,
# командой 'make gendoc').
#

set-version: set-version.oms



#
# group: Версия OMS-файлов
#

# target: show-oms-version
# Показывает версию OMS-файлов, входящих в состав модуля.
#
# Если же нужно проверить актуальность установленной версии
# системы OMS модулю, что чаще необходимо перед установкой модуля,
# можно выполнить команду:
# (code)
#    oms-load --version
# (end code)
#
# В случае, если версия ( правка, т.е. ревизия) OMS младше,
# чем версия OMS-файлов модуля, для работы с модулем
# рекомендуется обновить ( установить) OMS.
#
# Если версия ( правка) OMS старше, для разработки
# рекомендуется обновить версию OMS-файлов, входящих в состав
# модуля с помощью <oms-update-module>.
#
# Пример:
# (code)
#
# LysyonokE@msk-dit-1432-1 /cygdrive/c/svn/DWH/Module/CredilogicMView/Trunk
# $ make show-oms-version
# make[1]: Entering directory `/cygdrive/c/svn/DWH/Module/CredilogicMView/Trunk/DB'
# OMS files version: 1.5.2 ( rev. 638, 2010-08-12)
# make[1]: Leaving directory `/cygdrive/c/svn/DWH/Module/CredilogicMView/Trunk/DB'
#
# LysyonokE@msk-dit-1432-1 /cygdrive/c/svn/DWH/Module/CredilogicMView/Trunk
# $ oms-load --version
# oms-load (OMS) 1.5.2
# SVN Version Information:
#  Module root     : Oracle/Module/OraMakeSystem
#  File revision   : 648
#  File change date: 2010-08-13 15:32:51 +0400
# (end code)
#
# Версия OMS старше, чем версия файлов модуля CredilogicMView.
# Модуль можно устанавливать. Но для разработки
# рекомендуется обновить версию OMS-файлов модуля.

show-oms-version: show-oms-version.oms



# target: update-oms-version
# Обновляет OMS-файлы, входящие в состав модуля.

update-oms-version: update-oms-version.oms



#
# group: Генерация документации
#

# build var: GENDOC_DB_DIR
# Каталог для генерации документации по DB-части проекта.
GENDOC_DB_DIR      = ../Doc/DB/AutoGen

# build var: GENDOC_DB_FLAGS
# Дополнительные параметры генерации документации.
GENDOC_DB_FLAGS    =


# target: gendoc
# Генерирует документацию.

gendoc: gendoc.oms



# target: gendoc-clean
# Удаляет временные файлы ( кэш) системы документирования.

gendoc-clean: gendoc-clean.oms



# target: gendoc-menu
# Генерирует меню и документацию.

gendoc-menu: gendoc-menu.oms



#
# group: Загрузка файлов в БД
#

# build var: NLS_LANG
# Настройки локали, используемые при загрузке файлов в БД.
export NLS_LANG = AMERICAN_CIS.CL8MSWIN1251

# build var: PLSQL_WARNINGS
# Уровни предупреждения компилятора PL/SQL.
# Соответствуют параметру plsql_warnings сеанса Oracle.
#
# Пример значения: "ENABLE:SEVERE"
#
PLSQL_WARNINGS =

# build var: LOAD_DB
# БД для загрузки.
LOAD_DB =

# build var: LOAD_USERID
# Пользователь для загрузки ( формат: [userName[/password]][@dbName]).
#
# Имеет приоритет в случае противоречия с <LOAD_DB>.
LOAD_USERID =

# Корректируем БД для загрузки по пользователю для загрузки.
override LOAD_DB := $(call getDbName,$(LOAD_USERID),$(LOAD_DB))

# build var: LOAD_DB2
# БД для загрузки #2.
LOAD_DB2 =

# build var: LOAD_USERID2
# Пользователь для загрузки #2 ( формат: [userName[/password]][@dbName]).
#
# Имеет приоритет в случае противоречия с <LOAD_DB2>.
LOAD_USERID2 =

# Корректируем БД для загрузки по пользователю для загрузки.
override LOAD_DB2 := $(call getDbName,$(LOAD_USERID2),$(LOAD_DB2))

# build var: LOAD_DB3
# БД для загрузки #3.
LOAD_DB3 =

# build var: LOAD_USERID3
# Пользователь для загрузки #3 ( формат: [userName[/password]][@dbName]).
#
# Имеет приоритет в случае противоречия с <LOAD_DB3>.
LOAD_USERID3 =

# Корректируем БД для загрузки по пользователю для загрузки.
override LOAD_DB3 := $(call getDbName,$(LOAD_USERID3),$(LOAD_DB3))

# build var: LOAD_DB4
# БД для загрузки #4.
LOAD_DB4 =

# build var: LOAD_USERID4
# Пользователь для загрузки #4 ( формат: [userName[/password]][@dbName]).
#
# Имеет приоритет в случае противоречия с <LOAD_DB4>.
LOAD_USERID4 =

# Корректируем БД для загрузки по пользователю для загрузки.
override LOAD_DB4 := $(call getDbName,$(LOAD_USERID4),$(LOAD_DB4))

# build var: LOAD_DB5
# БД для загрузки #5.
LOAD_DB5 =

# build var: LOAD_USERID5
# Пользователь для загрузки #5 ( формат: [userName[/password]][@dbName]).
#
# Имеет приоритет в случае противоречия с <LOAD_DB5>.
LOAD_USERID5 =

# Корректируем БД для загрузки по пользователю для загрузки.
override LOAD_DB5 := $(call getDbName,$(LOAD_USERID5),$(LOAD_DB5))

# build var: LOAD_DB6
# БД для загрузки #6.
LOAD_DB6 =

# build var: LOAD_USERID6
# Пользователь для загрузки #6 ( формат: [userName[/password]][@dbName]).
#
# Имеет приоритет в случае противоречия с <LOAD_DB6>.
LOAD_USERID6 =

# Корректируем БД для загрузки по пользователю для загрузки.
override LOAD_DB6 := $(call getDbName,$(LOAD_USERID6),$(LOAD_DB6))

# build var: LOAD_DB7
# БД для загрузки #7.
LOAD_DB7 =

# build var: LOAD_USERID7
# Пользователь для загрузки #7 ( формат: [userName[/password]][@dbName]).
#
# Имеет приоритет в случае противоречия с <LOAD_DB7>.
LOAD_USERID7 =

# Корректируем БД для загрузки по пользователю для загрузки.
override LOAD_DB7 := $(call getDbName,$(LOAD_USERID7),$(LOAD_DB7))

# build var: LOAD_DB8
# БД для загрузки #8.
LOAD_DB8 =

# build var: LOAD_USERID8
# Пользователь для загрузки #8 ( формат: [userName[/password]][@dbName]).
#
# Имеет приоритет в случае противоречия с <LOAD_DB8>.
LOAD_USERID8 =

# Корректируем БД для загрузки по пользователю для загрузки.
override LOAD_DB8 := $(call getDbName,$(LOAD_USERID8),$(LOAD_DB8))

# build var: LOAD_DB9
# БД для загрузки #9.
LOAD_DB9 =

# build var: LOAD_USERID9
# Пользователь для загрузки #9 ( формат: [userName[/password]][@dbName]).
#
# Имеет приоритет в случае противоречия с <LOAD_DB9>.
LOAD_USERID9 =

# Корректируем БД для загрузки по пользователю для загрузки.
override LOAD_DB9 := $(call getDbName,$(LOAD_USERID9),$(LOAD_DB9))



# build var: LOAD_OPERATORID
# Оператор для загрузки ( формат: operatorName[/password] ).
#
# В случае наличия значения, сразу после подключения производится
# регистрация оператора ( через модуль Operator) в БД.
# По умолчанию используется значение переменной окружения ORACLE_OPERATOR
# ( если есть).
LOAD_OPERATORID := $(ORACLE_OPERATOR)



# build var: LOCAL_DB_DIR
# Имя подкаталога для загрузки локальных скриптов, относящихся к определенной
# БД.
# Определяется автоматически на основе значения параметра <LOAD_DB9>
# ( <LOAD_USERID9>).
LOCAL_DB_DIR = $(call getProductionDbName,$(LOAD_DB9))

# Обеспечиваем корректное значение переменной
override LOCAL_DB_DIR := $(call getLocalDbDir,$(LOCAL_DB_DIR))

# build var: LOCAL_USER_DIR
# Расположение подкаталога для загрузки локальных скриптов, относящихся к
# определенному пользователю БД ( формат: dbDirName/userDirName).
# Определяется автоматически на основе значения параметров <LOCAL_DB_DIR> и
# <LOAD_USERID9>.
#
LOCAL_USER_DIR = \
  $(addsuffix /$(call getUserName,$(LOAD_USERID9)),$(LOCAL_DB_DIR))

# Обеспечиваем корректное значение переменной
override LOCAL_USER_DIR := $(call getLocalUserDir,$(LOCAL_USER_DIR))



# build var: SQL_DEFINE
# Список значений для переменных SQL*Plus.
#
# Переменные создаются командой define перед загрузкой каждого файла.
# Список указывается в формате name=value[,name2=value2]..., для экранирования
# разделителя может использоваться обратный слэш, при наличии пробелов нужно
# заключать список в кавычки. Пробелы в части имени, а также начальные и
# конечные пробелы в части значения, игнорируются, для добавления начальных
# или конечных пробелов в значение нужно использовать кавычки ( например,
# SQL_DEFINE="name1 = \"a \, b\", name2 =c" задает переменную name1 со значением
# "a , b"  и переменную name2 со значением "c").
SQL_DEFINE =

# build var: SKIP_FILE_MASK
# Маски для исключения выполнения ( загрузки) файлов в БД.
#
# Представляет собой список ( через запятую) масок на путь к файлам
# относительно каталога DB ( можно использовать спецсимволы "*" и "?").
# Файлы, соответствующие маске, не выполняются ( не загружаются) в БД.
SKIP_FILE_MASK :=

# build var: FILE_MASK
# Маски для выполнения ( загрузки) файлов в БД.
#
# Представляет собой список ( через запятую) масок на путь к файлам
# относительно каталога DB ( можно использовать спецсимволы "*" и "?").
# В случае задания файлы, не соответствующие маске, не выполняются ( не
# загружаются) в БД.
FILE_MASK :=


# build var: LOAD_FILE_MASK
# Маски для ограничения загружаемых по цели load файлов.
#
# Представляет собой список ( через запятую) масок на путь к загружаемым файлам
# относительно каталога DB ( можно использовать спецсимволы "*" и "?").
# Если маска задана, то файлы, которые должны загружаться по цели load и не
# соответствующие маскам, при загрузке игнорируются.
LOAD_FILE_MASK :=

# Файлы для загрузки
loadTarget =                             \
  $(addsuffix .$(lu),                    \
    $(wildcard Install/Schema/Last/*.vw) \
    $(wildcard Install/Schema/Last/*.trg) \
    $(wildcard *.typ *.pks *.tyb *.pkb) \
  )                                      \
  $(addsuffix .$(lu2),                  \
  )                                      \
  $(addsuffix .$(lu3),                  \
  )                                      \

# Список параметров для загружаемых файлов.
#
# Для настройки передачи параметров при загрузке/выполнении указанного файла
# в список нужно добавить строку вида
#   $(call getArgumentDefine,<filePath>.$([l,r]u*),"arg1"[ "arg2"]...)
# где filePath - путь к файлу относительно каталога DB ( с учетом регистра,
# можно использовать метасимволы).
# С помощью дополнительного расширения ( например, $(lu2)) определяется схема
# и тип загрузки, для которой используются параметры ( если для любых, то можно
# указать *).
loadArgumentList +=



# target: load
# Загружает файлы в БД.

load: load.oms



# target: load-clean
# Удаляет временные файлы, созданные при загрузке в БД.

load-clean: load-clean.oms



#
# group: Установка модуля в БД
#

# build var: INSTALL_VERSION
# Версия модуля для установки в БД.
# При первоначальной установке модуля в БД должно указываться значение "Last".
INSTALL_VERSION  =

# Список дополнительных параметров установки, используемых в модуле.
# Обеспечивает сохранение в логе установки значений дополнительных параметров
# ( в случае наличия у них непустых значений).
# Формат: список имен параметров с пробелом в качестве разделителя.
installAddonOptionList =


# Список объектов для обязательной проверки блокировок перед установкой и
# отменой установки.
# Формат списка такой же, как у loadTarget.
mandatoryCheckLockTarget =



# Список объектов для проверки отсутствия блокировок перед установкой.
# Из списка автоматически исключаются файлы, не загружаемые из-за значений
# параметров FILE_MASK, LOAD_FILE_MASK и SKIP_FILE_MASK.
# Формат списка такой же, как у loadTarget.
installCheckLockTarget =                      \
  $(loadTarget)                               \

# Стандартные действия, выполняемые перед установкой.
# Действия выполняются скриптами, входящими в OMS.
# Значение переменной не должно изменяться в модуле ( в случае необходимости
# можно убирать использование этой переменной из installBeforeTarget и указывать
# там вручную необходимые стандартные действия).
installBeforeOmsAction =                      \
  oms-check-lock.sql                          \
  oms-show-invalid.sql                        \



# Действия, выполняемые перед установкой модуля.
installBeforeTarget =                          \
  $(addsuffix .$(ru),                          \
    $(if $(INSTALL_VERSION), \
      $(wildcard Install/Config/$(INSTALL_VERSION)/before-action.sql),) \
    $(wildcard Install/Config/before-action.sql)      \
    $(installBeforeOmsAction)                  \
  )                                            \
  $(addsuffix .$(ru2),                        \
    $(installBeforeOmsAction)                  \
  )                                            \
  $(addsuffix .$(ru3),                        \
    $(installBeforeOmsAction)                  \
  )                                            \



# target: install-before
# Выполняет предварительные действия перед установкой.

install-before: install-before.oms



# Скрипты для выполнения установки схемы
installSchemaTarget =                   \
  $(addsuffix .$(lu),                    \
    $(wildcard Install/Schema/$(INSTALL_VERSION)/run.sql)      \
  )                                      \
  $(addsuffix .$(lu2),                  \
    $(wildcard Install/Schema/$(INSTALL_VERSION)/Dba/run.sql)      \
  )                                      \
  $(addsuffix .$(lu3),                  \
  )                                      \



# target: install-schema
# Устанавливает объекты схемы в БД.
# Для выполнения установки/обновления объектов схемы выполняются скрипты из
# каталога Install/Schema/$(INSTALL_VERSION).

install-schema: install-schema.oms



# target: install-load
# Загружает объекты в БД при выполнении установки.
# Список загружаемых объектов такой же, как для цели load ( берется из
# переменной loadTarget).

install-load: install-load.oms



# Каталоги для загрузки установочных данных.
# Всегда загружаются данные из каталога Install/Data/Last. Дополнительно при
# установке обновления ( INSTALL_VERSION=N.N.N) загружаются данные из каталога
# Install/Data/N.N.N, при первоначальной установке последней версии
# ( INSTALL_VERSION=Last) загружаются данные из всех каталогов обновлений
# ( Install/Data/[0-9.]*) в порядке нумерации версий.
#
installDataDir = \
  $(wildcard Install/Data/Last) \
  $(if $(INSTALL_VERSION), \
    $(if $(call nullif,$(call upper,$(INSTALL_VERSION)),LAST), \
      $(wildcard Install/Data/$(INSTALL_VERSION)) \
      , $(call getVersionDir,Install/Data) \
    ) \
  )



# build func: getInstallDataFile
# Возвращает файлы для загрузки установочных данных.
#
# Параметры:
# $(1)    - название подкаталога в основном каталоге скриптов, откуда берутся
#           файлы для загрузки ( по умолчанию из основного каталога)
#
# Замечания:
# - в случае отсутствия в каталоге скриптов run.sql и run-after.sql для
#   загрузки выбираются скрипты *.sql ( за исключением revert.sql) и *.dat,
#   в противном случае берутся скрипты run.sql, *.dat, run-after.sql;
#
# Пример:
# - для получения скриптов из каталогов Install/Data/*/RFInfoP в соответствии
#   с номером устанавливаемой версии нужно прописать
#   $(call getInstallDataFile,RFInfoP)
#
getInstallDataFile = \
  $(filter-out %/revert.sql, \
    $(foreach d,$(addsuffix $(addprefix /,$(1)),$(installDataDir)), \
      $(wildcard \
        $(if $(wildcard $(d)/run.sql $(d)/run-after.sql), \
          $(d)/run.sql $(d)/*.dat $(d)/run-after.sql \
          , $(d)/*.sql $(d)/*.dat \
        ) \
      ) \
    ) \
  )



# Скрипты для загрузки установочных данных в БД.
installDataTarget = \
  $(addsuffix .$(lu), \
    $(call getInstallDataFile) \
    $(wildcard Install/Data/Last/AccessOperator/*.sql) \
  ) \
  $(addsuffix .$(lu2), \
  ) \
  $(addsuffix .$(lu3), \
  ) \



# target: install-data
# Загружает установочные данные в БД.
# Для выполнения загрузки установочных данных выполняются скрипты из
# каталога Install/Data/$(INSTALL_VERSION).

install-data: install-data.oms



# build var: BATCH_MASK
# Список масок для ограничения устанавливаемых пакетных заданий. Представляет
# собой список ( через запятую) масок коротких наименований пакетных заданий.
# ( можно использовать спецсимволы "*" и "?").
#
# Пример: для загрузки пакетных
# заданий ClearOldLog и CheckBatchExecution маска дожна быть равна
# "ClearOldLog,CheckBatchExecution".
BATCH_MASK =

# build var: SKIP_BATCH_MASK
# Список масок для исключения устанавливаемых пакетных заданий. Представляет
# собой список ( через запятую) масок коротких наименований пакетных заданий.
# ( можно использовать спецсимволы "*" и "?").
#
# Пример: для исключения пакетных
# заданий, начинающихся с Check, маска должна быть равна "Check*".
#
# Маски BATCH_MASK, SKIP_BATCH_MASK можно использовать одновременно с
# FILE_MASK, SKIP_FILE_MASK.
SKIP_BATCH_MASK =

# build var: SKIP_CHECK_JOB
# Флаг пропуска проверки корректности ( компиляции) PL/SQL-блоков заданий
# модуля Scheduler ( "1" не проверять, по умолчанию проверять).
SKIP_CHECK_JOB =

# build var: SKIP_LOAD_OPTION
# Флаг исключения загрузки параметров пакетных заданий модуля Scheduler.  Для
# исключения загрузки нужно указать 1, по умолчанию загружать для
# устанавливаемых пакетных заданий.
SKIP_LOAD_OPTION =

# build var: UPDATE_OPTION_VALUE
# Флаг изменения существующих значений параметров пакетных заданий модуля
# Scheduler при их загрузке согласно указанным в спецификации ( "1" обновлять,
# по умолчанию не обновлять ( при этом значения параметров, указанные в
# спецификации, используются только при создании параметров)).
UPDATE_OPTION_VALUE =

# build var: UPDATE_SCHEDULE
# Флаг обновления расписания запуска пакетных заданий модуля Scheduler согласно
# указанному в спецификации ( "1" обновлять, по умолчанию не обновлять ( при
# этом расписание в спецификации используется только для нового пакетного
# задания)). В понятие расписание входит число повторных попыток и интервал
# между повторениями выполнения батчей. Определение того, является ли пакетное
# задание новым в случае разных файлов для одного и того же пакетного задания (
# batch.xml и batch-config.xml) реализовано в модуле Scheduler.
UPDATE_SCHEDULE =

# Скрипты для загрузки пакетных заданий и их параметров в БД.
installBatchTarget = \
  $(addsuffix .$(lu), \
    $(wildcard Install/Batch/Last/*.job.sql) \
    $(wildcard Install/Batch/Last/*/*.job.sql) \
    $(wildcard Install/Batch/Last/*/batch.xml) \
  ) \
  $(addsuffix .$(lu2), \
  ) \
  $(addsuffix .$(lu3), \
  ) \



# target: install-batch
# Устанавливает пакетные задания в БД.

install-batch: install-batch.oms



# Стандартные действия, выполняемые после установки.
# Действия выполняются скриптами, входящими в OMS.
# Значение переменной не должно изменяться в модуле ( в случае необходимости
# можно убирать использование этой переменной из installAfterTarget и указывать
# там вручную необходимые стандартные действия).
installAfterOmsAction =                        \
  oms-compile-invalid.sql                      \



# Действия, выполняемые после установки.
installAfterTarget =                          \
  $(addsuffix .$(ru),                          \
    $(installAfterOmsAction)                  \
    $(wildcard Install/Config/after-action.sql)      \
    $(if $(INSTALL_VERSION), \
      $(wildcard Install/Config/$(INSTALL_VERSION)/after-action.sql),) \
  )                                            \
  $(addsuffix .$(ru2),                        \
    $(installAfterOmsAction)                  \
  )                                            \
  $(addsuffix .$(ru3),                        \
    $(installAfterOmsAction)                  \
  )                                            \



# target: install-after
# Выполняет завершающие установку действия.

install-after: install-after.oms



# target: install-save-info
# Сохраняет в БД информацию об установке модуля.

install-save-info: install-save-info.oms



# target: install
# Устанавливает модуль в БД.

install: install.oms



#
# group: Тестирование модуля
#

# Скрипты для загрузки в БД объектов для тестирования
installTestTarget = \
  $(addsuffix .$(lu), \
    $(wildcard \
      $(addprefix Test/, \
        *.typ *.pks *.tyb *.pkb \
      ) \
    ) \
  ) \
  $(addsuffix .$(lu2), \
  ) \
  $(addsuffix .$(lu3), \
  ) \



# target: install-test
# Загружает объекты для тестирования в БД.
install-test: install-test.oms



# build var: TEST_SCRIPT
# Имя скрипта для запуска тестирования.
TEST_SCRIPT = run.sql

# Полное имя скрипта для запуска тестирования ( вместе с расширением).
# При отсутствии расширения добавляется используемое по умолчанию ( ".sql").
ifneq ($(TEST_SCRIPT),)
  testScript = \
    $(if $(suffix $(TEST_SCRIPT)),$(TEST_SCRIPT),$(TEST_SCRIPT).sql)
else
  testScript = ""
endif

# Скрипты для запуска тестирования
testTarget = \
  $(addsuffix .$(ru), \
    $(wildcard Test/$(testScript)) \
  ) \
  $(addsuffix .$(ru2), \
  ) \
  $(addsuffix .$(ru3), \
  ) \



# target: test
# Запускает тестирование модуля.
test: test.oms



#
# group: Отмена установки модуля в БД
#

# build var: UNINSTALL_RESULT_VERSION
# Версия модуля, которая получается в результате отмены установки версии модуля.
# Используется при сохранении в БД информации об отмене установки, необходимо
# указывать при отмене установки обновления.
UNINSTALL_RESULT_VERSION  =

# Список объектов для проверки отсутствия блокировок перед отменой установки.
# Из списка автоматически исключаются файлы, не загружаемые из-за значения
# параметра FILE_MASK, SKIP_FILE_MASK.
# Формат списка такой же, как у loadTarget.
uninstallCheckLockTarget = \
  $(uninstallLoadTarget) \


# Стандартные действия, выполняемые перед отменой установки.
# Действия выполняются скриптами, входящими в OMS.
# Значение переменной не должно изменяться в модуле ( в случае необходимости
# можно убирать использование этой переменной из uninstallBeforeTarget и
# указывать там вручную необходимые стандартные действия).
uninstallBeforeOmsAction = \
  oms-check-lock.sql \
  oms-show-invalid.sql \



# Действия, выполняемые перед отменой установки модуля.
uninstallBeforeTarget = \
  $(addsuffix .$(ru), \
    $(if $(INSTALL_VERSION), \
      $(wildcard Install/Config/$(INSTALL_VERSION)/Revert/before-action.sql),) \
    $(wildcard Install/Config/before-action.sql) \
    $(uninstallBeforeOmsAction) \
  ) \
  $(addsuffix .$(ru2), \
    $(uninstallBeforeOmsAction) \
  ) \
  $(addsuffix .$(ru3), \
    $(uninstallBeforeOmsAction) \
  ) \



# target: uninstall-before
# Выполняет предварительные действия перед отменой установки.

uninstall-before: uninstall-before.oms



# Скрипты для отмены установки схемы
uninstallSchemaTarget = \
  $(addsuffix .$(ru), \
    $(wildcard Install/Schema/$(INSTALL_VERSION)/revert.sql) \
  ) \
  $(addsuffix .$(ru2), \
  ) \
  $(addsuffix .$(ru3), \
  ) \



# target: uninstall-schema
# Отменяет изменения, внесенные в объекты схемы при выполнении установки
# ( по цели <install-schema>).
# Для отмены изменений, внесенных в объекты схемы, выполняются скрипты из
# каталога Install/Schema/$(INSTALL_VERSION).

uninstall-schema: uninstall-schema.oms



# Файлы с предыдущими версиями объектов, загружаемыми при отмене установки.
uninstallLoadTarget = \
  $(addsuffix .$(ru), \
    $(if $(INSTALL_VERSION), \
      $(foreach m, *.typ *.vw *.pks *.trg *.tyb *.pkb, \
        $(wildcard Install/Schema/$(INSTALL_VERSION)/Revert/$(m)) \
      ) \
    ,) \
  ) \
  $(addsuffix .$(ru2), \
  ) \
  $(addsuffix .$(ru3), \
  ) \



# target: uninstall-load
# Загружает предыдущие версии объектов в БД при отмене установки.

uninstall-load: uninstall-load.oms



# Скрипты для отмены изменений, внесенных при загрузке установочных данных в БД.
uninstallDataTarget = \
  $(addsuffix .$(ru), \
    $(wildcard Install/Data/$(INSTALL_VERSION)/revert.sql) \
  ) \
  $(addsuffix .$(ru2), \
  ) \
  $(addsuffix .$(ru3), \
  ) \



# target: uninstall-data
# Отменяет изменения, внесенные при загрузке установочных данные в БД
# ( по цели <install-data>).

uninstall-data: uninstall-data.oms



# Стандартные действия, выполняемые после отмены установки.
# Действия выполняются скриптами, входящими в OMS.
# Значение переменной не должно изменяться в модуле ( в случае необходимости
# можно убирать использование этой переменной из uninstallAfterTarget и
# указывать там вручную необходимые стандартные действия).
uninstallAfterOmsAction = \
  oms-compile-invalid.sql \



# Действия, выполняемые после отмены установки.
uninstallAfterTarget = \
  $(addsuffix .$(ru), \
    $(uninstallAfterOmsAction) \
    $(wildcard Install/Config/after-action.sql) \
    $(if $(INSTALL_VERSION), \
      $(wildcard Install/Config/$(INSTALL_VERSION)/Revert/after-action.sql),) \
  ) \
  $(addsuffix .$(ru2), \
    $(uninstallAfterOmsAction) \
  ) \
  $(addsuffix .$(ru3), \
    $(uninstallAfterOmsAction) \
  ) \



# target: uninstall-after
# Выполняет завершающие отмену установки действия.

uninstall-after: uninstall-after.oms



# target: uninstall-save-info
# Сохраняет в БД информацию об отмене установки модуля.

uninstall-save-info: uninstall-save-info.oms



# target: uninstall
# Отменяет установку версии модуля в БД.
# Версия, установка которой отменяется, определяется значением параметра
# <INSTALL_VERSION>.

uninstall: uninstall.oms



#
# group: Выдача прав пользователям БД
#

# build var: TO_USERNAME
# Имя пользователя БД, которому выдаются права.
TO_USERNAME =

# build var: GRANT_SCRIPT
# Имя скрипта для выдачи прав.
GRANT_SCRIPT = run.sql

# Полное имя скрипта для выдачи прав ( вместе с расширением).
# При отсутствии расширения добавляется используемое по умолчанию ( ".sql").
ifneq ($(GRANT_SCRIPT),)
grantScript = \
  $(if $(suffix $(GRANT_SCRIPT)),$(GRANT_SCRIPT),$(GRANT_SCRIPT).sql)
else
grantScript = ""
endif

# Версия скриптов, используемых для выдачи прав.
# По умолчанию используется Last.
grantVersion = $(if $(INSTALL_VERSION),$(INSTALL_VERSION),Last)

# Действия, выполняемые для выдачи прав.
grantTarget =                           \
  $(addsuffix .$(ru),                   \
    $(wildcard Install/Grant/$(grantVersion)/$(grantScript)) \
  )                                     \
  $(addsuffix .$(ru2),                  \
  )                                     \
  $(addsuffix .$(ru3),                  \
  )                                     \



# target: grant-exec
# Выполняет скрипты выдачи прав.

grant-exec: grant-exec.oms



# target: grant-save-info
# Сохраняет в БД информацию о выдаче прав пользователю.

grant-save-info: grant-save-info.oms



# target: grant
# Выдает права пользователю БД.

grant: grant.oms



# Включаем стандартные настройки сборки проекта
include $(omsModuleDir)/common.mk
