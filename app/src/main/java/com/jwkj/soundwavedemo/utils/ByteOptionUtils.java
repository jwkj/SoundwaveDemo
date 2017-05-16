package com.jwkj.soundwavedemo.utils;

/**
 * byte数组操作工具
 * Created by dali on 2017/5/16.
 */

public class ByteOptionUtils {

    private static int makeInt(byte b3, byte b2, byte b1, byte b0) {
        return (((b3) << 24) | ((b2 & 0xff) << 16) | ((b1 & 0xff) << 8) | ((b0 & 0xff)));
    }

    /**
     * 通过下表获取int值
     *
     * @param buff
     * @param startIndex
     * @return
     */
    public static int getInt(byte[] buff, int startIndex) {
        return makeInt(buff[startIndex + 3], buff[startIndex + 2], buff[startIndex + 1], buff[startIndex]);
    }
}
