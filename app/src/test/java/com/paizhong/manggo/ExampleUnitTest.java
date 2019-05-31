package com.paizhong.weipan;

import android.util.SparseArray;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest<T> {
    @Test
    public void addition_isCorrect() {
//        SparseArray<Boolean> array = new SparseArray<>();
//        System.out.println(array.get(0));
//
//
//        /**
//         * 上界；
//         *  1、表示superMaps里面是Map的所有子类，但具体是哪一种，并不知道；
//         *  2、<? extends Map> 不是一个集合，而是T的某一种子类的意思，只有一种；
//         */
//        List<? extends Map> superMaps = new ArrayList<>();
//
//        //可以从里面获取数据的；
//        Map o = superMaps.get(0);
//        //错误，因为编译器压根不知道superMaps存的是什么类型；
//        superMaps.add(new HashMap());
//
//
//
//        /**
//         * 下界；
//         *  1、表示lowerMaps存放String的所有父类，但具体是哪一种，并不知道；
//         *  2、<? super AB> 表示可以存放T的所有父类；
//         */
//        List<? super Apple> list = new ArrayList<>();
//
//        // 可以添加数据，那么当我add的时候，我不能add Apple的父类，
//        // 因为不能确定List里面存放的到底是哪个父类。但是我可以add Apple及其子类
//        list.add(new Apple());
//        list.add(new RedApple());
//
//        //报错，Apple的父类这么多，用什么接着呢，除了Object，其他的都接不住。
//        Apple a = list.get(0);

    }

    class Fruit {
    }

    class Apple extends Fruit {
    }

    class RedApple extends Apple {
    }

}