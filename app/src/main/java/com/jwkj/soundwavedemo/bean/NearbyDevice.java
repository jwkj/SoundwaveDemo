package com.jwkj.soundwavedemo.bean;

import com.jwkj.soundwavedemo.utils.ByteOptionUtils;

/**
 * 附近的设备
 * Created by dali on 2017/5/16.
 */

public class NearbyDevice {
    private int cmd;//接收的命令id
    private int errCode;
    private int msgVersion;//消息版本
    private int currVersion;//当前版本号
    private int deviceId;//设备id
    private int deviceType;//设备类型
    private int deviceSubType;//设备子类型
    private int pwdFlag;//有无密码标记，1有密码，0无密码
    private String ip;//IP地址

    public static int bytesToInt(byte[] src, int offset) {
        int value;
        value = (src[offset] & 0xFF) | ((src[offset + 1] & 0xFF) << 8)
                | ((src[offset + 2] & 0xFF) << 16) | ((src[offset + 3] & 0xFF) << 24);
        return value;
    }

    public static NearbyDevice getDeviceInfoByByteArray(byte[] data) {
        NearbyDevice device = new NearbyDevice();
        int msgVersion =ByteOptionUtils.getInt(data,12);
        int curVersion = (msgVersion >> 4) & 0x1;
        device.setDeviceType(ByteOptionUtils.getInt(data,20));
        device.setDeviceSubType(ByteOptionUtils.getInt(data,80));
        device.setDeviceId(ByteOptionUtils.getInt(data,16));
        device.setCmd(ByteOptionUtils.getInt(data,0));
        device.setPwdFlag(ByteOptionUtils.getInt(data,24));
        device.setCurrVersion(curVersion);
        device.setMsgVersion(msgVersion);
        return device;
    }

    public int getMsgVersion() {
        return msgVersion;
    }

    public void setMsgVersion(int msgVersion) {
        this.msgVersion = msgVersion;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getCurrVersion() {
        return currVersion;
    }

    public void setCurrVersion(int currVersion) {
        this.currVersion = currVersion;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getDeviceSubType() {
        return deviceSubType;
    }

    public void setDeviceSubType(int deviceSubType) {
        this.deviceSubType = deviceSubType;
    }

    public int getPwdFlag() {
        return pwdFlag;
    }

    public void setPwdFlag(int pwdFlag) {
        this.pwdFlag = pwdFlag;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "NearbyDevice{" +
                "cmd=" + cmd +
                ", currVersion=" + currVersion +
                ", deviceId=" + deviceId +
                ", deviceType=" + deviceType +
                ", deviceSubType=" + deviceSubType +
                ", pwdFlag=" + pwdFlag +
                ", ip='" + ip + '\'' +
                '}';
    }
}
