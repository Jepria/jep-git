# EnvironmentPropertySupport: сценарий тестирования функционала
*Тестирование последний раз проводилось 12.11.2019*

### Подготовка приложений на инстансе Томката

Разворачивается три копии приложения под разными контекстными путями: `/app1`,`/app2`,`/app1/e`.
Приложения различаются только контекстным путём и файлами конфигурации по умолчанию `/WEB-INF/app-conf.default.properties`
Приложения имеют функционал (один и тот же — ведь приложения одинаковые) получения значения переменной по имени вида `EnvironmentPropertySupport.getInstance(...).getProperty(name)`

- **a)** настройка файлов конфигурации по умолчанию в двух приложениях:

Содержимое файла `/app1/WEB-INF/app-conf.default.properties`
```
a1=a1-1-default
a2=a2-1-default
b2=b2-1-default
```
Содержимое файла `/app2/WEB-INF/app-conf.default.properties`
```
a1=a1-2-default
```
Приложение `/app1/e` конфигурации по умолчанию не имеет.

- **b)** настройка переменных окружения машины, на которой запущен томкат
```
b2=b2-env
b3=b3-env
app2#b3=b3-2-env
app-conf.file=C:\b.properties
b5=b5-env
app2#app-conf.file=C:\b-app2.properties
app2#b7=app2#b7-env
c2=c2-env
```
Содержимое файла `C:\b.properties`
```
b4=b4-env-file
b5=b5-env-file
```
Содержимое файла `C:\b-app2.properties`
```
b6=b6-2-env-file
b7=b7-2-env-file
```

- **c)** настройка параметров VM, с которыми стартует томкат
```
-Dc2=c2-prop
-Dc3=c3-prop
-Dapp2#c3=c3-2-prop
-Dapp-conf.file=C:\c.properties
-Dc5=c5-prop
-Dapp2#app-conf.file=C:\c-app2.properties
-Dapp2#c7=app2#c7-prop
-Dd2=d2-prop
```
Содержимое файла `C:\c.properties`
```
c4=c4-prop-file
c5=c5-prop-file
```
Содержимое файла `C:\c-app2.properties`
```
c6=c6-2-prop-file
c7=c7-2-prop-file
```

- **d)** настройка контекстных переменных томката в файле `TOMCAT_HOME/conf/context.xml`
```
<Environment name="d2" value="d2-tom" type="java.lang.String" override="true" />
<Environment name="d3" value="d3-tom" type="java.lang.String" override="true" />
<Environment name="app2#d3" value="d3-2-tom" type="java.lang.String" override="true" />
<Environment name="app-conf.file" value="C:\d.properties" type="java.lang.String" override="true" />
<Environment name="d5" value="d5-tom" type="java.lang.String" override="true" />
<Environment name="app2#app-conf.file" value="C:\d-app2.properties" type="java.lang.String" override="true" />
<Environment name="app2#d7" value="app2#d7-tom" type="java.lang.String" override="true" />
<Environment name="app1#e#e" value="app1#e#e-tom" type="java.lang.String" override="true" />
```
Содержимое файла `C:\d.properties`
```
d4=d4-tom-file
d5=d5-tom-file
```
Содержимое файла `C:\d-app2.properties`
```
d6=d6-2-tom-file
d7=d7-2-tom-file
```

### Тестирование
|веб-запрос в виде `приложение/имя-переменной`|корректное значение переменной|веб-запрос в виде `приложение/имя-переменной`|корректное значение переменной|
|-|-|-|-|
|`app1#a1`|`a1-1-default`|`app2#a1`|`a1-2-default`|
|`app1#a2`|`a2-1-default`|`app2#a2`|`null`|
|`app1#b1`|`null`|`app2#b1`|`null`|
|`app1#b2`|`b2-env`|`app2#b2`|`b2-env`|
|`app1#b3`|`b3-env`|`app2#b3`|`b3-2-env`|
|`app1#b4`|`b4-env-file`|`app2#b4`|`b4-env-file`|
|`app1#b5`|`b5-env`|`app2#b5`|`b5-env`|
|`app1#b6`|`null`|`app2#b6`|`b6-2-env-file`|
|`app1#b7`|`null`|`app2#b7`|`app2#b7-env`|
|`app1#c1`|`null`|`app2#c1`|`null`|
|`app1#c2`|`c2-prop`|`app2#c2`|`c2-prop`|
|`app1#c3`|`c3-prop`|`app2#c3`|`c3-2-prop`|
|`app1#c4`|`c4-prop-file`|`app2#c4`|`c4-prop-file`|
|`app1#c5`|`c5-prop`|`app2#c5`|`c5-prop`|
|`app1#c6`|`null`|`app2#c6`|`c6-2-prop-file`|
|`app1#c7`|`null`|`app2#c7`|`app2#c7-prop`|
|`app1#d1`|`null`|`app2#d1`|`null`|
|`app1#d2`|`d2-tom`|`app2#d2`|`d2-tom`|
|`app1#d3`|`d3-tom`|`app2#d3`|`d3-2-tom`|
|`app1#d4`|`d4-tom-file`|`app2#d4`|`d4-tom-file`|
|`app1#d5`|`d5-tom`|`app2#d5`|`d5-tom`|
|`app1#d6`|`null`|`app2#d6`|`d6-2-tom-file`|
|`app1#d7`|`null`|`app2#d7`|`app2#d7-tom`|

Веб-запрос переменной с именем `e` из приложения `/app1/e`: корректный результат `app1#e#e-tom`
