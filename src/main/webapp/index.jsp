<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Главная страница</title>
        <style type="text/css">
            h1{font-size: 20px;}
            #phones{font-size: 22px;}
        </style>
    </head>
    <body>
        <form id="form" action="Zagruzka.serv" method="post">
            <h1>Номера телефонов</h1>
            <span id="info"></span> <br> <%--!!!!!!!!!!!!!!!!!!!!!!!!!!!! --%>
            <textarea id="phones" rows="10" cols="20" name="phoneNumbers"></textarea>
            <br><%--!!!!!!!!!!!!!!!!!!!!!!!!!!!! --%>
        </form>
        <button  onclick="validate()">Отправит</button>
        <script src='scripts/jquery-1.11.1.min.js'></script> 
        <script>
            function validate() {
                var del = /\n/;
                var canon = /^7\d{10}$/;
                var phones = ($("#phones").val()).split(del);
                for (var i = 0; i < phones.length; i++) {
                    if (!canon.test(String(phones[i]))) {
                        $("#info").css("color", "red").text("В списке номеров есть ошибки.Ошибка в строке №" + (i+1));
                        return;
                    }
                }
                $("#info").css("color", "green").text("Отправка данных");
                $("#form").submit();
            }
        </script>
    </body>
</html>
