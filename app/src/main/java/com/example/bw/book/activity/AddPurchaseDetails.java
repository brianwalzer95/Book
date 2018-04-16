package com.example.bw.book.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bw.book.R;
import com.example.bw.book.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddPurchaseDetails extends AppCompatActivity {

    EditText creditCardNumber, shippingAddress;
    RadioButton creditRadio, debitRadio;

    DatabaseReference mBookRef, mCartRef, mUserRef;
    FirebaseAuth mAuth;
    FirebaseUser fbUser;
    Context context;

    Button proceed;

    String userName, bookID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_purchase_details);

        creditCardNumber = (EditText) findViewById(R.id.cardNum);
        shippingAddress = (EditText) findViewById(R.id.shippingAddress);
        creditRadio = (RadioButton) findViewById(R.id.creditRadio);
        debitRadio = (RadioButton) findViewById(R.id.debitRadio);

        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();

        proceed = (Button)findViewById(R.id.proceedtopayment);

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getUserDetails();
            }
        });



    }

    public void getUserDetails() {
        mUserRef = FirebaseDatabase.getInstance().getReference("User");
        mUserRef.child(fbUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName = user.getName();
                String type;

                if(creditRadio.equals(false))
                {
                    type = "Debit";
                }
                else
                {
                    type = "Credit";
                }

                String creditNumber = creditCardNumber.getText().toString();
                String address = shippingAddress.getText().toString();

                String name = user.getName();
                String email = user.getEmail();
                String password = user.getPassword();

                mUserRef.child(fbUser.getUid()).child("name").setValue(name);
                mUserRef.child(fbUser.getUid()).child("email").setValue(email);
                mUserRef.child(fbUser.getUid()).child("password").setValue(password);
                mUserRef.child(fbUser.getUid()).child("creditNumber").setValue(creditNumber);
                mUserRef.child(fbUser.getUid()).child("address").setValue(address);
                mUserRef.child(fbUser.getUid()).child("type").setValue(type);

                Toast.makeText(getApplicationContext(), "Details added for:" + user.getName(), Toast.LENGTH_LONG).show();

                startActivity(new Intent(AddPurchaseDetails.this, FinalPurchase.class));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}