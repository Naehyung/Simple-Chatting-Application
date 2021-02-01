package com.example.nh.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nh.R;
import com.example.nh.RestClient;
import com.example.nh.RetrofitService;
import com.example.nh.User;
import com.example.nh.chatting.ChattingActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment1 extends Fragment {

    public static final String TAG = "Fragment1";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initializati on parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private fragment1Adapter mAdapter;
    private ArrayList<User> userList;
    private String username;

    public Fragment1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment1 newInstance(String param1, String param2) {
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

    }

    public void sendEchoViaRest(String USERNAME1, String USERNAME2) {

        List<User> tempUserList = new ArrayList<>();
        User tempUser1 = new User();
        User tempUser2 = new User();
        tempUser1.setEmail(USERNAME1);
        tempUser2.setEmail(USERNAME2);
        tempUserList.add(tempUser1);
        tempUserList.add(tempUser2);
        RestClient.getInstance().getExampleRepository().checkChattingRoom("chatting-room-users", tempUserList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChatRoom>() {
                               @Override
                               public void onSubscribe(@NonNull Disposable d) {

                               }

                               @Override
                               public void onNext(@NonNull ChatRoom chatRoomTemp) {

                                   Log.d(TAG, "Test" + chatRoomTemp.getRoomId());

                                   Intent intent = new Intent(getActivity(), ChattingActivity.class);
                                   intent.putExtra("USERNAME1", USERNAME1);
                                   intent.putExtra("USERNAME2", USERNAME2);
                                   intent.putExtra("roomId", chatRoomTemp.getRoomId());
                                   intent.putExtra("ChatRoom", chatRoomTemp);
                                   startActivity(intent);

                               }

                               @Override
                               public void onError(@NonNull Throwable e) {

                               }

                               @Override
                               public void onComplete() {

                               }
                           }
                );



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);

        username = mParam1;

        userList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerview_fragment1);
        mAdapter = new fragment1Adapter(userList,username);
        mAdapter.setOnClickListener(new OnAdapterListener() {
            @Override
            public void onButtonClicked(String message) {
                Log.d(TAG, message);
                sendEchoViaRest(username, message);
            }

            @Override
            public void onViewClicked(Integer chatRoom) {

            }
        });
        LinearLayoutManager manager
                = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080//user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service1 = retrofit.create(RetrofitService.class);

        Call<List<User>> call = service1.getUsers("getUsers");
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                if (response.isSuccessful()) {

                    for(int i = 0; i < response.body().size(); i++) {

                        User userTemp = new User();
                        userTemp.setEmail(response.body().get(i).getEmail());

                        if(response.body().get(i).getEmail().equals(username)) {

                            userList.add(0, userTemp);
                            mAdapter.notifyItemInserted(0);

                        } else {

                            userList.add(userTemp);
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    Log.d("NH", "response 성공" + response.body().toString());

                } else {

                    Log.d("NH", "response 실패");

                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

                t.printStackTrace();

                Log.d("NH", "Fail to connect");

            }
        });



        return view;

    }





}