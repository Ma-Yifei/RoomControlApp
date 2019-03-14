package com.example.administrator.roomcontrolapp.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.administrator.roomcontrolapp.R;
import com.example.administrator.roomcontrolapp.util.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private TextView registerText;
    private Button login;
    private EditText ed_userName;
    private EditText ed_password;
    private Button register;

    private String sign_in_userName;
    private String sign_in_password;

    private String userName;
    private String password;
    private User user = new User();
    private ArrayList<User> users = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs

        ed_userName = findViewById(R.id.user_name);
        ed_password = findViewById(R.id.password);
        login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        //点击新用户注册进入注册界面
        registerText = findViewById(R.id.register);
        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //将刚注册成功的账户填入登陆界面中
        sign_in_userName = getIntent().getStringExtra("username");
        sign_in_password = getIntent().getStringExtra("password");
        ed_userName.setText(sign_in_userName);
        ed_password.setText(sign_in_password);

    }

    public void login(){
        //将输入的用户名以及密码提交到服务器，服务器返回指令key来说明是否登录成功。
        userName = ed_userName.getText().toString();
        password = ed_password.getText().toString();

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post("http://182.254.136.237:8080/RoomServer/UserLoginServlet")
                .addBodyParameter("username",userName)
                .addBodyParameter("password",password)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String key = response.getString("key");
                            if (key.equals("1")){
                                Toast.makeText(LoginActivity.this,"登录成功！", Toast.LENGTH_SHORT).show();

                                /*将每个用户的用户名和密码都存入一个arraylist中，并将arraylist存入SharedPreferences中，
                                以备切换用户功能使用*/
                                SharedPreferences sp = getSharedPreferences("users",MODE_PRIVATE);
                                String json1 = sp.getString("user",null);
                                //SP中还没有存入任何用户
                                if (json1 == null || json1.equals("")){
                                    user.setUsername(userName);
                                    user.setPassword(password);
                                    users.add(user);
                                }
                                //SP中已经存入数据
                                else{
                                    Type type = new TypeToken<List<User>>(){}.getType();
                                    Gson gson1 = new Gson();
                                    users = gson1.fromJson(json1, type);
                                    //判断之前是否已经登陆过该用户，用户信息存入SP中，如果已经存入将之前存入的删去将刚输入的信息存入（或者直接用上次的信息）
                                    for (int i=0;i<users.size();i++){
                                        if (users.get(i).getUsername().equals(userName)){
                                            users.remove(i);
                                        }
                                    }
                                    user.setUsername(userName);
                                    user.setPassword(password);
                                    users.add(user);
                                }
                                SharedPreferences.Editor editor = getSharedPreferences("users",MODE_PRIVATE).edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(users);
                                editor.putString("user",json);
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else if (key.equals("2")){
                                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                            }else if (key.equals("3")){
                                Toast.makeText(LoginActivity.this, "用户名不存在,"+response.getString("info"), Toast.LENGTH_SHORT).show();
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
