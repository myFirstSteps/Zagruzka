package tk.pankratovm.zagruzka.web;

import tk.pankratovm.zagruzka.model.Phones;
import tk.pankratovm.zagruzka.model.XMLAnswer;
import tk.pankratovm.zagruzka.model.XMLSender;
import tk.pankratovm.zagruzka.model.PhonesJDBCDAO;
import java.io.IOException;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

/*Поскольку в задании не указана версия Tomcat, используется сервлет API ниже 3.0 (без аннотаций),
 для совместимости с Tomcat младше 7 версии. Необходимые параметры инициализации в web.xml.
 */
public class ZagruzkaServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init();
        //Инициализация сервлета.

        Map<String, String> param = new TreeMap<>();

        /* В т.з. указано, что адрес партнера должен настраиваться. 
         Однако, не уточняется должно ли это происходить на этапе развертывания или 
         выполнения сервлета. Поэтому принято решение задавать адрес в дескрипторе с последующей 
         переноской его в контекст приложения в качестве атрибута. В этом случае
         он при необходимости может быть изменен из другого сервлета. Остальные 
         инициализационные параметры ведут себя аналогично.
         */
        param.put("partnerURL", config.getInitParameter("PARTNER_URL"));
        param.put("dbURL", config.getInitParameter("DB_URL"));
        param.put("dbLogin", config.getInitParameter("DB_LOGIN"));
        param.put("dbPassword", config.getInitParameter("DB_PASSWORD"));
        param.put("phoneListsTable", config.getInitParameter("PHONE_LISTS"));
        param.put("phonesTable", config.getInitParameter("PHONES"));
        
        
        ServletContext context = config.getServletContext();
        for (Map.Entry<String, String> p : param.entrySet()) {
            //проверяем все ли инициализационные параметры заданы.
            if (p.getValue() == null) {
               /* Здесь и в остальных классах я не использую логер, чтобы не 
                       перегружать проект зависимостями и кодом. Я  умею 
                       пользоваться log4j, но вместо log.error(e) или log.debug(e)
                       я буду писать System.err.println(e).  */
                System.err.println(String.format("Ошибка в дескрипторе развертывания. Не задан: %s", p.getKey()));
                throw new ServletException("Ошибка в дескрипторе развертывания.");
            } else {
                //Если все в порядке, добавляем атрибуты в контекст.
                context.setAttribute(p.getKey(), p.getValue());
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(405);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = request.getServletContext();
        request.setCharacterEncoding("UTF-8");
        //Список телефонов
        Set<Long> phoneSet = new LinkedHashSet<>();
        //Ответ партнера
        XMLAnswer answer = new XMLAnswer();
        try {
            //Извлечение номеров телефонов из запроса.
            for (String phoneNumber : request.getParameter("phoneNumbers").split("\r\n")) {
                phoneSet.add(Long.parseLong(phoneNumber));
            }
            // Создание отправляемой сущности
            Phones phones = new Phones(request.getRemoteAddr(), phoneSet);
            /*В т.з. не указано какой интерфейc у принимающего веб-сервиса, поэтому XML
                отправляется в виде обычного потока через HttpURLConnection. Без параметров*/
            URL url = new URL((String) context.getAttribute("partnerURL"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml");
            //Отправляем список партнеру
            answer = XMLSender.sendXML(phones, con);
            //Присваиваем ответ
            phones.setAnswer(answer);
            //Сохраняем в DB
            new PhonesJDBCDAO(
                    (String) context.getAttribute("dbURL"),
                    (String) context.getAttribute("dbLogin"),
                    (String) context.getAttribute("dbPassword"),
                    (String) context.getAttribute("phoneListsTable"),
                    (String) context.getAttribute("phonesTable")
            ).savePhones(phones);
            //регистрируем ответ для отображения
            request.setAttribute("XMLAnswer", answer);

        } catch (NumberFormatException e) {
            /*Если проскочил некорректный номер(например, при отправке  через консоль),
             отвечаем ошибкой
            */
            response.getWriter().println("Ошибка при вводе телефонов.");    
            return;
        }
        //пробрасываем на отображение
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
