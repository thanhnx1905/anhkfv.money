package anhkfv.infomation.detail;

import java.io.Serializable;
import java.util.Date;

public class InfomationDetail implements Serializable{
    private Date date;
    private String personName;
    private String idMoney;
    private Float money;
    private String approval;
    private String info;
    private String keyRandom;

    public InfomationDetail(Date date, String personName,  String idMoney, Float money, String approval, String info, String keyRandom){
        this.date = date;
        this.personName = personName;
        this.idMoney = idMoney;
        this.money = money;
        this.approval = approval;
        this.info = info;
        this.keyRandom = keyRandom;
    }

    public InfomationDetail(){
    }

    public static InfomationDetail createInfoDetail(Date date, String personName,  String idMoney, Float money, String approval, String info, String keyRandom){
        return new InfomationDetail(date, personName, idMoney, money, approval, info, keyRandom);
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getIdMoney() {
        return idMoney;
    }

    public void setIdMoney(String personId) {
        this.idMoney = personId;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public String getApproval() {
        return approval;
    }

    public void setApproval(String approval) {
        this.approval = approval;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getKeyRandom() {
        return keyRandom;
    }

    public void setKeyRandom(String keyRandom) {
        this.keyRandom = keyRandom;
    }
}
