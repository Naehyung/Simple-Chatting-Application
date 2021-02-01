package com.example.nh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nh.chatting.ChattingActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button buttonCreateAnAccount;
    private Button buttonLogin;
    private EditText textViewEmail;
    private EditText textViewPassword;

    public String USERNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonCreateAnAccount = (Button) findViewById(R.id.CreateAnAccount);
        buttonLogin = (Button) findViewById(R.id.LogIn);
        textViewEmail = findViewById(R.id.EmailLine);
        textViewPassword = findViewById(R.id.PasswordLine);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://54.206.238.187:8080/user/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080//user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service1 = retrofit.create(RetrofitService.class);

        buttonLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                User user = new User();
                user.setEmail(textViewEmail.getText().toString());
                user.setPassword(textViewPassword.getText().toString());
                Call<User> postCall = service1.loginCheck("loginCheck", user);

                postCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {

                        if (textViewEmail.getText().toString().matches("")) {

                            toast("Please type your email address and password");

                        } else {

                            if (response.isSuccessful()) {

                                Log.d("NH", "response 성공" + response.body().toString());

                                if (user.getEmail().equals(response.body().getEmail())
                                        && user.getPassword().equals(response.body().getPassword())) {

                                    USERNAME = user.getEmail();

                                    //openChattingActivity();

                                    openMenuActivity();

                                } else if (!user.getEmail().equals(response.body().getEmail())
                                        || !user.getPassword().equals(response.body().getPassword())) {

                                    toast("Couldn't find your account");

                                }


                            } else {

                                Log.d("NH", "response 실패");

                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                        t.printStackTrace();

                        Log.d("NH", "Fail to connect");

                    }
                });



            }
        });

        buttonCreateAnAccount.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                openCreateAnAccountActivity();
            }
        });

    }

    private void openMenuActivity() {

        Intent intent = new Intent("android.intent.action.MENU");
        intent.putExtra("USERNAME", USERNAME);
        startActivity(intent);
    }

    private void openChattingActivity() {

        Intent intent = new Intent("android.intent.action.CHATTING");
        startActivity(intent);
    }

    private void openCreateAnAccountActivity() {


        Intent intent = new Intent(this, CreateAnAccountActivity.class);
        startActivity(intent);
    }

    private void toast(String text) {
        Log.i(TAG, text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}