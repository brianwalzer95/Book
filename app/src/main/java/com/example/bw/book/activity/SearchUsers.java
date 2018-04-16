package com.example.bw.book.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.bw.book.R;
import com.example.bw.book.entity.GlobalVariables;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchUsers extends AppCompatActivity {

    EditText mSearchEditText;
    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    FirebaseUser firebaseUser;
    ArrayList<String> idList;
    ArrayList<String> nameList;
    ArrayList<String> emailList;
    ArrayList<String> addressList;


    UserSearchAdapter userSearchAdapter;
    private static RecyclerViewClickListener itemListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        mSearchEditText = (EditText) findViewById(R.id.searchUser);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        idList = new ArrayList<>();
        nameList = new ArrayList<>();
        emailList = new ArrayList<>();
        addressList = new ArrayList<>();


        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    setAdapter(s.toString());
                } else {
                    idList.clear();
                    nameList.clear();
                    emailList.clear();
                    addressList.clear();
                    recyclerView.removeAllViews();
                }
            }
        });

    }

    private void setAdapter(final String searchedString) {
        mDatabase.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the list for each new search
                idList.clear();
                nameList.clear();
                emailList.clear();
                addressList.clear();
                recyclerView.removeAllViews();

                int counter = 0;

                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    String uid = ds.getKey();
                    String id = ds.child("id").getValue(String.class);
                    String name = ds.child("name").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    String address = ds.child("address").getValue(String.class);

                    if (name.contains(searchedString)){
                        idList.add(id);
                        nameList.add(name);
                        emailList.add(email);
                        addressList.add(address);
                        counter++;
                    } else if (email.contains(searchedString)) {
                        idList.add(id);
                        nameList.add(name);
                        emailList.add(email);
                        addressList.add(address);
                        counter++;
                    }
                    if(counter == 15){
                        break;
                    }

                    userSearchAdapter = new UserSearchAdapter(SearchUsers.this, idList, nameList, emailList, addressList, new RecyclerViewClickListener() {
                        @Override
                        public void recyclerViewLisClicked(View v, int position) {
                            ((GlobalVariables) SearchUsers.this.getApplication()).setCurrentBook(idList.get(position).toString());
                            startActivity(new Intent(SearchUsers.this, ViewBook.class));
                        }
                    });
                    recyclerView.setAdapter(userSearchAdapter);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
