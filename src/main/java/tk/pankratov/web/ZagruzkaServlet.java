package tk.pankratov.web;

import java.io.IOException;
import java.net.*;
import java.util.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import tk.pankratov.model.*;

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
         Однако не уточняется должно ли это происходить на этапе развертывания или 
         выполнения сервлета. Поэтому, принято решение задавать адрес в дескрипторе с последующей 
         переноской его в контекст приложения в качестве атрибута. В этом случае
         он, при необходимости может быть изменен из другого сервлета. Остальные 
         инициализационные параметры ведут себя похожим образом.
         */
        param.put("partnerURL", config.getInitParameter("PARTNER_URL"));
        param.put("dbURL", config.getInitParameter("DB_URL"));
        System.out.println(config.getInitParameter("DB_URL"));
        param.put("dbLogin", config.getInitParameter("DB_LOGIN"));
        param.put("dbPassword", config.getInitParameter("DB_PASSWORD"));
        param.put("phoneListsTable", config.getInitParameter("PHONE_LISTS"));
        param.put("phonesTable", config.getInitParameter("PHONES"));

        ServletContext context = config.getServletContext();
        for (Map.Entry<String, String> p : param.entrySet()) {
            if (p.getValue() == null) {
                System.out.println(String.format("Ошибка в дескрипторе развертывания. Не задан: %s", p.getKey()));
                throw new ServletException("Ошибка в дескрипторе развертывания.");
            } else {
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
        Set<Long> phoneSet = new LinkedHashSet<>();
        XMLAnswer answer = new XMLAnswer();
        try {
            //Извлечение номеров телефонов.
            for (String phoneNumber : request.getParameter("phoneNumbers").split("\r\n")) {
                phoneSet.add(Long.parseLong(phoneNumber));
            }
            // Создание отправляемой сущности
            Phones phones = new Phones(request.getRemoteAddr(), phoneSet);

            URL url = new URL((String)context.getAttribute("partnerURL"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml");
            answer = XMLSender.sendXML(phones, con);
            System.out.println(answer);
            phones.setAnswer(answer);
            new PhonesJDBCDAO((String)context.getAttribute("dbURL"), (String)context.getAttribute("dbLogin"),(String)context.getAttribute("dbPassword")).savePhones(
                    phones, (String)context.getAttribute("phoneListsTable"), (String)context.getAttribute("phonesTable"));
            request.setAttribute("XMLAnswer", answer);

        } catch (NumberFormatException e) {
            response.getWriter().println("Ошибка при вводе телефонов");
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
