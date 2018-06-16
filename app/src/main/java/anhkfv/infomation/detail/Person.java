package anhkfv.infomation.detail;

import java.io.Serializable;

public class
Person implements Serializable{
    private String personName;
    private String personId;
    private String group;
    private boolean checkAll;
    private boolean checkOne;

    public Person(String personName, String personId, String group) {
        this.personName = personName;
        this.personId = personId;
        this.checkAll = false;
        this.checkOne = false;
        this.group = group;
    }

    public Person(String personName, String personId, boolean checkAll, boolean checkOne) {
        this.personName = personName;
        this.personId = personId;
        this.checkAll = checkAll;
        this.checkOne = checkOne;
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

    public boolean isCheckAll() {
        return checkAll;
    }

    public void setCheckAll(boolean checkALl) {
        this.checkAll = checkALl;
    }

    public boolean isCheckOne() {
        return checkOne;
    }

    public void setCheckOne(boolean checkOne) {
        this.checkOne = checkOne;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
