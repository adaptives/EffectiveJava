package gc;

import java.util.LinkedList;
import java.util.List;

public final class TrashCan {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<Object> trash = new LinkedList<Object>();
        for (int i = 1; 1 <= 100000; i ++) {
            for (int j = 1; j <= 100000; j++ ) {
                trash.add(new byte[1000]);
            }

            trash.clear();
        }
    }

}
