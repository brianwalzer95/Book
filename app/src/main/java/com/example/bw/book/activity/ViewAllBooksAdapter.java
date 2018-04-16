package com.example.bw.book.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bw.book.R;
import com.example.bw.book.entity.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ViewAllBooksAdapter extends ArrayAdapter<Book> {
    Context context;
    ArrayList<Book> books;
    ArrayList<String> idList;
    int pos;

    private static class ViewHolder {
        TextView titleTV;
        TextView authorTV;
        TextView categoryTV;
        TextView priceTV;
        ImageView imageView;
    }

    public ViewAllBooksAdapter(ArrayList<Book> books, Context context) {
        super(context, R.layout.search_list_items, books);
        this.books = books;
        this.idList = idList;
        this.context = context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        ViewHolder viewHolder;

        viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.search_list_items, parent, false);
        viewHolder.titleTV = (TextView) convertView.findViewById(R.id.titleSearchItem);
        viewHolder.authorTV = (TextView) convertView.findViewById(R.id.authorSearchItem);
        viewHolder.categoryTV = (TextView) convertView.findViewById(R.id.categorySearchItem);
        viewHolder.priceTV = (TextView) convertView.findViewById(R.id.priceSearchItem);
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.bookImage);

        viewHolder.titleTV.setText(books.get(position).getTitle());
        viewHolder.authorTV.setText(books.get(position).getAuthor());
        viewHolder.categoryTV.setText(books.get(position).getCategory());
        viewHolder.priceTV.setText("€ " + books.get(position).getPrice().toString());

        Picasso.with(context).load(books.get(position).getImage()).fit().placeholder(R.mipmap.ic_launcher_round).into(viewHolder.imageView);

        return convertView;
    }
}
