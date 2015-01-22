package tk.pankratov.model;

import java.io.IOException;
import java.net.*;
import javax.xml.bind.*;

public class XMLSender {

    static final int READ_TIMEOUT = 3000;
    /*В т.з. не указано каков интерфей у принимающего веб-сервиса, поэтому XML
    отправляется в иде обычного потока через HttpURLConnection*/
    public static XMLAnswer sendXML(Sendable s, HttpURLConnection con) {
        XMLAnswer answer=new XMLAnswer();
        try {
            JAXBContext reqContext = JAXBContext.newInstance(s.getClass());
            Marshaller m = reqContext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(s, System.out);
            m.marshal(s, con.getOutputStream());
            con.getOutputStream().flush();
            
            JAXBContext respContext = JAXBContext.newInstance(XMLAnswer.class);
            Unmarshaller unm=respContext.createUnmarshaller();
            con.setReadTimeout(READ_TIMEOUT);
            answer.setStatus(String.valueOf(con.getResponseCode()));
            answer=(XMLAnswer)unm.unmarshal(con.getInputStream());
            con.disconnect();
        } catch (PropertyException e) {
        } catch (JAXBException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            con.disconnect();
        }
        return  answer;
    }

}
