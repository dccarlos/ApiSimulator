package com.dccarlos.learning.asim.client;

import org.junit.Test;

public class TestUtilities {

    @Test
    public void test() {
//         KISDummyProvider.getOperations().forEach(System.out::println);
         
         KISDummyProvider.getListOfMirrorMakers().forEach(System.out::println);
         
//        KISDummyProvider.getBrokerConfigOf("45").getProperties().forEach((k, v) -> {
//            System.out.println("Key: " + k + ", Value: " + v);
//        });
    }
}
