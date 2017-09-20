package org.cocos2dx.cpp;

import android.app.Activity;
import android.widget.Toast;

import com.tencent.msdk.api.CardRet;
import com.tencent.msdk.api.LocationRet;
import com.tencent.msdk.api.LoginRet;
import com.tencent.msdk.api.ShareRet;
import com.tencent.msdk.api.TokenRet;
import com.tencent.msdk.api.WGPlatform;
import com.tencent.msdk.api.WGPlatformObserver;
import com.tencent.msdk.api.WakeupRet;
import com.tencent.msdk.consts.CallbackFlag;
import com.tencent.msdk.consts.EPlatform;
import com.tencent.msdk.consts.TokenType;
import com.tencent.msdk.remote.api.RelationRet;
import com.tencent.msdk.tools.Logger;

public class MsdkCallback implements WGPlatformObserver {
    public static AppActivity mainActivity;

    public MsdkCallback(Activity activity) {
        mainActivity = (AppActivity) activity;
    }
    public void OnLoginNotify(LoginRet ret) {
        Logger.d("called");
        Logger.d("ret.flag" + ret.flag);
        switch (ret.flag) {
            case CallbackFlag.eFlag_Succ:
                mainActivity.stopWaiting();
                // 登陆成功, 读取各种票据
                String openId = ret.open_id;
                String pf = ret.pf;
                String pfKey = ret.pf_key;
               // MainActivity.platform = ret.platform;
                String wxAccessToken = "";
                long wxAccessTokenExpire = 0;
                String wxRefreshToken = "";
                long wxRefreshTokenExpire = 0;
                for (TokenRet tr : ret.token) {
                    switch (tr.type) {
                        case TokenType.eToken_WX_Access:
                            wxAccessToken = tr.value;
                            wxAccessTokenExpire = tr.expiration;
                            break;
                        case TokenType.eToken_WX_Refresh:
                            wxRefreshToken = tr.value;
                            wxRefreshTokenExpire = tr.expiration;
                            break;
                        default:
                            break;
                    }
                }
                Toast.makeText(mainActivity, "登录成功", Toast.LENGTH_LONG).show();
                mainActivity.letUserLogin();
                break;
            // 游戏逻辑，对登陆失败情况分别进行处理
            case CallbackFlag.eFlag_NotInWhiteList:
                mainActivity.stopWaiting();
                mainActivity.letUserLogout();
                System.out.println("不在白名单内");
                Toast.makeText(mainActivity, "登录授权失败", Toast.LENGTH_LONG).show();
                break;
            case CallbackFlag.eFlag_Need_Realname_Auth:
                System.out.println("需要实名认证");
                Toast.makeText(mainActivity, "需要实名认证", Toast.LENGTH_LONG).show();

                // TODO 游戏如果配置MSDK_REAL_NAME_AUTH_SWITCH=0或者1在这里取消对msdk的超时处理

                // TODO 游戏如果配置MSDK_REAL_NAME_AUTH_SWITCH=2在这里调用自定义的实名认证界面

                // 这里模拟用户在游戏自定义界面填好信息准备提交注册
                /*
                if(mainActivity.isCustomUI()){
                    RealNameAuthInfo info = new RealNameAuthInfo();
                    info.name = "张霞";
                    info.identityType = eIDType.IDCards;
                    info.identityNum = "430455198411262142";
                    info.provinceID = 11;
                    WGPlatform.WGRealNameAuth(info);
                };
                */
                break;
            case CallbackFlag.eFlag_Login_NetworkErr:
            case CallbackFlag.eFlag_WX_UserCancel:
            case CallbackFlag.eFlag_WX_NotInstall:
            case CallbackFlag.eFlag_WX_NotSupportApi:
            case CallbackFlag.eFlag_WX_LoginFail:
            case CallbackFlag.eFlag_QQ_LoginFail:
            case CallbackFlag.eFlag_Local_Invalid:
                Toast.makeText(mainActivity, "登录授权失败", Toast.LENGTH_LONG).show();
                Logger.d(ret.desc);
            default:
                // 显示登陆界面
                mainActivity.stopWaiting();
                mainActivity.letUserLogout();
                break;
        }
    }
    @Override
    public void OnWakeupNotify(WakeupRet wakeupRet) {
        // TODO GAME 这里增加处理异账号的逻辑
        if (CallbackFlag.eFlag_Succ == wakeupRet.flag || CallbackFlag.eFlag_AccountRefresh == wakeupRet.flag) {
            // 本地账号登录成功可直接进入游戏
            // 游戏TODO：进入游戏逻辑
            if(EPlatform.ePlatform_Weixin.val() == wakeupRet.platform) {
                //"微信登录成功";
            } else if (EPlatform.ePlatform_QQ.val() == wakeupRet.platform) {
                //"QQ登录成功";
            } else if(EPlatform.ePlatform_QQHall.val() == wakeupRet.platform) {
                //"大厅登录成功";
            }
        } else if (CallbackFlag.eFlag_UrlLogin == wakeupRet.flag) {
            // 本地无账号信息，自动用拉起的账号登录，登录结果在OnLoginNotify()中回调
        } else if (CallbackFlag.eFlag_NeedSelectAccount == wakeupRet.flag) {
            // 游戏TODO：异账号时，游戏需要弹出提示框让用户选择需要登录的账号，并根据用户的选择调用WGSwitchUser接口

            //"异账号！";
        } else if (CallbackFlag.eFlag_NeedLogin == wakeupRet.flag) {
            // 游戏TODO： 没有有效的票据，登出游戏让用户重新登录
            //"登录失败";
            //"请重新登录";
        } else {
            //"未登录";
            //message = "OnWakeupNotify    flag : " + wakeupRet.flag + "\ndesc : " + wakeupRet.desc;
            WGPlatform.WGLogout();
        }

    }

    @Override
    public void OnShareNotify(ShareRet ret) {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnRelationNotify(RelationRet relationRet) {
        if (relationRet.flag == CallbackFlag.eFlag_Succ) {
            // 查询关系链成功
        } else {
            // 查询关系链失败
        }

    }
    @Override
    public void OnLocationNotify(RelationRet relationRet) {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnLocationGotNotify(LocationRet locationRet) {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnFeedbackNotify(int flag, String desc) {
        // TODO Auto-generated method stub

    }

    @Override
    public String OnCrashExtMessageNotify() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public byte[] OnCrashExtDataNotify() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void OnAddWXCardNotify(CardRet ret) {
        // TODO Auto-generated method stub

    }

}