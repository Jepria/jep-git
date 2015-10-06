insert into
  jrs_request
(
  shop_id
  , request_date
  , request_status_code
  , goods_id
  , goods_quantity
)
select
  s.shop_id
  , s.request_date
  , s.request_status_code
  , s.goods_id
  , s.goods_quantity
from
  (
  select
    sp.shop_id
    , sysdate - 15
      - ( gd.goods_id + sp.shop_id + floor( gd.purchasing_price) / 1000) / 10
      as request_date
    , rs.request_status_code
    , gd.goods_id
    , 3 + mod(
          abs( gd.goods_id + sp.shop_id - ceil( gd.purchasing_price))
          , 10
        )
      as goods_quantity
  from
    jrs_goods gd
    cross join jrs_shop sp
    inner join
      (
      select
        rst.request_status_code
        , rownum as order_number
      from
        jrs_request_status rst
      order by
        1
      ) rs
      on rs.order_number =
        mod( gd.goods_id + sp.shop_id + floor( gd.purchasing_price), 7) + 1
  where
    mod( gd.goods_id + sp.shop_id + floor( gd.purchasing_price), 4) = 1
  ) s
where
  not exists
    (
    select
      null
    from
      jrs_request t
    where
      t.shop_id = s.shop_id
      and t.goods_id = s.goods_id
    )
order by
  s.request_date
/

commit
/
