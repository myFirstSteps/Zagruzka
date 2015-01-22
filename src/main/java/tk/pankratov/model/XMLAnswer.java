package tk.pankratov.model;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
/**
 *
 * @author pankratov
 */
@XmlRootElement(name = "ROOT")
public class XMLAnswer {
   private String httpCode="500";
   private String status ="";
   private String description = "";
   public XMLAnswer() {
   }

    public void setStatus(String status) {
        this.status = status;
    }
    @XmlElement
    public String getStatus() {
        return status;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @XmlElement
    public String getDescription() {
        return description;
    }
     public void setHttpCode(String httpCode) {
        this.httpCode = httpCode;
    }
    @XmlElement
    public String getHttpCode() {
        return httpCode;
    }
    @Override
    public String toString(){
        return String.format("STATUS=%s\nDESCRIPTION=%s", status,description);
    }
}
