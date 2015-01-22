package tk.pankratov.model;
import java.util.*;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "ROOT")
public class Phones implements Sendable {

    private long time;
    private String ip = "";
    /*В задании не указано допускается ли дублирование номеров в списке.
     Исходя из здравого смысла принимаю решение,что дублирование не допускается. */
    private Set<Long> phoneNumbers = new LinkedHashSet<>();
    private XMLAnswer answer = new XMLAnswer();

    public Phones() {
    }

    public Phones(String SenderIp, Set<Long> SetOfPhones) {
        time = System.currentTimeMillis();
        phoneNumbers = SetOfPhones;
        ip=SenderIp;
    }

    @XmlTransient
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @XmlTransient
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @XmlElement(name = "PHONE")
    public Set<Long> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<Long> list) {
        this.phoneNumbers = list;
    }

    @XmlTransient
    public XMLAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(XMLAnswer answer) {
        this.answer = answer;
    }
}
