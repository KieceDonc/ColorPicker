package com.vvdev.colorpicker.ui.ImportImgVid;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class ImportImgVidViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ImportImgVidViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}