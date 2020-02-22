package com.readingbuddy.ui.books;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.readingbuddy.Book;
import com.readingbuddy.BookManager;
import com.readingbuddy.ui.closeup.Book_Closeup;
import com.readingbuddy.DrawerLocker;
import com.readingbuddy.R;
import com.readingbuddy.preferenceHandler;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class BooksFragment extends Fragment {

    public  BookManager bookManager;
    public List<Book> bookList;
    private GridView allBooksGridView;
    protected Context currentContext;

    private List<Book> selectedBooks = new ArrayList<Book>();
    private int count_delete_books = 0;

    List<String> bookStates;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View currentView = inflater.inflate(R.layout.fragment_books, container,false);
        allBooksGridView  = currentView.findViewById(R.id.gridViewBooks);
        FloatingActionButton addBtn = currentView.findViewById(R.id.floatingActionButtonAdd);
        FloatingActionButton addGoodreadsBtn = currentView.findViewById(R.id.floatingActionButtonAddGoodreads);

        bookStates = new ArrayList<String>();
        bookStates.add("To read");
        bookStates.add("Currently reading");
        bookStates.add("Finished");
        bookStates.add("DNF");

        currentContext = getActivity();

        bookManager = new BookManager();
        bookList = bookManager.getBooks(currentContext);

        final GridAdapter gridAdapter = new GridAdapter();
        allBooksGridView.setAdapter(gridAdapter);
        allBooksGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                preferenceHandler prefHandler  = new preferenceHandler(currentContext);
                Intent intent = new Intent(getContext(), Book_Closeup.class);
                intent.putExtra("Theme", prefHandler.getModePreference().getInt());
                intent.putExtra("Book", bookList.get(position).serialize());
                intent.putExtra("EditMode", false);

                startActivity(intent);
            }
        });

        allBooksGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        allBooksGridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Book b = bookList.get(position);

                if (checked)
                {
                    count_delete_books++;
                    selectedBooks.add(b);
                }
                else
                {
                    count_delete_books--;
                    selectedBooks.remove(b);
                }
                mode.setTitle(count_delete_books + " items selected");

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.deletebook_menu, menu);

                ((DrawerLocker) currentContext).setDrawerEnabled(false);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                count_delete_books = 0;
                selectedBooks.clear();
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.delete_button:
                        bookManager.removeBooks(currentContext, selectedBooks);
                        allBooksGridView.invalidateViews();
                        Toast.makeText(currentContext, "Deleted " + count_delete_books  + " books.", Toast.LENGTH_LONG).show();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                ((DrawerLocker) getActivity()).setDrawerEnabled(true);
                bookList = bookManager.getBooks(currentContext);
                allBooksGridView.invalidateViews();
            }
        });

        addBtn.setOnClickListener(new FloatingActionButton.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Book newBook = new Book("", new Random().nextLong() ,"", "", Book.State.TO_READ, 0, 0);

                BookManager bm = new BookManager();
                bm.addBook(currentContext, newBook);

                bookList.add(newBook);
                allBooksGridView.invalidateViews();

                preferenceHandler prefHandler  = new preferenceHandler(currentContext);
                Intent intent = new Intent(getContext(), Book_Closeup.class);
                intent.putExtra("Theme", prefHandler.getModePreference().getInt());
                intent.putExtra("Book", newBook.serialize());
                intent.putExtra("EditMode", true);
                startActivity(intent);

            }
        });
        addGoodreadsBtn.setOnClickListener(new FloatingActionButton.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Book dummyBook = new Book("Dummy Title2", new Random().nextLong(), "Dummy author2", "", Book.State.TO_READ, 800, 0);

                BookManager bm = new BookManager();
                bm.addBook(currentContext, dummyBook);

                bookManager.addBook(currentContext, dummyBook);
                bookList.add(dummyBook);
                allBooksGridView.invalidateViews();
            }
        });

        return currentView;
    }

    @Override
    public void onResume() {
        ((DrawerLocker) getActivity()).setDrawerEnabled(true);
        bookList = bookManager.getBooks(currentContext);
        allBooksGridView.invalidateViews();
        super.onResume();
    }

    private class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return bookList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //TODO this causes the new book to have the image of the first book. (not actually, just visually)
            //if (convertView == null)
            {
                LayoutInflater inflater = (LayoutInflater) currentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.books_grid_item, parent, false);
            }

            View view = convertView;

            ImageView bookImage = view.findViewById(R.id.books_image);
            TextView bookTitle = view.findViewById(R.id.books_title);
            TextView bookAuthor = view.findViewById(R.id.books_author);
            TextView bookStatus = view.findViewById(R.id.books_Status);
            RatingBar bookRating = view.findViewById(R.id.rating);
            Book currentBook = bookList.get(position);

            bookTitle.setText(currentBook.Title);
            bookAuthor.setText(currentBook.Author);
            bookStatus.setText(bookStates.get(currentBook.CurrentState.getInt()));
            bookRating.setRating(currentBook.Rating * bookRating.getStepSize());
            if (currentBook.CurrentState == Book.State.FINISHED ||
                currentBook.CurrentState == Book.State.DNF)
            {
                bookRating.setVisibility(View.VISIBLE);
            }
            else
            {
                bookRating.setVisibility(View.GONE);
            }

            if (currentBook.CoverPath.length() > 1)
            {
                File file = new File(URI.create(currentBook.CoverPath).getPath());
                if (file.exists()) {
                    bookImage.setImageURI(Uri.parse(currentBook.CoverPath));
                    bookImage.setColorFilter(Color.argb(0, 255, 255, 255));
                }
            }

            return view;
        }
    }
}

//TODO create sort functions for the bookList item. if sorted differently, refresh allbooksgrid/
    //sort bookList
    //allBooksGridView.invalidateViews();