package tk.pankratovm.zagruzka.model;

import java.io.IOException;
import java.net.*;
import javax.xml.bind.*;

public class XMLSender {
    

    public static XMLAnswer sendXML(Phones s, HttpURLConnection con) {
        XMLAnswer answer = new XMLAnswer();
        try {
            JAXBContext reqContext = JAXBContext.newInstance(s.getClass());
            Marshaller m = reqContext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(s, con.getOutputStream());
            con.getOutputStream().flush();

            JAXBContext respContext = JAXBContext.newInstance(XMLAnswer.class);
            Unmarshaller unm = respContext.createUnmarshaller(); 
            if (con.getResponseCode() >= 200 && con.getResponseCode() < 300 && con.getInputStream().available() > 0) {
                try {
                    answer = (XMLAnswer) unm.unmarshal(con.getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            answer.setHttpCode(con.getResponseCode());
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }
        return answer;
    }

}
