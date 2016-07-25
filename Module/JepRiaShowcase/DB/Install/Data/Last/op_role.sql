-- TODO: Сделать выполнение из-под отдельного пользователя с общим функционалом (e.g. common).
declare

  -- Число добавленный ролей
  nCreated integer := 0;

  -- Число ранее созданных ролей
  nExists integer := 0;



  /*
    Добавляет роль в случае ее отсутствия.
  */
  procedure addRole(
    shortName varchar2
    , roleName varchar2
    , roleNameEn varchar2
    , description varchar2
  )
  is

    roleId integer;
  begin
    select
      min( t.role_id)
    into roleId
    from
      op_role t
    where
      t.short_name = shortName
    ;
    if roleId is null then
      roleId := pkg_Operator.createRole(
        roleName      => roleName
        , roleNameEn  => roleNameEn
        , shortName   => shortName
        , description => description
        , operatorId  => pkg_Operator.getCurrentUserId()
      );
      dbms_output.put_line(
        'role created: ' || shortName || ' ( role_id=' || roleId || ')'
      );
      nCreated := nCreated + 1;
    else
      nExists := nExists + 1;
    end if;
  end addRole;



-- main
begin
  addRole(
    shortName     => 'JrsEditFeature'
    , roleName    =>
        'JepRiaShowcase: Редактирование данных по запросам на функционал'
    , roleNameEn  =>
        'JepRiaShowcase: Access to edit features data'
    , description =>
        'Пользователь с данной ролью имеет доступ к управлению запросами на новый функционал'
  );
  addRole(
    shortName     => 'JrsEditGoods'
    , roleName    =>
        'JepRiaShowcase: Редактирование данных по товарам'
    , roleNameEn  =>
        'JepRiaShowcase: Access to edit goods data'
    , description =>
        'Пользователь с данной ролью может работать с данными о товарах'
  );
  addRole(
    shortName     => 'JrsEditRequest'
    , roleName    =>
        'JepRiaShowcase: Редактирование данных по запросам на закупку'
    , roleNameEn  =>
        'JepRiaShowcase: Access to edit goods data'
    , description =>
        'Пользователь с данной ролью может работать с данными по запросам на закупку'
  );
  addRole(
    shortName     => 'JrsEditRequestProcess'
    , roleName    =>
        'JepRiaShowcase: Редактирование данных по обработке запросов на закупку'
    , roleNameEn  =>
        'JepRiaShowcase: Access to edit goods data'
    , description =>
        'Пользователь с данной ролью может работать с данными по обработке запросов на закупку'
  );
  addRole(
    shortName     => 'JrsEditShopGoods'
    , roleName    =>
        'JepRiaShowcase: Редактирование данных по товарам в магазинах'
    , roleNameEn  =>
        'JepRiaShowcase: Access to edit data about goods in shop'
    , description =>
        'Пользователь с данной ролью может работать с данными по товарам в магазинах'
  );
  addRole(
    shortName     => 'JrsEditSupplier'
    , roleName    =>
        'JepRiaShowcase: Редактирование данных по поставщикам'
    , roleNameEn  =>
        'JepRiaShowcase: Access to edit suppliers data'
    , description =>
        'Пользователь с данной ролью может работать с данными о поставщиках'
  );

  dbms_output.put_line(
    'roles created: ' || nCreated
    || ' ( already exists: ' || nExists || ')'
  );
  commit;
end;
/
