package com.faucet.quickutils.core.controler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.faucet.quickutils.R;
import com.faucet.quickutils.language.LanguageUtil;
import com.faucet.quickutils.utils.PermissionEnum;
import com.faucet.quickutils.utils.PermissionUtils;
import com.faucet.quickutils.utils.ToastUtil;
import com.faucet.quickutils.utils.WActivityManager;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.gyf.barlibrary.ImmersionBar;
import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * Created by Faucet on 2016/9/2.
 */
public class BaseActivity extends SwipeBackActivity {

    private final int REQUEST = 200;
    private OnPermissionResult onPermissionResult;
    private String[] permissons;
    private int[] outAnim;
    public ImmersionBar mImmersionBar;
    private KProgressHUD hud;

    /**
     *
     * @param intent
     * @param enterAnim 进入动画
     * @param outAnim 推出动画
     */
    public void startActivityForOverridePendingTransition(Intent intent, int[] enterAnim, int[] outAnim){
        if(enterAnim==null&&outAnim==null){
            enterAnim = new int[]{R.anim.activity_up_enter, R.anim.activity_no_move};
            outAnim = new int[]{R.anim.activity_no_move, R.anim.activity_down_out};
        }
        intent.putExtra("outAnim", outAnim);
        startActivity(intent);
        if(enterAnim!=null&&enterAnim.length==2){
            overridePendingTransition(enterAnim[0],enterAnim[1]);
        }
    }

    public void checkHasSelfPermissions(OnPermissionResult onPermissionResult, String... permissions){
        this.onPermissionResult = onPermissionResult;
        this.permissons = permissions;
        if (PermissionUtils.hasSelfPermissions(this, permissions)) {
            onPermissionResult.permissionAllow();
        }else{
            ActivityCompat.requestPermissions(this, permissions, REQUEST);
        }
    }

    public interface OnPermissionResult {
        void permissionAllow();
        void permissionForbid();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST:
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    onPermissionResult.permissionAllow();
                } else {
                    if (!PermissionUtils.shouldShowRequestPermissionRationale(this, this.permissons)) {
                        String permission = PermissionUtils.getDontAskAgainPermission(this, this.permissons);
                        if(!permission.equals("")){
                            String title = "您已关闭"+ PermissionEnum.statusOf(permission).getName()+"权限，并选择永不询问，是否前往设置手动开启？";
                            final NormalDialog dialog = new NormalDialog(this);
                            BaseAnimatorSet baseAnimatorSet = new BounceTopEnter();
                            dialog.content(title);
                            dialog.showAnim(baseAnimatorSet);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.btnText("取消", "去设置");
                            dialog.setOnBtnClickL(new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {
                                    dialog.dismiss();
                                }
                            }, new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {
                                    PermissionUtils.openSetting(BaseActivity.this);
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        }
                    }else{
                        ToastUtil.showShort(this, PermissionEnum.statusOf(PermissionUtils.getShouldShowRequestPermission(this, this.permissons)).getDenidStr());
                    }
                    onPermissionResult.permissionForbid();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取控件
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T getView(int id) {
        //noinspection unchecked
        return (T) findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.outAnim = getIntent().getIntArrayExtra("outAnim");
        if(this.outAnim!=null){
            setSwipeBackEnable(false);
        }
        if(mImmersionBar == null) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar.init();
        }
        if(mImmersionBar != null) {
            mImmersionBar.statusBarDarkFont(true, 0.2f).init();
        }
        WActivityManager.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        if(mImmersionBar != null) {
            mImmersionBar.keyboardEnable(true).init();  //解决软键盘与底部输入框冲突问题
        }
    }

    @Override
    protected void onPause() {
        dismissDialog();
        hintKbOne();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mImmersionBar != null) {
            mImmersionBar.destroy();  //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        }
        WActivityManager.getInstance().removeActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
        if(outAnim!=null&&outAnim.length==2){
            overridePendingTransition(outAnim[0],outAnim[1]);
        }
    }

    public void showProgressDialog() {
        if (hud == null) {
            hud = KProgressHUD.create(this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        }
        if (!hud.isShowing()) {
            hud.show();
        }
    }

    public void dismissDialog() {
        if (hud != null) {
            hud.dismiss();
        }
    }

    private void hintKbOne() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    //实现点击空白区域，收起输入法
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);

            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LanguageUtil.attachBaseContext(base));
    }
}
