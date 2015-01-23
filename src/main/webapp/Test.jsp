<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<%-- Изменение конфигурационных параметров--%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Test Page</title>
    </head>
    <body>
        <h1>Настройки:</h1>
        <br>
        ${Changed}
        <br>
        <form method="get" action="Test">
            Url партнера: <input type="text" value="http://127.0.0.1:8080/Zagruzka/Test" name="partnerURL"><br>
            Url к DB: <input type="text" value="jdbc:mysql://127.0.0.1:3306/zagruzka" name="dbURL"><br>
            DB Login: <input type="text" value="root" name="dbLogin"><br>
            DB Password: <input type="text" value="589031" name="dbPassword"><br>
            Таблица отправок: <input type="text" value="phone_list" name="phoneListsTable"><br>
            Таблица номеров: <input type="text" value="phones" name="phonesTable"><br>
            Сценарий:<select name="scenario" >
                <option selected value="ok">ok
                <option value="error">error
            </select><br>
            <input type="submit" value="Отправить">
        </form>
    </body>
</html>