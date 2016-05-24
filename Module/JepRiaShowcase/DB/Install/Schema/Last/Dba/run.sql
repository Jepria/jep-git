-- script: Install/Schema/Last/Dba/run.sql
-- Скрипт для выполнения под пользователем, обладающим ролью DBA.

create tablespace itm_data
datafile itm_data size 100M autoextend on
/

create tablespace itm_data
datafile itm_data size 100M autoextend on
/

create user itm identified by itm
/
alter user itm default tablespace itm
/
alter user itm quota unlimited on itm_data
/
alter user itm quota unlimited on itm_index
/

grant create any synonym to itm
/
grant create procedure to itm
/
grant create sequence to itm
/
grant create session to itm
/
grant create table to itm
/
grant create trigger to itm
/
grant create type to itm
/
grant create view to itm
/

create user itm_user identified by itm_user
/
grant create session to itm_user
/


