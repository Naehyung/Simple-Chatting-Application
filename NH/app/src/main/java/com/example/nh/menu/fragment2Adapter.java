package com.example.nh.menu;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nh.R;
import com.example.nh.User;
import com.example.nh.chatting.DataItem;

import java.util.ArrayList;
import java.util.List;

public class fragment2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "fragment1Adapter";

    private ArrayList<ChatRoom> chatRoomList = null;
    private String username;
    OnAdapterListener mListener;


    public fragment2Adapter(ArrayList<ChatRoom> chatRoomList, String username) {

        this.chatRoomList = chatRoomList;
        this.username = username;



    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        view = inflator.inflate(R.layout.fragment2_content, parent, false);

        return new LeftViewFragment2Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof LeftViewFragment2Holder) {

            ((LeftViewFragment2Holder) holder).name.setText(chatRoomList.get(position).getUserList().toString());
            if(!chatRoomList.get(position).getDataItemList().isEmpty()) {
                List<DataItem> dataItemList = chatRoomList.get(position).getDataItemList();

                ((LeftViewFragment2Holder) holder).content.setText(dataItemList.get(dataItemList.size()-1).getContent());
            } else {
                ((LeftViewFragment2Holder) holder).content.setText("");
            }


        }

    }

    @Override
    public int getItemCount() {
        return chatRoomList.size();

    }



    public class LeftViewFragment2Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        TextView content;
        ImageView image;

        private long mLastClickTime = 0;

        public LeftViewFragment2Holder(View itemView) {
            super(itemView);

            content = itemView.findViewById(R.id.fragment2_content);
            name = itemView.findViewById(R.id.fragment2_name);
            image = itemView.findViewById(R.id.fragment2_image);
            itemView.setOnClickListener(this);

        }

        public TextView getName() {
            return name;
        }

        public void setName(TextView name) {
            this.name = name;
        }

        public ImageView getImage() {
            return image;
        }

        public void setImage(ImageView image) {
            this.image = image;
        }

        public TextView getContent() {
            return content;
        }

        public void setContent(TextView content) {
            this.content = content;
        }

        @Override
        public void onClick(View v) {

            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();


            if(mListener != null) {

                mListener.onViewClicked(getAdapterPosition());
            }

        }
    }

    public void setOnClickListener(OnAdapterListener listener){
        mListener = listener;
    }



}


