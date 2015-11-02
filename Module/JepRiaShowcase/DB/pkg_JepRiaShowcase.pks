create or replace package pkg_JepRiaShowcase is
/* package: pkg_JepRiaShowcase
  ������������ ����� ������ JepRiaShowcase.

  SVN root: JEP/Module/JepRiaShowcase
*/



/* group: ��������� */

/* const: Module_Name
  �������� ������, � �������� ��������� �����.
*/
Module_Name constant varchar2(30) := 'JepRiaShowcase';



/* group: ���� */

/* const: Goods_RoleSName
  �������� �������� ���� "�������������� ������ �� �������".
*/
Goods_RoleSName constant varchar2(50) :=
  'JrsEditGoods'
;

/* const: Request_RoleSName
  �������� �������� ���� "�������������� ������ �� �������� �� �������".
*/
Request_RoleSName constant varchar2(50) :=
  'JrsEditRequest'
;

/* const: RequestProcess_RoleSName
  �������� �������� ����
  "�������������� ������ �� ��������� �������� �� �������".
*/
RequestProcess_RoleSName constant varchar2(50) :=
  'JrsEditRequestProcess'
;

/* const: ShopGoods_RoleSName
  �������� �������� ���� "�������������� ������ �� ������� � ���������".
*/
ShopGoods_RoleSName constant varchar2(50) :=
  'JrsEditShopGoods'
;

/* const: Supplier_RoleSName
  �������� �������� ���� "�������������� ������ �� �����������".
*/
Supplier_RoleSName constant varchar2(50) :=
  'JrsEditSupplier'
;



/* group: ������� */



/* group: ��������� */

/* pfunc: createSupplier
  ������� ����������.

  ���������:
  supplierName                - ������������ ����������
  contractFinishDate          - ���� �� ������� ��������� �������
                                ( � ��������� �� ���, ������������)
  exclusiveSupplierFlag       - ����������������� ��������� ( 1 ��, 0 ���)
                                ( �� ��������� 0)
  privilegeSupplierFlag       - ����������������� ��������� ( 1 ��, 0 ���)
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
  �������� ������ ����������.

  ���������:
  supplierId                  - Id ����������
  supplierName                - ������������ ����������
  contractFinishDate          - ���� �� ������� ��������� �������
                                ( � ��������� �� ���, ������������)
  exclusiveSupplierFlag       - ����������������� ��������� ( 1 ��, 0 ���)
  privilegeSupplierFlag       - ����������������� ��������� ( 1 ��, 0 ���)
  phoneNumber                 - �������
  faxNumber                   - ����
  bankBic                     - ����
  recipientName               - ����������
  settlementAccount           - ��������� ����
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

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
  ������� ����������.

  ���������:
  supplierId                  - Id ����������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ( <body::deleteSupplier>)
*/
procedure deleteSupplier(
  supplierId integer
  , operatorId integer := null
);

/* pfunc: findSupplier
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
  privilegeSupplierFlag       - ����������������� ��������� ( 1 ��, 0 ���)
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
  privilege_supplier_flag     - ����������������� ��������� ( 1 ��, 0 ���)
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



/* group: ����� */

/* pfunc: createGoods
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
  ������� �����.

  ���������:
  goodsId                     - Id ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ( <body::deleteGoods>)
*/
procedure deleteGoods(
  goodsId integer
  , operatorId integer := null
);

/* pfunc: findGoods
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

  ( <body::getGoods>)
*/
function getGoods(
  goodsName varchar2 := null
  , maxRowCount integer := null
)
return sys_refcursor;



/* group: �������� ��� ������ */

/* pproc: createGoodsSegmentLink
  ��������� ������� ��� ������.

  ���������:
  goodsId                     - Id ������
  goodsSegmentCode            - ��� �������� ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ( <body::createGoodsSegmentLink>)
*/
procedure createGoodsSegmentLink(
  goodsId integer
  , goodsSegmentCode varchar2
  , operatorId integer := null
);

/* pproc: deleteGoodsSegmentLink
  ������� ������� ��� ������.

  ���������:
  goodsId                     - Id ������
  goodsSegmentCode            - ��� �������� ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ( <body::deleteGoodsSegmentLink>)
*/
procedure deleteGoodsSegmentLink(
  goodsId integer
  , goodsSegmentCode varchar2
  , operatorId integer := null
);

/* pfunc: getGoodsSegmentLink
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

  ( <body::getGoodsSegmentLink>)
*/
function getGoodsSegmentLink(
  goodsId integer
  , operatorId integer := null
)
return sys_refcursor;



/* group: ������� �������� ��� ������ */

/* pproc: setGoodsCatalogLink
  ������������� ������� ��������, � ������� ��������� �����.

  ���������:
  goodsId                     - Id ������
  goodsCatalogIdList          - Id �������� ��������, � ������� ���������
                                ����� ( ������ ����� �������, null ���
                                ���������� ��������)
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ( <body::setGoodsCatalogLink>)
*/
procedure setGoodsCatalogLink(
  goodsId integer
  , goodsCatalogIdList varchar2
  , operatorId integer := null
);

/* pfunc: getGoodsCatalog
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

  ( <body::getGoodsCatalog>)
*/
function getGoodsCatalog(
  parentGoodsCatalogId integer := null
  , goodsId integer := null
)
return sys_refcursor;



/* group: ����� � �������� */

/* pfunc: createShopGoods
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
  �������� ������ � ������ � ��������.

  ���������:
  shopGoodsId                 - Id ������ � ��������
  goodsQuantity               - ���������� ������
  sellPrice                   - ��������� ����
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ( <body::updateShopGoods>)
*/
procedure updateShopGoods(
  shopGoodsId integer
  , goodsQuantity number
  , sellPrice number
  , operatorId integer := null
);

/* pproc: deleteShopGoods
  ������� ������ � ������ � ��������.

  ���������:
  shopGoodsId                 - Id ������ � ��������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ( <body::deleteShopGoods>)
*/
procedure deleteShopGoods(
  shopGoodsId integer
  , operatorId integer := null
);

/* pfunc: findShopGoods
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



/* group: ������ �� ������� */

/* pfunc: createRequest
  ������� ������ �� �������.

  ���������:
  shopId                      - Id ��������
  goodsId                     - Id ������
  goodsQuantity               - ���������� ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  �������:
  Id ��������� ������.

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
  �������� ������ �� �������.

  ���������:
  requestId                   - Id �������
  requestStatusCode           - ��� ������� �������
  goodsId                     - Id ������
  goodsQuantity               - ���������� ������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

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
  ������� ������ �� �������.

  ���������:
  requestId                   - Id �������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ( <body::deleteRequest>)
*/
procedure deleteRequest(
  requestId integer
  , operatorId integer := null
);

/* pfunc: findRequest
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



/* group: ��������� ������� */

/* pfunc: createRequestProcess
  ������� ������ �� ��������� ������� �� �������.

  ���������:
  requestId                   - Id �������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  �������:
  Id ��������� ������.

  ( <body::createRequestProcess>)
*/
function createRequestProcess(
  requestId integer
  , operatorId integer := null
)
return integer;

/* pproc: deleteRequestProcess
  ������� ������ �� ��������� ������� �� �������.

  ���������:
  requestProcessId            - Id ������ �� ��������� �������
  operatorId                  - Id ���������, ������������ ��������
                                ( �� ��������� �������)

  ( <body::deleteRequestProcess>)
*/
procedure deleteRequestProcess(
  requestProcessId integer
  , operatorId integer := null
);

/* pfunc: findRequestProcess
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



/* group: ����������� */

/* pfunc: getBank
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

  ( <body::getBank>)
*/
function getBank(
  bankBic varchar2 := null
  , maxRowCount integer := null
)
return sys_refcursor;

/* pfunc: getGoodsSegment
  ���������� �������� �������.

  ������� ( ������):
  goods_segment_code          - ��� �������� ������
  goods_segment_name          - ������������ �������� ������

  ���������:
  - ������������ ������ ������������� �� goods_segment_name;

  ( <body::getGoodsSegment>)
*/
function getGoodsSegment
return sys_refcursor;

/* pfunc: getGoodsType
  ���������� ���� �������.

  ������� ( ������):
  goods_type_code           - ��� ���� ������
  goods_type_name           - ������������ ���� ������

  ���������:
  - ������������ ������ ������������� �� goods_type_name;

  ( <body::getGoodsType>)
*/
function getGoodsType
return sys_refcursor;

/* pfunc: getMotivationType
  ���������� ���� ���������.

  ������� ( ������):
  motivation_type_code        - ��� ���� ���������
  motivation_type_name        - ������������ ���� ���������
  motivation_type_comment     - ����������� ��� ���� ���������

  ���������:
  - ������������ ������ ������������� �� motivation_type_name;

  ( <body::getMotivationType>)
*/
function getMotivationType
return sys_refcursor;

/* pfunc: getRequestStatus
  ���������� ������� ������� �� �������.

  ������� ( ������):
  request_status_code         - ��� ������� �������
  request_status_name         - ������������ ������� �������

  ���������:
  - ������������ ������ ������������� �� request_status_name;

  ( <body::getRequestStatus>)
*/
function getRequestStatus
return sys_refcursor;

/* pfunc: getShop
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

  ( <body::getShop>)
*/
function getShop(
  shopName varchar2 := null
  , maxRowCount integer := null
)
return sys_refcursor;

/* pfunc: getUnit
  ���������� ������� ���������.

  ������� ( ������):
  unit_code                   - ��� ������� ���������
  unit_short_name             - ������� ������������ ������� ���������
  unit_name                   - ������������ ������� ���������

  ���������:
  - ������������ ������ ������������� �� unit_name;

  ( <body::getUnit>)
*/
function getUnit
return sys_refcursor;

end pkg_JepRiaShowcase;
/
