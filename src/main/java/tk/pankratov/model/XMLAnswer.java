package tk.pankratov.model;
import javax.xml.bind.*;
import javax.xml.bind.annotation.*;
/**
 *
 * @author pankratov
 */
@XmlRootElement(name = "ROOT")
public class XMLAnswer {

   private String status = "500";
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
    @Override
    public String toString(){
        return String.format("STATUS=%s\nDESCRIPTION=%s", status,description);
    }
}
