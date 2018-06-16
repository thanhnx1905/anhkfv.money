package anhkfv.person;

import java.util.Comparator;

import anhkfv.infomation.detail.InfomationDetail;

public class SortInfomation implements Comparator<InfomationDetail> {
    @Override
    public int compare(InfomationDetail o1, InfomationDetail o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}
