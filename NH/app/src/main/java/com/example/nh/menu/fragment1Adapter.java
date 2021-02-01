package com.example.nh.menu;

import android.content.Context;
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

import java.util.ArrayList;

public class fragment1Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = "fragment1Adapter";

    private ArrayList<User> userList = null;
    private String username;
    OnAdapterListener mListener;

    public fragment1Adapter(ArrayList<User> userList, String username) {

        this.userList = userList;
        this.username = username;



    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        Context context = parent.getContext();
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        view = inflator.inflate(R.layout.fragment1_content, parent, false);

        return new LeftViewFragment1Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof LeftViewFragment1Holder) {

            ((LeftViewFragment1Holder) holder).name.setText(userList.get(position).getEmail());

        }

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class LeftViewFragment1Holder extends RecyclerView.ViewHolder {

        TextView name;
        ImageView image;
        Button button;

        public LeftViewFragment1Holder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.fragment1_name);
            image = itemView.findViewById(R.id.fragment1_image);
            button = itemView.findViewById(R.id.chatButton);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mListener != null) {

                        mListener.onButtonClicked(name.getText().toString());
                    }
                }
            });

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

        public Button getButton() {
            return button;
        }

        public void setButton(Button button) {
            this.button = button;
        }
    }

    public void setOnClickListener(OnAdapterListener listener){
        mListener = listener;
    }
}


