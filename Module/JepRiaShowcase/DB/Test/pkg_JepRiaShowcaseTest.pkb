create or replace package body pkg_JepRiaShowcaseTest is
/* package body: pkg_JepRiaShowcaseTest::body */



/* group: ���������� */

/* ivar: logger
  ����� ������.
*/
logger lg_logger_t := lg_logger_t.getLogger(
  moduleName    => pkg_JepRiaShowcase.Module_Name
  , objectName  => 'pkg_JepRiaShowcaseTest'
);



/* group: ������� */

/* proc: testUserApi
  ������������ API ��� ����������������� ����������.
*/
procedure testUserApi
is

  -- ��������� ���������� �������
  rc sys_refcursor;

  -- �������� ��� ������ API-�������
  operatorId integer := pkg_Operator.getCurrentUserId();

  -- �������� ������
  supplierId integer;
  supplierId2 integer;

  goodsId integer;
  goodsId2 integer;

  requestId integer;



  /*
    ��������� ����� ������� � �������.
  */
  procedure checkCursor(
    functionName varchar2
    , expectedRowCount integer := null
  )
  is
  begin
    pkg_TestUtility.compareRowCount(
      rc
      , expectedRowCount => coalesce( expectedRowCount, 0)
      , failMessageText  =>
          functionName || ': ������������ ����� ������� � �������'
    );
  end checkCursor;



  /*
    ��������� ����� ������� � ������� �� ����� ������� � �������.
  */
  procedure checkCursor(
    functionName varchar2
    , tableName varchar2
    , whereSql varchar2 := null
    , filterCondition varchar2 := null
  )
  is

    expectedRowCount integer;

  begin
    execute immediate
'select
  count(*)
from
  ' || tableName || ' t'
|| case when whereSql is not null then
'
where
  ' || whereSql
end
    into
      expectedRowCount
    ;
    pkg_TestUtility.compareRowCount(
      rc
      , expectedRowCount => expectedRowCount
      , filterCondition  => filterCondition
      , failMessageText  =>
          functionName || ': ������������ ����� ������� � �������'
    );
  exception when others then
    raise_application_error(
      pkg_Error.ErrorStackInfo
      , logger.errorStack(
          '������ ��� ��������� ����� ������� � ������� � ������� ('
          || ' tableName="' || tableName || '"'
          || ', whereSql="' || whereSql || '"'
          || ').'
        )
      , true
    );
  end checkCursor;



  /*
    ���� ������� %Supplier.
  */
  procedure testSupplierApi
  is
  begin
    supplierId := pkg_JepRiaShowcase.createSupplier(
      supplierName            => '$TEST-Supplier 1'
      , contractFinishDate    => DATE '4099-05-14'
      , exclusiveSupplierFlag => 1
      , phoneNumber           => '98-01-01'
      , faxNumber             => '98-02-01'
      , bankBic               => '044585187'
      , recipientName         => 'Test recipient 1'
      , settlementAccount     => '000001'
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_supplier'
      , filterCondition   => '
supplier_id = ''' || supplierId || '''
and supplier_name = ''$TEST-Supplier 1''
and contract_finish_date = DATE ''4099-05-14''
and exclusive_supplier_flag = 1
and phone_number = ''98-01-01''
and fax_number = ''98-02-01''
and bank_bic = ''044585187''
and recipient_name = ''Test recipient 1''
and settlement_account = ''000001''
'
      , expectedRowCount  => 1
      , failMessageText   =>
          'createSupplier: ������ �� ������� ��� �����������'
    );

    pkg_JepRiaShowcase.updateSupplier(
      supplierId              => supplierId
      , supplierName          => '$TEST-Supplier 2'
      , contractFinishDate    => DATE '4099-05-15'
      , exclusiveSupplierFlag => 0
      , phoneNumber           => '98-01-02'
      , faxNumber             => '98-02-02'
      , bankBic               => '044525448'
      , recipientName         => 'Test recipient 2'
      , settlementAccount     => '000002'
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_supplier'
      , filterCondition   => '
supplier_id = ''' || supplierId || '''
and supplier_name = ''$TEST-Supplier 2''
and contract_finish_date = DATE ''4099-05-15''
and exclusive_supplier_flag = 0
and phone_number = ''98-01-02''
and fax_number = ''98-02-02''
and bank_bic = ''044525448''
and recipient_name = ''Test recipient 2''
and settlement_account = ''000002''
'
      , expectedRowCount  => 1
      , failMessageText   =>
          'updateSupplier: ������ �� �������� ��� �����������'
    );

    rc := pkg_JepRiaShowcase.findSupplier(
      supplierName            => '$TEST-Supplier 1'
      , maxRowCount           => 5
      , operatorId            => operatorId
    );
    checkCursor( 'findSupplier: absent name', 0);

    rc := pkg_JepRiaShowcase.findSupplier(
      supplierId              => supplierId
      , operatorId            => operatorId
    );
    checkCursor( 'findSupplier: id', 1);

    rc := pkg_JepRiaShowcase.findSupplier(
      supplierId              => supplierId
      , supplierName          => '$test-SUPP%2'
      , contractFinishDateFrom  => DATE '4099-05-01'
      , contractFinishDateTo    => DATE '4099-05-28'
      , exclusiveSupplierFlag => 0
      , maxRowCount           => 5
      , operatorId            => operatorId
    );
    checkCursor( 'findSupplier: all args', 1);

    rc := pkg_JepRiaShowcase.findSupplier(
      supplierName            => '$TEST-SUPP%'
      , contractFinishDateTo  => DATE '4099-05-15'
      , maxRowCount           => 5
      , operatorId            => operatorId
    );
    checkCursor( 'findSupplier: name/dateTo', 1);

    rc := pkg_JepRiaShowcase.findSupplier(
      contractFinishDateFrom  => DATE '4099-05-14'
      , exclusiveSupplierFlag => 0
      , operatorId            => operatorId
    );
    checkCursor( 'findSupplier: dateFrom/exlFlag', 1);

    pkg_JepRiaShowcase.deleteSupplier(
      supplierId              => supplierId
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_supplier'
      , filterCondition   => '
supplier_id = ''' || supplierId || '''
'
      , expectedRowCount  => 0
      , failMessageText   =>
          'deleteSupplier: ������ �� ���� �������'
    );

    -- ������� ������ ��� ����������� ������
    supplierId := pkg_JepRiaShowcase.createSupplier(
      supplierName            => '$TEST-Supplier 1'
      , contractFinishDate    => DATE '4099-05-14'
      , exclusiveSupplierFlag => 1
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_supplier'
      , filterCondition   => 'supplier_id = ''' || supplierId || ''''
      , expectedRowCount  => 1
      , failMessageText   =>
          'createSupplier: T1: ������ �� �������'
    );
    supplierId2 := pkg_JepRiaShowcase.createSupplier(
      supplierName            => '$TEST-Supplier 2'
      , contractFinishDate    => DATE '4099-05-01'
      , exclusiveSupplierFlag => 1
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_supplier'
      , filterCondition   => 'supplier_id = ''' || supplierId2 || ''''
      , expectedRowCount  => 1
      , failMessageText   =>
          'createSupplier: T2: ������ �� �������'
    );
  exception when others then
    raise_application_error(
      pkg_Error.ErrorStackInfo
      , logger.errorStack(
          '������ ��� ������������ ������� %Supplier.'
        )
      , true
    );
  end testSupplierApi;



  /*
    ���� ������� %Goods.
  */
  procedure testGoodsApi
  is

    parentGoodsCatalogId integer;
    goodsCatalogId integer;
    maxGoodsCatalogId integer;
    otherGoodsCatalogId integer;

  begin
    goodsId := pkg_JepRiaShowcase.createGoods(
      supplierId                => supplierId2
      , goodsName               => '$TEST-������, 1 �.'
      , goodsTypeCode           => 'FOOD'
      , unitCode                => 'L'
      , purchasingPrice         => 20.75
      , motivationTypeCode      => 'MONTH'
      , goodsPhotoMimeType      => 'jpeg'
      , goodsPhotoExtension     => 'jpg'
      , goodsPortfolioMimeType  => 'text-rtf'
      , goodsPortfolioExtension => 'rtf'
      , operatorId              => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_goods'
      , filterCondition   => '
goods_id = ''' || goodsId || '''
and goods_name = ''$TEST-������, 1 �.''
and goods_type_code = ''FOOD''
and unit_code = ''L''
and purchasing_price = 20.75
and motivation_type_code = ''MONTH''
and goods_photo_mime_type = ''jpeg''
and goods_photo_extension = ''jpg''
and goods_portfolio_mime_type = ''text-rtf''
and goods_portfolio_extension = ''rtf''
'
      , expectedRowCount  => 1
      , failMessageText   =>
          'createGoods: all args: ������ �� ������� ��� �����������'
    );

    pkg_JepRiaShowcase.updateGoods(
      goodsId                   => goodsId
      , supplierId              => supplierId
      , goodsName               => '$TEST-���� �������'
      , goodsTypeCode           => 'INDUSTRIAL'
      , unitCode                => 'ITEM'
      , purchasingPrice         => 18.00
      , motivationTypeCode      => 'QUARTER'
      , goodsPhotoMimeType      => 'image-png'
      , goodsPhotoExtension     => 'png'
      , goodsPortfolioMimeType  => 'text-plain'
      , goodsPortfolioExtension => 'txt'
      , operatorId              => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_goods'
      , filterCondition   => '
goods_id = ''' || goodsId || '''
and goods_name = ''$TEST-���� �������''
and goods_type_code = ''INDUSTRIAL''
and unit_code = ''ITEM''
and purchasing_price = 18.00
and motivation_type_code = ''QUARTER''
and goods_photo_mime_type = ''image-png''
and goods_photo_extension = ''png''
and goods_portfolio_mime_type = ''text-plain''
and goods_portfolio_extension = ''txt''
'
      , expectedRowCount  => 1
      , failMessageText   =>
          'updateGoods: ������ �� �������� ��� �����������'
    );

    -- %GoodsSegmentLink
    pkg_JepRiaShowcase.createGoodsSegmentLink(
      goodsId                 => goodsId
      , goodsSegmentCode      => 'EVERYDAY'
      , operatorId            => operatorId
    );
    pkg_JepRiaShowcase.createGoodsSegmentLink(
      goodsId                 => goodsId
      , goodsSegmentCode      => 'HOME'
      , operatorId            => operatorId
    );
    pkg_JepRiaShowcase.createGoodsSegmentLink(
      goodsId                 => goodsId
      , goodsSegmentCode      => 'TOY'
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_goods_segment_link'
      , filterCondition   => '
goods_id = ''' || goodsId || '''
and goods_segment_code in ( ''EVERYDAY'', ''HOME'',  ''TOY'')
'
      , expectedRowCount  => 3
      , failMessageText   =>
          'createGoodsSegmentLink: ������ �� ��������� ��� �����������'
    );
    pkg_JepRiaShowcase.deleteGoodsSegmentLink(
      goodsId                 => goodsId
      , goodsSegmentCode      => 'TOY'
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_goods_segment_link'
      , filterCondition   => '
goods_id = ''' || goodsId || '''
and goods_segment_code in ( ''EVERYDAY'', ''HOME'',  ''TOY'')
'
      , expectedRowCount  => 2
      , failMessageText   =>
          'deleteGoodsSegmentLink: ������ �� �������'
    );
    rc := pkg_JepRiaShowcase.getGoodsSegmentLink(
      goodsId                 => goodsId
      , operatorId            => operatorId
    );
    checkCursor( 'getGoodsSegmentLink', 2);

    -- %GoodsCatalogLink
    select
      min( gc.parent_goods_catalog_id)
        keep( dense_rank first order by gc.goods_catalog_id)
      , min( gc.goods_catalog_id)
        keep( dense_rank first order by gc.goods_catalog_id)
      , max( gc.goods_catalog_id)
    into parentGoodsCatalogId, goodsCatalogId, maxGoodsCatalogId
    from
      jrs_goods_catalog gc
    where
      gc.parent_goods_catalog_id is not null
    ;
    select
      min( gc.goods_catalog_id)
    into otherGoodsCatalogId
    from
      jrs_goods_catalog gc
    where
      gc.goods_catalog_id not in (
        parentGoodsCatalogId
        , goodsCatalogId
        , maxGoodsCatalogId
      )
    ;
    pkg_JepRiaShowcase.setGoodsCatalogLink(
      goodsId                 => goodsId
      , goodsCatalogIdList    =>
          parentGoodsCatalogId
          || ',' || otherGoodsCatalogId
          || ', ,, '
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_goods_catalog_link'
      , filterCondition   => 'goods_id = ''' || goodsId || ''''
      , expectedRowCount  => 2
      , failMessageText   =>
          'setGoodsCatalogLink: ������������ ����� �������'
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_goods_catalog_link'
      , filterCondition   => '
goods_id = ''' || goodsId || '''
and goods_catalog_id in (
  ''' || parentGoodsCatalogId || '''
  , ''' || otherGoodsCatalogId || '''
)
'
      , expectedRowCount  => 2
      , failMessageText   =>
          'setGoodsCatalogLink: ������ �����������'
    );

    pkg_JepRiaShowcase.setGoodsCatalogLink(
      goodsId                 => goodsId
      , goodsCatalogIdList    =>
          parentGoodsCatalogId
          || ',' || goodsCatalogId
          || ',' || maxGoodsCatalogId
          || ', ,, '
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_goods_catalog_link'
      , filterCondition   => 'goods_id = ''' || goodsId || ''''
      , expectedRowCount  => 3
      , failMessageText   =>
          'setGoodsCatalogLink(2): ������������ ����� �������'
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_goods_catalog_link'
      , filterCondition   => '
goods_id = ''' || goodsId || '''
and goods_catalog_id in (
  ''' || parentGoodsCatalogId || '''
  , ''' || goodsCatalogId || '''
  , ''' || maxGoodsCatalogId || '''
)
'
      , expectedRowCount  => 3
      , failMessageText   =>
          'setGoodsCatalogLink(2): ������ �����������'
    );

    -- getGoodsCatalog
    rc := pkg_JepRiaShowcase.getGoodsCatalog();
    checkCursor(
      'getGoodsCatalog( no args)'
      , 'jrs_goods_catalog'
      , 'parent_goods_catalog_id is null'
      ,
-- ����� � ���� �� ��������� ������� �� descendant_goods_link_flag ��-��
-- ������ � pkg_TestUtility
'has_child_flag = 1
and goods_link_flag is null
--and descendant_goods_link_flag is null
'
    );

    rc := pkg_JepRiaShowcase.getGoodsCatalog(
      parentGoodsCatalogId  => null
      , goodsId             => goodsId
    );
    checkCursor(
      'getGoodsCatalog( goodsId)'
      , 'jrs_goods_catalog'
      , 'parent_goods_catalog_id is null'
      ,
'has_child_flag = 1
and goods_link_flag in ( 0, 1)
--and descendant_goods_link_flag in ( 0, 1)
'
    );

    select
      gc.parent_goods_catalog_id
    into otherGoodsCatalogId
    from
      jrs_goods_catalog gc
    where
      gc.goods_catalog_id = parentGoodsCatalogId
    ;
    rc := pkg_JepRiaShowcase.getGoodsCatalog(
      parentGoodsCatalogId  => otherGoodsCatalogId
      , goodsId             => goodsId
    );
    pkg_TestUtility.compareRowCount(
      rc
      , expectedRowCount  => 1
      , filterCondition   =>
'goods_catalog_id = ' || parentGoodsCatalogId || '
and has_child_flag = 1
and goods_link_flag = 1
--and descendant_goods_link_flag = 1
'
      , failMessageText   =>
          'getGoodsCatalog( parent): ������������ ������ ������'
    );


    -- findGoods
    rc := pkg_JepRiaShowcase.findGoods(
      maxRowCount             => 1
      , operatorId            => operatorId
    );
    checkCursor( 'findGoods: maxRowCount', 1);

    rc := pkg_JepRiaShowcase.findGoods(
      goodsName               => '$TEST-???'
      , operatorId            => operatorId
    );
    checkCursor( 'findGoods: absent name', 0);

    rc := pkg_JepRiaShowcase.findGoods(
      goodsIdList             => to_char( goodsId)
      , operatorId            => operatorId
    );
    checkCursor( 'findGoods: id', 1);

    rc := pkg_JepRiaShowcase.findGoods(
      goodsIdList               => ',' || to_char( goodsId) || ',,,'
      , supplierId              => supplierId
      , goodsName               => '$test-%����%'
      , goodsTypeCode           => 'INDUSTRIAL'
      , goodsSegmentCodeList    => 'TOY,HOME'
      , goodsCatalogIdList      => to_char( parentGoodsCatalogId) || ',-1'
      , maxRowCount             => 5
      , operatorId              => operatorId
    );
    checkCursor( 'findGoods: all args', 1);

    rc := pkg_JepRiaShowcase.findGoods(
      supplierId              => supplierId
      , goodsName             => '$test-%'
      , maxRowCount           => 5
      , operatorId            => operatorId
    );
    checkCursor( 'findGoods: supplier/name', 1);

    rc := pkg_JepRiaShowcase.findGoods(
      goodsName               => '$test-%'
      , goodsCatalogIdList    => '-5,' || to_char( maxGoodsCatalogId)
      , operatorId            => operatorId
    );
    checkCursor( 'findGoods: name/catalog', 1);

    rc := pkg_JepRiaShowcase.getGoods(
      goodsName               => '$test-%'
    );
    checkCursor( 'getGoods: name', 1);

    rc := pkg_JepRiaShowcase.getGoods(
      maxRowCount             => 1
    );
    checkCursor( 'getGoods: maxRowCount', 1);

    pkg_JepRiaShowcase.deleteGoods(
      goodsId                 => goodsId
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_goods'
      , filterCondition   => '
goods_id = ''' || goodsId || '''
'
      , expectedRowCount  => 0
      , failMessageText   =>
          'deleteGoods: ������ �� ���� �������'
    );

    -- ������� ������ ��� ����������� ������
    goodsId := pkg_JepRiaShowcase.createGoods(
      supplierId                => supplierId
      , goodsName               => '$TEST-������, 1 �.'
      , goodsTypeCode           => 'FOOD'
      , unitCode                => 'L'
      , purchasingPrice         => 20.75
      , motivationTypeCode      => 'MONTH'
      , goodsPhotoMimeType      => 'jpeg'
      , goodsPhotoExtension     => 'jpg'
      , goodsPortfolioMimeType  => 'text-rtf'
      , goodsPortfolioExtension => 'rtf'
      , operatorId              => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_goods'
      , filterCondition   => '
goods_id = ''' || goodsId || '''
'
      , expectedRowCount  => 1
      , failMessageText   =>
          'createGoods: test1: ������ �� ������� ��� �����������'
    );
    goodsId2 := pkg_JepRiaShowcase.createGoods(
      supplierId                => supplierId
      , goodsName               => '$TEST-���� �������'
      , goodsTypeCode           => 'INDUSTRIAL'
      , unitCode                => 'ITEM'
      , purchasingPrice         => 18.00
      , operatorId              => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_goods'
      , filterCondition   => '
goods_id = ''' || goodsId2 || '''
and motivation_type_code = ''USUAL''
'
      , expectedRowCount  => 1
      , failMessageText   =>
          'createGoods: test2: ������ �� ������� ��� �����������'
    );

    -- findGoods ��� ���������� �������
    rc := pkg_JepRiaShowcase.findGoods(
      goodsIdList             => goodsId2 || ',,' || goodsId2 || ',' || goodsId
      , operatorId            => operatorId
    );
    checkCursor( 'findGoods: few id', 2);
  exception when others then
    raise_application_error(
      pkg_Error.ErrorStackInfo
      , logger.errorStack(
          '������ ��� ������������ ������� %Goods.'
        )
      , true
    );
  end testGoodsApi;



  /*
    ���� ������� %ShopGoods.
  */
  procedure testShopGoodsApi
  is

    shopId integer;
    shopGoodsId integer;

  begin
    select
      min( t.shop_id)
    into shopId
    from
      jrs_shop t
    ;
    shopGoodsId := pkg_JepRiaShowcase.createShopGoods(
      shopId                  => shopId
      , goodsId               => goodsId
      , goodsQuantity         => 5.0001
      , sellPrice             => 85.88
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_shop_goods'
      , filterCondition   => '
shop_goods_id = ''' || shopGoodsId || '''
and shop_id = ''' || shopId || '''
and goods_id = ''' || goodsId || '''
and goods_quantity = 5.0001
and sell_price = 85.88
'
      , expectedRowCount  => 1
      , failMessageText   =>
          'createShopGoods: ������ �� ������� ��� �����������'
    );

    pkg_JepRiaShowcase.updateShopGoods(
      shopGoodsId             => shopGoodsId
      , goodsQuantity         => 3
      , sellPrice             => 60
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_shop_goods'
      , filterCondition   => '
shop_goods_id = ''' || shopGoodsId || '''
and goods_quantity = 3
and sell_price = 60
'
      , expectedRowCount  => 1
      , failMessageText   =>
          'updateShopGoods: ������ �� �������� ��� �����������'
    );

    rc := pkg_JepRiaShowcase.findShopGoods(
      maxRowCount             => 1
      , operatorId            => operatorId
    );
    checkCursor( 'findShopGoods: maxRowCount', 1);

    rc := pkg_JepRiaShowcase.findShopGoods(
      shopGoodsId             => shopGoodsId
      , operatorId            => operatorId
    );
    checkCursor( 'findShopGoods: id', 1);

    rc := pkg_JepRiaShowcase.findShopGoods(
      shopGoodsId             => shopGoodsId
      , shopId                => shopId
      , goodsId               => goodsId
      , maxRowCount           => 5
      , operatorId            => operatorId
    );
    checkCursor( 'findShopGoods: all args', 1);

    rc := pkg_JepRiaShowcase.findShopGoods(
      goodsId                 => goodsId
      , operatorId            => operatorId
    );
    checkCursor( 'findShopGoods: goods', 1);

    rc := pkg_JepRiaShowcase.getShop(
      maxRowCount             => 1
    );
    checkCursor( 'getShop: maxRowCount', 1);

    pkg_JepRiaShowcase.deleteShopGoods(
      shopGoodsId             => shopGoodsId
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_shop_goods'
      , filterCondition   => '
shop_goods_id = ''' || shopGoodsId || '''
'
      , expectedRowCount  => 0
      , failMessageText   =>
          'deleteShopGoods: ������ �� ���� �������'
    );
  exception when others then
    raise_application_error(
      pkg_Error.ErrorStackInfo
      , logger.errorStack(
          '������ ��� ������������ ������� %ShopGoods.'
        )
      , true
    );
  end testShopGoodsApi;



  /*
    ���� ������� %Request.
  */
  procedure testRequestApi
  is

    shopId integer;

  begin
    select
      min( t.shop_id)
    into shopId
    from
      jrs_shop t
    ;
    requestId := pkg_JepRiaShowcase.createRequest(
      shopId                  => shopId
      , goodsId               => goodsId2
      , goodsQuantity         => 5.0001
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_request'
      , filterCondition   => '
request_id = ''' || requestId || '''
and shop_id = ''' || shopId || '''
and goods_id = ''' || goodsId2 || '''
and goods_quantity = 5.0001
and request_status_code = ''NEW''
'
      , expectedRowCount  => 1
      , failMessageText   =>
          'createRequest: ������ �� ������� ��� �����������'
    );

    pkg_JepRiaShowcase.updateRequest(
      requestId               => requestId
      , requestStatusCode     => 'REJECTED'
      , goodsId               => goodsId
      , goodsQuantity         => 3
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_request'
      , filterCondition   => '
request_id = ''' || requestId || '''
and request_status_code = ''REJECTED''
and goods_id = ''' || goodsId || '''
and goods_quantity = 3
'
      , expectedRowCount  => 1
      , failMessageText   =>
          'updateRequest: ������ �� �������� ��� �����������'
    );

    rc := pkg_JepRiaShowcase.findRequest(
      maxRowCount             => 1
      , operatorId            => operatorId
    );
    checkCursor( 'findRequest: maxRowCount', 1);

    rc := pkg_JepRiaShowcase.findRequest(
      requestId               => requestId
      , operatorId            => operatorId
    );
    checkCursor( 'findRequest: id', 1);

    rc := pkg_JepRiaShowcase.findRequest(
      requestId               => requestId
      , shopId                => shopId
      , requestDateFrom       => trunc( sysdate)
      , requestDateTo         => trunc( sysdate)
      , requestStatusCode     => 'REJECTED'
      , goodsId               => goodsId
      , maxRowCount           => 5
      , operatorId            => operatorId
    );
    checkCursor( 'findRequest: all args', 1);

    rc := pkg_JepRiaShowcase.findRequest(
      goodsId                 => goodsId
      , operatorId            => operatorId
    );
    checkCursor( 'findRequest: goods', 1);

    pkg_JepRiaShowcase.deleteRequest(
      requestId               => requestId
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_request'
      , filterCondition   => '
request_id = ''' || requestId || '''
'
      , expectedRowCount  => 0
      , failMessageText   =>
          'deleteRequest: ������ �� ���� �������'
    );

    -- ������� ������ ��� ����������� ������
    requestId := pkg_JepRiaShowcase.createRequest(
      shopId                  => shopId
      , goodsId               => goodsId
      , goodsQuantity         => 4
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_request'
      , filterCondition   => '
request_id = ''' || requestId || '''
'
      , expectedRowCount  => 1
      , failMessageText   =>
          'createRequest: test1: ������ �� ������� ��� �����������'
    );
  exception when others then
    raise_application_error(
      pkg_Error.ErrorStackInfo
      , logger.errorStack(
          '������ ��� ������������ ������� %Request.'
        )
      , true
    );
  end testRequestApi;



  /*
    ���� ������� %RequestProcess.
  */
  procedure testRequestProcessApi
  is

    requestProcessId integer;

  begin
    requestProcessId := pkg_JepRiaShowcase.createRequestProcess(
      requestId               => requestId
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_request_process'
      , filterCondition   => '
request_process_id = ''' || requestProcessId || '''
and request_id = ''' || requestId || '''
'
      , expectedRowCount  => 1
      , failMessageText   =>
          'createRequestProcess: ������ �� ������� ��� �����������'
    );

    rc := pkg_JepRiaShowcase.findRequestProcess(
      maxRowCount             => 1
      , operatorId            => operatorId
    );
    checkCursor( 'findRequestProcess: maxRowCount', 1);

    rc := pkg_JepRiaShowcase.findRequestProcess(
      requestProcessId        => requestProcessId
      , operatorId            => operatorId
    );
    checkCursor( 'findRequestProcess: id', 1);

    rc := pkg_JepRiaShowcase.findRequestProcess(
      requestProcessId        => requestProcessId
      , requestId             => requestId
      , dateInsFrom           => trunc( sysdate)
      , dateInsTo             => trunc( sysdate)
      , insertOperatorId      => operatorId
      , maxRowCount           => 5
      , operatorId            => operatorId
    );
    checkCursor( 'findRequestProcess: all args', 1);

    rc := pkg_JepRiaShowcase.findRequestProcess(
      requestId               => requestId
      , operatorId            => operatorId
    );
    checkCursor( 'findRequestProcess: requestId', 1);

    pkg_JepRiaShowcase.deleteRequestProcess(
      requestProcessId        => requestProcessId
      , operatorId            => operatorId
    );
    pkg_TestUtility.compareRowCount(
      tableName           => 'jrs_request_process'
      , filterCondition   => '
request_process_id = ''' || requestProcessId || '''
'
      , expectedRowCount  => 0
      , failMessageText   =>
          'deleteRequestProcess: ������ �� ���� �������'
    );
  exception when others then
    raise_application_error(
      pkg_Error.ErrorStackInfo
      , logger.errorStack(
          '������ ��� ������������ ������� %RequestProcess.'
        )
      , true
    );
  end testRequestProcessApi;



  /*
    ���� ������� ���������� ������� get%
  */
  procedure testGetFunction
  is

    bankBic varchar2(100);

  begin
    rc := pkg_JepRiaShowcase.getBank(
      bankBic => '$'
    );
    checkCursor( 'getBank[ bad bic]', 0);

    select
      t.bic
    into bankBic
    from
      v_bic_bank t
    where
      rownum <= 1
    ;
    rc := pkg_JepRiaShowcase.getBank(
      bankBic => bankBic
    );
    checkCursor( 'getBank[ ok bic]', 1);

    rc := pkg_JepRiaShowcase.getBank(
      bankBic         => '%'
      , maxRowCount   => 3
    );
    checkCursor( 'getBank[ all params]', 3);

    rc := pkg_JepRiaShowcase.getBank();
    checkCursor( 'getBank[ all]', 'v_bic_bank');
  exception when others then
    raise_application_error(
      pkg_Error.ErrorStackInfo
      , logger.errorStack(
          '������ ��� ������������ ������� get%.'
        )
      , true
    );
  end testGetFunction;



-- testUserApi
begin
  pkg_TestUtility.beginTest(
    'user API'
  );
  testSupplierApi();
  testGoodsApi();
  testShopGoodsApi();
  testRequestApi();
  testRequestProcessApi();
  testGetFunction();
  pkg_TestUtility.endTest();
  rollback;
exception when others then
  raise_application_error(
    pkg_Error.ErrorStackInfo
    , logger.errorStack(
        '������ ��� ������������ API ��� ����������������� ����������.'
      )
    , true
  );
end testUserApi;

end pkg_JepRiaShowcaseTest;
/
