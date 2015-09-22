insert into
  jrs_shop_goods
(
  shop_id
  , goods_id
  , goods_quantity
  , sell_price
)
select
  s.shop_id
  , s.goods_id
  , s.goods_quantity
  , s.sell_price
from
  (
  select
    sp.shop_id
    , gd.goods_id
    , 1 + mod(
          abs( gd.goods_id + sp.shop_id - ceil( gd.purchasing_price))
          , 15
        )
      as goods_quantity
    , gd.purchasing_price
      * ( 1.1 + mod( gd.goods_id + sp.shop_id, 8)/10)
      as sell_price
  from
    jrs_goods gd
    cross join jrs_shop sp
  where
    mod( gd.goods_id + sp.shop_id + floor( gd.purchasing_price), 3) = 0
  ) s
where
  not exists
    (
    select
      null
    from
      jrs_shop_goods t
    where
      t.shop_id = s.shop_id
      and t.goods_id = s.goods_id
    )
/

commit
/
