package com.example.bw.book.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bw.book.R;
import com.example.bw.book.activity.CustomerWelcome;
import com.example.bw.book.activity.PurchaseAdapter;

import com.example.bw.book.entity.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FinalPurchase extends AppCompatActivity {

    DatabaseReference mCartRef, mUserRef, mBookRef;
    FirebaseAuth mAuth;
    FirebaseUser fbUser;

    Button finalPurchase;
    RecyclerView mFinalPurchaseView;
    int counter;

    String myGrossTotal, myDiscount, myNetTotal;
    String total;
    double grossTotals, userDiscount, netTotals;


    TextView grossTotal, discount, netTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_purchase);
        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();

        finalPurchase = (Button) findViewById(R.id.purchase);
        mFinalPurchaseView = (RecyclerView) findViewById(R.id.finalProductRecyclerView);
        grossTotal = (TextView) findViewById(R.id.grossTotal);
        discount = (TextView)findViewById(R.id.discount);
        netTotal = (TextView)findViewById(R.id.netTotal);

        mFinalPurchaseView.setHasFixedSize(true);
        mFinalPurchaseView.setLayoutManager(new LinearLayoutManager(this));
        mFinalPurchaseView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        addCartItems();

        finalPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FinalPurchase.this, CustomerWelcome.class));
            }
        });
    }

    public void addCartItems(){

        final ArrayList<String> images = new ArrayList<>();
        final ArrayList<String> titles = new ArrayList<>();
        final ArrayList<String> quantities = new ArrayList<>();
        final ArrayList<String> totals = new ArrayList<>();

        final ArrayList<Book> books = new ArrayList<>();
        counter = 0;

        mCartRef = FirebaseDatabase.getInstance().getReference("Cart");
        mCartRef.child(fbUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (!dataSnapshot.exists()) {
                    } else {
                        String title = ds.child("title").getValue(String.class);
                        String quantity = ds.child("quantity").getValue(Integer.class).toString();
                        total = ds.child("total").getValue(Double.class).toString();
                        String image = ds.child("image").getValue(String.class);


                        images.add(image);
                        titles.add(title);
                        quantities.add(quantity);

                        totals.add(total);
                        counter++;

                        PurchaseAdapter purchaseAdapter = new PurchaseAdapter(FinalPurchase.this, titles, quantities, totals, images, books);
                        mFinalPurchaseView.setAdapter(purchaseAdapter);

                    }
                    double num = Double.parseDouble(total);
                    grossTotals = grossTotals + num;
                    userDiscount = Math.round(grossTotals * 0.1);
                    netTotals = grossTotals - userDiscount;
                    myGrossTotal = Double.toString(grossTotals);
                    myDiscount = (String.valueOf(userDiscount));
                    myNetTotal = String.valueOf(netTotals);

                }
                if(grossTotals >= 50) {
                    discount.setText(myDiscount);
                }
                else
                {
                    userDiscount = 0;
                    discount.setText("Not eligiable for dicount");
                }
                grossTotal.setText(myGrossTotal);

                netTotal.setText(myNetTotal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}