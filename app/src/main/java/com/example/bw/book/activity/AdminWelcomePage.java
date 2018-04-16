package com.example.bw.book.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bw.book.R;
import com.example.bw.book.entity.Book;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdminWelcomePage extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mFirebaseRef;
    ArrayList<Book> allBooks;
    ViewAllBooksAdapter booksAdapter;
    ArrayList<String> idList;

    ListView listView;

    Button addBook,signOut, searchBooks, viewCart, titleAscending, titleDescending, authorAscending, authorDescending, priceAscending, priceDescending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome_page);
        mAuth = FirebaseAuth.getInstance();

        signOut = (Button) findViewById(R.id.signout1);
        addBook = (Button) findViewById(R.id.goToAdd);
        searchBooks = (Button) findViewById(R.id.goToSearch);
        viewCart = (Button) findViewById(R.id.viewCart);
        titleAscending = (Button) findViewById(R.id.titleAscendingBtn);
        titleDescending = (Button) findViewById(R.id.titleDescendingBtn);
        authorAscending = (Button) findViewById(R.id.authorAscedingBtn);
        authorDescending = (Button) findViewById(R.id.authorDescedingBtn);
        priceAscending = (Button) findViewById(R.id.priceAscendingBtn);
        priceDescending = (Button) findViewById(R.id.priceDescendingBtn);
        listView = (ListView) findViewById(R.id.allBooksListView);

        allBooks = new ArrayList<>();


        getAllBooks();
        sortBooks();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Signing out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminWelcomePage.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminWelcomePage.this, AddBookActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        searchBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminWelcomePage.this, SearchBooks.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    public void getAllBooks(){
        mFirebaseRef = FirebaseDatabase.getInstance().getReference("Book");
        mFirebaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String id = ds.child("id").getValue(String.class);
                    String title = ds.child("title").getValue(String.class);
                    String author = ds.child("author").getValue(String.class);
                    String category = ds.child("category").getValue(String.class);
                    String image = ds.child("image").getValue(String.class);
                    Double price = ds.child("price").getValue(Double.class);
                    Integer quantity = ds.child("quantity").getValue(Integer.class);
                    Integer noReviews = ds.child("noOfReviews").getValue(Integer.class);
                    Double rating = ds.child("rating").getValue(Double.class);


                    Book book = new Book(id, title, author, category, image, noReviews, quantity, price, rating);
//                        idList.add(id);
                    allBooks.add(book);
                }
                booksAdapter = new ViewAllBooksAdapter(allBooks,AdminWelcomePage.this);
                listView.setAdapter(booksAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            ((GlobalVariables) CustomerWelcome.this.getApplication()).setCurrentBook(idList.get(position).toString());
//                            startActivity(new Intent(CustomerWelcome.this, ViewBook.class));
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void sortBooks() {
        titleAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleAscending.setVisibility(View.INVISIBLE);
                titleDescending.setVisibility(View.VISIBLE);
                Collections.sort(allBooks, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        return book.getTitle().compareTo(t1.getTitle());
                    }
                });
                booksAdapter.notifyDataSetChanged();
            }
        });
        titleDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleDescending.setVisibility(View.INVISIBLE);
                titleAscending.setVisibility(View.VISIBLE);
                Collections.sort(allBooks, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        return t1.getTitle().compareTo(book.getTitle());
                    }
                });
                booksAdapter.notifyDataSetChanged();
            }
        });

        authorAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authorAscending.setVisibility(View.INVISIBLE);
                authorDescending.setVisibility(View.VISIBLE);
                Collections.sort(allBooks, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        return book.getAuthor().compareTo(t1.getAuthor());
                    }
                });
                booksAdapter.notifyDataSetChanged();
            }
        });

        authorDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authorDescending.setVisibility(View.INVISIBLE);
                authorAscending.setVisibility(View.VISIBLE);
                Collections.sort(allBooks, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        return t1.getAuthor().compareTo(book.getAuthor());
                    }
                });
                booksAdapter.notifyDataSetChanged();
            }
        });

        priceAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceAscending.setVisibility(View.INVISIBLE);
                priceDescending.setVisibility(View.VISIBLE);
                Collections.sort(allBooks, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        return t1.getPrice().compareTo(book.getPrice());
                    }
                });
                booksAdapter.notifyDataSetChanged();
            }
        });

        priceDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceDescending.setVisibility(View.INVISIBLE);
                priceAscending.setVisibility(View.VISIBLE);
                Collections.sort(allBooks, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        return book.getPrice().compareTo(t1.getPrice());
                    }
                });
                booksAdapter.notifyDataSetChanged();
            }
        });
    }
}