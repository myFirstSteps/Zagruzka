package tk.pankratovm.zagruzka.model;

import java.util.*;
import javax.xml.bind.annotation.*;
/*
 Сущность пользовательской посылки. Содержит список отправляемых номеров, 
 ip пользователя, время отправки. После успешной отправки добавляется ответ партнера.
 */

@XmlRootElement(name = "ROOT")
public class Phones {

    private long time = System.currentTimeMillis();
    ;
    private String ip = "";
    /*В задании не указано допускается ли дублирование номеров в списке.
     Исходя из здравого смысла, принимаю решение,что дублирование не допускается. */
    private Set<Long> phoneNumbers = new LinkedHashSet<>();
    /*В задании сказано, что номера в формате int(11). Однако, если под "11" понимать
     кол-во бит, максимальное значение такого числа 1023. Если  "11" понимать, как
     кол-во значащих цифр, то номер должен хранится в long.
     */
    private XMLAnswer answer = new XMLAnswer();

    public Phones() {
    }

    public Phones(String SenderIp, Set<Long> SetOfPhones) {
        phoneNumbers = SetOfPhones;
        ip = SenderIp;
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
