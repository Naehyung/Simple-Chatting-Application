package com.example.nh.menu;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.example.nh.R;
import com.example.nh.RestClient;
import com.example.nh.RetrofitService;
import com.example.nh.User;
import com.example.nh.chatting.ChattingActivity;
import com.example.nh.chatting.DataItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {

    private static final String TAG = "Fragment2";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String username;

    private List<DataItem> dataItemList;

    private RecyclerView recyclerView;
    private fragment2Adapter mAdapter;
    private ArrayList<ChatRoom> chatRoomList;



    public Fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_2, container, false);

        username = mParam1;
        chatRoomList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerview_fragment2);
        mAdapter = new fragment2Adapter(chatRoomList, username);
        mAdapter.setOnClickListener(new OnAdapterListener() {
            @Override
            public void onButtonClicked(String message) {

            }

            @Override
            public void onViewClicked(Integer position) {

                List<String> userList = chatRoomList.get(position).getUserList();

                Log.d("TAG", "OnClick: " + userList.toString());

                sendEchoViaRest(userList.get(0), userList.get(1));

            }
        });
        LinearLayoutManager manager
                = new LinearLayoutManager(view.getContext());

        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);

        Log.d(TAG, username);

        RetrofitService service = RestClient.getInstance().getExampleRepository();

        Call<List<ChatRoom>> call = service.getChatRooms2("getChatRooms", username);

        call.enqueue(new Callback<List<ChatRoom>>() {
            @Override
            public void onResponse(Call<List<ChatRoom>> call, Response<List<ChatRoom>> response) {
                Log.d(TAG, username+"hi");
                Log.d(TAG, response.body().get(0).getRoomId());
                for(ChatRoom chatRoomTemp: response.body()) {

                    Log.d(TAG, chatRoomTemp.getRoomId());
                    chatRoomList.add(chatRoomTemp);
                    mAdapter.notifyDataSetChanged();




                }
            }

            @Override
            public void onFailure(Call<List<ChatRoom>> call, Throwable t) {

            }
        });

        // Inflate the layout for this fragment
        return view;
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




}

