package aop.prototypes.callBySome;


import org.checkerframework.checker.units.qual.A;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CallBySomeApplication {


    public static void main(String[] args) {
        SpringApplication.run(CallBySomeApplication.class, args);

        int ai = 1;
        mutableTestInt(ai);
        System.out.println("after int value :"+ai);

        String a = "test1";
        mutableTest(a);
        System.out.println("after plus string : "+a);

        List<String> testArrayString = new ArrayList<>();
        testArrayString.add("a");
        testArrayString.add("b");
        testArrayString.add("c");

        listArrayString(testArrayString);

        for (String ta : testArrayString) {
            System.out.println("testArrayString : "+ta);
        }

        long[] testArray = new long[]{1L, 2L, 3L};

        listArrayTest(testArray);

        for (long ta : testArray) {
            System.out.println("testArray : "+ta);
        }
    }

    public static void stringArray(String[] a) {
        for (String aa : a) {
            String b = aa + " method";
            System.out.println("a string test :"+b);
        }
    }

    public static void intArrayTest(int[] a) {
        for ( int i =0; i<a.length; i++) {
            a[i] *= 10;
        }
    }

    public static void mutableTest(String aa) {
        aa = aa + " method";
        System.out.println("string : "+aa);
    }

    public static void mutableTestInt(int a) {
        a += 1;
        System.out.println("int : "+a);
    }

    public static void listArrayTest(long[] array) {
        for (int i =0; i<array.length; i++) {
            array[i] += 10;
            System.out.println(" arr : "+array[i]);
        }
    }

    public static void listArrayString(List<String> st) {
        for ( int i = 0; i< st.size(); i++) {

            st.set(i,st.get(i) + "ddd");
            System.out.println("st : "+st.get(i));
        }
    }
}
