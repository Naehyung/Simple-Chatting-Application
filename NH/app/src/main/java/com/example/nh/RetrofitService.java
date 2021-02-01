package com.example.nh;

import com.example.nh.menu.ChatRoom;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitService {

    @GET("{post}")
    Call<List<User>> getUsers(@Path("post") String post);

    @POST("{post}")
    Call<User> addUser(@Path("post") String post, @Body User user);

    @POST("{post}")
    Call<User> loginCheck(@Path("post") String post, @Body User user);

    @POST("{post}")
    Observable<ChatRoom> checkChattingRoom(@Path("post") String post, @Body List<User> userList);

    @POST("{post}")
    Call<List<User>> checkChattingRoom2(@Path("post") String post, @Body List<User> userList);

    @POST("{post}")
    Observable<List<ChatRoom>> getChatRooms(@Path("post") String post, @Body String username);

    @POST("{post}")
    Call<List<ChatRoom>> getChatRooms2(@Path("post") String post, @Body String username);

}
