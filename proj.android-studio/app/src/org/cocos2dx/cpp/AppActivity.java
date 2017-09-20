/****************************************************************************
Copyright (c) 2015-2017 Chukong Technologies Inc.
 
http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
package org.cocos2dx.cpp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.tencent.msdk.api.MsdkBaseInfo;
import com.tencent.msdk.api.WGPlatform;
import com.tencent.msdk.api.WGQZonePermissions;
import com.tencent.msdk.consts.EPlatform;
import com.tencent.msdk.tools.Logger;

import org.cocos2dx.lib.Cocos2dxActivity;

public class AppActivity extends Cocos2dxActivity {
    public static ProgressDialog mAutoLoginWaitingDlg;
    private boolean isFirstLogin = false;
    protected void onCreate(Bundle saved)
    {  
        super.onCreate(saved);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        MsdkBaseInfo baseInfo = new MsdkBaseInfo();
        baseInfo.qqAppId = "1105633713";
        baseInfo.qqAppKey = "U8WArhtwJLhcxtaY";
        //2.18.0版本已经删除qAppKey字段
        baseInfo.wxAppId = "wx139241008fc3a064";
        baseInfo.msdkKey = "2d8e95f19150bca7ffb42aec1c84d885";
        baseInfo.offerId = "1450008323";
        // TODO GAME 自2.7.1a开始游戏可在初始化msdk时动态设置版本号，灯塔和bugly的版本号由msdk统一设置
        // 1、版本号组成 = versionName + versionCode
        // 2、游戏如果不赋值给appVersionName（或者填为""）和appVersionCode(或者填为-1)，
        // msdk默认读取AndroidManifest.xml中android:versionCode="51"及android:versionName="2.7.1"
        // 3、游戏如果在此传入了appVersionName（非空）和appVersionCode（正整数）如下，则灯塔和bugly上获取的版本号为2.7.1.271
        baseInfo.appVersionName = "2.15.0";
        baseInfo.appVersionCode = 68903;

        // 注意：传入Initialized的activity即this，在游戏运行期间不能被销毁，否则会产生Crash
        WGPlatform.Initialized(this, baseInfo);
        // 设置拉起QQ时候需要用户授权的项
        WGPlatform.WGSetPermission(WGQZonePermissions.eOPEN_ALL);
        // 全局回调类，游戏自行实现
        WGPlatform.WGSetObserver(new MsdkCallback(this));
        isFirstLogin = true;
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        WGPlatform.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        WGPlatform.onResume();

        // TODO GAME 模拟游戏自动登录，这里需要游戏添加加载动画
        // WGLogin是一个异步接口, 传入ePlatform_None则调用本地票据验证票据是否有效
        // 如果从未登录过，则会立即在onLoginNotify中返回flag为eFlag_Local_Invalid，此时应该拉起授权界面
        // 建议在此时机调用WGLogin,它应该在handlecallback之后进行调用。
        if(isFirstLogin) {
            isFirstLogin = false;
            startWaiting();
            WGPlatform.WGLogin(EPlatform.ePlatform_None);
        }
    }

    // TODO GAME 游戏需要集成此方法并调用WGPlatform.onPause()
    @Override
    protected void onPause() {
        super.onPause();
        WGPlatform.onPause();

    }

    // TODO GAME 游戏需要集成此方法并调用WGPlatform.onStop()
    @Override
    protected void onStop() {
        super.onStop();
        WGPlatform.onStop();
    }

    // TODO GAME 游戏需要集成此方法并调用WGPlatform.onDestory()
    @Override
    protected void onDestroy() {
        super.onDestroy();
        WGPlatform.onDestory(this);
        Logger.d("onDestroy");
    }

    // TODO GAME 在onActivityResult中需要调用WGPlatform.onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        WGPlatform.onActivityResult(requestCode, resultCode, data);
        Logger.d("onActivityResult");
    }

    // TODO GAME 在onNewIntent中需要调用handleCallback将平台带来的数据交给MSDK处理
    @Override
    protected void onNewIntent(Intent intent) {
        Logger.d("onNewIntent");
        super.onNewIntent(intent);

        // TODO GAME 处理游戏被拉起的情况
        // launchActivity的onCreat()和onNewIntent()中必须调用
        // WGPlatform.handleCallback()。否则会造成微信登录无回调
        if (WGPlatform.wakeUpFromHall(intent)) {
            Logger.d("LoginPlatform is Hall");
            Logger.d(intent);
        } else {
            Logger.d("LoginPlatform is not Hall");
            Logger.d(intent);
            WGPlatform.handleCallback(intent);
        }
    }
    public void startWaiting() {
        Logger.d("startWaiting");
        mAutoLoginWaitingDlg = new ProgressDialog(this);
        stopWaiting();
        mAutoLoginWaitingDlg.setTitle("自动登录中...");
        mAutoLoginWaitingDlg.show();
    }

    public void stopWaiting() {
        Logger.d("stopWaiting");
        if (mAutoLoginWaitingDlg != null && mAutoLoginWaitingDlg.isShowing()) {
            mAutoLoginWaitingDlg.dismiss();
        }
    }
    public void letUserLogin(){

    }
    public void letUserLogout(){

    }
    public static void qqlogin(){
        WGPlatform.WGLogin (EPlatform.ePlatform_QQ);
    }
    public static void wxlogin(){
        WGPlatform.WGLogin (EPlatform.ePlatform_Weixin);
    }
    public static void wxQrCodelogin(){
        WGPlatform.WGQrCodeLogin (EPlatform.ePlatform_Weixin);
    }
   public static void  loginout(){
       WGPlatform.WGLogout();
   }
}
