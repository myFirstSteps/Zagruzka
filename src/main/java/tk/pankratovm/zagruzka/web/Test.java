package tk.pankratovm.zagruzka.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.xml.bind.*;
import java.util.*;
import tk.pankratovm.zagruzka.model.Phones;
import tk.pankratovm.zagruzka.model.XMLAnswer;
//Партнер для тестирования функционала.

public class Test extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        for (Map.Entry<String, String[]> p : request.getParameterMap().entrySet()) {
            getServletConfig().getServletContext().setAttribute(p.getKey(), p.getValue()[0]);
        }
        request.setAttribute("Changed", "Настройки изменены.");
        request.getRequestDispatcher("Test.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            JAXBContext reqContext = JAXBContext.newInstance(Phones.class);
            //Смотрим получившийся XML. Сушность нам получать не нужно.
            while (request.getReader().ready()) {
                System.out.println(request.getReader().readLine());
            }
            //Формируем ответ.
            XMLAnswer answer = new XMLAnswer();
            answer.setDescription("Все супер.");
            answer.setStatus("ОК");
            answer.setHttpCode(200);
            String scenario;
            //Сценарий тестирования.
            scenario = (scenario = (String) getServletConfig().getServletContext().getAttribute("scenario")) == null ? "ok" : scenario;
            switch (scenario) {
                case "error":
                    response.sendError(500);
                    break;
                case "ok":
                default:
                    JAXBContext respContext = JAXBContext.newInstance(XMLAnswer.class);
                    Marshaller m = respContext.createMarshaller();
                    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                    m.marshal(answer, response.getWriter());
            }
        } catch (Exception ex) {
            System.err.println(ex);
        };
    }

}
