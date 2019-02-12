package zz.flybird.com.autoread;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import zz.flybird.com.autoread.util.LogUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AccessibilityManager mAccessibilityManager;
    private TextView tv_text;
    private Button btn_open_service;
    private Button btn_root;
    private Button btn_souhu;
    private Button btn_zqkd;
    private Button btn_htt;
    private Button btn_lxk;
    private Button btn_wlkk;
    private Button btn_ttdd;
    private Button btn_jrtt;
    private Button btn_nntt;
    private Button btn_dftt;
    private Button btn_qtt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mAccessibilityManager = (AccessibilityManager) this.getSystemService(Context.ACCESSIBILITY_SERVICE);

        if (!checkAccessibilityEnabled(Context.ACCESSIBILITY_SERVICE)) {
            LogUtils.e("请开启无障碍服务");
            tv_text.setText("请开启无障碍服务");
        } else {
            LogUtils.e("无障碍服务已开启,请打开新闻客户端");
            tv_text.setText("无障碍服务已开启,请打开新闻客户端");
        }
    }


    /**
     * Check当前辅助服务是否启用
     *
     * @param serviceName serviceName
     * @return 是否启用
     */
    private boolean checkAccessibilityEnabled(String serviceName) {
        List<AccessibilityServiceInfo> accessibilityServices =
                mAccessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 前往开启辅助服务界面
     */
    public void goAccess() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initView() {
        tv_text = (TextView) findViewById(R.id.tv_text);
        btn_open_service = (Button) findViewById(R.id.btn_open_service);
        btn_root = (Button) findViewById(R.id.btn_root);
        btn_open_service.setOnClickListener(this);
        btn_root.setOnClickListener(this);
        btn_root.setVisibility(View.GONE);
        btn_souhu = (Button) findViewById(R.id.btn_souhu);
        btn_souhu.setOnClickListener(this);
        btn_zqkd = (Button) findViewById(R.id.btn_zqkd);
        btn_zqkd.setOnClickListener(this);
        btn_htt = (Button) findViewById(R.id.btn_htt);
        btn_htt.setOnClickListener(this);
        btn_lxk = (Button) findViewById(R.id.btn_lxk);
        btn_lxk.setOnClickListener(this);
        btn_wlkk = (Button) findViewById(R.id.btn_wlkk);
        btn_wlkk.setOnClickListener(this);
        btn_ttdd = (Button) findViewById(R.id.btn_ttdd);
        btn_ttdd.setOnClickListener(this);
        btn_jrtt = (Button) findViewById(R.id.btn_jrtt);
        btn_jrtt.setOnClickListener(this);
        btn_nntt = (Button) findViewById(R.id.btn_nntt);
        btn_nntt.setOnClickListener(this);
        btn_dftt = (Button) findViewById(R.id.btn_dftt);
        btn_dftt.setOnClickListener(this);
        btn_qtt = (Button) findViewById(R.id.btn_qtt);
        btn_qtt.setOnClickListener(this);
    }

    public final String rootPowerCommand = "chmod 777 /dev/block/mmcblk0";// 授权root权限命令

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_service:
                goAccess();
                break;
            case R.id.btn_root:
                openApp(ViewIdConfig.jrtt_jisu_package, ViewIdConfig.jrtt_jisu_splash);
                break;
            case R.id.btn_souhu:
                openApp(ViewIdConfig.souhu_package, ViewIdConfig.souhu_splash);
                break;
            case R.id.btn_zqkd:
                openApp(ViewIdConfig.zq_packahe, ViewIdConfig.zq_splash);
                break;
            case R.id.btn_htt:
                openApp(ViewIdConfig.htt_package, ViewIdConfig.zq_splash);
                break;
            case R.id.btn_lxk:
                openApp(ViewIdConfig.lxk_package, ViewIdConfig.lxk_splash);
                break;
            case R.id.btn_wlkk:
                openApp(ViewIdConfig.wlkk_package, ViewIdConfig.wlkk_splash);
                break;
            case R.id.btn_ttdd:
                openApp(ViewIdConfig.ttdd_package, ViewIdConfig.ttdd_splash);
                break;
            case R.id.btn_jrtt:
                openApp(ViewIdConfig.jrtt_jisu_package, ViewIdConfig.jrtt_jisu_splash);
                break;
            case R.id.btn_nntt:
                openApp(ViewIdConfig.nntt_pakage, ViewIdConfig.nntt_splash);
                break;
            case R.id.btn_dftt:
                openApp(ViewIdConfig.dftt_package, ViewIdConfig.dftt_splash);
                break;
            case R.id.btn_qtt:
                break;
        }
    }

    private void openSohu() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName("com.sohu.infonews",
                "com.sohu.quicknews.splashModel.activity.SplashActivity");
        intent.setComponent(cn);
        if (intent.resolveActivityInfo(getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY) != null) {//启动的intent存在
            startActivity(intent);
        } else {
            Toast.makeText(this, "应用未安装", Toast.LENGTH_SHORT).show();
        }
    }

    private void openApp(String packageName, String launchClassName) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        ComponentName cn = new ComponentName(packageName, launchClassName);
        intent.setComponent(cn);
        if (intent.resolveActivityInfo(getPackageManager(), PackageManager.MATCH_DEFAULT_ONLY) != null) {//启动的intent存在
            startActivity(intent);
        } else {
            Toast.makeText(this, "应用未安装", Toast.LENGTH_SHORT).show();
        }
    }
}
