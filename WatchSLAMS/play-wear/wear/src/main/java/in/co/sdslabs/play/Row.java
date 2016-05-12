package in.co.sdslabs.play;

import android.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akshay on 12-03-2015.
 */
public class Row {

    final List<Fragment> columns = new ArrayList<Fragment>();

    public Row(Fragment... fragments) {
        for (Fragment f : fragments) {
            add(f);
        }
    }

    public void add(Fragment f) {
        columns.add(f);
    }

    Fragment getColumn(int i) {
        return columns.get(i);
    }

    public int getColumnCount() {
        return columns.size();
    }
}
