package com.readingbuddy;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;

import static android.content.Context.MODE_PRIVATE;

public class BookManager {

    private static final String FILE_NAME = "Books.txt";
    private static final String FILE_NAME_TEMP = "newBooks.txt";

    public List<Book> getStateBooks(Context currentContext, Book.State CheckState)
    {
        List<Book> allBooks = new ArrayList<Book>();
        FileInputStream fis = null;
        try
        {
            fis = currentContext.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String BookStr;
            while ((BookStr = br.readLine()) != null)
            {
                Book currentBook = new Book(BookStr);
                if (currentBook.CurrentState == CheckState)
                {
                    allBooks.add(currentBook);
                }
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allBooks;
    }

    public List<Book> getBooks(Context currentContext)
    {
        List<Book> allBooks = new ArrayList<Book>();
        FileInputStream fis = null;
        try
        {
            fis = currentContext.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String BookStr;
            while ((BookStr = br.readLine()) != null)
            {
                Book currentBook = new Book(BookStr);
                allBooks.add(currentBook);
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allBooks;
    }


    public Book getBook(Context currentContext, long randomId)
    {
        FileInputStream fis = null;
        try
        {
            fis = currentContext.openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String BookStr;
            while ((BookStr = br.readLine()) != null)
            {
                if (BookStr.contains(String.valueOf(randomId)))
                {
                    return new Book(BookStr);
                }
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addBook(Context currentContext, Book bookToAdd)
    {
        String BookString = bookToAdd.serialize();
        BookString += System.lineSeparator();

        FileOutputStream fos = null;
        try
        {
            fos = currentContext.openFileOutput(FILE_NAME, Context.MODE_APPEND);
            fos.write(BookString.getBytes());

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void removeBooks(Context currentContext, List<Book> books)
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try
        {
            fis = currentContext.openFileInput(FILE_NAME);
            fos = currentContext.openFileOutput(FILE_NAME_TEMP, MODE_PRIVATE);

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String BookStr;
            while ((BookStr = br.readLine()) != null)
            {
                boolean deleteThisRow = false;
                for (int i = 0; i < books.size(); i++)
                {
                    Book deleteBook = books.get(i);
                    boolean isFound = BookStr.contains(String.valueOf(deleteBook.RandomId));
                    if (isFound)
                    {
                        deleteThisRow = true;

                        if (deleteBook.CoverPath != null && deleteBook.CoverPath.length() > 1)
                        {
                            File file = new File(URI.create(deleteBook.CoverPath).getPath());
                            file.delete();
                        }
                        break;
                    }
                }
                if (!deleteThisRow)
                {
                    String writeString = BookStr + System.lineSeparator();
                    fos.write(writeString.getBytes());
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        currentContext.deleteFile(FILE_NAME);
        moveFile(currentContext, FILE_NAME_TEMP, FILE_NAME);

    }

    public void editBook(Context currentContext, Book b)
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try
        {
            fis = currentContext.openFileInput(FILE_NAME);
            fos = currentContext.openFileOutput(FILE_NAME_TEMP, MODE_PRIVATE);

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String BookStr;
            while ((BookStr = br.readLine()) != null) {
                boolean isFound = BookStr.contains(String.valueOf(b.RandomId));
                if (isFound) {
                    String writeString = b.serialize() + System.lineSeparator();
                    fos.write(writeString.getBytes());
                } else {
                    String writeString = BookStr + System.lineSeparator();
                    fos.write(writeString.getBytes());
                }
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        currentContext.deleteFile(FILE_NAME);
        moveFile(currentContext, FILE_NAME_TEMP, FILE_NAME);
    }

    private void moveFile(Context currentContext, String fileName, String newFileName)
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try
        {
            fis = currentContext.openFileInput(fileName);
            fos = currentContext.openFileOutput(newFileName, MODE_PRIVATE);

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            String BookStr;
            while ((BookStr = br.readLine()) != null)
            {
                String writeString = BookStr + System.lineSeparator();
                fos.write(writeString.getBytes());
            }

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //public List<Book> getCurrentlyReading(Context currentContext)
    {
    //    return getWithState(currentContext, Book.State.CURRENTLY_READING);
    }

    //public List<Book> getWithRating(Context currentContext, int Rating)
    {
        //TODO: get where Currentstate == Book.State.FINISHED && Rating == Rating
    }

    //public List<Book> getWithState(Context currentContext, Book.State currentState)
    {
        //TODO: get where Currentstate == CurrentState
    }


}
