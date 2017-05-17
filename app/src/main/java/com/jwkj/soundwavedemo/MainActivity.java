package com.jwkj.soundwavedemo;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hdl.elog.ELog;
import com.hdl.udpsenderlib.UDPResult;
import com.jwkj.soundwave.ResultCallback;
import com.jwkj.soundwave.SoundWaveSender;
import com.jwkj.soundwave.bean.NearbyDevice;

public class MainActivity extends AppCompatActivity {
    private boolean isNeedSendWave = true;//是否需要发送声波，没有接到正确数据之前都需要发送哦
    private String wifiSSID;//wifi名字
    private String wifiPwd;//wifi密码
    private TextView tvWifiName, tvLog;
    private EditText etWifiPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        getWifiName();
    }

    /**
     * 初始化view
     */
    private void initView() {
        tvWifiName = (TextView) findViewById(R.id.tv_wifiname);
        tvLog = (TextView) findViewById(R.id.tv_log);
        etWifiPwd = (EditText) findViewById(R.id.et_wifi_pwd);
    }

    /**
     * 获取wifi名字
     *
     * @return
     */
    private boolean getWifiName() {
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            ELog.e("SSID", wifiInfo.getSSID());
            tvWifiName.setText(wifiInfo.getSSID());
            wifiSSID = wifiInfo.getSSID();
            //去掉首尾"号
            if ("\"".equals(wifiSSID.substring(0, 1)) && "\"".equals(wifiSSID.substring(wifiSSID.length() - 1, wifiSSID.length()))) {
                wifiSSID = wifiSSID.substring(1, wifiSSID.length() - 1);
            }
            ELog.e("wifiSSID=" + wifiSSID);
            return true;
        } else {
            showMsg("请连接wifi");
            return false;
        }
    }

    /**
     * 开始发送声波
     *
     * @param view
     */
    public void onSend(View view) {
        wifiPwd = etWifiPwd.getText().toString().trim();//记录密码
        tvLog.append("\n声波发送中....");
        sendSoundWave();
    }

    /**
     * 开始发送声波
     */
    private void sendSoundWave() {
        SoundWaveSender.getInstance().with(this).setWifiSet(wifiSSID, wifiPwd).send(new ResultCallback() {

            @Override
            public void onNext(UDPResult udpResult) {
//                ELog.e(udpResult.toString());
                NearbyDevice device = NearbyDevice.getDeviceInfoByByteArray(udpResult.getResultData());
                device.setIp(udpResult.getIp());
                ELog.e(device.toString());
                tvLog.append("\n设备联网成功：（设备信息）" + device.toString());
                isNeedSendWave = false;
            }

            @Override
            public void onError(Throwable throwable) {
                super.onError(throwable);
                ELog.e(""+throwable);
                SoundWaveSender.getInstance().stopSend();//出错了就要停止任务，然后重启发送
                sendSoundWave();
            }

            /**
             * 当声波停止的时候
             */
            @Override
            public void onStopSend() {
                if (isNeedSendWave) {//是否需要继续发送声波
                    tvLog.append("\n继续发送声波...");
                    sendSoundWave();
                } else {//结束了就需要将发送器关闭
                    SoundWaveSender.getInstance().stopSend();
                }
            }
        });
    }

    /**
     * 显示提示消息
     *
     * @param msg
     */
    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 停止发送声波
     *
     * @param view
     */
    public void onStop(View view) {
        SoundWaveSender.getInstance().stopSend();
    }

}
