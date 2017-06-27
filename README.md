## 声波配网demo

### 配置

**Step 1.** Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```java
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2.** Add the dependency

```java
dependencies {
        compile 'com.github.jwkj:SoundWaveSender:v1.1'
}
```

### 忽略文件

添加忽略文件(app/proguard-rules.pro)
```java
-libraryjars libs/EMTMFSDK_0101_160914.jar
-dontwarn com.lsemtmf.**
-keep class com.lsemtmf.**{*; }
-dontwarn com.larksmart.**
-keep class com.larksmart.**{*; }
```



#### 初始化

 在发送广播之前需要先初始化，建议在发送广播的前2s之前初始化，所以建议在发送广播的上一个配置页就初始化（见demo）

 ```java
 SoundWaveManager.init(this);//初始化声波配置
 ```

在初始化页销毁的时候,需要将声波管理器销毁以节省系统资源

```java
   /**
     * 销毁的时候也要及时销毁
     */
    @Override
    protected void onDestroy() {
        SoundWaveManager.onDestroy(this);
    }
```
#### 发送声波

```java
  SoundWaveSender.getInstance()
                .with(this)//不要忘记写哦
                .setWifiSet(wifiSSID, wifiPwd)//wifi名字和wifi密码
                .send(new ResultCallback() {
                  /**
                    *拿到结果的时候会回调（温馨提示：由于设备的重发机制，可能会收到多条重复数据，需自己处理哦）
                    */
                    @Override
                    public void onNext(UDPResult udpResult) {
                      //get result
                    }
                   /**
                     * 声波发送失败的时候会回调
                     */
                    @Override
                    public void onError(Throwable throwable) {
                        //发生错误的时候需要处理一下，一般是先关闭声波发送，再重发
                    }

                    /**
                     * 当声波停止的时候
                     */
                    @Override
                    public void onStopSend() {
                       //当声波播放完成的时候会回调，此时如果还没拿到结果，那么建议在此处重新发送声波
                    }
                });
```

#### 关闭声波发送

```java
SoundWaveSender.getInstance().stopSend();
```

此外，为了避免非正常情况退出应用导致未能及时调用stopsend()停止任务，建议在activity/fragment的生命周期销毁的时候也关闭任务

```java
   /**
     * 销毁的时候也要及时关闭
     */
    @Override
    protected void onDestroy() {
        SoundWaveSender.getInstance().stopSend();
    }
```

>截图

![](https://github.com/jwkj/SoundwaveDemo/blob/master/demo.gif)


**温馨提示：**

- 建议将手机音量调至最大

- 建议将手机靠近设备30cm以内

- 需要将手机连接到wifi

- 暂不支持5G的wifi