package com.example.bw.book.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bw.book.R;
import com.example.bw.book.entity.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddBookActivity extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase;

    private EditText titleText, authorText, priceText, stockText, quantityText;
    private Button mAddButton, chooseButton;
    private ImageView imageView;
    private Uri filePath;
    private Spinner mSpinner;
    private final int PICK_IMAGE_REQUEST = 71;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    Book book;

    String bookId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        titleText = (EditText)findViewById(R.id.TitleAdd);
        authorText = (EditText)findViewById(R.id.AuthorAdd);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        priceText = (EditText)findViewById(R.id.PriceAdd);
        quantityText = (EditText)findViewById(R.id.QuantityAdd);

        mAddButton = (Button)findViewById(R.id.addBook);
        chooseButton = (Button)findViewById(R.id.chooseImage);
        imageView = (ImageView)findViewById(R.id.imageView);

        final String[] category = getResources().getStringArray(R.array.categories);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, category);
        mSpinner.setAdapter(arrayAdapter);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("Book");
        bookId = mFirebaseDatabase.push().getKey();

        mAddButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  final String bookID = mFirebaseDatabase.push().getKey();
                  final String title = titleText.getText().toString();
                  final String author = authorText.getText().toString();
                  final Double price = Double.parseDouble(priceText.getText().toString());

                  final String selectedCategory = mSpinner.getSelectedItem().toString();
                  final int quantity = Integer.parseInt(quantityText.getText().toString());
                  if (title.equals("")) {
                      Toast.makeText(getApplicationContext(), "Please enter in a title", Toast.LENGTH_SHORT).show();
                      return;
                  } else if (author.equals("")) {
                      Toast.makeText(getApplicationContext(), "Please enter in a author", Toast.LENGTH_SHORT).show();
                      return;
                  } else if (price.equals("")) {
                      Toast.makeText(getApplicationContext(), "Please enter in a price", Toast.LENGTH_SHORT).show();
                      return;
                  } else if (quantity <= 0) {
                      Toast.makeText(getApplicationContext(), "Stock cannot be 0 or a negative number", Toast.LENGTH_SHORT).show();
                      return;
                  } else {
                      final Double avgRating = 0.00;
                      final int noOfRatings = 0;
                      StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
                      ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                              @SuppressWarnings("VisibleForTests")
                              Uri uri = taskSnapshot.getDownloadUrl();
                              String imagePath = uri.toString();
                              Toast.makeText(AddBookActivity.this, "Uploaded Image", Toast.LENGTH_SHORT).show();
                              book = new Book(bookID, title, author, selectedCategory, imagePath, noOfRatings, quantity , price, avgRating);
                              mFirebaseDatabase.child(bookID).setValue(book);
                              mAddButton.setVisibility(View.INVISIBLE);
                              Toast.makeText(getApplicationContext(), "Book added: " + book.getTitle(), Toast.LENGTH_LONG).show();
                              startActivity(new Intent(AddBookActivity.this, AdminWelcomePage.class));
                          }
                      });
                  }
              }
          });


        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

    }

    private void chooseFile()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {

            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

                StorageReference reference = storageReference.child("images").child(filePath.getLastPathSegment());
                reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(AddBookActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

}