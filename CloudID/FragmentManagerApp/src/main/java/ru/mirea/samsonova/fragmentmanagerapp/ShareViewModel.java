package ru.mirea.samsonova.fragmentmanagerapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShareViewModel extends ViewModel {
    private final MutableLiveData<Country> selected = new MutableLiveData<>();

    public void select(Country country) {
        selected.setValue(country);
    }

    public LiveData<Country> selected() {
        return selected;
    }
}
