package com.example.nh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;


public class CreateAnAccountActivity extends AppCompatActivity {

    private String email;
    private String password;

    private TextView textViewResult;
    private EditText textViewEmail;
    private EditText textViewPassword;

    private Button buttonCreateAnAccount;
    private Button buttonDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_an_account);

        textViewEmail = findViewById(R.id.CA_EmailLine);
        textViewPassword = findViewById(R.id.CA_PasswordLine);

        buttonCreateAnAccount = findViewById(R.id.CA_CreateAnAccount);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080//user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service1 = retrofit.create(RetrofitService.class);

        //Call<List<User>> call = service1.getUsers("getUsers");



        buttonCreateAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User();
                user.setEmail(textViewEmail.getText().toString());
                user.setPassword(textViewPassword.getText().toString());
                Call<User> postCall = service1.addUser("addUsers", user);

                postCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()) {

                            Log.d("NH","response 성공" + user.getEmail() + user.getPassword());
                            openMainActivity();

                        } else {

                            Log.d("NH","response 실패");

                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                        Log.d("NH", "Fail to connect");

                    }
                });



//                call.enqueue(new Callback<List<User>>() {
//                    @Override
//                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
//                        if(response.isSuccessful()) {
//
//                            List<User> users = response.body();
//
//                            Log.d("NH","onResponse: 성공, 결과\n" + users.get(0).toString());
//
//                        } else {
//
//                            Log.d("NH", "onResponse: 실패");
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<User>> call, Throwable t) {
//                        Log.d("NH", "onFailure: " + t.getMessage());
//
//                    }
//
//                });



            }
        });

    }

    private void openMainActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}



