package com.faucet.faucetexample;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        Double count = 20000d;
//        Double temp = 20000d;
//        Double count2 = 0d;
//        for (int i = 0; i < 30; i++) {
//            if (i != 0) {
//                temp *= 1.0683;
//                count += temp;
//            }
//            count2 += temp;
//            count *= 1.2541;
//            System.out.println("第" + (i+1) + "年追投" + (i == 0?(count/1.2541):temp.longValue()) + "元,总投资"+count2.longValue()+"元, 账户总额" + count.longValue() + "元");
//        }
//        for (int i = 0; i < 30; i++) {
//            count *= 1.0683;
//            System.out.println("第" + (i+1) + "年同等购买力金额为" + count.longValue() + "元");
//        }
        Double benjin = 8000d;
        for (int i = 0; i < 30; i++) {
            Double gangang = benjin*3;
            Double zhuan = gangang * 0.1;
            benjin += zhuan;
            System.out.println(i+"--" +benjin);
        }
        System.out.println("=======");
        Double benjin1 = 186d;
        for (int i = 0; i < 30; i++) {
            benjin1 *= 1.1;
            System.out.println(i+"--" +benjin1);
        }
    }
}