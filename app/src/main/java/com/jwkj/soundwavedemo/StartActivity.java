package com.jwkj.soundwavedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hdl.elog.ELog;
import com.jwkj.soundwavedemo.bean.EMTMFInit;
import com.larksmart.emtmf.jni.EMTMFOptions;
import com.lsemtmf.genersdk.tools.commen.AlertUtils;
import com.lsemtmf.genersdk.tools.emtmf.EMTMFSDK;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        int errcode = EMTMFSDK.getInstance(this).initSDK(this, EMTMFInit.manufacturer,
                EMTMFInit.client, EMTMFInit.productModel,
                EMTMFInit.license);
        if (errcode == EMTMFOptions.INITSDK_ERRCOE_WIFIDISABLE) {

            ELog.hdl("wifi不可用");
        } else if (errcode == EMTMFOptions.INITSDK_INVAILDDATA) {
            AlertUtils.SimpleAlert(this, "SDK初始化的参数非法",
                    "请检查SDK初始化时传入的参数是否正确~");
        } else {
            ELog.hdl("正常。。。。。。。。");
        }
        ELog.hdl("errcode=" + errcode);

    }

    public void onNext(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMTMFSDK.getInstance(this).exitEMTFSDK(this);
    }
}
