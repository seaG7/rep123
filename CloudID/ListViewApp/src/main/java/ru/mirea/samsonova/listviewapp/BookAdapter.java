package ru.mirea.samsonova.listviewapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class BookAdapter extends BaseAdapter {
    private final List<Book> books;
    private final LayoutInflater inflater;

    public BookAdapter(LayoutInflater inflater, List<Book> books) {
        this.inflater = inflater;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Book getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            row = inflater.inflate(R.layout.item_book, parent, false);
        }
        Book book = getItem(position);
        TextView title = row.findViewById(R.id.textTitle);
        TextView author = row.findViewById(R.id.textAuthor);
        title.setText(book.title);
        author.setText(book.author);
        return row;
    }
}
