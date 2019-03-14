package com.example.administrator.roomcontrolapp.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.administrator.roomcontrolapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private Button register;
    private EditText ed_userName;
    private EditText ed_password;
    private EditText ed_ensurePassword;

    private String userName;
    private String password;
    private String ensurePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs

        register = findViewById(R.id.sign_in_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });



    }
//    public static void saveRegisterInfo(Context context, String username, String password){
//        //获取SharedPreferences对象
//        SharedPreferences sharedPre=context.getSharedPreferences("config", context.MODE_PRIVATE);
//        //获取Editor对象
//        SharedPreferences.Editor editor=sharedPre.edit();
//        //设置参数
//        editor.putString("username", username);
//        editor.putString("password", password);
//        //提交
//        editor.commit();
//    }

    public void register(){

        ed_userName = findViewById(R.id.user_name);
        ed_password = findViewById(R.id.password);
        ed_ensurePassword = findViewById(R.id.ensure_password);

        userName = ed_userName.getText().toString();
        password = ed_password.getText().toString();
        ensurePassword = ed_ensurePassword.getText().toString();
        if (userName.equals("") || password.equals("") || ensurePassword.equals("")){
            Toast.makeText(this, "用户名、密码或确认密码不能为空", Toast.LENGTH_SHORT).show();
        }
        else if (!(password.equals(ensurePassword))){
            Toast.makeText(this, "密码与确认密码不一致", Toast.LENGTH_SHORT).show();

        }
        else {
            //通过注册，向服务器发送请求
            AndroidNetworking.initialize(getApplicationContext());
            AndroidNetworking.post("http://182.254.136.237:8080/RoomServer/UserRegisterServlet")
                    .addBodyParameter("username",userName)
                    .addBodyParameter("password",password)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("key").equals("1")){
                                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                    //注册成功，将刚注册的用户名以及密码返回到登录界面
                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                    intent.putExtra("username",userName);
                                    intent.putExtra("password",password);
                                    startActivity(intent);
                                }else if (response.getString("key").equals("2")){
                                    Toast.makeText(RegisterActivity.this, "注册失败，请重新注册", Toast.LENGTH_SHORT).show();
                                    ed_userName.setText("");
                                    ed_password.setText("");
                                    ed_ensurePassword.setText("");
                                }else if (response.getString("key").equals("3")){
                                    Toast.makeText(RegisterActivity.this, "该用户已被注册，请重新注册", Toast.LENGTH_SHORT).show();
                                    ed_userName.setText("");
                                    ed_password.setText("");
                                    ed_ensurePassword.setText("");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }
    }
}
