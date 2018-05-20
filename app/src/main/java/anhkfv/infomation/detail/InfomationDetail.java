package anhkfv.infomation.detail;

import java.util.Date;

public class InfomationDetail {
    private Date date;
    private String personName;
    private String idMoney;
    private Float money;
    private String approval;
    private String info;

    public InfomationDetail(Date date, String personName,  String idMoney, Float money, String approval, String info){
        this.date = date;
        this.personName = personName;
        this.idMoney = idMoney;
        this.money = money;
        this.approval = approval;
        this.info = info;
    }

    public InfomationDetail(){
    }

    public static InfomationDetail createInfoDetail(Date date, String personName,  String idMoney, Float money, String approval, String info){
        return new InfomationDetail(date, personName, idMoney, money, approval, info);
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
        this.idMoney = idMoney;
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
}
