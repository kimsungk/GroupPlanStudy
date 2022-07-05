package com.example.groupplanstudy.ui.opengroup.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OpenGroupViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public OpenGroupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This 오픈그룹");
    }

    public LiveData<String> getText() {
        return mText;
    }
}