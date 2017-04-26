package com.gonna;

/**
 * Created by Maxim on 4/19/2017.
 */

public class Singleton {

    private Singleton() {
        // singleton
    }

    public static Singleton getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static final class InstanceHolder {
        private static final Singleton INSTANCE = new Singleton();
    }
}