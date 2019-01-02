package modelo;

import java.util.Comparator;

public class MyDateComp implements Comparator<HistoryValue> {

    @Override
    public int compare(HistoryValue e1, HistoryValue e2) {
        if (e1.getDate().compareTo(e2.getDate()) == 0) {
            return 0;
        }
        if (e1.getDate().compareTo(e2.getDate()) > 0) {
            return 1;
        }
        return -1;

    }
}
