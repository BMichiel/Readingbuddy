package com.readingbuddy.ui.goals;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GoalsViewModel  extends ViewModel {
    private MutableLiveData<String> mText;

    public GoalsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Goal fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}

