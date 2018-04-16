package com.example.bw.book.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bw.book.R;

import java.util.ArrayList;


public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.SearchViewHolder> {

    Context context;
    private static RecyclerViewClickListener itemListener;
    ArrayList<String> idList;
    ArrayList<String> nameList;
    ArrayList<String> emailList;
    ArrayList<String> addressList;


    public UserSearchAdapter(Context context, ArrayList<String> idList, ArrayList<String> nameList, ArrayList<String> emailList, ArrayList<String> addressList, RecyclerViewClickListener itemListener) {
        this.context = context;
        this.itemListener = itemListener;
        this.idList = idList;
        this.nameList = nameList;
        this.emailList = emailList;
        this.addressList = addressList;
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mName, mEmail, mAddress;


        public SearchViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mName = (TextView) itemView.findViewById(R.id.nameSearchItem);
            mEmail = (TextView) itemView.findViewById(R.id.emailSearchItem);
            mAddress = (TextView) itemView.findViewById(R.id.addressSearchItem);


        }

        @Override
        public void onClick(View v){
            itemListener.recyclerViewLisClicked(v, this.getLayoutPosition());
        }
    }

    @Override
    public UserSearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_user_search, parent, false);
        return new UserSearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.mName.setText(nameList.get(position));
        holder.mEmail.setText(emailList.get(position));
        holder.mAddress.setText(addressList.get(position));

    }

    @Override
    public int getItemCount() {
        return nameList.size();
    }

}