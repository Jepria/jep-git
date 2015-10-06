create or replace package body pkg_JepRiaShowcase is
/* package body: pkg_JepRiaShowcase::body */



/* group: ��������� */

/* iconst: Usual_MotivationTCode
  ��� ���� ��������� "������� ���������".
*/
Usual_MotivationTCode constant varchar2(10) := 'USUAL';



/* group: ���������� */

/* ivar: logger
  ����� ������.
*/
logger lg_logger_t := lg_logger_t.getLogger(
  moduleName    => Module_Name
  , objectName  => 'pkg_JepRiaShowcase'
);



/* group: ������� */




/* group: ��������� */

/* iproc: lockSupplier
  ��������� � ���������� ������ � ������� ����������.

  ���������:
  dataRec                     - ������ ������ ( �������)
  supplierId                  - Id ����������
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
        '������ ��� ���������� ������ � ������� ���������� ('
        || ' supplierId=' || supplierId
        || ').'
      )
    , true
  );
end lockSupplier;

/* func: createSupplier
  ������� ����������.

  ���������:
  supplierName                - ������������ ����������
  contractFinishDate          - ���� �� ������� ��������� �������
                                ( � ��������� �� ���, ������������)
  exclusiveSupplierFlag       - ����������������� ��������� ( 1 ��, 0 ���)
                                ( �� ��������� 0)
  phoneNumber                 - �������
                                ( �� ��������� �����������)
  faxNumber                   - ����
                                ( �� ��������� �����������)
  bankBic                     - ����
                                ( �� ��������� �����������)
  recipientName               - ����������
                                ( �� ��������� �����������)
  settlementAccount           - ��������� ����
                                ( �� ��������� �����������)
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  �������:
  Id ��������� ������.
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

  -- Id ��������� ������
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
        '������ ��� �������� ���������� ('
        || ' supplierName="' || supplierName || '"'
        || ', exclusiveSupplierFlag=' || exclusiveSupplierFlag
        || ').'
      )
    , true
  );
end createSupplier;

/* proc: updateSupplier
  �������� ������ ����������.

  ���������:
  supplierId                  - Id ����������
  supplierName                - ������������ ����������
  contractFinishDate          - ���� �� ������� ��������� �������
                                ( � ��������� �� ���, ������������)
  exclusiveSupplierFlag       - ����������������� ��������� ( 1 ��, 0 ���)
  phoneNumber                 - �������
  faxNumber                   - ����
  bankBic                     - ����
  recipientName               - ����������
  settlementAccount           - ��������� ����
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)
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

  -- ������ ������
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
        '������ ��� ��������� ������ ���������� ('
        || ' supplierId=' || supplierId
        || ').'
      )
    , true
  );
end updateSupplier;

/* proc: deleteSupplier
  ������� ����������.

  ���������:
  supplierId                  - Id ����������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)
*/
procedure deleteSupplier(
  supplierId integer
  , operatorId integer := null
)
is

  -- ������ ������
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
        '������ ��� �������� ���������� ('
        || ' supplierId=' || supplierId
        || ').'
      )
    , true
  );
end deleteSupplier;

/* func: findSupplier
  ����� ����������.

  ���������:
  supplierId                  - Id ����������
                                ( �� ��������� ��� �����������)
  supplierName                - ������������ ����������
                                ( ����� �� like ��� ����� ��������)
                                ( �� ��������� ��� �����������)
  contractFinishDateFrom      - ���� �� ������� ��������� �������, ��
                                ( � ��������� �� ���, ������������)
                                ( �� ��������� ��� �����������)
  contractFinishDateTo        - ���� �� ������� ��������� �������, ��
                                ( � ��������� �� ���, ������������)
                                ( �� ��������� ��� �����������)
  exclusiveSupplierFlag       - ����������������� ��������� ( 1 ��, 0 ���)
                                ( �� ��������� ��� �����������)
  maxRowCount                 - ������������ ����� ������������ �������
                                ( �� ��������� ��� �����������)
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ������� ( ������):
  supplier_id                 - Id ����������
  supplier_name               - ������������ ����������
  contract_finish_date        - ���� �� ������� ��������� �������
  exclusive_supplier_flag     - ����������������� ��������� ( 1 ��, 0 ���)
  supplier_description        - �������� ����������
  phone_number                - �������
  fax_number                  - ����
  bank_bic                    - ����
  bankname                    - ������������ �����
  ks                          - �������
  recipient_name              - ����������
  settlement_account          - ��������� ����

  ���������:
  - ������������ ������ ������������� �� supplier_name;
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

  -- ������������ ������
  rc sys_refcursor;

  -- ����������� ����������� ����� �������
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
        '������ ��� ������ ����������.'
      )
    , true
  );
end findSupplier;



/* group: ����� */

/* iproc: lockGoods
  ��������� � ���������� ������ � ������� ������.

  ���������:
  dataRec                     - ������ ������ ( �������)
  goodsId                     - Id ������
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
        '������ ��� ���������� ������ � ������� ������ ('
        || ' goodsId=' || goodsId
        || ').'
      )
    , true
  );
end lockGoods;

/* func: createGoods
  ������� �����.

  ���������:
  supplierId                  - Id ����������
  goodsName                   - ������������ ������
  goodsTypeCode               - ��� ���� ������
  unitCode                    - ��� ������� ���������
  purchasingPrice             - ���������� ����
  motivationTypeCode          - ��� ���� ���������
                                ( �� ��������� ��� ��� "������� ���������")
  goodsPhotoMimeType          - MIME-��� ����� � ����������� ������
  goodsPhotoExtension         - ���������� ����� � ����������� ������
  goodsPortfolioMimeType      - MIME-��� ����� �� ������������� ������
  goodsPortfolioExtension     - ���������� ����� �� ������������� ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  �������:
  Id ��������� ������.
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

  -- Id ��������� ������
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
        '������ ��� �������� ������ ('
        || ' supplierId=' || supplierId
        || ', goodsName="' || goodsName || '"'
        || ').'
      )
    , true
  );
end createGoods;

/* proc: updateGoods
  �������� ������ ������.

  ���������:
  goodsId                     - Id ������
  supplierId                  - Id ����������
  goodsName                   - ������������ ������
  goodsTypeCode               - ��� ���� ������
  unitCode                    - ��� ������� ���������
  purchasingPrice             - ���������� ����
  motivationTypeCode          - ��� ���� ���������
  goodsPhotoMimeType          - MIME-��� ����� � ����������� ������
  goodsPhotoExtension         - ���������� ����� � ����������� ������
  goodsPortfolioMimeType      - MIME-��� ����� �� ������������� ������
  goodsPortfolioExtension     - ���������� ����� �� ������������� ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)
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

  -- ������ ������
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
        '������ ��� ��������� ������ ������ ('
        || ' goodsId=' || goodsId
        || ').'
      )
    , true
  );
end updateGoods;

/* proc: deleteGoods
  ������� �����.

  ���������:
  goodsId                     - Id ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)
*/
procedure deleteGoods(
  goodsId integer
  , operatorId integer := null
)
is

  -- ������ ������
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
        '������ ��� �������� ������ ('
        || ' goodsId=' || goodsId
        || ').'
      )
    , true
  );
end deleteGoods;

/* func: findGoods
  ����� ������.

  ���������:
  goodsIdList                 - Id ������ ( ������ ����� �������)
                                ( �� ��������� ��� �����������)
  supplierId                  - Id ����������
                                ( �� ��������� ��� �����������)
  goodsName                   - ������������ ������
                                ( ����� �� like ��� ����� ��������)
                                ( �� ��������� ��� �����������)
  goodsTypeCode               - ��� ���� ������
                                ( �� ��������� ��� �����������)
  goodsSegmentCodeList        - ��� �������� ������ ( ������ ����� �������)
                                ( �� ��������� ��� �����������)
  goodsCatalogIdList          - Id ������� �������� ( ������ ����� �������)
                                ( ��� ���� ����� ����������� ������,
                                  ����������� � ����������� �������� ��������)
                                ( �� ��������� ��� �����������)
  maxRowCount                 - ������������ ����� ������������ �������
                                ( �� ��������� ��� �����������)
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ������� ( ������):
  goods_id                    - Id ������
  supplier_id                 - Id ����������
  supplier_name               - ������������ ����������
  goods_name                  - ������������ ������
  goods_type_code             - ��� ���� ������
  goods_type_name             - ������������ ���� ������
  unit_code                   - ��� ������� ���������
  unit_name                   - ������������ ������� ���������
  purchasing_price            - ���������� ����
  motivation_type_code        - ��� ���� ���������
  motivation_type_name        - ������������ ���� ���������
  goods_photo                 - ���������� ������
  goods_photo_mime_type       - MIME-��� ����� � ����������� ������
  goods_photo_extension       - ���������� ����� � ����������� ������
  goods_portfolio             - ������������ ������
  goods_portfolio_mime_type   - MIME-��� ����� �� ������������� ������
  goods_portfolio_extension   - ���������� ����� �� ������������� ������

  ���������:
  - ������������ ������ ������������� �� goods_name;
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

  -- ������������ ������
  rc sys_refcursor;

  -- ����������� ����������� ����� �������
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
          '������ ��� ���������� SQL:'
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
        '������ ��� ������ ������.'
      )
    , true
  );
end findGoods;

/* func: getGoods
  ��������� ������ �������.

  ���������:
  goodsName                   - ������������ ������
                                ( ����� �� like ��� ����� ��������)
                                ( �� ��������� ��� �����������)
  maxRowCount                 - ������������ ����� ������������ �������
                                ( �� ��������� ��� �����������)

  ������� ( ������):
  goods_id                    - Id ������
  goods_name                  - ������������ ������

  ���������:
  - ������������ ������ ������������� �� goods_name;
*/
function getGoods(
  goodsName varchar2 := null
  , maxRowCount integer := null
)
return sys_refcursor
is

  -- ������������ ������
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
        '������ ��� ������� ������� ( '
        || 'goodsName="' || goodsName || '"'
        || ')'
      )
    , true
  );
end getGoods;



/* group: �������� ��� ������ */

/* proc: createGoodsSegmentLink
  ��������� ������� ��� ������.

  ���������:
  goodsId                     - Id ������
  goodsSegmentCode            - ��� �������� ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)
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
        '������ ��� ���������� �������� ��� ������ ('
        || ' goodsId=' || goodsId
        || ', goodsSegmentCode="' || goodsSegmentCode || '"'
        || ').'
      )
    , true
  );
end createGoodsSegmentLink;

/* proc: deleteGoodsSegmentLink
  ������� ������� ��� ������.

  ���������:
  goodsId                     - Id ������
  goodsSegmentCode            - ��� �������� ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)
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
      , '������ ��� �������� �� �������.'
    );
  end if;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        '������ ��� �������� �������� ��� ������ ('
        || ' goodsId=' || goodsId
        || ', goodsSegmentCode="' || goodsSegmentCode || '"'
        || ').'
      )
    , true
  );
end deleteGoodsSegmentLink;

/* func: getGoodsSegmentLink
  ���������� �������� ��� ������.

  ���������:
  goodsId                     - Id ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ������� ( ������):
  goods_id                    - Id ������
  goods_segment_code          - ��� �������� ������
  goods_segment_name          - ������������ �������� ������

  ���������:
  - ������������ ������ ������������� �� goods_segment_name;
*/
function getGoodsSegmentLink(
  goodsId integer
  , operatorId integer := null
)
return sys_refcursor
is

  -- ������������ ������
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
        '������ ��� ������� ��������� ��� ������ ('
        || ' goodsId=' || goodsId
        || ').'
      )
    , true
  );
end getGoodsSegmentLink;



/* group: ������� �������� ��� ������ */

/* proc: setGoodsCatalogLink
  ������������� ������� ��������, � ������� ��������� �����.

  ���������:
  goodsId                     - Id ������
  goodsCatalogIdList          - Id �������� ��������, � ������� ���������
                                ����� ( ������ ����� �������, null ���
                                ���������� ��������)
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)
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
        '������ ��� ��������� �������� �������� ��� ������ ('
        || ' goodsId=' || goodsId
        || ', goodsCatalogIdList="' || goodsCatalogIdList || '"'
        || ').'
      )
    , true
  );
end setGoodsCatalogLink;

/* func: getGoodsCatalog
  ���������� ������� �������� �������.

  ���������:
  parentGoodsCatalogId        - Id ������������� ������� ��������
                                 ( null ��� ��������� �������� �������� ������)
                                 ( �� ��������� null)
  goodsId                     - Id ������, ��� �������� ������������ ����������
                                �� ��������� � ��� �������� �������� �
                                ����� goods_link_flag �
                                descendant_goods_link_flag
                                ( null ���� ��� ���� �� �����������
                                  ( �� ��������� null))

  ������� ( ������):
  goods_catalog_id            - Id ������� ��������
  goods_catalog_name          - ������������ ������� ��������
  has_child_flag              - ���� ������� �������� �������� ��������
                                ( 1 ��, 0 ���)
  goods_link_flag             - ���� �������������� ���������� ������ �
                                ������� ��������
                                ( 1 ��, 0 ���, null ���� �� ������ goodsId)
  descendant_goods_link_flag  - ���� �������������� ���������� ������ �
                                ���������� ( �������) ������� ��������
                                ( 1 ��, 0 ���, null ���� �� ������ goodsId)

  ���������:
  - ������������ ������ ������������� �� goods_catalog_name;
*/
function getGoodsCatalog(
  parentGoodsCatalogId integer := null
  , goodsId integer := null
)
return sys_refcursor
is

  -- ������������ ������
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
          -- �������� ������
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
        '������ ��� ������� �������� �������� ������� ('
        || ' parentGoodsCatalogId=' || parentGoodsCatalogId
        || ', goodsId=' || goodsId
        || ').'
      )
    , true
  );
end getGoodsCatalog;



/* group: ����� � �������� */

/* iproc: lockShopGoods
  ��������� � ���������� ������ � ������� ������ � ��������.

  ���������:
  dataRec                     - ������ ������ ( �������)
  shopGoodsId                 - Id ������ � ��������
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
        '������ ��� ���������� ������ � ������� ������ � �������� ('
        || ' shopGoodsId=' || shopGoodsId
        || ').'
      )
    , true
  );
end lockShopGoods;

/* func: createShopGoods
  ������� ������ ��� ������ � ��������.

  ���������:
  shopId                      - Id ��������
  goodsId                     - Id ������
  goodsQuantity               - ���������� ������
  sellPrice                   - ��������� ����
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  �������:
  Id ��������� ������.
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

  -- Id ��������� ������
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
        '������ ��� �������� ������ ��� ������ � �������� ('
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
  �������� ������ � ������ � ��������.

  ���������:
  shopGoodsId                 - Id ������ � ��������
  goodsQuantity               - ���������� ������
  sellPrice                   - ��������� ����
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)
*/
procedure updateShopGoods(
  shopGoodsId integer
  , goodsQuantity number
  , sellPrice number
  , operatorId integer := null
)
is

  -- ������ ������
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
        '������ ��� ��������� ������ � ������ � �������� ('
        || ' shopGoodsId=' || shopGoodsId
        || ', goodsQuantity=' || goodsQuantity
        || ', sellPrice=' || sellPrice
        || ').'
      )
    , true
  );
end updateShopGoods;

/* proc: deleteShopGoods
  ������� ������ � ������ � ��������.

  ���������:
  shopGoodsId                 - Id ������ � ��������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)
*/
procedure deleteShopGoods(
  shopGoodsId integer
  , operatorId integer := null
)
is

  -- ������ ������
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
        '������ ��� �������� ������ � �������� ('
        || ' shopGoodsId=' || shopGoodsId
        || ').'
      )
    , true
  );
end deleteShopGoods;

/* func: findShopGoods
  ����� ������ � ��������.

  ���������:
  shopGoodsId                 - Id ������ � ��������
                                ( �� ��������� ��� �����������)
  shopId                      - Id ��������
                                ( �� ��������� ��� �����������)
  goodsId                     - Id ������
                                ( �� ��������� ��� �����������)
  maxRowCount                 - ������������ ����� ������������ �������
                                ( �� ��������� ��� �����������)
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ������� ( ������):
  shop_goods_id               - Id ������ � ��������
  shop_id                     - Id ��������
  shop_name                   - ������������ ��������
  goods_id                    - Id ������
  goods_name                  - ������������ ������
  goods_quantity              - ���������� ������
  sell_price                  - ��������� ����

  ���������:
  - ������������ ������ ������������� �� shop_name, goods_name;
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

  -- ������������ ������
  rc sys_refcursor;

  -- ����������� ����������� ����� �������
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
          '������ ��� ���������� SQL:'
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
        '������ ��� ������ ������ � ��������.'
      )
    , true
  );
end findShopGoods;



/* group: ������ �� ������� */

/* iproc: lockRequest
  ��������� � ���������� ������ � ������� ������� �� �������.

  ���������:
  dataRec                     - ������ ������ ( �������)
  requestId                   - Id �������
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
        '������ ��� ���������� ������ � ������� ������� �� ������� ('
        || ' requestId=' || requestId
        || ').'
      )
    , true
  );
end lockRequest;

/* func: createRequest
  ������� ������ �� �������.

  ���������:
  shopId                      - Id ��������
  goodsId                     - Id ������
  goodsQuantity               - ���������� ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  �������:
  Id ��������� ������.
*/
function createRequest(
  shopId integer
  , goodsId integer
  , goodsQuantity number
  , operatorId integer := null
)
return integer
is

  -- Id ��������� ������
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
        '������ ��� �������� ������� �� ������� ('
        || ' shopId=' || shopId
        || ', goodsId=' || goodsId
        || ', goodsQuantity=' || goodsQuantity
        || ').'
      )
    , true
  );
end createRequest;

/* proc: updateRequest
  �������� ������ �� �������.

  ���������:
  requestId                   - Id �������
  requestStatusCode           - ��� ������� �������
  goodsId                     - Id ������
  goodsQuantity               - ���������� ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)
*/
procedure updateRequest(
  requestId integer
  , requestStatusCode varchar2
  , goodsId integer
  , goodsQuantity number
  , operatorId integer := null
)
is

  -- ������ ������
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
        '������ ��� ��������� ������� �� ������� ('
        || ' requestId=' || requestId
        || ').'
      )
    , true
  );
end updateRequest;

/* proc: deleteRequest
  ������� ������ �� �������.

  ���������:
  requestId                   - Id �������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)
*/
procedure deleteRequest(
  requestId integer
  , operatorId integer := null
)
is

  -- ������ ������
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
        '������ ��� �������� ������� �� ������� ('
        || ' requestId=' || requestId
        || ').'
      )
    , true
  );
end deleteRequest;

/* func: findRequest
  ����� ������� �� �������.

  ���������:
  requestId                   - Id �������
                                ( �� ��������� ��� �����������)
  shopId                      - Id ��������
                                ( �� ��������� ��� �����������)
  requestDateFrom             - ���� �������� �������, ��
                                ( � ��������� �� ���, ������������)
                                ( �� ��������� ��� �����������)
  requestDateTo               - ���� �������� �������, ��
                                ( � ��������� �� ���, ������������)
                                ( �� ��������� ��� �����������)
  requestStatusCode           - ��� ������� �������
                                ( �� ��������� ��� �����������)
  goodsId                     - Id ������
                                ( �� ��������� ��� �����������)
  maxRowCount                 - ������������ ����� ������������ �������
                                ( �� ��������� ��� �����������)
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ������� ( ������):
  request_id                  - Id �������
  shop_id                     - Id ��������
  shop_name                   - ������������ ��������
  request_date                - ���� �������� �������
  request_status_code         - ��� ������� �������
  request_status_name         - ������������ ������� �������
  goods_id                    - Id ������
  goods_name                  - ������������ ������
  goods_quantity              - ���������� ������

  ���������:
  - ������������ ������ ������������� �� shop_name, request_id;
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

  -- ������������ ������
  rc sys_refcursor;

  -- ����������� ����������� ����� �������
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
          '������ ��� ���������� SQL:'
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
        '������ ��� ������ ������� �� �������.'
      )
    , true
  );
end findRequest;



/* group: ��������� ������� */

/* func: createRequestProcess
  ������� ������ �� ��������� ������� �� �������.

  ���������:
  requestId                   - Id �������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  �������:
  Id ��������� ������.
*/
function createRequestProcess(
  requestId integer
  , operatorId integer := null
)
return integer
is

  -- Id ��������� ������
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
        '������ ��� �������� ������ �� ��������� ������� ('
        || ' requestId=' || requestId
        || ').'
      )
    , true
  );
end createRequestProcess;

/* proc: deleteRequestProcess
  ������� ������ �� ��������� ������� �� �������.

  ���������:
  requestProcessId            - Id ������ �� ��������� �������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)
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
      , '������ ��� �������� �� �������.'
    );
  end if;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        '������ ��� �������� ������ �� ��������� ������� ('
        || ' requestProcessId=' || requestProcessId
        || ').'
      )
    , true
  );
end deleteRequestProcess;

/* func: findRequestProcess
  ����� ������� �� ��������� ������� �� �������.

  ���������:
  requestProcessId            - Id ������ �� ��������� �������
                                ( �� ��������� ��� �����������)
  requestId                    - Id �������
                                ( �� ��������� ��� �����������)
  dateInsFrom                 - ���� ���������� ������, ��
                                ( � ��������� �� ���, ������������)
                                ( �� ��������� ��� �����������)
  dateInsTo                   - ���� ���������� ������, ��
                                ( � ��������� �� ���, ������������)
                                ( �� ��������� ��� �����������)
  insertOperatorId            - Id ���������, ����������� ������
                                ( �� ��������� ��� �����������)
  maxRowCount                 - ������������ ����� ������������ �������
                                ( �� ��������� ��� �����������)
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ������� ( ������):
  request_process_id          - Id ������ �� ��������� �������
  process_comment             - ����������� � ��������� �������
  date_ins                    - ���� ���������� ������
  operator_id                 - Id ���������, ����������� ������
  operator_name               - ��� ���������, ����������� ������

  ���������:
  - ������������ ������ ������������� �� request_process_id;
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

  -- ������������ ������
  rc sys_refcursor;

  -- ����������� ����������� ����� �������
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
          '������ ��� ���������� SQL:'
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
        '������ ��� ������ ������� �� ��������� �������.'
      )
    , true
  );
end findRequestProcess;



/* group: ����������� */

/* func: getBank
  ���������� ������ ������.

  ���������:
  bankBic                     - ���������� ����������������� ��� ( ���)
                                ( ����� �� like, �� ��������� ��� �����������)
  maxRowCount                 - ������������ ����� ������������ �������
                                ( �� ��������� ��� �����������)

  ������� ( ������):
  bic                         - ���������� ����������������� ��� ( ���)
  bankname                    - ������������ �����
  ks                          - �������

  ���������:
  - ������������ ������ ������������� �� bankname;
*/
function getBank(
  bankBic varchar2 := null
  , maxRowCount integer := null
)
return sys_refcursor
is

  -- ������������ ������
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
        '������ ��� ������� ������ ������ ('
        || ' bankBic="' || bankBic || '"'
        || ').'
      )
    , true
  );
end getBank;

/* func: getGoodsSegment
  ���������� �������� �������.

  ������� ( ������):
  goods_segment_code          - ��� �������� ������
  goods_segment_name          - ������������ �������� ������

  ���������:
  - ������������ ������ ������������� �� goods_segment_name;
*/
function getGoodsSegment
return sys_refcursor
is

  -- ������������ ������
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
        '������ ��� ������� ��������� �������.'
      )
    , true
  );
end getGoodsSegment;

/* func: getGoodsType
  ���������� ���� �������.

  ������� ( ������):
  goods_type_code           - ��� ���� ������
  goods_type_name           - ������������ ���� ������

  ���������:
  - ������������ ������ ������������� �� goods_type_name;
*/
function getGoodsType
return sys_refcursor
is

  -- ������������ ������
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
        '������ ��� ������� ����� �������.'
      )
    , true
  );
end getGoodsType;

/* func: getMotivationType
  ���������� ���� ���������.

  ������� ( ������):
  motivation_type_code        - ��� ���� ���������
  motivation_type_name        - ������������ ���� ���������
  motivation_type_comment     - ����������� ��� ���� ���������

  ���������:
  - ������������ ������ ������������� �� motivation_type_name;
*/
function getMotivationType
return sys_refcursor
is

  -- ������������ ������
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
        '������ ��� ������� ����� ���������.'
      )
    , true
  );
end getMotivationType;

/* func: getRequestStatus
  ���������� ������� ������� �� �������.

  ������� ( ������):
  request_status_code         - ��� ������� �������
  request_status_name         - ������������ ������� �������

  ���������:
  - ������������ ������ ������������� �� request_status_name;
*/
function getRequestStatus
return sys_refcursor
is

  -- ������������ ������
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
        '������ ��� ������� �������� ������� �� �������.'
      )
    , true
  );
end getRequestStatus;

/* func: getShop
  ���������� ��������.

  ���������:
  shopName                    - ������������ ��������
                                ( ����� �� like ��� ����� ��������)
                                ( �� ��������� ��� �����������)
  maxRowCount                 - ������������ ����� ������������ �������
                                ( �� ��������� ��� �����������)

  ������� ( ������):
  shop_id                     - Id ��������
  shop_name                   - ������������ ��������

  ���������:
  - ������������ ������ ������������� �� shop_name;
*/
function getShop(
  shopName varchar2 := null
  , maxRowCount integer := null
)
return sys_refcursor
is

  -- ������������ ������
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
        '������ ��� ������� ��������� ('
        || ' shopName="' || shopName || '"'
        || ').'
      )
    , true
  );
end getShop;

/* func: getUnit
  ���������� ������� ���������.

  ������� ( ������):
  unit_code                   - ��� ������� ���������
  unit_short_name             - ������� ������������ ������� ���������
  unit_name                   - ������������ ������� ���������

  ���������:
  - ������������ ������ ������������� �� unit_name;
*/
function getUnit
return sys_refcursor
is

  -- ������������ ������
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
        '������ ��� ������� ������ ���������.'
      )
    , true
  );
end getUnit;

end pkg_JepRiaShowcase;
/
