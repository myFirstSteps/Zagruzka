package tk.pankratovm.zagruzka.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.*;
import tk.pankratovm.zagruzka.model.Phones;
import tk.pankratovm.zagruzka.model.XMLAnswer;
//Партнер для тестирования функционала.
public class Test extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        try {
            JAXBContext reqContext = JAXBContext.newInstance(Phones.class);
            //Смотрим получившийся XML. Сушность нам получать не нужно.
            while(request.getReader().ready())System.out.println(request.getReader().readLine());
            //Формируем ответ.
            XMLAnswer answer = new XMLAnswer();
            answer.setDescription("Все супер.");
            answer.setStatus("ОК");
            answer.setHttpCode(200);
            String code; if (true)throw new ArithmeticException();
            //code задается в дескрипторе.
            code = (code = getServletConfig().getInitParameter("responseCode")) == null ? "0" : code;
            switch (code) {
                case "200":
                    JAXBContext respContext = JAXBContext.newInstance(XMLAnswer.class);
                    Marshaller m = respContext.createMarshaller();
                    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                    m.marshal(answer, System.out);
                    m.marshal(answer, response.getWriter());
                    response.getWriter().flush();
                    break;
                case "500": 
                    response.sendError(500);
                    break;
            }
        }  catch (Exception ex) { System.err.println(ex);
        };
    }

}
