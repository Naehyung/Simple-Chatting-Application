package com.example.nh.chatting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nh.R;
import com.example.nh.RestClient;
import com.example.nh.menu.ChatRoom;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

import static com.example.nh.RestClient.ANDROID_EMULATOR_LOCALHOST;

public class ChattingActivity extends AppCompatActivity {

    private static final String TAG = "ChattingActivity";

    private Button buttonSend;
    private EditText editTextMessageLine;
    private RecyclerView recyclerView;
    private MyAdapter mAdapter;

    private ArrayList<DataItem> dataList;
    private StompClient mStompClient;
    private Gson mGson = new GsonBuilder().create();
    private Disposable mRestPingDisposable;
    private CompositeDisposable compositeDisposable;
    private CompositeDisposable compositeDisposableRest = new CompositeDisposable();
    private String USERNAME1;
    private String USERNAME2;
    private String roomId;

    private List<DataItem> dataItemList = new ArrayList<>();

    private ChatRoom chatRoom;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Intent intent = getIntent();
        USERNAME1 = intent.getStringExtra("USERNAME1");
        USERNAME2 = intent.getStringExtra("USERNAME2");
        roomId = intent.getStringExtra("roomId");
        chatRoom = new ChatRoom();
        chatRoom = (ChatRoom)intent.getSerializableExtra("ChatRoom");
        Log.d(TAG, USERNAME1+USERNAME2+roomId);

        dataList = new ArrayList<>();
        buttonSend = (Button) findViewById(R.id.send);
        editTextMessageLine = (EditText) findViewById(R.id.meesageLine);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new MyAdapter(dataList);
        LinearLayoutManager manager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(manager); // LayoutManager 등록
        recyclerView.setAdapter(mAdapter);  // Adapter 등록

        for(DataItem dataItem : chatRoom.getDataItemList()) {

            addItem(dataItem);

        }

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://" + ANDROID_EMULATOR_LOCALHOST
                + ":" + RestClient.SERVER_PORT + "/example-endpoint/websocket");

        Log.d(TAG, "Test4" + chatRoom.getRoomId());

        resetSubscriptions();

        connectStomp();


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendEchoViaStomp();

            }
        });


    }

    public void disconnectStomp(View view) {

        mStompClient.disconnect();

    }

    public static final String LOGIN = "login";

    public static final String PASSCODE = "passcode";

    public void connectStomp() {


        List<StompHeader> headers = new ArrayList<>();
        headers.add(new StompHeader(LOGIN, "guest"));
        headers.add(new StompHeader(PASSCODE, "guest"));

        mStompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);

        resetSubscriptions();

        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            toast("Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
                            toast("Stomp connection error");
                            break;
                        case CLOSED:
                            toast("Stomp connection closed");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            toast("Stomp failed server heartbeat");
                            break;
                    }
                });

        compositeDisposable.add(dispLifecycle);

        //Log.d(TAG, chatRoom.getRoomId());

        String destinationPath = "/topic/chatting/" + roomId;

        // Receive chatting
        Disposable dispTopic = mStompClient.topic(destinationPath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d(TAG, "Received " + topicMessage.getPayload());
                    Integer getDataItemType;
                    DataItem dataItem = mGson.fromJson(topicMessage.getPayload(), DataItem.class);

                    if (USERNAME1.equals(dataItem.getName())) {

                        getDataItemType = 1;

                    } else {

                        getDataItemType = 0;

                    }

                    addItem(new DataItem(dataItem.getContent(), dataItem.getName(), getDataItemType));
                }, throwable -> {
                    Log.e(TAG, "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispTopic);

        mStompClient.connect(headers);
    }

    public void sendEchoViaStomp() {

        DataItem dataItem = new DataItem(editTextMessageLine.getText().toString(), USERNAME1, Code.ViewType.RIGHT_CONTENT);
        Gson gson = new Gson();
        String json = gson.toJson(dataItem);
//        if (!mStompClient.isConnected()) return;
        compositeDisposable.add(mStompClient.send("/topic/chatting-room/" + roomId
                , json)
                .compose(applySchedulers())
                .subscribe(() -> {
                    Log.d(TAG, "STOMP echo send successfully");
                }, throwable -> {

                    Log.e(TAG, "Error send STOMP echo", throwable);
                    toast(throwable.getMessage());
                }));
    }


    public void retrofitSender() {


    }






//    public void checkChatRoomViaRest(CustomCallback customCallback) {
//
//        List<User> tempUserList = new ArrayList<>();
//        User tempUser1 = new User();
//        User tempUser2 = new User();
//        tempUser1.setEmail(USERNAME1);
//        tempUser2.setEmail(USERNAME2);
//        tempUserList.add(tempUser1);
//        tempUserList.add(tempUser2);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080//user/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//
//        RetrofitService service1 = retrofit.create(RetrofitService.class);
//
//        Call<ChatRoom> postCall = service1.checkChattingRoom("chatting-room-users", tempUserList);
//
//        postCall.enqueue(new Callback<ChatRoom>() {
//            @Override
//            public void onResponse(Call<ChatRoom> call, Response<ChatRoom> response) {
//
//                if(response.isSuccessful()) {
//
//                    customCallback.onSuccess(response.body());
//
//                    Log.d("Chatting", "response 성공");
//
//                    Log.d(TAG, response.body().getId().toString());
//
//
//
//
//
//                } else {
//
//                    Log.d("Chatting", "response 실패");
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ChatRoom> call, Throwable t) {
//
//                t.printStackTrace();
//
//                Log.d("Chatting", "Fail to connect");
//
//            }
//        });
//
//
//
//    }






    private void addItem(DataItem dataItem) {
        dataList.add(dataItem);
        mAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(dataList.size() - 1);
    }

    private void toast(String text) {
        Log.i(TAG, text);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onDestroy() {
        mStompClient.disconnect();

        if (mRestPingDisposable != null) mRestPingDisposable.dispose();
        if (compositeDisposable != null) compositeDisposable.dispose();
        super.onDestroy();
    }



}