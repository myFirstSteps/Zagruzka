<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Главная страница</title>
        <style type="text/css">
            h1{font-size: 20px;}
            #phones{font-size: 22px;}
            #info{color: <c:choose><c:when test='${XMLAnswer.httpCode ge 200 and XMLAnswer.httpCode lt 300 }'>green</c:when>
                          <c:otherwise>red</c:otherwise>
                      </c:choose>;}
            </style>
        </head>
        <body>
            <form id="form" action="Zagruzka.serv" method="post">
            <h1>Номера телефонов</h1>
            <span  id="info">
                <c:choose><c:when test='${XMLAnswer.httpCode ge 300}'>Отсутствует связь с партнером!</c:when> 
                    <c:when test='${XMLAnswer!=null}'>Данные успешно переданы!
                    </c:when>
                </c:choose>
            </span> <br>
            <textarea id="phones" rows="10" cols="20" name="phoneNumbers"></textarea>
            <br>
        </form>
        <button  onclick="validateAndSend()">Отправит</button>
        <script src='scripts/jquery-1.11.1.min.js'></script> 
        <script>
            function validateAndSend() {
                var delimeter = /\n/;
                var pattern = /^7\d{10}$/;
                var phones = ($("#phones").val()).split(delimeter);
                for (var i = 0; i < phones.length; i++) {
                    if (!pattern.test(String(phones[i]))) {
                        $("#info").css("color", "red").text("В списке номеров есть ошибки.Ошибка в строке №" + (i + 1));
                        return;
                    }
                }
                $("#info").css("color", "green").text("Отправка данных");
                $("#form").submit();
            }
        </script>
    </body>
</html>
