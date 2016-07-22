-- script: Install/Schema/Last/Dba/run.sql
-- Скрипт для выполнения под пользователем, обладающим ролью DBA.
-- Параметры вызова:
-- 1 - имя файла данных для табличного пространства itm_data (например: C:\ORACLE\INFOT\ITM_DATA01.DBF)
-- 2 - имя файла данных для табличного пространства itm_index (например: C:\ORACLE\INFOT\ITM_INDEX01.DBF)

create tablespace itm_data
datafile '&1' size 100M reuse autoextend on next 100M maxsize unlimited
/

create tablespace itm_index
datafile '&2' size 100M reuse autoextend on next 100M maxsize unlimited
/

create user itm identified by itm
/
alter user itm default tablespace itm_data
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
alter user itm_user default tablespace itm_data
/

grant create session to itm_user
/


