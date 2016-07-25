-- TODO: ������� ���������� ��-��� ���������� ������������ � ����� ������������ (e.g. common).
declare

  -- ����� ����������� �����
  nCreated integer := 0;

  -- ����� ����� ��������� �����
  nExists integer := 0;



  /*
    ��������� ���� � ������ �� ����������.
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
        'JepRiaShowcase: �������������� ������ �� �������� �� ����������'
    , roleNameEn  =>
        'JepRiaShowcase: Access to edit features data'
    , description =>
        '������������ � ������ ����� ����� ������ � ���������� ��������� �� ����� ����������'
  );
  addRole(
    shortName     => 'JrsEditGoods'
    , roleName    =>
        'JepRiaShowcase: �������������� ������ �� �������'
    , roleNameEn  =>
        'JepRiaShowcase: Access to edit goods data'
    , description =>
        '������������ � ������ ����� ����� �������� � ������� � �������'
  );
  addRole(
    shortName     => 'JrsEditRequest'
    , roleName    =>
        'JepRiaShowcase: �������������� ������ �� �������� �� �������'
    , roleNameEn  =>
        'JepRiaShowcase: Access to edit goods data'
    , description =>
        '������������ � ������ ����� ����� �������� � ������� �� �������� �� �������'
  );
  addRole(
    shortName     => 'JrsEditRequestProcess'
    , roleName    =>
        'JepRiaShowcase: �������������� ������ �� ��������� �������� �� �������'
    , roleNameEn  =>
        'JepRiaShowcase: Access to edit goods data'
    , description =>
        '������������ � ������ ����� ����� �������� � ������� �� ��������� �������� �� �������'
  );
  addRole(
    shortName     => 'JrsEditShopGoods'
    , roleName    =>
        'JepRiaShowcase: �������������� ������ �� ������� � ���������'
    , roleNameEn  =>
        'JepRiaShowcase: Access to edit data about goods in shop'
    , description =>
        '������������ � ������ ����� ����� �������� � ������� �� ������� � ���������'
  );
  addRole(
    shortName     => 'JrsEditSupplier'
    , roleName    =>
        'JepRiaShowcase: �������������� ������ �� �����������'
    , roleNameEn  =>
        'JepRiaShowcase: Access to edit suppliers data'
    , description =>
        '������������ � ������ ����� ����� �������� � ������� � �����������'
  );

  dbms_output.put_line(
    'roles created: ' || nCreated
    || ' ( already exists: ' || nExists || ')'
  );
  commit;
end;
/
