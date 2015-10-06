insert into
  jrs_shop
(
  shop_name
)
select
  s.shop_name
from
  (
  select
    'Магазин №' || to_char( rownum) as shop_name
  from
    (
    select
      *
    from
      dual
    connect by
      null is null
    )
  where
    rownum <= 10
  ) s
where
  not exists
    (
    select
      null
    from
      jrs_shop t
    where
      t.shop_name = s.shop_name
    )
/

commit
/
