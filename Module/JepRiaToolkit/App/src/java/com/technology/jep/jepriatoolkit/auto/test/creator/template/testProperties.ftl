<#rt>﻿<#noparse>
# Параметры Selenium-TestNG-тестирования
﻿</#noparse><#rt>
host=localhost
appName=${moduleName}
<#rt>﻿<#noparse>
jepriaVersion=9.*.*

browserName=chrome
browserPath=C:/Users/ZaharovR/AppData/Local/Google/Chrome/Application/chrome.exe
driverPath=C:/Users/ZaharovR/AppData/Local/Google/Chrome/Application/chromedriver.exe
#!Внимание!
#Необходимо следить за актуальностью chromedriver, так как Chrome обновляется автоматически.
#Например, вдруг тесты перестали запускаться, а именно окно Chrome не отвечает, то 
#большая вероятность, что Chrome обновился. 

#browserName=firefox
#browserPath=C:/Program Files/Mozilla Firefox/firefox.exe


# Обязателен любой непустой общий постфикс (01) в названиях свойств
﻿</#noparse><#rt>
<#list forms as form>
testSuite0${form?index}=**/auto/**/${form.formName}AutoTest.xml
testUsername0${form?index}=Username
testPassword0${form?index}=123
testDbUrl0${form?index}=jdbc:oracle:thin:@host:1521:orcl
testDbUser0${form?index}=itm
testDbPassword0${form?index}=itm

</#list>