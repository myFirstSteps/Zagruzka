package tk.pankratov.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.*;
import tk.pankratov.model.*;
import java.net.*;

/**
 *
 * @author pankratov
 */
/*Поскольку в задании не указана версия Tomcat, используется сервлет API ниже 3.0 (без аннотаций),
 для совместимости с Tomcat младше 7 версии. Необходимые параметры инициализации в web.xml.
 */
public class ZagruzkaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendError(405);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletConfig conf = getServletConfig();
        request.setCharacterEncoding("UTF-8");
        Set<Long> phoneSet = new LinkedHashSet<>();
        XMLAnswer answer = new XMLAnswer();
        try {
            for (String phoneNumber : request.getParameter("phoneNumbers").split("\r\n")) {
                phoneSet.add(Long.parseLong(phoneNumber));
            }
            Phones phones = new Phones(request.getRemoteAddr(), phoneSet);

            URL url = new URL(conf.getInitParameter("PARTNER_URL"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET"); //В т.з. не уточнен метод. Применяю GET
            con.setRequestProperty("Content-Type", "text/xml");
            answer = XMLSender.sendXML(phones, con);
            System.out.println(answer);
            phones.setStatus(answer.getStatus());
            phones.setDescription(answer.getDescription());
            new PhonesJDBCDAO(conf.getInitParameter("JDBC_URL"), conf.getInitParameter("DB_LOGIN"), conf.getInitParameter("DB_PASSWORD")).savePhones(
                    phones, conf.getInitParameter("PHONE_LISTS"), conf.getInitParameter("PHONES"));
            request.setAttribute("XMLAnswer", answer );

        } catch (NumberFormatException e) {
            response.getWriter().println("Ошибка при вводе телефонов");
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
//20:00 9ч
