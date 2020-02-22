package com.readingbuddy.ui.currentlyReading;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.readingbuddy.Book;
import com.readingbuddy.BookManager;
import com.readingbuddy.ui.closeup.Book_Closeup;
import com.readingbuddy.R;
import com.readingbuddy.preferenceHandler;

import java.io.File;
import java.net.URI;
import java.util.List;

public class CurrentlyReadingFragment extends Fragment {

    private BookManager bm;
    private List<Book> bookList;
    private Context currentContext;

    private ListView currentBooks;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View currentView = inflater.inflate(R.layout.fragment_currently_reading, container,false);
        currentBooks = currentView.findViewById(R.id.currently_reading_books);

        currentContext = getActivity();
        bm = new BookManager();
        bookList = bm.getStateBooks(currentContext, Book.State.CURRENTLY_READING);

        currentBooks.setAdapter(new currentlyReadingListAdapter());
        return currentView;
    }


    @Override
    public void onResume() {
        bookList = bm.getStateBooks(currentContext, Book.State.CURRENTLY_READING);
        currentBooks.invalidateViews();

        super.onResume();
    }


    class currentlyReadingListAdapter extends BaseAdapter
    {
        @Override
        public int getCount() {
            return bookList.size();
        }

        @Override
        public Object getItem(int position) {
            return bookList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.currently_reading_list_item,null);
            ImageView bookImage = view.findViewById(R.id.currently_reading_books_image);
            TextView bookTitle =  view.findViewById(R.id.currently_reading_books_title);
            TextView bookAuthor = view.findViewById(R.id.currently_reading_books_author);
            TextView bookTime =  view.findViewById(R.id.currently_reading_books_time);
            TextView bookStartDate = view.findViewById(R.id.currently_reading_books_startdate);
            TextView bookLastReadDate = view.findViewById(R.id.currently_reading_books_last_read_date);
            TextView bookPages = view.findViewById(R.id.currently_reading_books_pages);

            Book currentBook = bookList.get(position);
            String currentReadTime = currentBook.getTotalReadTime();

            bookTitle.setText(currentBook.Title);
            bookAuthor.setText(currentBook.Author);
            bookTime.setText(currentReadTime);
            bookStartDate.setText(currentBook.getStartDate());
            bookLastReadDate.setText(currentBook.getLastReadDate());
            bookPages.setText(currentBook.getPages() +  "/" + currentBook.TotalPages);

            if (currentBook.CoverPath.length() > 1)
            {
                File file = new File(URI.create(currentBook.CoverPath).getPath());
                if (file.exists()) {
                    bookImage.setImageURI(Uri.parse(currentBook.CoverPath));
                    bookImage.setColorFilter(Color.argb(0, 255, 255, 255));
                }
            }

            view.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    book_click_event(v, currentBook);
                }
            });

            return view;
        }

        public void book_click_event(View v, Book currentBook) {

            preferenceHandler prefHandler  = new preferenceHandler(currentContext);
            Intent intent = new Intent(getContext(), Book_Closeup.class);
            intent.putExtra("Theme", prefHandler.getModePreference().getInt());
            intent.putExtra("Book", currentBook.serialize());
            intent.putExtra("EditMode", false);
            startActivity(intent);
        }



    }


}
