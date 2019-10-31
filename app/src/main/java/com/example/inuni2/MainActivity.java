package com.example.inuni2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //private final String TAG = MainActivity.class.getCanonicalName();
   // private EditText accoutNumber;
    //private EditText password;
   // private Map<String, String> Map;
    //继承:extends
    //onCreate方法主要是初次启动（指使用这个类）首先加载的方法
    // @Override中文意思就是重写
    // setContentView建立连接
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //findViewById(R.id.btnStartAnotherAty).setOnClickListener(new View.OnClickListener() {
            //@Override
           // public void onClick(View v) {
            //    startActivity(new Intent(MainActivity.this,AnotherAty.class));
           // }
       // });
       // accoutNumber = findViewById(R.id.accountNumber);
       // password = findViewById(R.id.password);
       // Map = new HashMap<>();
    }

    public void LoginRequest(final String accountNumber, final String password) {
        //请求地址
        String url = "http://47.97.201.175:8080/INUNI/LoginServlet";    //注①
        String tag = "Login";    //注②

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    //服务器向客户端传递数据，客户端解封装
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            String result = jsonObject.getString("Result");  //注④
                            if (result.equals("success")) {  //注⑤
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this,AnotherAty.class);
                                startActivity(intent);
                                //做自己的登录成功操作，如页面跳转
                            } else {
                                Toast.makeText(MainActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
                                //做自己的登录失败操作，如Toast提示
                            }
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this,"无网络连接",Toast.LENGTH_SHORT).show();
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,"请稍后重试",Toast.LENGTH_SHORT).show();
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Log.e("TAG", error.getMessage(), error);
                //Log.v 的调试颜色为黑色的 Log.d的输出颜色是蓝色的 Log.i的输出为绿色 Log.w的意思为橙色 Log.e为红色
            }
        }) {
            //客户端向服务器传递数据，客户端封装（在Volley的getParams函数中）
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("AccountNumber", accountNumber);  //注⑥
                params.put("Password", password);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);
    }

}
