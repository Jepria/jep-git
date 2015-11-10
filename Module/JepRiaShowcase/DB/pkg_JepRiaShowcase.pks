create or replace package pkg_JepRiaShowcase is
/* package: pkg_JepRiaShowcase
  Интерфейсный пакет модуля JepRiaShowcase.

  SVN root: JEP/Module/JepRiaShowcase
*/



/* group: Константы */

/* const: Module_Name
  Название модуля, к которому относится пакет.
*/
Module_Name constant varchar2(30) := 'JepRiaShowcase';



/* group: Роли */

/* const: Goods_RoleSName
  Короткое название роли "Редактирование данных по товарам".
*/
Goods_RoleSName constant varchar2(50) :=
  'JrsEditGoods'
;

/* const: Request_RoleSName
  Короткое название роли "Редактирование данных по запросам на закупку".
*/
Request_RoleSName constant varchar2(50) :=
  'JrsEditRequest'
;

/* const: RequestProcess_RoleSName
  Короткое название роли
  "Редактирование данных по обработке запросов на закупку".
*/
RequestProcess_RoleSName constant varchar2(50) :=
  'JrsEditRequestProcess'
;

/* const: ShopGoods_RoleSName
  Короткое название роли "Редактирование данных по товарам в магазинах".
*/
ShopGoods_RoleSName constant varchar2(50) :=
  'JrsEditShopGoods'
;

/* const: Supplier_RoleSName
  Короткое название роли "Редактирование данных по поставщикам".
*/
Supplier_RoleSName constant varchar2(50) :=
  'JrsEditSupplier'
;



/* group: Функции */



/* group: Поставщик */

/* pfunc: createSupplier
  Создает поставщика.

  Параметры:
  supplierName                - наименование поставщика
  contractFinishDate          - дата до которой действует договор
                                ( с точностью до дня, включительно)
  exclusiveSupplierFlag       - безальтернативный поставщик ( 1 да, 0 нет)
                                ( по умолчанию 0)
  privilegeSupplierFlag       - привилегированный поставщик ( 1 да, 0 нет)
                                ( по умолчанию 0)
  phoneNumber                 - телефон
                                ( по умолчанию отсутствует)
  faxNumber                   - факс
                                ( по умолчанию отсутствует)
  bankBic                     - банк
                                ( по умолчанию отсутствует)
  recipientName               - получатель
                                ( по умолчанию отсутствует)
  settlementAccount           - расчетный счет
                                ( по умолчанию отсутствует)
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат:
  Id созданной записи.

  ( <body::createSupplier>)
*/
function createSupplier(
  supplierName varchar2
  , contractFinishDate date
  , exclusiveSupplierFlag integer := null
  , privilegeSupplierFlag integer := null
  , phoneNumber varchar2 := null
  , faxNumber varchar2 := null
  , bankBic varchar2 := null
  , recipientName varchar2 := null
  , settlementAccount varchar2 := null
  , operatorId integer := null
)
return integer;

/* pproc: updateSupplier
  Изменяет данные поставщика.

  Параметры:
  supplierId                  - Id поставщика
  supplierName                - наименование поставщика
  contractFinishDate          - дата до которой действует договор
                                ( с точностью до дня, включительно)
  exclusiveSupplierFlag       - безальтернативный поставщик ( 1 да, 0 нет)
  privilegeSupplierFlag       - привилегированный поставщик ( 1 да, 0 нет)
  phoneNumber                 - телефон
  faxNumber                   - факс
  bankBic                     - банк
  recipientName               - получатель
  settlementAccount           - расчетный счет
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  ( <body::updateSupplier>)
*/
procedure updateSupplier(
  supplierId integer
  , supplierName varchar2
  , contractFinishDate date
  , exclusiveSupplierFlag integer
  , privilegeSupplierFlag integer
  , phoneNumber varchar2
  , faxNumber varchar2
  , bankBic varchar2
  , recipientName varchar2
  , settlementAccount varchar2
  , operatorId integer := null
);

/* pproc: deleteSupplier
  Удаляет поставщика.

  Параметры:
  supplierId                  - Id поставщика
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  ( <body::deleteSupplier>)
*/
procedure deleteSupplier(
  supplierId integer
  , operatorId integer := null
);

/* pfunc: findSupplier
  Поиск поставщика.

  Параметры:
  supplierId                  - Id поставщика
                                ( по умолчанию без ограничений)
  supplierName                - наименование поставщика
                                ( поиск по like без учета регистра)
                                ( по умолчанию без ограничений)
  contractFinishDateFrom      - дата до которой действует договор, от
                                ( с точностью до дня, включительно)
                                ( по умолчанию без ограничений)
  contractFinishDateTo        - дата до которой действует договор, до
                                ( с точностью до дня, включительно)
                                ( по умолчанию без ограничений)
  exclusiveSupplierFlag       - безальтернативный поставщик ( 1 да, 0 нет)
                                ( по умолчанию без ограничений)
  privilegeSupplierFlag       - привилегированный поставщик ( 1 да, 0 нет)
                                ( по умолчанию без ограничений)
  maxRowCount                 - максимальное число возвращаемых записей
                                ( по умолчанию без ограничений)
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат ( курсор):
  supplier_id                 - Id поставщика
  supplier_name               - наименование поставщика
  contract_finish_date        - дата до которой действует договор
  exclusive_supplier_flag     - безальтернативный поставщик ( 1 да, 0 нет)
  privilege_supplier_flag     - привилегированный поставщик ( 1 да, 0 нет)
  supplier_description        - описание поставщика
  phone_number                - телефон
  fax_number                  - факс
  bank_bic                    - банк
  bankname                    - наименование банка
  ks                          - корсчет
  recipient_name              - получатель
  settlement_account          - расчетный счет

  Замечания:
  - возвращаемые записи отсортированы по supplier_name;

  ( <body::findSupplier>)
*/
function findSupplier(
  supplierId integer := null
  , supplierName varchar2 := null
  , contractFinishDateFrom date := null
  , contractFinishDateTo date := null
  , exclusiveSupplierFlag integer := null
  , privilegeSupplierFlag integer := null
  , maxRowCount integer := null
  , operatorId integer := null
)
return sys_refcursor;



/* group: Товар */

/* pfunc: createGoods
  Создает товар.

  Параметры:
  supplierId                  - Id поставщика
  goodsName                   - наименование товара
  goodsTypeCode               - код типа товара
  unitCode                    - код единицы измерения
  purchasingPrice             - закупочная цена
  motivationTypeCode          - код типа мотивации
                                ( по умолчанию код для "Обычная мотивация")
  goodsPhotoMimeType          - MIME-тип файла с фотографией товара
  goodsPhotoExtension         - расширение файла с фотографией товара
  goodsPortfolioMimeType      - MIME-тип файла со спецификацией товара
  goodsPortfolioExtension     - расширение файла со спецификацией товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат:
  Id созданной записи.

  ( <body::createGoods>)
*/
function createGoods(
  supplierId integer
  , goodsName varchar2
  , goodsTypeCode varchar2
  , unitCode varchar2
  , purchasingPrice number
  , motivationTypeCode varchar2 := null
  , goodsPhotoMimeType varchar2 := null
  , goodsPhotoExtension varchar2 := null
  , goodsPortfolioMimeType varchar2 := null
  , goodsPortfolioExtension varchar2 := null
  , operatorId integer := null
)
return integer;

/* pproc: updateGoods
  Изменяет данные товара.

  Параметры:
  goodsId                     - Id товара
  supplierId                  - Id поставщика
  goodsName                   - наименование товара
  goodsTypeCode               - код типа товара
  unitCode                    - код единицы измерения
  purchasingPrice             - закупочная цена
  motivationTypeCode          - код типа мотивации
  goodsPhotoMimeType          - MIME-тип файла с фотографией товара
  goodsPhotoExtension         - расширение файла с фотографией товара
  goodsPortfolioMimeType      - MIME-тип файла со спецификацией товара
  goodsPortfolioExtension     - расширение файла со спецификацией товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  ( <body::updateGoods>)
*/
procedure updateGoods(
  goodsId integer
  , supplierId integer
  , goodsName varchar2
  , goodsTypeCode varchar2
  , unitCode varchar2
  , purchasingPrice number
  , motivationTypeCode varchar2
  , goodsPhotoMimeType varchar2
  , goodsPhotoExtension varchar2
  , goodsPortfolioMimeType varchar2
  , goodsPortfolioExtension varchar2
  , operatorId integer := null
);

/* pproc: deleteGoods
  Удаляет товар.

  Параметры:
  goodsId                     - Id товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  ( <body::deleteGoods>)
*/
procedure deleteGoods(
  goodsId integer
  , operatorId integer := null
);

/* pfunc: findGoods
  Поиск товара.

  Параметры:
  goodsIdList                 - Id товара ( список через запятую)
                                ( по умолчанию без ограничений)
  supplierId                  - Id поставщика
                                ( по умолчанию без ограничений)
  goodsName                   - наименование товара
                                ( поиск по like без учета регистра)
                                ( по умолчанию без ограничений)
  goodsTypeCode               - код типа товара
                                ( по умолчанию без ограничений)
  goodsSegmentCodeList        - код сегмента товара ( список через запятую)
                                ( по умолчанию без ограничений)
  goodsCatalogIdList          - Id раздела каталога ( список через запятую)
                                ( при этом также учитываются товары,
                                  относящиеся к подчиненным разделам каталога)
                                ( по умолчанию без ограничений)
  maxRowCount                 - максимальное число возвращаемых записей
                                ( по умолчанию без ограничений)
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат ( курсор):
  goods_id                    - Id товара
  supplier_id                 - Id поставщика
  supplier_name               - наименование поставщика
  goods_name                  - наименование товара
  goods_type_code             - код типа товара
  goods_type_name             - наименование типа товара
  unit_code                   - код единицы измерения
  unit_name                   - наименование единицы измерения
  purchasing_price            - закупочная цена
  motivation_type_code        - код типа мотивации
  motivation_type_name        - наименование типа мотивации
  goods_photo                 - Фотография товара
  goods_photo_mime_type       - MIME-тип файла с фотографией товара
  goods_photo_extension       - расширение файла с фотографией товара
  goods_portfolio             - спецификация товара
  goods_portfolio_mime_type   - MIME-тип файла со спецификацией товара
  goods_portfolio_extension   - расширение файла со спецификацией товара

  Замечания:
  - возвращаемые записи отсортированы по goods_name;

  ( <body::findGoods>)
*/
function findGoods(
  goodsIdList varchar2 := null
  , supplierId integer := null
  , goodsName varchar2 := null
  , goodsTypeCode varchar2 := null
  , goodsSegmentCodeList varchar2 := null
  , goodsCatalogIdList varchar2 := null
  , maxRowCount integer := null
  , operatorId integer := null
)
return sys_refcursor;

/* pfunc: getGoods
  Получение списка товаров.

  Параметры:
  goodsName                   - наименование товара
                                ( поиск по like без учета регистра)
                                ( по умолчанию без ограничений)
  maxRowCount                 - максимальное число возвращаемых записей
                                ( по умолчанию без ограничений)

  Возврат ( курсор):
  goods_id                    - Id товара
  goods_name                  - наименование товара

  Замечания:
  - возвращаемые записи отсортированы по goods_name;

  ( <body::getGoods>)
*/
function getGoods(
  goodsName varchar2 := null
  , maxRowCount integer := null
)
return sys_refcursor;



/* group: Сегменты для товара */

/* pproc: createGoodsSegmentLink
  Добавляет сегмент для товара.

  Параметры:
  goodsId                     - Id товара
  goodsSegmentCode            - код сегмента товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  ( <body::createGoodsSegmentLink>)
*/
procedure createGoodsSegmentLink(
  goodsId integer
  , goodsSegmentCode varchar2
  , operatorId integer := null
);

/* pproc: deleteGoodsSegmentLink
  Удаляет сегмент для товара.

  Параметры:
  goodsId                     - Id товара
  goodsSegmentCode            - код сегмента товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  ( <body::deleteGoodsSegmentLink>)
*/
procedure deleteGoodsSegmentLink(
  goodsId integer
  , goodsSegmentCode varchar2
  , operatorId integer := null
);

/* pfunc: getGoodsSegmentLink
  Возвращает сегменты для товара.

  Параметры:
  goodsId                     - Id товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат ( курсор):
  goods_id                    - Id товара
  goods_segment_code          - код сегмента товара
  goods_segment_name          - наименование сегмента товара

  Замечания:
  - возвращаемые записи отсортированы по goods_segment_name;

  ( <body::getGoodsSegmentLink>)
*/
function getGoodsSegmentLink(
  goodsId integer
  , operatorId integer := null
)
return sys_refcursor;



/* group: Разделы каталога для товара */

/* pproc: setGoodsCatalogLink
  Устанавливает разделы каталога, к которым относится товар.

  Параметры:
  goodsId                     - Id товара
  goodsCatalogIdList          - Id разделов каталога, к которым относится
                                товар ( список через запятую, null при
                                отсутствии разделов)
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  ( <body::setGoodsCatalogLink>)
*/
procedure setGoodsCatalogLink(
  goodsId integer
  , goodsCatalogIdList varchar2
  , operatorId integer := null
);

/* pfunc: getGoodsCatalog
  Возвращает разделы каталога товаров.

  Параметры:
  parentGoodsCatalogId        - Id родительского раздела каталога
                                 ( null для получения разделов верхнего уровня)
                                 ( по умолчанию null)
  goodsId                     - Id товара, для которого возвращается информация
                                по связанным с ним разделам каталога в
                                полях goods_link_flag и
                                descendant_goods_link_flag
                                ( null если эти поля не заполняются
                                  ( по умолчанию null))

  Возврат ( курсор):
  goods_catalog_id            - Id раздела каталога
  goods_catalog_name          - наименование раздела каталога
  has_child_flag              - флаг наличия дочерних разделов каталога
                                ( 1 да, 0 нет)
  goods_link_flag             - флаг принадлежности указанного товара к
                                разделу каталога
                                ( 1 да, 0 нет, null если не указан goodsId)
  descendant_goods_link_flag  - флаг принадлежности указанного товара к
                                подразделу ( потомку) раздела каталога
                                ( 1 да, 0 нет, null если не указан goodsId)

  Замечания:
  - возвращаемые записи отсортированы по goods_catalog_name;

  ( <body::getGoodsCatalog>)
*/
function getGoodsCatalog(
  parentGoodsCatalogId integer := null
  , goodsId integer := null
)
return sys_refcursor;



/* group: Товар в магазине */

/* pfunc: createShopGoods
  Создает запись для товара в магазине.

  Параметры:
  shopId                      - Id магазина
  goodsId                     - Id товара
  goodsQuantity               - количество товара
  sellPrice                   - отпускная цена
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат:
  Id созданной записи.

  ( <body::createShopGoods>)
*/
function createShopGoods(
  shopId integer
  , goodsId integer
  , goodsQuantity number
  , sellPrice number
  , operatorId integer := null
)
return integer;

/* pproc: updateShopGoods
  Изменяет данные о товаре в магазине.

  Параметры:
  shopGoodsId                 - Id товара в магазине
  goodsQuantity               - количество товара
  sellPrice                   - отпускная цена
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  ( <body::updateShopGoods>)
*/
procedure updateShopGoods(
  shopGoodsId integer
  , goodsQuantity number
  , sellPrice number
  , operatorId integer := null
);

/* pproc: deleteShopGoods
  Удаляет запись о товаре в магазине.

  Параметры:
  shopGoodsId                 - Id товара в магазине
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  ( <body::deleteShopGoods>)
*/
procedure deleteShopGoods(
  shopGoodsId integer
  , operatorId integer := null
);

/* pfunc: findShopGoods
  Поиск товара в магазине.

  Параметры:
  shopGoodsId                 - Id товара в магазине
                                ( по умолчанию без ограничений)
  shopId                      - Id магазина
                                ( по умолчанию без ограничений)
  goodsId                     - Id товара
                                ( по умолчанию без ограничений)
  maxRowCount                 - максимальное число возвращаемых записей
                                ( по умолчанию без ограничений)
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат ( курсор):
  shop_goods_id               - Id товара в магазине
  shop_id                     - Id магазина
  shop_name                   - наименование магазина
  goods_id                    - Id товара
  goods_name                  - наименование товара
  goods_quantity              - количество товара
  sell_price                  - отпускная цена

  Замечания:
  - возвращаемые записи отсортированы по shop_name, goods_name;

  ( <body::findShopGoods>)
*/
function findShopGoods(
  shopGoodsId integer := null
  , shopId integer := null
  , goodsId integer := null
  , maxRowCount integer := null
  , operatorId integer := null
)
return sys_refcursor;



/* group: Запрос на закупку */

/* pfunc: createRequest
  Создает запрос на закупку.

  Параметры:
  shopId                      - Id магазина
  goodsId                     - Id товара
  goodsQuantity               - количество товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат:
  Id созданной записи.

  ( <body::createRequest>)
*/
function createRequest(
  shopId integer
  , goodsId integer
  , goodsQuantity number
  , operatorId integer := null
)
return integer;

/* pproc: updateRequest
  Изменяет запрос на закупку.

  Параметры:
  requestId                   - Id запроса
  requestStatusCode           - код статуса запроса
  goodsId                     - Id товара
  goodsQuantity               - количество товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  ( <body::updateRequest>)
*/
procedure updateRequest(
  requestId integer
  , requestStatusCode varchar2
  , goodsId integer
  , goodsQuantity number
  , operatorId integer := null
);

/* pproc: deleteRequest
  Удаляет запрос на закупку.

  Параметры:
  requestId                   - Id запроса
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  ( <body::deleteRequest>)
*/
procedure deleteRequest(
  requestId integer
  , operatorId integer := null
);

/* pfunc: findRequest
  Поиск запроса на закупку.

  Параметры:
  requestId                   - Id запроса
                                ( по умолчанию без ограничений)
  shopId                      - Id магазина
                                ( по умолчанию без ограничений)
  requestDateFrom             - дата создания запроса, от
                                ( с точностью до дня, включительно)
                                ( по умолчанию без ограничений)
  requestDateTo               - дата создания запроса, до
                                ( с точностью до дня, включительно)
                                ( по умолчанию без ограничений)
  requestStatusCode           - код статуса запроса
                                ( по умолчанию без ограничений)
  goodsId                     - Id товара
                                ( по умолчанию без ограничений)
  maxRowCount                 - максимальное число возвращаемых записей
                                ( по умолчанию без ограничений)
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат ( курсор):
  request_id                  - Id запроса
  shop_id                     - Id магазина
  shop_name                   - наименование магазина
  request_date                - дата создания запроса
  request_status_code         - код статуса запроса
  request_status_name         - наименование статуса запроса
  goods_id                    - Id товара
  goods_name                  - наименование товара
  goods_quantity              - количество товара

  Замечания:
  - возвращаемые записи отсортированы по shop_name, request_id;

  ( <body::findRequest>)
*/
function findRequest(
  requestId integer := null
  , shopId integer := null
  , requestDateFrom date := null
  , requestDateTo date := null
  , requestStatusCode varchar2 := null
  , goodsId integer := null
  , maxRowCount integer := null
  , operatorId integer := null
)
return sys_refcursor;



/* group: Обработка запроса */

/* pfunc: createRequestProcess
  Создает запись по обработке запроса на закупку.

  Параметры:
  requestId                   - Id запроса
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат:
  Id созданной записи.

  ( <body::createRequestProcess>)
*/
function createRequestProcess(
  requestId integer
  , operatorId integer := null
)
return integer;

/* pproc: deleteRequestProcess
  Удаляет запись по обработке запроса на закупку.

  Параметры:
  requestProcessId            - Id записи по обработке запроса
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  ( <body::deleteRequestProcess>)
*/
procedure deleteRequestProcess(
  requestProcessId integer
  , operatorId integer := null
);

/* pfunc: findRequestProcess
  Поиск записей по обработке запроса на закупку.

  Параметры:
  requestProcessId            - Id записи по обработке запроса
                                ( по умолчанию без ограничений)
  requestId                    - Id запроса
                                ( по умолчанию без ограничений)
  dateInsFrom                 - дата добавления записи, от
                                ( с точностью до дня, включительно)
                                ( по умолчанию без ограничений)
  dateInsTo                   - дата добавления записи, до
                                ( с точностью до дня, включительно)
                                ( по умолчанию без ограничений)
  insertOperatorId            - Id оператора, добавившего запись
                                ( по умолчанию без ограничений)
  maxRowCount                 - максимальное число возвращаемых записей
                                ( по умолчанию без ограничений)
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат ( курсор):
  request_process_id          - Id записи по обработке запроса
  process_comment             - комментарий к обработке запроса
  date_ins                    - дата добавления записи
  operator_id                 - Id оператора, добавившего запись
  operator_name               - ФИО оператора, добавившего запись

  Замечания:
  - возвращаемые записи отсортированы по request_process_id;

  ( <body::findRequestProcess>)
*/
function findRequestProcess(
  requestProcessId integer := null
  , requestId integer := null
  , dateInsFrom date := null
  , dateInsTo date := null
  , insertOperatorId integer := null
  , maxRowCount integer := null
  , operatorId integer := null
)
return sys_refcursor;



/* group: Справочники */

/* pfunc: getBank
  Возвращает список банков.

  Параметры:
  bankBic                     - банковский идентификационный код ( БИК)
                                ( поиск по like, по умолчанию без ограничений)
  maxRowCount                 - максимальное число возвращаемых записей
                                ( по умолчанию без ограничений)

  Возврат ( курсор):
  bic                         - банковский идентификационный код ( БИК)
  bankname                    - наименование банка
  ks                          - корсчет

  Замечания:
  - возвращаемые записи отсортированы по bankname;

  ( <body::getBank>)
*/
function getBank(
  bankBic varchar2 := null
  , maxRowCount integer := null
)
return sys_refcursor;

/* pfunc: getGoodsSegment
  Возвращает сегменты товаров.

  Возврат ( курсор):
  goods_segment_code          - код сегмента товара
  goods_segment_name          - наименование сегмента товара

  Замечания:
  - возвращаемые записи отсортированы по goods_segment_name;

  ( <body::getGoodsSegment>)
*/
function getGoodsSegment
return sys_refcursor;

/* pfunc: getGoodsType
  Возвращает типы товаров.

  Возврат ( курсор):
  goods_type_code           - код типа товара
  goods_type_name           - наименование типа товара

  Замечания:
  - возвращаемые записи отсортированы по goods_type_name;

  ( <body::getGoodsType>)
*/
function getGoodsType
return sys_refcursor;

/* pfunc: getMotivationType
  Возвращает типы мотивации.

  Возврат ( курсор):
  motivation_type_code        - код типа мотивации
  motivation_type_name        - наименование типа мотивации
  motivation_type_comment     - комментарий для типа мотивации

  Замечания:
  - возвращаемые записи отсортированы по motivation_type_name;

  ( <body::getMotivationType>)
*/
function getMotivationType
return sys_refcursor;

/* pfunc: getRequestStatus
  Возвращает статусы запроса на закупку.

  Возврат ( курсор):
  request_status_code         - код статуса запроса
  request_status_name         - наименование статуса запроса

  Замечания:
  - возвращаемые записи отсортированы по request_status_name;

  ( <body::getRequestStatus>)
*/
function getRequestStatus
return sys_refcursor;

/* pfunc: getShop
  Возвращает магазины.

  Параметры:
  shopName                    - наименование магазина
                                ( поиск по like без учета регистра)
                                ( по умолчанию без ограничений)
  maxRowCount                 - максимальное число возвращаемых записей
                                ( по умолчанию без ограничений)

  Возврат ( курсор):
  shop_id                     - Id магазина
  shop_name                   - наименование магазина

  Замечания:
  - возвращаемые записи отсортированы по shop_name;

  ( <body::getShop>)
*/
function getShop(
  shopName varchar2 := null
  , maxRowCount integer := null
)
return sys_refcursor;

/* pfunc: getUnit
  Возвращает единицы измерения.

  Возврат ( курсор):
  unit_code                   - код единицы измерения
  unit_short_name             - краткое наименование единицы измерения
  unit_name                   - наименование единицы измерения

  Замечания:
  - возвращаемые записи отсортированы по unit_name;

  ( <body::getUnit>)
*/
function getUnit
return sys_refcursor;

end pkg_JepRiaShowcase;
/
