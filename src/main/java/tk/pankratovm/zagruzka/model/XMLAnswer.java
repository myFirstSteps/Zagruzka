/*
Ответ партнера преобразуется к данной сущности.
*/
package tk.pankratovm.zagruzka.model;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "ROOT")
public class XMLAnswer {
   //При создании присваивается код 500
   private int httpCode=500;
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
     public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }
    @XmlTransient
    public int getHttpCode() {
        return httpCode;
    }
    @Override
    public String toString(){
        return String.format("STATUS=%s\nDESCRIPTION=%s\nHttpCode=%d", status,description,httpCode);
    }
}
