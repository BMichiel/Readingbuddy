package com.readingbuddy.ui.currentlyReading;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CurrentlyReadingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public CurrentlyReadingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}