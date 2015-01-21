package tk.pankratov.zagruzka.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.*;
import tk.pankratov.model.Phones;
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
        try {
            for (String phoneNumber : request.getParameter("phoneNumbers").split("\r\n")) {
                phoneSet.add(Long.parseLong(phoneNumber));
            }
            Phones phones = new Phones(request.getRemoteAddr(), phoneSet);

            try {
                JAXBContext context = JAXBContext.newInstance(Phones.class);
                Marshaller m = context.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                m.marshal(phones, System.out);
                URL url = new URL(conf.getInitParameter("PARTNER_URL"));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("GET"); //В т.з. не уточнен метод. Применяю GET
                con.setRequestProperty("Content-Type", "text/xml");
                m.marshal(phones, con.getOutputStream());
                con.getOutputStream().flush();
                System.out.println(con.getResponseCode());
                con.disconnect();
            } catch (JAXBException e) {
                e.printStackTrace();
            }catch (ConnectException e){
                
            }

        } catch (NumberFormatException e) {
            response.getWriter().println("Ошибка при вводе телефонов");
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}