package tk.pankratovm.zagruzka.model;

import java.io.IOException;
import java.net.*;
import javax.xml.bind.*;

public class XMLSender {

    public static XMLAnswer sendXML(Phones s, URL url) throws IOException {
        XMLAnswer answer = new XMLAnswer();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "text/xml");
            JAXBContext reqContext = JAXBContext.newInstance(s.getClass());
            Marshaller m = reqContext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(s, con.getOutputStream());
            con.getOutputStream().flush();

            JAXBContext respContext = JAXBContext.newInstance(XMLAnswer.class);
            Unmarshaller unm = respContext.createUnmarshaller();
            //Пробуем получить ответ.
            try {
                answer = (XMLAnswer) unm.unmarshal(con.getInputStream());
            } catch (Exception e) {
                System.err.println(e);
            }

            answer.setHttpCode(con.getResponseCode());
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            con.disconnect();
        }
        return answer;
    }

}
