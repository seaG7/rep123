package ru.mirea.samsonova.fragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TasksFragment extends Fragment {
    private static final String[] TASKS = {
            "Разобрать жизненный цикл фрагмента",
            "Передать номер по списку аргументом",
            "Собрать список и карточку на одном экране",
            "Открыть шторку с текстом из другого фрагмента",
            "Показать каталог родов через ViewModel",
            "Вернуться с карточки кнопкой «Назад»",
            "Собрать экран профиля по данным входа"
    };
    private static final boolean[] DONE = {true, true, false, false, true, false, false};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        int number = requireArguments().getInt("my_number_student");
        TextView textNumber = view.findViewById(R.id.textNumber);
        textNumber.setText("Номер по списку: " + number);
        LinearLayout list = view.findViewById(R.id.taskList);
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        for (int index = 0; index < TASKS.length; index++) {
            View row = inflater.inflate(R.layout.item_task, list, false);
            CheckBox check = row.findViewById(R.id.checkTask);
            TextView title = row.findViewById(R.id.textTask);
            title.setText(TASKS[index]);
            check.setChecked(DONE[index]);
            title.setOnClickListener(v -> check.setChecked(!check.isChecked()));
            list.addView(row);
        }
    }
}
