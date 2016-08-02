<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2 Final//EN">
<html>
  <body bgcolor="white">
    Пакет содержит application часть модуля ${moduleName}.<br/>
    <h2>Содержание</h2>
    <ul>
      <li><a href="#about">Описание</a>
      <li><a href="#binRepoPreparation">Подготовка бинарного репозитория</a>
      <li><a href="#deploy">Установка</a>
      <li><a href="#error">Ошибки</a>
      <li><a href="#todo">Доработки</a>
    </ul>
    <h3><a name="about">Описание</a></h3>
    Данный модуль содержит функционал.
    <h3><a name="binRepoPreparation">Подготовка бинарного репозитория</a></h3>
    Для возможности установки данного модуля необходимо сперва развернуть бинарный репозиторий на локальном окружении.<br/>
    Для этого достаточно скачать модуль <a href="svn://srvbl08/JEP/Bin">Bin</a> в подходящую директорию на локальной файловой системе ОС, ссылка на которую будет использована во время установки модуля.
    <h3><a name="deploy">Установка</a></h3>
    Перед установкой необходимо убедиться в наличии бинарного репозитория на локальном окружении, с которого производится установка данного модуля:
    <ul>
      <li>В случае его отсутствия необходимо выполнить действия из раздела <a href="#binRepoPreparation">Подготовка бинарного репозитория</a> и продолжить установку.</li>
      <li>Если бинарный репозиторий уже настроен, то рекомендуется произвести его актуализацию, выполнив команду SVN Update, после чего продолжить установку.</li>
    </ul>
    Для продолжения установки текущего модуля, необходимо перейти в директорию <i>App</i> модуля и выполнить команду:<br/>
    <i>ant deploy -DDEPLOYMENT_PATH=&lt;DEPLOYMENT_PATH&gt; -DLOGIN=&lt;LOGIN&gt; -DPASSWORD=&lt;PASSWORD&gt; -DLOAD_OPERATORID=&lt;LOAD_OPERATORID&gt;&nbsp; -DBIN_HOME=&lt;BIN_HOME&gt;&nbsp;</i>, где<br/>
    <ul>
      <li>DEPLOYMENT_PATH - адрес установки модуля вида <i>[opmn://]host:opmnPort[/iASInstanceName/oc4jInstanceName]</i></li>
      <li>PORT - параметр нужен в случае, если http-порт на сервере отличен от 80го (по-умолчанию значение 80)</li>
      <li>LOGIN - логин пользователя, под которым происходит установка модуля</li>
      <li>PASSWORD - пароль пользователя, под которым происходит установка модуля</li>
      <li>LOAD_OPERATORID - логин/пароль учетной записи в системе RFInfo, от чьего имени производится установка</li>
      <li>BIN_HOME - директория бинарного репозитория</li>
    </ul>
    Пример:
    <pre>ant deploy -DDEPLOYMENT_PATH=opmn://10.90.7.148:6003/OracleAS_1.srvapp7.d.t/OC4J_4 -DPORT=8888 -DLOGIN=LOGIN -DPASSWORD=PASSWORD -DLOAD_OPERATORID=nagornyys/123 -DBIN_HOME=C:/Project/JEP/Bin<br/></pre>
    <h3><a name="error">Ошибки</a></h3>
    <br/>
    <h3><a name="todo">Доработки</a></h3>
    <br/>
  </body>
</html>
