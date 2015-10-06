create or replace package body pkg_JepRiaShowcase is
/* package body: pkg_JepRiaShowcase::body */



/* group: Константы */

/* iconst: Usual_MotivationTCode
  Код типа мотивации "Обычная мотивация".
*/
Usual_MotivationTCode constant varchar2(10) := 'USUAL';



/* group: Переменные */

/* ivar: logger
  Логер пакета.
*/
logger lg_logger_t := lg_logger_t.getLogger(
  moduleName    => Module_Name
  , objectName  => 'pkg_JepRiaShowcase'
);



/* group: Функции */




/* group: Поставщик */

/* iproc: lockSupplier
  Блокирует и возвращает запись с данными поставщика.

  Параметры:
  dataRec                     - данные записи ( возврат)
  supplierId                  - Id поставщика
*/
procedure lockSupplier(
  dataRec out nocopy jrs_supplier%rowtype
  , supplierId integer
)
is
begin
  select
    t.*
  into dataRec
  from
    jrs_supplier t
  where
    t.supplier_id = supplierId
  for update nowait;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при блокировке записи с данными поставщика ('
        || ' supplierId=' || supplierId
        || ').'
      )
    , true
  );
end lockSupplier;

/* func: createSupplier
  Создает поставщика.

  Параметры:
  supplierName                - наименование поставщика
  contractFinishDate          - дата до которой действует договор
                                ( с точностью до дня, включительно)
  exclusiveSupplierFlag       - безальтернативный поставщик ( 1 да, 0 нет)
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
*/
function createSupplier(
  supplierName varchar2
  , contractFinishDate date
  , exclusiveSupplierFlag integer := null
  , phoneNumber varchar2 := null
  , faxNumber varchar2 := null
  , bankBic varchar2 := null
  , recipientName varchar2 := null
  , settlementAccount varchar2 := null
  , operatorId integer := null
)
return integer
is

  -- Id созданной записи
  supplierId integer;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Supplier_RoleSName
  );
  insert into
    jrs_supplier
  (
    supplier_name
    , contract_finish_date
    , exclusive_supplier_flag
    , phone_number
    , fax_number
    , bank_bic
    , recipient_name
    , settlement_account
    , operator_id
  )
  values
  (
    supplierName
    , trunc( contractFinishDate)
    , exclusiveSupplierFlag
    , phoneNumber
    , faxNumber
    , bankBic
    , recipientName
    , settlementAccount
    , operatorId
  )
  returning
    supplier_id
  into
    supplierId
  ;
  return supplierId;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при создании поставщика ('
        || ' supplierName="' || supplierName || '"'
        || ', exclusiveSupplierFlag=' || exclusiveSupplierFlag
        || ').'
      )
    , true
  );
end createSupplier;

/* proc: updateSupplier
  Изменяет данные поставщика.

  Параметры:
  supplierId                  - Id поставщика
  supplierName                - наименование поставщика
  contractFinishDate          - дата до которой действует договор
                                ( с точностью до дня, включительно)
  exclusiveSupplierFlag       - безальтернативный поставщик ( 1 да, 0 нет)
  phoneNumber                 - телефон
  faxNumber                   - факс
  bankBic                     - банк
  recipientName               - получатель
  settlementAccount           - расчетный счет
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)
*/
procedure updateSupplier(
  supplierId integer
  , supplierName varchar2
  , contractFinishDate date
  , exclusiveSupplierFlag integer
  , phoneNumber varchar2
  , faxNumber varchar2
  , bankBic varchar2
  , recipientName varchar2
  , settlementAccount varchar2
  , operatorId integer := null
)
is

  -- Данные записи
  rec jrs_supplier%rowtype;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Supplier_RoleSName
  );
  lockSupplier(
    dataRec             => rec
    , supplierId        => supplierId
  );
  update
    jrs_supplier d
  set
    d.supplier_name               = supplierName
    , d.contract_finish_date      = trunc( contractFinishDate)
    , d.exclusive_supplier_flag   = exclusiveSupplierFlag
    , d.phone_number              = phoneNumber
    , d.fax_number                = faxNumber
    , d.bank_bic                  = bankBic
    , d.recipient_name            = recipientName
    , d.settlement_account        = settlementAccount
  where
    d.supplier_id = supplierId
  ;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при изменении данных поставщика ('
        || ' supplierId=' || supplierId
        || ').'
      )
    , true
  );
end updateSupplier;

/* proc: deleteSupplier
  Удаляет поставщика.

  Параметры:
  supplierId                  - Id поставщика
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)
*/
procedure deleteSupplier(
  supplierId integer
  , operatorId integer := null
)
is

  -- Данные записи
  rec jrs_supplier%rowtype;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Supplier_RoleSName
  );
  lockSupplier(
    dataRec             => rec
    , supplierId        => supplierId
  );
  delete
    jrs_supplier d
  where
    d.supplier_id = supplierId
  ;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при удалении поставщика ('
        || ' supplierId=' || supplierId
        || ').'
      )
    , true
  );
end deleteSupplier;

/* func: findSupplier
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
  maxRowCount                 - максимальное число возвращаемых записей
                                ( по умолчанию без ограничений)
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат ( курсор):
  supplier_id                 - Id поставщика
  supplier_name               - наименование поставщика
  contract_finish_date        - дата до которой действует договор
  exclusive_supplier_flag     - безальтернативный поставщик ( 1 да, 0 нет)
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
*/
function findSupplier(
  supplierId integer := null
  , supplierName varchar2 := null
  , contractFinishDateFrom date := null
  , contractFinishDateTo date := null
  , exclusiveSupplierFlag integer := null
  , maxRowCount integer := null
  , operatorId integer := null
)
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

  -- Динамически формируемый текст запроса
  dsql dyn_dynamic_sql_t := dyn_dynamic_sql_t( '
select
  a.*
from
  (
  select
    t.supplier_id
    , t.supplier_name
    , t.contract_finish_date
    , t.exclusive_supplier_flag
    , t.supplier_description
    , t.phone_number
    , t.fax_number
    , t.bank_bic
    , bb.bankname
    , bb.ks
    , t.recipient_name
    , t.settlement_account
  from
    jrs_supplier t
    left join v_bic_bank bb
      on bb.bic = t.bank_bic
  where
    $(condition)
  order by
    t.supplier_name
  ) a
where
  $(rownumCondition)
'
  );

-- findSupplier
begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Supplier_RoleSName
  );
  dsql.addCondition(
    't.supplier_id =', supplierId is null
  );
  dsql.addCondition(
    'upper( t.supplier_name) like upper( :supplierName) escape ''\'''
    , supplierName is null
  );
  dsql.addCondition(
    't.contract_finish_date >= trunc( :contractFinishDateFrom)'
    , contractFinishDateFrom is null
  );
  dsql.addCondition(
    't.contract_finish_date <= trunc( :contractFinishDateTo)'
    , contractFinishDateTo is null
  );
  dsql.addCondition(
    't.exclusive_supplier_flag =', exclusiveSupplierFlag is null
  );
  dsql.useCondition( 'condition');
  dsql.addCondition(
    'rownum <= :maxRowCount', maxRowCount is null
  );
  dsql.useCondition( 'rownumCondition');
  open rc for
    dsql.getSqlText()
  using
    supplierId
    , supplierName
    , contractFinishDateFrom
    , contractFinishDateTo
    , exclusiveSupplierFlag
    , maxRowCount
  ;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при поиске поставщика.'
      )
    , true
  );
end findSupplier;



/* group: Товар */

/* iproc: lockGoods
  Блокирует и возвращает запись с данными товара.

  Параметры:
  dataRec                     - данные записи ( возврат)
  goodsId                     - Id товара
*/
procedure lockGoods(
  dataRec out nocopy jrs_goods%rowtype
  , goodsId integer
)
is
begin
  select
    t.*
  into dataRec
  from
    jrs_goods t
  where
    t.goods_id = goodsId
  for update nowait;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при блокировке записи с данными товара ('
        || ' goodsId=' || goodsId
        || ').'
      )
    , true
  );
end lockGoods;

/* func: createGoods
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
return integer
is

  -- Id созданной записи
  goodsId integer;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Goods_RoleSName
  );
  insert into
    jrs_goods
  (
    supplier_id
    , goods_name
    , goods_type_code
    , unit_code
    , purchasing_price
    , motivation_type_code
    , goods_photo_mime_type
    , goods_photo_extension
    , goods_portfolio_mime_type
    , goods_portfolio_extension
    , operator_id
  )
  values
  (
    supplierId
    , goodsName
    , goodsTypeCode
    , unitCode
    , purchasingPrice
    , coalesce( motivationTypeCode, Usual_MotivationTCode)
    , goodsPhotoMimeType
    , goodsPhotoExtension
    , goodsPortfolioMimeType
    , goodsPortfolioExtension
    , operatorId
  )
  returning
    goods_id
  into
    goodsId
  ;
  return goodsId;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при создании товара ('
        || ' supplierId=' || supplierId
        || ', goodsName="' || goodsName || '"'
        || ').'
      )
    , true
  );
end createGoods;

/* proc: updateGoods
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
)
is

  -- Данные записи
  rec jrs_goods%rowtype;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Goods_RoleSName
  );
  lockGoods(
    dataRec             => rec
    , goodsId           => goodsId
  );
  update
    jrs_goods d
  set
    d.supplier_id                   = supplierId
    , d.goods_name                  = goodsName
    , d.goods_type_code             = goodsTypeCode
    , d.unit_code                   = unitCode
    , d.purchasing_price            = purchasingPrice
    , d.motivation_type_code        = motivationTypeCode
    , d.goods_photo_mime_type       = goodsPhotoMimeType
    , d.goods_photo_extension       = goodsPhotoExtension
    , d.goods_portfolio_mime_type   = goodsPortfolioMimeType
    , d.goods_portfolio_extension   = goodsPortfolioExtension
  where
    d.goods_id = goodsId
  ;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при изменении данных товара ('
        || ' goodsId=' || goodsId
        || ').'
      )
    , true
  );
end updateGoods;

/* proc: deleteGoods
  Удаляет товар.

  Параметры:
  goodsId                     - Id товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)
*/
procedure deleteGoods(
  goodsId integer
  , operatorId integer := null
)
is

  -- Данные записи
  rec jrs_goods%rowtype;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Goods_RoleSName
  );
  lockGoods(
    dataRec             => rec
    , goodsId           => goodsId
  );
  delete
    jrs_goods d
  where
    d.goods_id = goodsId
  ;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при удалении товара ('
        || ' goodsId=' || goodsId
        || ').'
      )
    , true
  );
end deleteGoods;

/* func: findGoods
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
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

  -- Динамически формируемый текст запроса
  dsql dyn_dynamic_sql_t := dyn_dynamic_sql_t( '
select
  a.*
from
  (
  select
    t.goods_id
    , t.supplier_id
    , sp.supplier_name
    , t.goods_name
    , t.goods_type_code
    , gt.goods_type_name
    , t.unit_code
    , ut.unit_name
    , t.purchasing_price
    , t.motivation_type_code
    , mt.motivation_type_name
    , t.goods_photo
    , t.goods_photo_mime_type
    , t.goods_photo_extension
    , t.goods_portfolio
    , t.goods_portfolio_mime_type
    , t.goods_portfolio_extension
  from
    jrs_goods t
    inner join jrs_supplier sp
      on sp.supplier_id = t.supplier_id
    inner join jrs_goods_type gt
      on gt.goods_type_code = t.goods_type_code
    inner join jrs_unit ut
      on ut.unit_code = t.unit_code
    inner join jrs_motivation_type mt
      on mt.motivation_type_code = t.motivation_type_code
  where
    $(condition)
  order by
    t.goods_name
  ) a
where
  $(rownumCondition)
'
  );

-- findGoods
begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Goods_RoleSName
  );
  dsql.addCondition(
    't.goods_id in
(
select
  to_number( tt.column_value) as goods_id
from
  table( pkg_Common.split( :goodsIdList)) tt
)'
    , goodsIdList is null
  );
  dsql.addCondition(
    't.supplier_id =', supplierId is null
  );
  dsql.addCondition(
    'upper( t.goods_name) like upper( :goodsName) escape ''\'''
    , goodsName is null
  );
  dsql.addCondition(
    't.goods_type_code =', goodsTypeCode is null
  );
  dsql.addCondition(
    't.goods_id in
(
select
  gsl.goods_id
from
  table( pkg_Common.split( :goodsSegmentCodeList)) tt
  inner join jrs_goods_segment_link gsl
    on gsl.goods_segment_code = tt.column_value
)'
    , goodsSegmentCodeList is null
  );
  dsql.addCondition(
    't.goods_id in
(
select
  gcl.goods_id
from
  jrs_goods_catalog_link gcl
where
  gcl.goods_catalog_id in
    (
    select
      gc.goods_catalog_id
    from
      jrs_goods_catalog gc
    connect by
      prior gc.goods_catalog_id = gc.parent_goods_catalog_id
    start with
      gc.goods_catalog_id in
        (
        select
          to_number( tt.column_value) as goods_catalog_id
        from
          table( pkg_Common.split( :goodsCatalogIdList)) tt
        )
    )
)'
    , goodsCatalogIdList is null
  );
  dsql.useCondition( 'condition');
  dsql.addCondition(
    'rownum <= :maxRowCount', maxRowCount is null
  );
  dsql.useCondition( 'rownumCondition');
  begin
    open rc for
      dsql.getSqlText()
    using
      goodsIdList
      , supplierId
      , goodsName
      , goodsTypeCode
      , goodsSegmentCodeList
      , goodsCatalogIdList
      , maxRowCount
    ;
  exception when others then
    raise_application_error(
      pkg_Error.ErrorStackInfo
      , logger.errorStack(
          'Ошибка при выполнении SQL:'
          || chr(10) || dsql.getSqlText()
          || chr(10) || '.'
        )
      , true
    );
  end;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при поиске товара.'
      )
    , true
  );
end findGoods;

/* func: getGoods
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
*/
function getGoods(
  goodsName varchar2 := null
  , maxRowCount integer := null
)
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

-- getGoods
begin
  open rc for
    select
      t.goods_id
      , t.goods_name
    from
      (
      select
        t.*
      from
        jrs_goods t
      where
        goodsName is null
        or upper( t.goods_name) like upper( goodsName) escape '\'
      order by
        t.goods_name
      ) t
    where
      maxRowCount is null
      or rownum <= maxRowCount
  ;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при выборке товаров ( '
        || 'goodsName="' || goodsName || '"'
        || ')'
      )
    , true
  );
end getGoods;



/* group: Сегменты для товара */

/* proc: createGoodsSegmentLink
  Добавляет сегмент для товара.

  Параметры:
  goodsId                     - Id товара
  goodsSegmentCode            - код сегмента товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)
*/
procedure createGoodsSegmentLink(
  goodsId integer
  , goodsSegmentCode varchar2
  , operatorId integer := null
)
is
begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Goods_RoleSName
  );
  insert into
    jrs_goods_segment_link
  (
    goods_id
    , goods_segment_code
    , operator_id
  )
  values
  (
    goodsId
    , goodsSegmentCode
    , operatorId
  );
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при добавлении сегмента для товара ('
        || ' goodsId=' || goodsId
        || ', goodsSegmentCode="' || goodsSegmentCode || '"'
        || ').'
      )
    , true
  );
end createGoodsSegmentLink;

/* proc: deleteGoodsSegmentLink
  Удаляет сегмент для товара.

  Параметры:
  goodsId                     - Id товара
  goodsSegmentCode            - код сегмента товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)
*/
procedure deleteGoodsSegmentLink(
  goodsId integer
  , goodsSegmentCode varchar2
  , operatorId integer := null
)
is
begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Goods_RoleSName
  );
  delete
    jrs_goods_segment_link d
  where
    d.goods_id = goodsId
    and d.goods_segment_code = goodsSegmentCode
  ;
  if sql%rowcount = 0 then
    raise_application_error(
      pkg_Error.IllegalArgument
      , 'Запись для удаления не найдена.'
    );
  end if;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при удалении сегмента для товара ('
        || ' goodsId=' || goodsId
        || ', goodsSegmentCode="' || goodsSegmentCode || '"'
        || ').'
      )
    , true
  );
end deleteGoodsSegmentLink;

/* func: getGoodsSegmentLink
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
*/
function getGoodsSegmentLink(
  goodsId integer
  , operatorId integer := null
)
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Goods_RoleSName
  );
  open rc for
    select
      t.goods_id
      , t.goods_segment_code
      , gs.goods_segment_name
    from
      jrs_goods_segment_link t
      inner join jrs_goods_segment gs
        on gs.goods_segment_code = t.goods_segment_code
    where
      t.goods_id = goodsId
    order by
      gs.goods_segment_name
  ;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при выборке сегментов для товара ('
        || ' goodsId=' || goodsId
        || ').'
      )
    , true
  );
end getGoodsSegmentLink;



/* group: Разделы каталога для товара */

/* proc: setGoodsCatalogLink
  Устанавливает разделы каталога, к которым относится товар.

  Параметры:
  goodsId                     - Id товара
  goodsCatalogIdList          - Id разделов каталога, к которым относится
                                товар ( список через запятую, null при
                                отсутствии разделов)
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)
*/
procedure setGoodsCatalogLink(
  goodsId integer
  , goodsCatalogIdList varchar2
  , operatorId integer := null
)
is
begin
  delete
    jrs_goods_catalog_link d
  where
    d.goods_id = goodsId
    and d.goods_catalog_id not in
      (
      select
        a.goods_catalog_id
      from
        (
        select
          to_number( trim( t.column_value)) as goods_catalog_id
        from
          table( pkg_Common.split( goodsCatalogIdList)) t
        ) a
      where
        a.goods_catalog_id is not null
      )
  ;
  insert into
    jrs_goods_catalog_link
  (
    goods_id
    , goods_catalog_id
    , operator_id
  )
  select
    goodsId as goods_id
    , a.goods_catalog_id
    , operatorId
  from
    (
    select
      to_number( trim( t.column_value)) as goods_catalog_id
    from
      table( pkg_Common.split( goodsCatalogIdList)) t
    ) a
  where
    a.goods_catalog_id is not null
    and a.goods_catalog_id not in
      (
      select
        t.goods_catalog_id
      from
        jrs_goods_catalog_link t
      where
        t.goods_id = goodsId
      )
  ;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при установке разделов каталога для товара ('
        || ' goodsId=' || goodsId
        || ', goodsCatalogIdList="' || goodsCatalogIdList || '"'
        || ').'
      )
    , true
  );
end setGoodsCatalogLink;

/* func: getGoodsCatalog
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
*/
function getGoodsCatalog(
  parentGoodsCatalogId integer := null
  , goodsId integer := null
)
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

begin
  open rc for
    select
      t.goods_catalog_id
      , t.goods_catalog_name
      , (
        select
          count(*)
        from
          jrs_goods_catalog gc
        where
          gc.parent_goods_catalog_id = t.goods_catalog_id
          and rownum <= 1
        )
        as has_child_flag
      , case when goodsId is not null  then
          coalesce( g.goods_link_flag, 0)
        end
        as goods_link_flag
      , case when goodsId is not null  then
          coalesce( g.descendant_goods_link_flag, 0)
        end
        as descendant_goods_link_flag
    from
      jrs_goods_catalog t
      left join
        (
        select
          gc.goods_catalog_id
          , case when min( level) = 1 then 1 else 0 end
            as goods_link_flag
          , case when max( level) > 1 then 1 else 0 end
            as descendant_goods_link_flag
        from
          jrs_goods_catalog gc
        start with
          gc.goods_catalog_id in
            (
            select
              gcl.goods_catalog_id
            from
              jrs_goods_catalog_link gcl
            where
              gcl.goods_id = goodsId
            )
        connect by
          -- обратное дерево
          gc.goods_catalog_id = prior gc.parent_goods_catalog_id
        group by
          gc.goods_catalog_id
        ) g
        on g.goods_catalog_id = t.goods_catalog_id
    where
      parentGoodsCatalogId is null
        and t.parent_goods_catalog_id is null
      or t.parent_goods_catalog_id = parentGoodsCatalogId
    order by
      t.goods_catalog_name
  ;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при выборке разделов каталога товаров ('
        || ' parentGoodsCatalogId=' || parentGoodsCatalogId
        || ', goodsId=' || goodsId
        || ').'
      )
    , true
  );
end getGoodsCatalog;



/* group: Товар в магазине */

/* iproc: lockShopGoods
  Блокирует и возвращает запись с данными товара в магазине.

  Параметры:
  dataRec                     - данные записи ( возврат)
  shopGoodsId                 - Id товара в магазине
*/
procedure lockShopGoods(
  dataRec out nocopy jrs_shop_goods%rowtype
  , shopGoodsId integer
)
is
begin
  select
    t.*
  into dataRec
  from
    jrs_shop_goods t
  where
    t.shop_goods_id = shopGoodsId
  for update nowait;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при блокировке записи с данными товара в магазине ('
        || ' shopGoodsId=' || shopGoodsId
        || ').'
      )
    , true
  );
end lockShopGoods;

/* func: createShopGoods
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
*/
function createShopGoods(
  shopId integer
  , goodsId integer
  , goodsQuantity number
  , sellPrice number
  , operatorId integer := null
)
return integer
is

  -- Id созданной записи
  shopGoodsId integer;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , ShopGoods_RoleSName
  );
  insert into
    jrs_shop_goods
  (
    shop_id
    , goods_id
    , goods_quantity
    , sell_price
    , operator_id
  )
  values
  (
    shopId
    , goodsId
    , goodsQuantity
    , sellPrice
    , operatorId
  )
  returning
    shop_goods_id
  into
    shopGoodsId
  ;
  return shopGoodsId;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при создании записи для товара в магазине ('
        || ' shopId=' || shopId
        || ', goodsId=' || goodsId
        || ', goodsQuantity=' || goodsQuantity
        || ', sellPrice=' || sellPrice
        || ').'
      )
    , true
  );
end createShopGoods;

/* proc: updateShopGoods
  Изменяет данные о товаре в магазине.

  Параметры:
  shopGoodsId                 - Id товара в магазине
  goodsQuantity               - количество товара
  sellPrice                   - отпускная цена
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)
*/
procedure updateShopGoods(
  shopGoodsId integer
  , goodsQuantity number
  , sellPrice number
  , operatorId integer := null
)
is

  -- Данные записи
  rec jrs_shop_goods%rowtype;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , ShopGoods_RoleSName
  );
  lockShopGoods(
    dataRec             => rec
    , shopGoodsId       => shopGoodsId
  );
  update
    jrs_shop_goods d
  set
    d.goods_quantity          = goodsQuantity
    , d.sell_price            = sellPrice
  where
    d.shop_goods_id = shopGoodsId
  ;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при изменении данных о товаре в магазине ('
        || ' shopGoodsId=' || shopGoodsId
        || ', goodsQuantity=' || goodsQuantity
        || ', sellPrice=' || sellPrice
        || ').'
      )
    , true
  );
end updateShopGoods;

/* proc: deleteShopGoods
  Удаляет запись о товаре в магазине.

  Параметры:
  shopGoodsId                 - Id товара в магазине
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)
*/
procedure deleteShopGoods(
  shopGoodsId integer
  , operatorId integer := null
)
is

  -- Данные записи
  rec jrs_shop_goods%rowtype;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , ShopGoods_RoleSName
  );
  lockShopGoods(
    dataRec             => rec
    , shopGoodsId       => shopGoodsId
  );
  delete
    jrs_shop_goods d
  where
    d.shop_goods_id = shopGoodsId
  ;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при удалении товара в магазине ('
        || ' shopGoodsId=' || shopGoodsId
        || ').'
      )
    , true
  );
end deleteShopGoods;

/* func: findShopGoods
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
*/
function findShopGoods(
  shopGoodsId integer := null
  , shopId integer := null
  , goodsId integer := null
  , maxRowCount integer := null
  , operatorId integer := null
)
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

  -- Динамически формируемый текст запроса
  dsql dyn_dynamic_sql_t := dyn_dynamic_sql_t( '
select
  a.*
from
  (
  select
    t.shop_goods_id
    , t.shop_id
    , sh.shop_name
    , t.goods_id
    , gd.goods_name
    , t.goods_quantity
    , t.sell_price
  from
    jrs_shop_goods t
    inner join jrs_shop sh
      on sh.shop_id = t.shop_id
    inner join jrs_goods gd
      on gd.goods_id = t.goods_id
  where
    $(condition)
  order by
    sh.shop_name
    , gd.goods_name
  ) a
where
  $(rownumCondition)
'
  );

-- findShopGoods
begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , ShopGoods_RoleSName
  );
  dsql.addCondition(
    't.shop_goods_id =', shopGoodsId is null
  );
  dsql.addCondition(
    't.shop_id =', shopId is null
  );
  dsql.addCondition(
    't.goods_id =', goodsId is null
  );
  dsql.useCondition( 'condition');
  dsql.addCondition(
    'rownum <= :maxRowCount', maxRowCount is null
  );
  dsql.useCondition( 'rownumCondition');
  begin
    open rc for
      dsql.getSqlText()
    using
      shopGoodsId
      , shopId
      , goodsId
      , maxRowCount
    ;
  exception when others then
    raise_application_error(
      pkg_Error.ErrorStackInfo
      , logger.errorStack(
          'Ошибка при выполнении SQL:'
          || chr(10) || dsql.getSqlText()
          || chr(10) || '.'
        )
      , true
    );
  end;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при поиске товара в магазине.'
      )
    , true
  );
end findShopGoods;



/* group: Запрос на закупку */

/* iproc: lockRequest
  Блокирует и возвращает запись с данными запроса на закупку.

  Параметры:
  dataRec                     - данные записи ( возврат)
  requestId                   - Id запроса
*/
procedure lockRequest(
  dataRec out nocopy jrs_request%rowtype
  , requestId integer
)
is
begin
  select
    t.*
  into dataRec
  from
    jrs_request t
  where
    t.request_id = requestId
  for update nowait;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при блокировке записи с данными запроса на закупку ('
        || ' requestId=' || requestId
        || ').'
      )
    , true
  );
end lockRequest;

/* func: createRequest
  Создает запрос на закупку.

  Параметры:
  shopId                      - Id магазина
  goodsId                     - Id товара
  goodsQuantity               - количество товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат:
  Id созданной записи.
*/
function createRequest(
  shopId integer
  , goodsId integer
  , goodsQuantity number
  , operatorId integer := null
)
return integer
is

  -- Id созданной записи
  requestId integer;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Request_RoleSName
  );
  insert into
    jrs_request
  (
    shop_id
    , goods_id
    , goods_quantity
    , operator_id
  )
  values
  (
    shopId
    , goodsId
    , goodsQuantity
    , operatorId
  )
  returning
    request_id
  into
    requestId
  ;
  return requestId;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при создании запроса на закупку ('
        || ' shopId=' || shopId
        || ', goodsId=' || goodsId
        || ', goodsQuantity=' || goodsQuantity
        || ').'
      )
    , true
  );
end createRequest;

/* proc: updateRequest
  Изменяет запрос на закупку.

  Параметры:
  requestId                   - Id запроса
  requestStatusCode           - код статуса запроса
  goodsId                     - Id товара
  goodsQuantity               - количество товара
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)
*/
procedure updateRequest(
  requestId integer
  , requestStatusCode varchar2
  , goodsId integer
  , goodsQuantity number
  , operatorId integer := null
)
is

  -- Данные записи
  rec jrs_request%rowtype;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Request_RoleSName
  );
  lockRequest(
    dataRec             => rec
    , requestId         => requestId
  );
  update
    jrs_request d
  set
    d.request_status_code     = requestStatusCode
    , d.goods_id              = goodsId
    , d.goods_quantity        = goodsQuantity
  where
    d.request_id = requestId
  ;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при изменении запроса на закупку ('
        || ' requestId=' || requestId
        || ').'
      )
    , true
  );
end updateRequest;

/* proc: deleteRequest
  Удаляет запрос на закупку.

  Параметры:
  requestId                   - Id запроса
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)
*/
procedure deleteRequest(
  requestId integer
  , operatorId integer := null
)
is

  -- Данные записи
  rec jrs_request%rowtype;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Request_RoleSName
  );
  lockRequest(
    dataRec             => rec
    , requestId       => requestId
  );
  delete
    jrs_request d
  where
    d.request_id = requestId
  ;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при удалении запроса на закупку ('
        || ' requestId=' || requestId
        || ').'
      )
    , true
  );
end deleteRequest;

/* func: findRequest
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
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

  -- Динамически формируемый текст запроса
  dsql dyn_dynamic_sql_t := dyn_dynamic_sql_t( '
select
  a.*
from
  (
  select
    t.request_id
    , t.shop_id
    , sh.shop_name
    , t.request_date
    , t.request_status_code
    , rs.request_status_name
    , t.goods_id
    , gd.goods_name
    , t.goods_quantity
  from
    jrs_request t
    inner join jrs_shop sh
      on sh.shop_id = t.shop_id
    inner join jrs_goods gd
      on gd.goods_id = t.goods_id
    inner join jrs_request_status rs
      on rs.request_status_code = t.request_status_code
  where
    $(condition)
  order by
    sh.shop_name
    , t.request_id
  ) a
where
  $(rownumCondition)
'
  );

-- findRequest
begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , Request_RoleSName
  );
  dsql.addCondition(
    't.request_id =', requestId is null
  );
  dsql.addCondition(
    't.shop_id =', shopId is null
  );
  dsql.addCondition(
    't.request_date >= trunc( :requestDateFrom)'
    , requestDateFrom is null
  );
  dsql.addCondition(
    't.request_date <= trunc( :requestDateTo) + ( 1 - 1/86400)'
    , requestDateTo is null
  );
  dsql.addCondition(
    't.request_status_code =', requestStatusCode is null
  );
  dsql.addCondition(
    't.goods_id =', goodsId is null
  );
  dsql.useCondition( 'condition');
  dsql.addCondition(
    'rownum <= :maxRowCount', maxRowCount is null
  );
  dsql.useCondition( 'rownumCondition');
  begin
    open rc for
      dsql.getSqlText()
    using
      requestId
      , shopId
      , requestDateFrom
      , requestDateTo
      , requestStatusCode
      , goodsId
      , maxRowCount
    ;
  exception when others then
    raise_application_error(
      pkg_Error.ErrorStackInfo
      , logger.errorStack(
          'Ошибка при выполнении SQL:'
          || chr(10) || dsql.getSqlText()
          || chr(10) || '.'
        )
      , true
    );
  end;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при поиске запроса на закупку.'
      )
    , true
  );
end findRequest;



/* group: Обработка запроса */

/* func: createRequestProcess
  Создает запись по обработке запроса на закупку.

  Параметры:
  requestId                   - Id запроса
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)

  Возврат:
  Id созданной записи.
*/
function createRequestProcess(
  requestId integer
  , operatorId integer := null
)
return integer
is

  -- Id созданной записи
  requestProcessId integer;

begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , RequestProcess_RoleSName
  );
  insert into
    jrs_request_process
  (
    request_id
    , operator_id
  )
  values
  (
    requestId
    , operatorId
  )
  returning
    request_process_id
  into
    requestProcessId
  ;
  return requestProcessId;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при создании записи по обработке запроса ('
        || ' requestId=' || requestId
        || ').'
      )
    , true
  );
end createRequestProcess;

/* proc: deleteRequestProcess
  Удаляет запись по обработке запроса на закупку.

  Параметры:
  requestProcessId            - Id записи по обработке запроса
  operatorId                  - Id оператора, выполняющего операцию
                                ( по умолчанию текущий)
*/
procedure deleteRequestProcess(
  requestProcessId integer
  , operatorId integer := null
)
is
begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , RequestProcess_RoleSName
  );
  delete
    jrs_request_process d
  where
    d.request_process_id = requestProcessId
  ;
  if sql%rowcount = 0 then
    raise_application_error(
      pkg_Error.IllegalArgument
      , 'Запись для удаления не найдена.'
    );
  end if;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при удалении записи по обработке запроса ('
        || ' requestProcessId=' || requestProcessId
        || ').'
      )
    , true
  );
end deleteRequestProcess;

/* func: findRequestProcess
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
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

  -- Динамически формируемый текст запроса
  dsql dyn_dynamic_sql_t := dyn_dynamic_sql_t( '
select
  a.*
from
  (
  select
    t.request_process_id
    , t.process_comment
    , t.date_ins
    , t.operator_id
    , op.operator_name
  from
    jrs_request_process t
    inner join op_operator op
      on op.operator_id = t.operator_id
  where
    $(condition)
  order by
    t.request_process_id
  ) a
where
  $(rownumCondition)
'
  );

-- findRequestProcess
begin
  pkg_Operator.isRole(
    coalesce( operatorId, pkg_Operator.getCurrentUserId())
    , RequestProcess_RoleSName
  );
  dsql.addCondition(
    't.request_process_id =', requestProcessId is null
  );
  dsql.addCondition(
    't.request_id =', requestId is null
  );
  dsql.addCondition(
    't.date_ins >= trunc( :dateInsFrom)'
    , dateInsFrom is null
  );
  dsql.addCondition(
    't.date_ins <= trunc( :dateInsTo) + ( 1 - 1/86400)'
    , dateInsTo is null
  );
  dsql.addCondition(
    't.operator_id =', insertOperatorId is null
  );
  dsql.useCondition( 'condition');
  dsql.addCondition(
    'rownum <= :maxRowCount', maxRowCount is null
  );
  dsql.useCondition( 'rownumCondition');
  begin
    open rc for
      dsql.getSqlText()
    using
      requestProcessId
      , requestId
      , dateInsFrom
      , dateInsTo
      , insertOperatorId
      , maxRowCount
    ;
  exception when others then
    raise_application_error(
      pkg_Error.ErrorStackInfo
      , logger.errorStack(
          'Ошибка при выполнении SQL:'
          || chr(10) || dsql.getSqlText()
          || chr(10) || '.'
        )
      , true
    );
  end;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при поиске записей по обработке запроса.'
      )
    , true
  );
end findRequestProcess;



/* group: Справочники */

/* func: getBank
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
*/
function getBank(
  bankBic varchar2 := null
  , maxRowCount integer := null
)
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

begin
  open rc for
    select
      a.*
    from
      (
      select
        t.bic
        , t.bankname
        , t.ks
      from
        v_bic_bank t
      where
        ( bankBic is null or t.bic like bankBic)
      order by
        t.bankname
      ) a
    where
      maxRowCount is null
      or rownum <= maxRowCount
  ;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при выборке списка банков ('
        || ' bankBic="' || bankBic || '"'
        || ').'
      )
    , true
  );
end getBank;

/* func: getGoodsSegment
  Возвращает сегменты товаров.

  Возврат ( курсор):
  goods_segment_code          - код сегмента товара
  goods_segment_name          - наименование сегмента товара

  Замечания:
  - возвращаемые записи отсортированы по goods_segment_name;
*/
function getGoodsSegment
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

begin
  open rc for
    select
      t.goods_segment_code
      , t.goods_segment_name
    from
      jrs_goods_segment t
    order by
      t.goods_segment_name
  ;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при выборке сегментов товаров.'
      )
    , true
  );
end getGoodsSegment;

/* func: getGoodsType
  Возвращает типы товаров.

  Возврат ( курсор):
  goods_type_code           - код типа товара
  goods_type_name           - наименование типа товара

  Замечания:
  - возвращаемые записи отсортированы по goods_type_name;
*/
function getGoodsType
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

begin
  open rc for
    select
      t.goods_type_code
      , t.goods_type_name
    from
      jrs_goods_type t
    order by
      t.goods_type_name
  ;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при выборке типов товаров.'
      )
    , true
  );
end getGoodsType;

/* func: getMotivationType
  Возвращает типы мотивации.

  Возврат ( курсор):
  motivation_type_code        - код типа мотивации
  motivation_type_name        - наименование типа мотивации
  motivation_type_comment     - комментарий для типа мотивации

  Замечания:
  - возвращаемые записи отсортированы по motivation_type_name;
*/
function getMotivationType
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

begin
  open rc for
    select
      t.motivation_type_code
      , t.motivation_type_name
      , t.motivation_type_comment
    from
      jrs_motivation_type t
    order by
      t.motivation_type_name
  ;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при выборке типов мотивации.'
      )
    , true
  );
end getMotivationType;

/* func: getRequestStatus
  Возвращает статусы запроса на закупку.

  Возврат ( курсор):
  request_status_code         - код статуса запроса
  request_status_name         - наименование статуса запроса

  Замечания:
  - возвращаемые записи отсортированы по request_status_name;
*/
function getRequestStatus
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

begin
  open rc for
    select
      t.request_status_code
      , t.request_status_name
    from
      jrs_request_status t
    order by
      t.request_status_name
  ;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при выборке статусов запроса на закупку.'
      )
    , true
  );
end getRequestStatus;

/* func: getShop
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
*/
function getShop(
  shopName varchar2 := null
  , maxRowCount integer := null
)
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

begin
  open rc for
    select
      t.shop_id
      , t.shop_name
    from
      (
      select
        t.*
      from
        jrs_shop t
      where
        shopName is null
        or upper( t.shop_name) like upper( shopName) escape '\'
      order by
        t.shop_name
      ) t
    where
      maxRowCount is null
      or rownum <= maxRowCount
  ;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при выборке магазинов ('
        || ' shopName="' || shopName || '"'
        || ').'
      )
    , true
  );
end getShop;

/* func: getUnit
  Возвращает единицы измерения.

  Возврат ( курсор):
  unit_code                   - код единицы измерения
  unit_short_name             - краткое наименование единицы измерения
  unit_name                   - наименование единицы измерения

  Замечания:
  - возвращаемые записи отсортированы по unit_name;
*/
function getUnit
return sys_refcursor
is

  -- Возвращаемый курсор
  rc sys_refcursor;

begin
  open rc for
    select
      t.unit_code
      , t.unit_short_name
      , t.unit_name
    from
      jrs_unit t
    order by
      t.unit_name
  ;
  return rc;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        'Ошибка при выборке единиц измерения.'
      )
    , true
  );
end getUnit;

end pkg_JepRiaShowcase;
/
