package zz.flybird.com.autoread;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import zz.flybird.com.autoread.util.ADBUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AccessibilityManager mAccessibilityManager;
    private TextView tv_text;
    private Button btn_open_service;
    private Button btn_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        mAccessibilityManager = (AccessibilityManager) this.getSystemService(Context.ACCESSIBILITY_SERVICE);

        if (!checkAccessibilityEnabled(Context.ACCESSIBILITY_SERVICE)) {
            tv_text.setText("请开启无障碍服务");
        } else {
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
    }
    public final String rootPowerCommand = "chmod 777 /dev/block/mmcblk0";// 授权root权限命令
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_open_service:
                goAccess();
                break;
            case R.id.btn_root:
//                ADBUtil.execShellCmd(rootPowerCommand);
                break;
        }
    }
}
