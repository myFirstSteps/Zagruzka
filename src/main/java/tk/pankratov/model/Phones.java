/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tk.pankratov.model;
import java.util.*;
import javax.xml.bind.annotation.*;
/**
 *
 * @author pankratov
 */
@XmlRootElement(name = "ROOT")
public class Phones {
    private long time;
    private String ip;
    /*В задании не указано допускается ли дублирование номеров.
    Исходя из здравого смысла принимаю решение,что дублирование не допускается. */
    private Set<Long> phoneNumbers=new LinkedHashSet<>(); 
    private String status;
    private String description;
    public Phones(){}
    public Phones(String SenderIp,Set<Long>SetOfPhones){
        time=System.currentTimeMillis();
        phoneNumbers=SetOfPhones;
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
    @XmlElement(name="PHONE")
    public Set<Long> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(Set<Long> list) {
        this.phoneNumbers = list;
    }
     @XmlTransient
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @XmlTransient
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
