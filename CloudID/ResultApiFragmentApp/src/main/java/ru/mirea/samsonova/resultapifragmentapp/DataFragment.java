package ru.mirea.samsonova.resultapifragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DataFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        view.findViewById(R.id.buttonOpenBottomSheet).setOnClickListener(click -> {
            EditText edit = view.findViewById(R.id.editTextInfo);
            String text = edit.getText() == null ? "" : edit.getText().toString();
            Bundle bundle = new Bundle();
            bundle.putString("key", text);
            getChildFragmentManager().setFragmentResult("requestKey", bundle);
            BottomSheetFragment sheet = new BottomSheetFragment();
            sheet.show(getChildFragmentManager(), "ModalBottomSheet");
        });
        return view;
    }
}
