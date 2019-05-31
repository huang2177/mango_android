package com.paizhong.weipan;

import java.util.ArrayList;
import java.util.List;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleInstrumentedTest {
    public void useAppContext() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        List<Integer> list1 = new ArrayList<>();
        list1.add(5);

        list.addAll(list1);
        for (int i = 0; i < list.size(); i++) {

        }
    }
}
