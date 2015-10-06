# makefile: Сборка OMS

# Ниже указана версия OMS-шаблона, на основе которого был создан файл.
#
# SVN Version Information:
# OMS root: Oracle/Module/OraMakeSystem
# $Revision: 2133 $
# $LastChangedDate:: 2014-07-09 10:34:20 +0400 #$
#

# Версия OMS-шаблона
OMS_VERSION=1.7.3



# Строка для фиктивного изменения файла ( для получения новой правки для OMS,
# необходимой для корректного обновления OMS-файлов модулей в случае применения
# патчей, например при изменении в шаблоне файла DB/Makefile).
# +



# Фильтрует цели, с которыми был запущен make.
#
# Параметры:
# (1)     - список целей для фильтрации
filterGoals = $(strip $(filter $(1), $(MAKECMDGOALS)))

# Цели запуска make, относящиеся к установке.
installGoals := $(call filterGoals, \
    install \
    install-after \
    install-batch \
    install-before \
    install-data \
    install-load \
    install-save-info \
    install-schema \
  )

# Цели запуска make, относящиеся к отмене установки.
uninstallGoals := $(call filterGoals, \
    uninstall \
    uninstall-after \
    uninstall-before \
    uninstall-data \
    uninstall-load \
    uninstall-save-info \
    uninstall-schema \
  )

# Цели запуска make, относящиеся к выдаче прав.
grantGoals := $(call filterGoals, \
    grant \
    grant-exec \
    grant-save-info \
  )



# Включение настроек make, действующих при установке или отмене установки.
ifneq ($(strip $(installGoals) $(uninstallGoals)),)

  -include Install/Config/define.mk

  ifneq ($(INSTALL_VERSION),)

    ifneq ($(installGoals),)

      -include Install/Config/$(INSTALL_VERSION)/define.mk

    endif

    ifneq ($(uninstallGoals),)

      -include Install/Config/$(INSTALL_VERSION)/Revert/define.mk

    endif
  endif
endif



#
# Абстрактные цели
#
.PHONY:                       \
  all.oms                     \
  clean.oms                   \
  set-version.oms             \
  show-oms-version.oms        \
  update-oms-version.oms      \
  gendoc.oms                  \
  gendoc-clean.oms            \
  gendoc-menu.oms             \
  grant.oms                   \
  grant-exec.oms              \
  grant-save-info.oms         \
  load-start-log.oms          \
  load.oms                    \
  load-clean.oms              \
  install.oms                 \
  install-after.oms           \
  install-batch.oms           \
  install-before.oms          \
  install-data.oms            \
  install-load.oms            \
  install-save-info.oms       \
  install-schema.oms          \
  install-test.oms            \
  test.oms                    \
  uninstall.oms               \
  uninstall-after.oms         \
  uninstall-before.oms        \
  uninstall-data.oms          \
  uninstall-load.oms          \
  uninstall-save-info.oms     \
  uninstall-schema.oms        \



#
# group: Общие цели
#

# build var: OMS_DEBUG_LEVEL
# Уровень вывода отладочной информации скриптами OMS.
# Возможные значения:
# 0   - не выводить ( по умолчанию);
# 1   - базовый;
# 2   - расширенный;
# 3   - расширенный с выводом содержимого временных файлов и т.д.;
#
export OMS_DEBUG_LEVEL = 0

# build var: OMS_INSTALL_DATA_DIR
# Путь к каталогу с установленными файлами OMS.
export OMS_INSTALL_DATA_DIR = /usr/local/share/oms

# build var: OMS_SAVE_FILE_INSTALL_INFO
# Флаг сохранения информации в БД об устанавливаемых файлах.
# Информация сохраняется в случае загрузки файлов скриптом <oms-load> с помощью
# SQL*Plus.
#
# Возможные значения:
# 0   - не сохранять
# 1   - сохранять ( по умолчанию)
#
export OMS_SAVE_FILE_INSTALL_INFO ?= 1



# target: all.oms
# Целиком собирает проект.

all.oms:                      \
  gendoc-menu.oms             \
  load.oms                    \



# target: clean.oms
# Удаляет файлы, созданные при сборке.

clean.oms:                    \
  load-clean.oms              \
  gendoc-clean.oms            \



#
# group: Версия модуля
#

# Загружаем текст map-файла для целей, работающих с БД.
mapFileXml := $(shell \
  if [[ -f ../Doc/map.xml ]]; then cat ../Doc/map.xml; fi; \
)

# Получаем значение тэга version из map-файла
moduleVersion := $(call getXmlElementValue,version,$(mapFileXml))

# Определяем значение по умолчанию для MODULE_VERSION.
ifeq ($(MODULE_VERSION),)
  ifneq ($(origin MODULE_VERSION),command line)

    ifneq ($(call lower,$(INSTALL_VERSION)),last)
      ifeq ($(call compareVersion,$(INSTALL_VERSION),$(moduleVersion)),1)

        moduleVersionNew := $(shell \
          oms-module set-version --directory .. \
            --used-only --quiet "$(INSTALL_VERSION)" \
          && echo \
            "OMS: module version changed using INSTALL_VERSION: $(INSTALL_VERSION) ( please, run \"make gendoc\")" >&2 \
        )

        ifneq ($(moduleVersionNew),)

          moduleVersion := $(moduleVersionNew)

        endif

      endif
    endif

    MODULE_VERSION := $(moduleVersion)

  endif
endif

# Возвращает флаг полной установки модуля.
#
# Параметры:
# (1)                         - номер устанавливаемой версии или Last ( без
#                               учета регистра) при полной установке текущей
#                               версии модуля
#
# Возврат:
# 1 при полной установке, 0 в случае частичной ( если указана версия), иначе
# пустая строка.
#
getIsFullInstall = \
  $(if $(call nullif,last,$(call lower,$(1))),$(if $(1),0),1)

# Возвращает устанавливаемую версию модуля.
#
# Параметры:
# (1)                         - номер устанавливаемой версии или Last ( без
#                               учета регистра), если устанавливается текущая
#                               версия модуля
#
getInstallVersion = \
  $(if $(call nullif,1,$(call getIsFullInstall,$(1))),$(1),$(MODULE_VERSION))



# Экспортируем параметры установки для скрипта oms-load

export OMS_MODULE_INITIAL_SVN_PATH := \
  $(call getXmlElementValue,initialPath,$(mapFileXml))

export OMS_MODULE_SVN_ROOT := \
  $(call getXmlElementValue,path,$(mapFileXml))

export OMS_IS_FULL_MODULE_INSTALL := \
  $(call getIsFullInstall,$(INSTALL_VERSION))

export OMS_MODULE_INSTALL_VERSION := \
  $(call getInstallVersion,$(INSTALL_VERSION))

export OMS_MODULE_VERSION := $(MODULE_VERSION)

processStartTimeId := $(shell date "+%Y-%m-%dT%H:%M:%S%z $$PPID")
export OMS_PROCESS_ID := $(word 2,$(processStartTimeId))
export OMS_PROCESS_START_TIME := $(firstword $(processStartTimeId))

export OMS_PLSQL_WARNINGS := $(PLSQL_WARNINGS)

getSvnInfo := $(shell oms-module --directory .. show-svn-info --quiet)
export OMS_SVN_FILE_PATH := $(wordlist 2,999,$(getSvnInfo))
export OMS_SVN_VERSION_INFO := $(firstword $(getSvnInfo))

export OMS_ACTION_GOALS = $(MAKECMDGOALS)



# target: set-version.oms
# Устанавливает номер текущей версии модуля.

set-version.oms:
	@oms-module set-version --directory .. "$(MODULE_VERSION)"



#
# group: Версия OMS-файлов
#

# Номер ревизии файла в OMS
omsRevisionValue    := \$$Revision:: 2133                     $$

omsRevision          := $(strip $(shell           \
  omsRevisionValue='$(omsRevisionValue)';         \
  echo "$${omsRevisionValue:12:25}"               \
  ))

# Дата последнего изменения файла в OMS
omsChangeDateValue  := \$$Date:: 2014-07-09 10:34:20 +0400 #$$

omsChangeDate      := $(strip $(shell             \
  omsChangeDateValue='$(omsChangeDateValue)';     \
  echo "$${omsChangeDateValue:8:11}"              \
  ))



# target: show-oms-version.oms
# Показывает версию OMS-файлов, входящих в состав модуля.

show-oms-version.oms:
	@echo "OMS files version: $(OMS_VERSION) ( rev. $(omsRevision), $(omsChangeDate))"



# target: update-oms-version.oms
# Обновляет OMS-файлы, входящие в состав модуля.

update-oms-version.oms:
	@oms-update-module --from-revision "$(omsRevision)" -d ..



#
# group: Генерация документации
#

# Обеспечиваем полное обновление документации при безусловной сборке.
ifneq ($(call isMakeFlag,B),)
  GENDOC_DB_FLAGS += "--rebuild"
endif

# Общие параметры и значения, передаваемые скрипту oms-auto-doc
autoDocFlags = -d ".." -o $(GENDOC_DB_DIR) \
  --nd-flags "$(GENDOC_DB_FLAGS)"

# target: gendoc.oms
# Генерирует документацию.

gendoc.oms:
	@[[ -n "$(GENDOC_DB_DIR)" ]] \
	&& ( oms-auto-doc $(autoDocFlags)) \
	|| exit 0

# target: gendoc-clean.oms
# Удаляет временные файлы ( кэш) системы документирования.

gendoc-clean.oms:
	@oms-auto-doc --clean $(autoDocFlags)

# target: gendoc-menu.oms
# Генерирует меню и документацию.
# Используется скрипт <oms-auto-doc>.

gendoc-menu.oms:
	@[[ -n "$(GENDOC_DB_DIR)" ]] \
	&& ( oms-auto-doc -m $(autoDocFlags)) \
	|| exit 0



#
# group: Загрузка файлов в БД
#

# Каталог со стандартными SQL-скриптами
omsSqlScriptDir  = $(OMS_INSTALL_DATA_DIR)/SqlScript

# Каталог с файлами, создаваемыми при загрузке в БД.
loadDir           = $(omsModuleDir)/Load

# Каталог с логами и другими файлами, создающимися при загрузке в БД
loadLogDir        = $(loadDir)/Log

# Каталог с логами установки модуля
installLogDir     = $(loadLogDir)/Install

# Каталог с файлами, фиксирующими факт загрузки в БД
loadStateDir      = $(loadDir)/State

# Фиктивное расширение файлов, используемое для выполнения файлов в БД
runExt            = .run

# Расширение файлов, фиксирующих факт загрузки в БД
loadExt            = .load

# Определяем путь для поиска файлов с указанным расширением
vpath %$(loadExt)    $(loadStateDir)

# Путь для поиска стандартных SQL-скриптов
vpath oms-%.sql    $(omsSqlScriptDir)



#
# Выполнение файла в БД
#

# build func: getFileModulePart
# Возвращает номер части модуля, к которой относится файл.
#
# Параметры:
# $(1)    - путь к файлу относительно каталога DB
#
# Возврат:
# - номер части модуля ( число от 1 до 9) либо пустую строку, если не удалось
#   определить
#
# Замечания:
# - если загрузка файла настроена для нескольких частей модуля, то будет
#   возвращен минимальный номер части модуля;
#
getFileModulePart = $(firstword \
  $(foreach part, 1 2 3 4 5 6 7 8 9, \
    $(if $(filter $(1).$(part),$(getFileModulePart_FilePartList)),$(part),) \
  ) \
)

# Сохраняем в переменной getFileModulePart_FilePartList список файлов,
# предназначенных для установки или загрузки, с указанием части модуля, к
# которой они относятся.
# Номера части модуля указывается в виде расширения файла, например, для файла
# "DB/Install/Schema/Last/run.sql", при его стандартной привязке в
# installSchemaTarget к первой части модуля с помощью $(lu), в списке будет
# элемент "DB/Install/Schema/Last/run.sql.1". Для этого переменным lu* и
# ru* временно присваиваются тривиальные значения.

lu  = 1
lu2 = 2
lu3 = 3
lu4 = 4
lu5 = 5
lu6 = 6
lu7 = 7
lu8 = 8
lu9 = 9

ru  = 1
ru2 = 2
ru3 = 3
ru4 = 4
ru5 = 5
ru6 = 6
ru7 = 7
ru8 = 8
ru9 = 9

getFileModulePart_FilePartList := $(strip \
  $(loadTarget) \
  $(installBeforeTarget) \
  $(installSchemaTarget) \
  $(installDataTarget) \
  $(installAfterTarget) \
  $(uninstallBeforeTarget) \
  $(uninstallSchemaTarget) \
  $(uninstallLoadTarget) \
  $(uninstallDataTarget) \
  $(uninstallAfterTarget) \
  $(grantTarget) \
)

lu  =
lu2 =
lu3 =
lu4 =
lu5 =
lu6 =
lu7 =
lu8 =
lu9 =

ru  =
ru2 =
ru3 =
ru4 =
ru5 =
ru6 =
ru7 =
ru8 =
ru9 =

# Скрипт, реализующий проверку имени файла по SKIP_FILE_MASK и FILE_MASK.
#
# Переменные:
# loadFile                    - имя проверяемого файла
# isNeedProcess               - результат проверки ( 1, если файл проходит
#                               по условиям выполнения по маскам, иначе 0)
#
checkFileMaskScript = \
	skipFileMask="$(strip $(subst $(comma),$(space),$(SKIP_FILE_MASK)))"; \
	isNeedProcess=1; \
	set -f ; set -- $$skipFileMask; set +f ; \
	for mask in "$$@" ; do \
		case "$$loadFile" in $$mask) \
			isNeedProcess=0; break; ;; \
		esac; \
	done; \
	if (( isNeedProcess)); then \
		fileMask="$(strip $(subst $(comma),$(space),$(FILE_MASK)))"; \
		set -f ; set -- $$fileMask; set +f ; \
		for mask in "$$@" ; do \
			isNeedProcess=0; \
			case "$$loadFile" in $$mask) \
				isNeedProcess=1; break; ;; \
			esac; \
		done; \
  fi;

# Исключает из списка файлы, которые подпадают под маски игнорируемых файлов из
# переменной SKIP_FILE_MASK и не подпадают под маски FILE_MASK в случае
# задания.
#
# Параметры:
# $(1)    - список имен файлов
#
filterOutFileMask = \
  $(if $(strip $(SKIP_FILE_MASK))$(strip $(FILE_MASK)),$(strip $(shell \
    loadFileList="$(strip $(1))"; \
    for loadFile in $$loadFileList ; do \
      $(checkFileMaskScript) \
      if (( isNeedProcess)) ; then \
        echo "$$loadFile"; \
      fi; \
   done; \
  )),$1)

# Выделяет пользователя ( без пароля) при загрузке из $@ и $<.
# Может вызываться только из правил.
# Предполагает, что зависимость $< представляет собой загружаемый в БД скрипт
# ( пример: Do/run.sql), а цель $@ представляет собой тот же скрипт с
# добавлением через точку имени пользователя и БД ( userName@dbName) и
# произвольного расширения ( пример: Do/run.sql.document@Exchange.load).
getLoadUser  = $(patsubst $(<F).%,%,$(basename $(@F)))

# Выделяет пользователя с паролем при загрузке из loadUserIdList с
# использованием функции getLoadUser.
getLoadUserId  =  \
  $(firstword $(filter $(subst @,/%@,$(getLoadUser)),$(loadUserIdList)))

# Возвращает номер части модуля, загружаемой в данную схему БД.
# В случае, если несколько частей модуля одновременно загружаются в одну и
# ту же схему БД, возвращается минимальный номер.
#
getLoadModulePart = \
  $(words 1 $(call wordListTo,$(getLoadUser),$(loadUserList)))

# Возвращает строку параметров, передаваемых загружаемому файлу.
getLoadArgument = $(shell case "$@" in $(loadArgumentList) esac)

# Функция выполнения файла в БД.
runFunction  = \
	{ \
	loadFile="$<"; \
	loadUser="$(getLoadUser)"; \
	loadUserId='$(getLoadUserId)'; \
	loadOperatorId='$(loadOperatorId)'; \
	$(checkFileMaskScript) \
	if (( isNeedProcess )) ; then \
		set -o pipefail; \
		{ \
		echo "$$loadFile: -> $$loadUser ..."; \
		oms-load \
			--file-module-part "$(firstword $(call getFileModulePart,$<) $(getLoadModulePart))" \
			$(if $(call isMakeFlag,i),--force) \
			$(if $(FILE_MASK),--file-mask "$(subst $(comma),$(space),$(strip $(FILE_MASK)))") \
			$(if $(SKIP_FILE_MASK),--skip-file-mask "$(subst $(comma),$(space),$(strip $(SKIP_FILE_MASK)))") \
			$(if $(SQL_DEFINE),--sql-define "$(subst ",\",$(SQL_DEFINE))") \
			$(if $(subst 0,,$(SKIP_CHECK_JOB)), --skip-check-job,) \
			$(if $(subst 0,,$(SKIP_LOAD_OPTION)), --skip-load-option,) \
			$(if $(subst 0,,$(UPDATE_OPTION_VALUE)), --update-option-value,) \
			$(if $(subst 0,,$(UPDATE_SCHEDULE)), --update-schedule,) \
			--userid "$${loadUserId:-$$loadUser}" \
			--operatorid "$$loadOperatorId" \
			--log-path-prefix "$(loadLogDir)/$$loadUser" \
			"$$loadFile" $(getLoadArgument); \
		} $(copyToLoadLogCmd); \
	fi; \
	}

# Скрипт, реализующий проверку имени файла по SKIP_FILE_MASK и FILE_MASK,
# а также вызывающий дополнительный скрипт.
#
# Параметры:
#
# 1                           - наименование переменной, в которой находится
#                               текст дополнительного скрипта для проверки
#                               файла
#
# Переменные:
# loadFileTargetList          - список целей для проверяемых файлов
#
# Результат выводится в стандартный выходной поток в виде списка целей для
# файлов загрузки.
checkFileTargetScript = \
  loadExtList="$(addprefix .,$(foreach e,$(loadExt) $(runExt),$(addsuffix $(e),$(loadUserListReal))))"; \
  for loadFileTarget in $$loadFileTargetList ; do \
    for loadExt in $$loadExtList ; do \
      loadFile="$${loadFileTarget%$$loadExt}"; \
      if [[ "$$loadFileTarget" = "$${loadFile}$${loadExt}" ]] ; then \
        $(1) \
        $(checkFileMaskScript) \
        if (( isNeedLoad)) && (( isNeedProcess)) ; then \
          echo "$$loadFileTarget"; \
        fi; \
        break; \
      fi; \
    done; \
  done;



#
# Загрузка файла в БД
#

# Скрипт, реализующий проверку имени файла по LOAD_FILE_MASK.
#
# Переменные:
# loadFile                    - имя проверяемого файла
# isNeedLoad                  - результат проверки ( 1 если подпадает под
#                               маску, иначе 0)
#
checkLoadFileMaskScript = \
	loadFileMask="$(strip $(subst $(comma),$(space),$(LOAD_FILE_MASK)))"; \
	isNeedLoad=1; \
	if [[ -n "$$loadFileMask" ]] ; then \
		isNeedLoad=0; \
		set -f ; set -- $$loadFileMask; set +f ; \
		for mask in "$$@" ; do \
			case "$$loadFile" in $$mask) \
				isNeedLoad=1; break; ;; \
			esac; \
		done; \
	fi;

# Оставляет в списке только загружаемые файлы.
# Из списка исключаются файлы, которые не подпадают под маски файлов из
# переменной LOAD_FILE_MASK и FILE_MASK( если они заданы) либо подпадают под
# маски переменной SKIP_FILE_MASK.
#
# Параметры:
# $(1)    - список имен загружаемых файлов ( с суффиксами $(lu),...)
#
filterLoadFileTarget = \
  $(if $(strip $(LOAD_FILE_MASK) $(SKIP_FILE_MASK) $(FILE_MASK)),$(strip $(shell \
    loadFileTargetList="$(strip $(1))"; \
	  $(call checkFileTargetScript, $(checkLoadFileMaskScript)) \
  )),$1)

# Проверяет присутствие загружаемого файла в списке загрузки для цели load.
#
# Параметры:
# $(1)    - загружаемый файл ( путь с добавлением $(lu*) или $(ru*))
#
# Возврат:
# 1 если при положительном результате, иначе ""
#
isLoadTarget = $(if $(filter $(1),$(loadTarget)),1,)

# Функция загрузки файла в БД.
# В случае успешного выполнения скрипта в БД, создает в каталоге $(loadStateDir)
# файл, совпадающий с именем цели ( для запоминания факта успешной загрузки).
loadFunction  =  \
	loadFile="$<"; \
	isLoadTarget="$(call isLoadTarget,$@)"; \
	if (( isLoadTarget )) ; then \
	$(checkLoadFileMaskScript) \
	else \
		isNeedLoad=1; \
	fi; \
	if (( isNeedLoad )) ; then \
		$(runFunction) \
		&& mkdir -p "$(loadStateDir)/$(@D)" && touch "$(loadStateDir)/$@"; \
	fi;


#
# Загрузка батчей в БД.
#

# Скрипт, реализующий проверку файла, относящегося к батчу.
#
# Переменные:
# loadFile                    - имя проверяемого файла предполагается, что
#								директория файла совпадает с именем батча
# isNeedLoad                  - результат проверки ( 1 если подпадает под
#                               маску, иначе 0)
#
checkBatchScript = \
  batchDirName=$${loadFile%/*}; \
  batchName=$${batchDirName\#\#*/}; \
  skipBatchMask="$(strip $(subst $(comma),$(space),$(SKIP_BATCH_MASK)))"; \
  isNeedLoad=1; \
  set -f ; set -- $$skipBatchMask; set +f ; \
  for mask in "$$@" ; do \
    case "$$batchName" in $$mask) \
      isNeedLoad=0; break; ;; \
    esac; \
  done; \
  if (( isNeedLoad)); then \
    batchMask="$(strip $(subst $(comma),$(space),$(BATCH_MASK)))"; \
    set -f ; set -- $$batchMask; set +f ; \
    for mask in "$$@" ; do \
      isNeedLoad=0; \
      case "$$batchName" in $$mask) \
        isNeedLoad=1; break; ;; \
      esac; \
    done; \
  fi;

# Получает имена скриптов из списка целей.
getSourceFileList = \
  $(foreach u, $(loadUserListReal), \
    $(foreach e, $(loadExt) $(runExt), \
      $(patsubst %.$(u)$(e),%, \
        $(filter %.$(u)$(e),$(1)) \
      ) \
    ) \
  )

# Оставляет в списке только те цели для файлов, в директории которых есть файл
# batch.xml в том же списке.
getBatchLocalTargetList = \
  $(foreach fileTarget, $(1), \
   $(if \
     $(filter \
        $(dir $(call getSourceFileList, $(fileTarget)))batch.xml \
        , $(call getSourceFileList, $(1)) \
     ) \
     , $(fileTarget) \
   ) \
  )

# Оставляет в списке только те цели для файлов батчей, которые не входят
# в список getBatchLocalTargetList.
getBatchCommonTargetList = \
  $(filter-out \
    $(call getBatchLocalTargetList, $(1)) \
    , $(1) \
  )


# Оставляет в списке файлов батчей только файлы, соответствующие батчам,
# которые удовлетворяют маске BATCH_MASK или SKIP_BATCH_MASK.
#
# Фильтр осуществляется только для файлов батчей, которые можно отнести к
# какому-либо батчу, т.е. в директории которого есть также файл batch.xml,
# который входит в installBatchTarget.
#
# Параметры:
# $(1)    - список имен загружаемых файлов ( с суффиксами $(lu),...)
#
filterInstallBatchTarget = \
  $(if \
    $(strip $(BATCH_MASK) $(SKIP_BATCH_MASK)) \
    ,  $(strip $(shell \
          echo "$(call getBatchCommonTargetList, $(strip $(1)))"; \
          loadFileTargetList="$(call getBatchLocalTargetList, $(strip $(1)))"; \
          $(call checkFileTargetScript, $(checkBatchScript)) \
       )) \
    , $1 \
  )


#
# Обработка параметров подключения к БД
#

# Определяем параметры регистрации оператора.
loadOperatorId    := $(LOAD_OPERATORID)

                                        # Пробуем получить пароль, если указан
                                        # оператор и нет пароля
ifneq ($(loadOperatorId),)
  ifeq ($(patsubst %/,%@,$(loadOperatorId)),$(subst /,@,$(loadOperatorId)))
    loadOperatorId    := $(shell            \
      oms-connect-info                      \
        --operatorid "$(loadOperatorId)"    \
        --out-operatorid                    \
        --ignore-absent-password            \
      )
  endif
endif

                                        # Выделяем логин оператора ( без пароля)
loadOperatorName  := $(firstword $(subst /, ,$(loadOperatorId)))

# Получает нормализованную строку подключения к БД.
#
# Параметры:
# (1)     - строка подключения к БД в формате [userName[/password]][@dbName]
# (2)     - имя БД по умолчанию ( используется, если в (1) БД не указана)
#
getConnectInfo = $(shell                \
  oms-connect-info                      \
    --userid '$(1)'                     \
    --default-db '$(2)'                 \
    --out-userid                        \
    --ignore-absent-password            \
  )

# Список пользователей для загрузки ( без паролей).
# Список упорядочен ( LOAD_USERID LOAD_USERID2 ...), при отсутствии значения
# на его место добавляется тире.
loadUserList         :=

# Список пользователей для загрузки ( возможно с паролями).
loadUserIdList       :=

# Определяем параметры подключения.
ifneq ($(LOAD_DB)$(LOAD_USERID),)

  loadUserId          := $(call getConnectInfo,$(LOAD_USERID),$(LOAD_DB))
  loadUser            := $(call getUser,$(loadUserId))

  loadUserList        += $(loadUser)
  loadUserIdList      += $(loadUserId)
  ru                  := $(loadUser)$(runExt)
  lu                  := $(loadUser)$(loadExt)

  %.$(loadUser)$(runExt): %
		@$(runFunction)

  %.$(loadUser)$(loadExt): %
		@$(loadFunction)

else

  loadUserList        += -

endif

# Определяем параметры подключения #2.
ifneq ($(LOAD_DB2)$(LOAD_USERID2),)

  loadUserId          := $(call getConnectInfo,$(LOAD_USERID2),$(LOAD_DB2))
  loadUser            := $(call getUser,$(loadUserId))

  loadUserList        += $(loadUser)
  loadUserIdList      += $(loadUserId)
  ru2                 := $(loadUser)$(runExt)
  lu2                 := $(loadUser)$(loadExt)

  %.$(loadUser)$(runExt): %
		@$(runFunction)

  %.$(loadUser)$(loadExt): %
		@$(loadFunction)

else

  loadUserList        += -

endif

# Определяем параметры подключения #3.
ifneq ($(LOAD_DB3)$(LOAD_USERID3),)

  loadUserId          := $(call getConnectInfo,$(LOAD_USERID3),$(LOAD_DB3))
  loadUser            := $(call getUser,$(loadUserId))

  loadUserList        += $(loadUser)
  loadUserIdList      += $(loadUserId)
  ru3                 := $(loadUser)$(runExt)
  lu3                 := $(loadUser)$(loadExt)

  %.$(loadUser)$(runExt): %
		@$(runFunction)

  %.$(loadUser)$(loadExt): %
		@$(loadFunction)

else

  loadUserList        += -

endif

# Определяем параметры подключения #4.
ifneq ($(LOAD_DB4)$(LOAD_USERID4),)

  loadUserId          := $(call getConnectInfo,$(LOAD_USERID4),$(LOAD_DB4))
  loadUser            := $(call getUser,$(loadUserId))

  loadUserList        += $(loadUser)
  loadUserIdList      += $(loadUserId)
  ru4                 := $(loadUser)$(runExt)
  lu4                 := $(loadUser)$(loadExt)

  %.$(loadUser)$(runExt): %
		@$(runFunction)

  %.$(loadUser)$(loadExt): %
		@$(loadFunction)

else

  loadUserList        += -

endif

# Определяем параметры подключения #5.
ifneq ($(LOAD_DB5)$(LOAD_USERID5),)

  loadUserId          := $(call getConnectInfo,$(LOAD_USERID5),$(LOAD_DB5))
  loadUser            := $(call getUser,$(loadUserId))

  loadUserList        += $(loadUser)
  loadUserIdList      += $(loadUserId)
  ru5                 := $(loadUser)$(runExt)
  lu5                 := $(loadUser)$(loadExt)

  %.$(loadUser)$(runExt): %
		@$(runFunction)

  %.$(loadUser)$(loadExt): %
		@$(loadFunction)

else

  loadUserList        += -

endif

# Определяем параметры подключения #6.
ifneq ($(LOAD_DB6)$(LOAD_USERID6),)

  loadUserId          := $(call getConnectInfo,$(LOAD_USERID6),$(LOAD_DB6))
  loadUser            := $(call getUser,$(loadUserId))

  loadUserList        += $(loadUser)
  loadUserIdList      += $(loadUserId)
  ru6                 := $(loadUser)$(runExt)
  lu6                 := $(loadUser)$(loadExt)

  %.$(loadUser)$(runExt): %
		@$(runFunction)

  %.$(loadUser)$(loadExt): %
		@$(loadFunction)

else

  loadUserList        += -

endif

# Определяем параметры подключения #7.
ifneq ($(LOAD_DB7)$(LOAD_USERID7),)

  loadUserId          := $(call getConnectInfo,$(LOAD_USERID7),$(LOAD_DB7))
  loadUser            := $(call getUser,$(loadUserId))

  loadUserList        += $(loadUser)
  loadUserIdList      += $(loadUserId)
  ru7                 := $(loadUser)$(runExt)
  lu7                 := $(loadUser)$(loadExt)

  %.$(loadUser)$(runExt): %
		@$(runFunction)

  %.$(loadUser)$(loadExt): %
		@$(loadFunction)

else

  loadUserList        += -

endif

# Определяем параметры подключения #8.
ifneq ($(LOAD_DB8)$(LOAD_USERID8),)

  loadUserId          := $(call getConnectInfo,$(LOAD_USERID8),$(LOAD_DB8))
  loadUser            := $(call getUser,$(loadUserId))

  loadUserList        += $(loadUser)
  loadUserIdList      += $(loadUserId)
  ru8                 := $(loadUser)$(runExt)
  lu8                 := $(loadUser)$(loadExt)

  %.$(loadUser)$(runExt): %
		@$(runFunction)

  %.$(loadUser)$(loadExt): %
		@$(loadFunction)

else

  loadUserList        += -

endif

# Определяем параметры подключения #9.
ifneq ($(LOAD_DB9)$(LOAD_USERID9),)

  loadUserId          := $(call getConnectInfo,$(LOAD_USERID9),$(LOAD_DB9))
  loadUser            := $(call getUser,$(loadUserId))

  loadUserList        += $(loadUser)
  loadUserIdList      += $(loadUserId)
  ru9                 := $(loadUser)$(runExt)
  lu9                 := $(loadUser)$(loadExt)

  %.$(loadUser)$(runExt): %
		@$(runFunction)

  %.$(loadUser)$(loadExt): %
		@$(loadFunction)

else

  loadUserList        += -

endif

# Список реально заданных пользователей для загрузки ( без дублирования).
loadUserListReal = $(sort $(filter-out -,$(loadUserList)))

# Включаем зависимости ( игнорируем отсутствие файла).
-include loaddeps.mk



#
# Лог загрузки файлов.
#

# Лог загрузки файлов в БД ( включая выполнение скриптов).
loadFileLog =

# Включаем лог загрузки файлов при установке модуля.
ifneq ($(strip $(installGoals) $(uninstallGoals) $(grantGoals)),)

  moduleName := $(call getXmlElementValue,name,$(mapFileXml))

  # Формируем имя файла лога
  loadFileLog := $(shell \
    date \
    +"$(installLogDir)/"%Y%m%d_%H%M%S"$(strip \
        $(addprefix -, \
          $(subst $(space),_,$(moduleName)) \
        ) \
      )$(strip \
        $(addprefix -, \
          $(if $(installGoals),, \
          $(if $(uninstallGoals),uninstall, \
          $(if $(grantGoals),grant, \
          ))) \
        ) \
      )$(strip \
        $(addprefix -,$(INSTALL_VERSION)) \
      )$(strip \
        $(addprefix -,$(word 2,$(subst @, , \
          $(firstword $(filter-out -,$(loadUserList)))  \
        ))) \
      ).txt"; \
  )

endif

# Строки для направления либо копирования вывода команды в лог загрузки файлов.
# Если имеет значение результат выполныения команды, предварительно должна
# быть выставлена опция "set -o pipefail".
ifeq ($(loadFileLog),)
  toLoadLogCmd     =
  copyToLoadLogCmd =
else
  toLoadLogCmd     = 2>&1 | unix2dos >> "$(loadFileLog)"
  copyToLoadLogCmd = \
		2>&1 | gawk '{ \
			print; fflush(); \
			printf( "%s\r\n", $$0) >> "$(loadFileLog)"; fflush( "$(loadFileLog)"); \
			}'
endif



# target: load-start-log.oms
# Записывает заголовок ( параметры make) в лог загрузки файлов.

load-start-log.oms:
	@if [[ -z "$(loadFileLog)" ]] ; then \
		echo "Error: not defined log filename loadFileLog." ; exit 10; \
	else \
		set -o pipefail; \
		mkdir -p "$(dir $(loadFileLog))" \
		&& { \
			   echo "moduleName          : $(moduleName)" \
			&& echo "modulePath          : $(OMS_MODULE_SVN_ROOT)" \
			&& echo "moduleInitialPath   : $(OMS_MODULE_INITIAL_SVN_PATH)" \
			&& echo "moduleVersion       : $(moduleVersion)" \
			&& echo "SVN path            : $(OMS_SVN_FILE_PATH)" \
			&& echo "SVN version info    : $(OMS_SVN_VERSION_INFO)" \
			&& echo "" \
			&& echo "module OMS version    : $(OMS_VERSION) ( rev. $(omsRevision), $(omsChangeDate))" \
			&& { \
					omsLoadVersion=$$(oms-load --version | tr "\n\r" "  ") \
			  && usedOmsVersion=$${omsLoadVersion#oms-load (OMS) } \
				&& usedOmsVersion=$${usedOmsVersion%% *} \
				&& usedOmsRevision=$${omsLoadVersion#*File revision*: } \
				&& usedOmsRevision=$${usedOmsRevision%% *} \
				&& usedOmsChangeDate=$${omsLoadVersion#*File change date*: } \
				&& usedOmsChangeDate=$${usedOmsChangeDate:0:10} \
				&& echo "installed OMS version : $$usedOmsVersion ( rev. $$usedOmsRevision, $$usedOmsChangeDate)" \
				; } \
			&& echo "" \
			&& echo "process ID          : $(OMS_PROCESS_ID)" \
			&& echo "process start time  : $(OMS_PROCESS_START_TIME)" \
			&& echo "" \
			&& echo "*** make option" \
			&& echo "MAKECMDGOALS        : $(MAKECMDGOALS)" \
			&& if [[ "$(origin MODULE_VERSION)" == "command line" ]]; then \
			   echo "MODULE_VERSION      : $(MODULE_VERSION)"; \
			   fi \
			&& echo "INSTALL_VERSION     : $(INSTALL_VERSION)" \
			&& echo "loadUserList        :$(loadUserList)" \
			&& echo "loadOperator        : $(loadOperatorName)" \
			&& if [[ -n "$(grantGoals)" ]]; then \
			   echo "GRANT_SCRIPT        : $(GRANT_SCRIPT)" \
			&& echo "TO_USERNAME         : $(TO_USERNAME)"; \
			   fi \
			&& echo "FILE_MASK           : $(FILE_MASK)" \
			&& echo "LOAD_FILE_MASK      : $(LOAD_FILE_MASK)" \
			&& if [[ "$(LOCAL_DB_DIR)" != "-" ]]; then \
			   echo "LOCAL_DB_DIR        : $(LOCAL_DB_DIR)"; \
			   fi \
			&& if [[ "$(LOCAL_USER_DIR)" != "-" ]]; then \
			   echo "LOCAL_USER_DIR      : $(LOCAL_USER_DIR)"; \
			   fi \
			&& echo "SKIP_FILE_MASK      : $(SKIP_FILE_MASK)" \
			&& echo "SQL_DEFINE          : $(subst ",\",$(SQL_DEFINE))" \
			&& echo "PLSQL_WARNINGS      : $(PLSQL_WARNINGS)" \
			&& if [[ -n "$(BATCH_MASK)" ]]; then \
			   echo "BATCH_MASK          : $(BATCH_MASK)"; \
			   fi \
			&& if [[ -n "$(SKIP_BATCH_MASK)" ]]; then \
			   echo "SKIP_BATCH_MASK     : $(SKIP_BATCH_MASK)"; \
			   fi \
			&& if [[ -n "$(SKIP_LOAD_OPTION)" ]]; then \
			   echo "SKIP_LOAD_OPTION    : $(SKIP_LOAD_OPTION)"; \
			   fi \
			&& if [[ -n "$(SKIP_CHECK_JOB)" ]]; then \
			   echo "SKIP_CHECK_JOB      : $(SKIP_CHECK_JOB)"; \
			   fi \
			&& if [[ -n "$(UPDATE_OPTION_VALUE)" ]]; then \
			   echo "UPDATE_OPTION_VALUE    : $(UPDATE_OPTION_VALUE)"; \
			   fi \
			&& if [[ -n "$(UPDATE_SCHEDULE)" ]]; then \
			   echo "UPDATE_SCHEDULE     : $(UPDATE_SCHEDULE)"; \
			   fi \
			&& echo "make flags          : $(call getMakeFlagList)" \
			&& if [[ "$(OMS_DEBUG_LEVEL)" != "0" ]]; then \
			   echo "OMS_DEBUG_LEVEL     : $(OMS_DEBUG_LEVEL)"; \
			   fi \
			&& if [[ "$(OMS_INSTALL_DATA_DIR)" != "/usr/local/share/oms" ]]; then \
			   echo "OMS_INSTALL_DATA_DIR: $(OMS_INSTALL_DATA_DIR)"; \
			   fi \
			&& if [[ "$(OMS_SAVE_FILE_INSTALL_INFO)" != "1" ]]; then \
			   echo "OMS_SAVE_FILE_INSTALL_INFO: $(OMS_SAVE_FILE_INSTALL_INFO)"; \
			   fi \
			$(foreach v,$(installAddonOptionList), \
			&& if [[ -n "$($(v))" ]]; then printf "%-20s: %s\n" "$(v)" "$($(v))"; fi \
			) \
			&& echo ""; \
			} $(toLoadLogCmd) \
		&& echo "start log: $(loadFileLog)"; \
	fi



# Удаляет из списка игнорируемые из-за отсутствия LOAD_DB* загружаемые файлы.
#
# Параметры:
# $(1)    - список загружаемых файлов
#
filterOutZeroDbTarget = $(filter-out %.,$(1))

# Выделяет имя файла из имени файла для загрузки.
# Из переданных имен удаляется расширение, определяющее тип загрузки, и
# расширение, определяющее базу для загрузки.
#
# Параметры:
# $(1)    - список файлов для загрузки
#
# Возврат:
# - список имен файлов
#
getTargetFileName = \
  $(foreach t,$(1) \
    ,$(foreach u,$(loadUserListReal) \
      ,$(foreach e,$(loadExt) $(runExt),$(strip \
        $(if $(filter %.$(u)$(e),$(t)),$(patsubst %.$(u)$(e),%,$(t)),) \
      )) \
    ) \
  )

# Реально загружаемые файлы.
# Исключаются:
# - игнорируемые из-за отсутствия БД;
# - не удовлетворяющие LOAD_FILE_MASK, если она задана;
# - не удовлетворяющие FILE_MASK, если она задана;
# - удовлетворяющие SKIP_FILE_MASK, если она задана;
#
loadTargetReal := \
  $(call filterLoadFileTarget,$(call filterOutZeroDbTarget,$(loadTarget)))



# target: load.oms
# Загружает файлы в БД.

load.oms: $(loadTargetReal)



# target: load-clean.oms
# Удаляет временные файлы, созданные при загрузке в БД.

load-clean.oms:
	-@rm -rf $(loadDir)/*



#
# group: Установка модуля в БД
#

# Настройка передачи параметров скрипту проверки блокировок oms-check-load.sql.
# Файлы для проверки берутся из installCheckLockTarget ( за исключением файлов,
# не загружаемых из-за FILE_MASK, LOAD_FILE_MASK и SKIP_FILE_MASK) и
# mandatoryCheckLockTarget.
# Промежуточная переменная используется для кэширования вычислений.

ifneq ($(call filterGoals, install install-before install-before.oms),)

  checkLockArgumentList := \
    $(foreach u, $(loadUserListReal) \
      ,$(call getNzArgumentDefine,oms-check-lock.sql.$(u)$(runExt),"$(strip \
          $(sort $(notdir \
            $(filter %.pks %.pkb %.prc %.snp %.tab %typ %tyb %.vw, \
              $(call filterOutFileMask, \
                $(foreach e, $(loadExt) $(runExt), \
                  $(patsubst %.$(u)$(e),%, \
                    $(filter $(loadTargetReal), \
                      $(filter %.$(u)$(e),$(installCheckLockTarget)) \
                    ) \
                    $(filter-out $(loadTarget), \
                      $(filter %.$(u)$(e),$(installCheckLockTarget)) \
                    ) \
                  ) \
                ) \
              ) \
              $(foreach e, $(loadExt) $(runExt), \
                $(patsubst %.$(u)$(e),%, \
                  $(filter %.$(u)$(e),$(mandatoryCheckLockTarget)) \
                ) \
              ) \
            ) \
          )) \
        )") \
    )

  loadArgumentList += $(checkLockArgumentList)

endif

# Исключаем загрузку скрипта oms-check-lock.sql при отсутствии аргументов.
installBeforeTargetReal = $(strip \
  $(foreach t, $(call filterOutZeroDbTarget,$(installBeforeTarget)), \
    $(if $(filter oms-check-lock.sql.%$(runExt),$(t)), \
      $(call ifArgumentDefined,$(t),$(loadArgumentList)) \
      , $(t) \
    ) \
  ))



# target: install-before.oms
# Выполняет предварительные действия перед установкой.

install-before.oms: \
  load-start-log.oms \
  $(installBeforeTargetReal)



# target: install-schema.oms
# Устанавливает объекты схемы в БД.

install-schema.oms: \
  load-start-log.oms \
  $(call filterOutZeroDbTarget,$(installSchemaTarget))



# target: install-load.oms
# Загружает объекты в БД при выполнении установки.

install-load.oms: \
  load-start-log.oms \
  load.oms



# target: install-data.oms
# Загружает установочные данные в БД.

install-data.oms: \
  load-start-log.oms \
  $(call filterOutZeroDbTarget,$(installDataTarget))



# Реально загружаемые файлы батчей.
# Исключаются:
# - игнорируемые из-за отсутствия БД;
# - не удовлетворяющие SKIP_FILE_MASK, FILE_MASK, если они заданы;
# - не удовлетворяющие BATCH_MASK, SKIP_BATCH_MASK, если они заданы и если файл
#   относится к какому либо батчу ( в списке файлов для загрузки в той же
#   директории присутствует batch.xml);
#
installBatchTargetReal = $(call filterInstallBatchTarget, $(call filterOutZeroDbTarget,$(installBatchTarget)))



# target: install-batch.oms
# Устанавливает пакетные задания в БД.

install-batch.oms: \
  load-start-log.oms \
  $(installBatchTargetReal)



# target: install-after.oms
# Выполняет завершающие установку действия.

install-after.oms: \
  load-start-log.oms \
  $(call filterOutZeroDbTarget,$(installAfterTarget))



#
# Сохранение в БД информации о действиях по установке ( подцели %-save-info)
#

# Возвращает имя цели для сохранения информации о действии по установке в
# случае, если оно должно выполняться при текущем запуске make.
#
# Параметры:
# (1)                         - имя основной цели
#

# Не выполняем сохранение информации о действиях по основым целям ( например,
# install и uninstall, кроме grant) в случае, если не была указана
# устанавливаемая версия модуля ( для цели grant в этом случае по-умолчанию
# используется Last)
#
getSaveInfoGoal = $(if $(call filterGoals, \
  $(if $(if $(call nullif,grant,$(1)),$(OMS_MODULE_INSTALL_VERSION),1), \
    $(1) $(1).oms) \
  $(1)-save-info $(1)-save-info.oms \
),$(1)-save-info.oms)

# Возвращает выполняемые файлы для цели %-save-info.
#
# Параметры:
# (1)                         - имя основной цели
#
getSaveInfoTarget = \
  $(addprefix oms-save-$(1)-info.sql, \
    $(addprefix .,$(addsuffix $(runExt),$(loadUserList))))

# Возвращает реально выполняемые файлы для цели %-save-info
#
# Параметры:
# (1)                         - имя основной цели
#
getSaveInfoTargetReal = \
  $(sort $(filter-out %.-$(runExt),$(call getSaveInfoTarget,$(1))))

# Аргументы выполнения файлов для цели %-save-info
#
# Параметры:
# (1)                         - имя основной цели
# (2)                         - дополнительные аргументы ( передаются скрипту
#                               начиная со второй позиции)
#
getSaveInfoArgumentList = \
  $(foreach f,$(call getSaveInfoTargetReal,$(1)), \
    $(call getArgumentDefine,$(f),"$(subst $(space),:,$(strip \
        $(call wordPosition,$(f),$(call getSaveInfoTarget,$(1))) \
      ))"$(if $(2), $(2))))



# target: install-save-info.oms
# Сохраняет в БД информацию об установке модуля.

ifneq ($(call getSaveInfoGoal,install),)

  loadArgumentList += \
    $(call getSaveInfoArgumentList,install)

endif

install-save-info.oms: \
  load-start-log.oms \
  $(call getSaveInfoTargetReal,install)



# target: install.oms
# Устанавливает модуль в БД.

install.oms:                            \
    load-start-log.oms                  \
    install-before.oms                  \
    install-schema.oms                  \
    install-load.oms                    \
    install-data.oms                    \
    install-batch.oms                   \
    install-after.oms                   \
    $(call getSaveInfoGoal,install)     \

	@echo -e "\ninstall: finished" $(toLoadLogCmd)



#
# group: Тестирование модуля
#

# target: install-test.oms
# Загружает объекты для тестирования в БД.
#
install-test.oms: \
  $(call filterOutZeroDbTarget,$(installTestTarget))



# target: test.oms
# Выполняет запуск скрипта по тестированию модуля.
#
test.oms: \
  $(call filterOutZeroDbTarget,$(testTarget))



#
# group: Отмена установки модуля в БД
#

# Реально загружаемые файлы при отмене установки.
uninstallLoadTargetReal := \
  $(call filterOutFileMask,$(call filterOutZeroDbTarget,$(uninstallLoadTarget)))


# Настройка передачи параметров скрипту проверки блокировок oms-check-load.sql.
# Файлы для проверки берутся из uninstallCheckLockTarget ( за исключением
# файлов, не загружаемых из-за FILE_MASK, SKIP_FILE_MASK) и
# mandatoryCheckLockTarget.  Промежуточная переменная используется для
# кэширования вычислений.

ifneq ($(call filterGoals, uninstall uninstall-before uninstall-before.oms),)

  uninstallCheckLockArgumentList := \
    $(foreach u, $(loadUserListReal) \
      ,$(call getNzArgumentDefine,oms-check-lock.sql.$(u)$(runExt),"$(strip \
          $(sort $(notdir \
            $(filter %.pks %.pkb %.prc %.snp %.tab %typ %tyb %.vw, \
              $(call filterOutFileMask, \
                $(foreach e, $(loadExt) $(runExt), \
                  $(patsubst %.$(u)$(e),%, \
                    $(filter $(uninstallLoadTargetReal), \
                      $(filter %.$(u)$(e),$(uninstallCheckLockTarget)) \
                    ) \
                    $(filter-out $(uninstallLoadTarget), \
                      $(filter %.$(u)$(e),$(uninstallCheckLockTarget)) \
                    ) \
                  ) \
                ) \
              ) \
              $(foreach e, $(loadExt) $(runExt), \
                $(patsubst %.$(u)$(e),%, \
                  $(filter %.$(u)$(e),$(mandatoryCheckLockTarget)) \
                ) \
              ) \
            ) \
          )) \
        )") \
    )

  loadArgumentList += $(uninstallCheckLockArgumentList)

endif

# Исключаем загрузку скрипта oms-check-lock.sql при отсутствии аргументов.
uninstallBeforeTargetReal = $(strip \
  $(foreach t, $(call filterOutZeroDbTarget,$(uninstallBeforeTarget)), \
    $(if $(filter oms-check-lock.sql.%$(runExt),$(t)), \
      $(call ifArgumentDefined,$(t),$(loadArgumentList)) \
      , $(t) \
    ) \
  ))



# target: uninstall-before.oms
# Выполняет предварительные действия перед отменой установки.

uninstall-before.oms: \
  load-start-log.oms \
  $(uninstallBeforeTargetReal)



# target: uninstall-schema.oms
# Отменяет изменения, внесенные в объекты схемы при выполнении установки.

uninstall-schema.oms: \
  load-start-log.oms \
  $(call filterOutZeroDbTarget,$(uninstallSchemaTarget))



# target: uninstall-load.oms
# Загружает предыдущие версии объектов в БД при отмене установки.

uninstall-load.oms: \
  load-start-log.oms \
  $(uninstallLoadTargetReal)



# target: uninstall-data.oms
# Отменяет изменения, внесенные при загрузке установочных данные в БД.

uninstall-data.oms: \
  load-start-log.oms \
  $(call filterOutZeroDbTarget,$(uninstallDataTarget))



# target: uninstall-after.oms
# Выполняет завершающие отмену установки действия.

uninstall-after.oms: \
  load-start-log.oms \
  $(call filterOutZeroDbTarget,$(uninstallAfterTarget))



# target: uninstall-save-info.oms
# Сохраняет в БД информацию об отмене установки модуля.

ifneq ($(call getSaveInfoGoal,uninstall),)

  loadArgumentList += \
    $(call getSaveInfoArgumentList,uninstall,"$(UNINSTALL_RESULT_VERSION)")

endif

uninstall-save-info.oms: \
  load-start-log.oms \
  $(call getSaveInfoTargetReal,uninstall)



# target: uninstall.oms
# Отменяет установку версии модуля в БД.

uninstall.oms:                            \
    load-start-log.oms                  \
    uninstall-before.oms                  \
    uninstall-schema.oms                  \
    uninstall-load.oms                    \
    uninstall-data.oms                    \
    uninstall-after.oms                   \
    $(call getSaveInfoGoal,uninstall)     \

	@echo -e "\nuninstall: finished" $(toLoadLogCmd)



#
# group: Выдача прав пользователям БД
#

# Реально выполняемые скрипты для выдачи прав.
grantTargetReal = $(call filterOutZeroDbTarget,$(grantTarget))

# Настройка передачи параметров скриптам выдачи прав.

ifneq ($(call filterGoals, grant grant.oms),)

  loadArgumentList += \
    $(call getArgumentDefine,$(grantTargetReal),"$(TO_USERNAME)")

endif

# target: grant-exec.oms
# Выполняет скрипты выдачи прав.

grant-exec.oms:                         \
  load-start-log.oms                    \
  $(grantTargetReal)                    \

	@set -o pipefail; \
	if [[ -z "$(firstword $(grantTargetReal))" ]]; then \
		echo -e "Error: grant script not found."; exit 11; \
	fi $(copyToLoadLogCmd)



# target: grant-save-info.oms
# Сохраняет в БД информацию о выдаче прав пользователю.

ifneq ($(call getSaveInfoGoal,grant),)

  loadArgumentList += \
    $(call getSaveInfoArgumentList,grant,"$(call getInstallVersion,$(grantVersion))" "$(call getIsFullInstall,$(grantVersion))" "$(grantScript)" "$(TO_USERNAME)")

endif

grant-save-info.oms: \
  load-start-log.oms \
  $(call getSaveInfoTargetReal,grant)



# target: grant.oms
# Выдает права пользователю БД.

grant.oms: \
  load-start-log.oms \
  grant-exec.oms \
  $(call getSaveInfoGoal,grant) \

	@echo -e "\ngrant: finished" $(toLoadLogCmd)

