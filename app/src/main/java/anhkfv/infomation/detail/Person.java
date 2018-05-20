package anhkfv.infomation.detail;

import java.io.Serializable;

public class Person implements Serializable{
    private String personName;
    private String personId;
    private boolean check;

    public Person(String personName, String personId) {
        this.personName = personName;
        this.personId = personId;
        this.check = false;
    }

    public Person(String personName, String personId, boolean check) {
        this.personName = personName;
        this.personId = personId;
        this.check = check;
    }

    public Person() {
    }

    public String getPersonName() {

        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
