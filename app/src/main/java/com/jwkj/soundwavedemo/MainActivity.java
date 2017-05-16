package com.jwkj.soundwavedemo;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hdl.elog.ELog;
import com.hdl.udpsenderlib.UDPReceiver;
import com.hdl.udpsenderlib.UDPResult;
import com.hdl.udpsenderlib.UDPResultCallback;
import com.jwkj.soundwavedemo.bean.NearbyDevice;
import com.lsemtmf.genersdk.tools.emtmf.EMTMFSDK;
import com.lsemtmf.genersdk.tools.emtmf.EMTMFSDKListener;

public class MainActivity extends AppCompatActivity {
    private static final int LOCAL_PORT = 9988;//本地接收端口
    private boolean isNeedSendWave = true;//是否需要发送声波，没有接到正确数据之前都需要发送哦
    private String wifiSSID;
    private String wifiPwd;
    private Context mContext;
    private TextView tvWifiName, tvLog;
    private EditText etWifiPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mContext = this;
        initView();
        EMTMFSDK.getInstance(this).setListener(emtmfsdkListener);//设置监听器
        getWifiName();
        receive();
    }

    private void receive() {
        UDPReceiver.getInstance().with(mContext)
                .setPort(LOCAL_PORT)
                .receive(new UDPResultCallback() {
                    @Override
                    public void onNext(UDPResult udpResult) {
                        ELog.hdl("udpresult---"+udpResult);
                        NearbyDevice device = NearbyDevice.getDeviceInfoByByteArray(udpResult.getResultData());
                        device.setIp(udpResult.getIp());
                        ELog.hdl("收到结果了------" + device);
                        tvLog.append("\n设备联网成功：（设备信息）" + device.toString());
                        UDPReceiver.getInstance().stopReceive();
                        EMTMFSDK.getInstance(mContext).stopSend();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ELog.hdl("出错了"+throwable);
                        //出错了就重发
                        EMTMFSDK.getInstance(mContext).sendWifiSet(mContext, wifiSSID, wifiPwd);//发送声波--传入wifi名字和wifi密码
                        receive();
                    }
                });
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
            ELog.hdl("SSID", wifiInfo.getSSID());
            tvWifiName.setText(wifiInfo.getSSID());
            wifiSSID = wifiInfo.getSSID();
            if ("\"".equals(wifiSSID.substring(0, 1)) && "\"".equals(wifiSSID.substring(wifiSSID.length() - 1, wifiSSID.length()))) {
                wifiSSID = wifiSSID.substring(1, wifiSSID.length() - 1);
            }
            ELog.hdl("wifiSSID=" + wifiSSID);
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
        EMTMFSDK.getInstance(this).sendWifiSet(this, wifiSSID, wifiPwd);//发送声波--传入wifi名字和wifi密码
    }

    public void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 停止发送声波
     *
     * @param view
     */
    public void onStop(View view) {
        EMTMFSDK.getInstance(mContext).stopSend();//停止发送声波
    }

    EMTMFSDKListener emtmfsdkListener = new EMTMFSDKListener() {
        @Override
        public void didDeviceOffline(String ip) {
            ELog.hdl("ip=" + ip);

        }

        @Override
        public void didWIFIChanged(String oldssid, String newssid) {
            ELog.hdl("");
        }

        @Override
        public void didDeviceReConnected(String ip) {
            ELog.hdl("");
        }

        @Override
        public void didNoDeviceInfoRecived() {
            ELog.hdl("");
        }

        @Override
        public void didWifiDisabled() {
            ELog.hdl("");
        }

        @Override
        public void didSDKErrcode(int errCode, String errMsg) {
            ELog.hdl("");
        }

        public void didSetWifiResult(int errcode, String content) {
            ELog.hdl("");
            if (errcode == EMTMFSDK.SET_SUCCESS) {
                ELog.hdl("设置成功了");
                tvLog.append("\n设置成功了");
                isNeedSendWave = false;
            }
        }

        public void didEndOfPlay() {
            if (isNeedSendWave) {
                ELog.hdl("继续发送声波");
                tvLog.append("\n继续发送声波");
                EMTMFSDK.getInstance(mContext).sendWifiSet(mContext, wifiSSID, wifiPwd);
            } else {
                ELog.hdl("已经停止发送了");
                tvLog.append("\n已经停止发送了");
            }
        }
    };
}
