<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
  <body bgcolor="white">
    Пакет содержит application часть модуля ${moduleName}.<br/>
    <h2>Содержание</h2>
    <ul>
      <li><a href="#about">Описание</a>
      <li><a href="#deploy">Установка</a>
      <li><a href="#error">Ошибки</a>
      <li><a href="#todo">Доработки</a>
    </ul>
    <h3><a name="about">Описание</a></h3>
    Данный модуль содержит функционал.
    <h3><a name="deploy">Установка</a></h3>
    Установка производится из директории <i>App</i> модуля командой:<br/>
    <i>ant deploy -DDEPLOYMENT_PATH=&lt;DEPLOYMENT_PATH&gt; -DLOGIN=&lt;LOGIN&gt; -DPASSWORD=&lt;PASSWORD&gt; -DOC4J_HOME=&lt;OC4J_HOME&gt; -DLOAD_OPERATORID=&lt;LOAD_OPERATORID&gt;&nbsp;</i>, где<br/>
    <ul>
      <li>DEPLOYMENT_PATH - адрес установки модуля вида <i>[opmn://]host:opmnPort[/iASInstanceName/oc4jInstanceName]</i>
      <li>PORT - параметр нужен в случае, если http-порт на сервере отличен от 80го (по-умолчанию значение 80)
      <li>LOGIN - логин пользователя, под которым происходит установка модуля
      <li>PASSWORD - пароль пользователя, под которым происходит установка модуля
      <li>OC4J_HOME - директория установки JDeveloper
      <li>LOAD_OPERATORID - логин/пароль учетной записи в системе RFInfo, от чьего имени производится установка
    </ul>
    Пример:
    <pre>ant deploy -DDEPLOYMENT_PATH=opmn://10.90.7.148:6003/OracleAS_1.srvapp7.d.t/OC4J_4 -DPORT=8888 -DLOGIN=LOGIN -DPASSWORD=PASSWORD -DOC4J_HOME=D:\Oracle\jdev101330 -DLOAD_OPERATORID=nagornyys/123<br/></pre>
    <h3><a name="error">Ошибки</a></h3>
    <br/>
    <h3><a name="todo">Доработки</a></h3>
    <br/>
  </body>
</html>
