package qz.rg.newspaper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import qz.rg.newspaper.R;
import qz.rg.newspaper.bean.User;
import qz.rg.newspaper.utils.UserManager;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etPhone;
    private TextInputEditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化控件
        etPhone = findViewById(R.id.et_phone);
        etPassword = findViewById(R.id.et_password);
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_login).setOnClickListener(v -> handleLogin());
    }

    private void handleLogin() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // 输入验证
        if (TextUtils.isEmpty(phone) || phone.length() != 11) {
            etPhone.setError("请输入有效的手机号");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            etPassword.setError("密码至少6位");
            return;
        }

        // 模拟登录成功（实际需调用接口验证）
        User user = new User(
                R.drawable.head,  // 用户头像URL（模拟）
                "李默然"  // 用户昵称（模拟）
        );
        UserManager.getInstance().setCurrentUser(user); // 保存用户信息

        // 登录成功后关闭当前页面，并通知MyFragment刷新
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK); // 设置结果码，用于通知MyFragment刷新
        finish();
    }
}