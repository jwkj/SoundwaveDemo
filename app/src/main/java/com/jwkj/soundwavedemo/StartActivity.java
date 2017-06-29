package com.jwkj.soundwavedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hdl.elog.ELog;
import com.jwkj.soundwave.SoundWaveManager;
import com.jwkj.soundwavedemo.runtimepermissions.PermissionsManager;
import com.jwkj.soundwavedemo.runtimepermissions.PermissionsResultAction;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        boolean isSuccess = SoundWaveManager.init(this);//初始化声波配置
        ELog.hdl("isSuccess="+ isSuccess);
        /**
         * 请求所有必要的权限----
         */
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
                //Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onNext(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * 销毁的时候也要及时销毁
     */
    @Override
    protected void onDestroy() {
        SoundWaveManager.onDestroy(this);
        super.onDestroy();
    }
}
