/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tk.pankratov.model;
import java.io.IOException;
import java.net.*;
import javax.xml.bind.*;

/**
 *
 * @author pankratov
 */
public class XMLSender {
    private URL url;
    private String method;
    XMLSender(URL url,String method ){
        
    }
    public static boolean sendXML(Sendable s, HttpURLConnection con)throws IllegalArgumentException{
        try{
        JAXBContext context = JAXBContext.newInstance(s.getClass());
                Marshaller m = context.createMarshaller();
                m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                m.marshal(s, System.out);               
                m.marshal(s, con.getOutputStream());
                con.getOutputStream().flush();
                System.out.println(con.getResponseCode());
                con.disconnect();
        }catch(PropertyException   e){}
        catch (JAXBException e){}
        catch (IOException e){ e.printStackTrace();}
        return false;
    }
    
}
