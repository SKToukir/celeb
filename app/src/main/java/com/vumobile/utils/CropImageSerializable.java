package com.vumobile.utils;

import java.io.Serializable;

/**
 * Created by IT-10 on 5/31/2017.
 */

public class CropImageSerializable implements Serializable {

    private static byte[] byteArray;

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

}
