package tk.pankratov.web;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import tk.pankratov.model.XMLAnswer;

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
            XMLAnswer answer = new XMLAnswer();
            answer.setDescription("Все ОК");
            answer.setStatus("ОК");
            answer.setHttpCode("200");
            String code;
            code = (code = getServletConfig().getInitParameter("responseCode")) == null ? "0" : code;
            switch (code) {
                case "200":
                    JAXBContext reqContext = JAXBContext.newInstance(XMLAnswer.class);
                    Marshaller m = reqContext.createMarshaller();
                    m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                    m.marshal(answer, System.out);
                    m.marshal(answer, response.getWriter());
                    response.getWriter().flush();
            }
        } /* response.sendError(408);*/ catch (Exception ex) {
        };
    }

}
