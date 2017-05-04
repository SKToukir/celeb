package com.vumobile.utils;

/**
 * Created by IT-10 on 5/4/2017.
 */

public class MyEnums {

    public enum notificationTypeFlag {
        // if data send from live notification 1 value will save
        // if data send from post 2 value will save to server
        LIVE(1), POST(2);
        private final int id;

        notificationTypeFlag(int id) {
            this.id = id;
        }

        public int getValue() {
            return id;
        }
    }



}
