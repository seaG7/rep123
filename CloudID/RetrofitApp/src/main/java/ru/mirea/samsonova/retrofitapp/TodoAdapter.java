package ru.mirea.samsonova.retrofitapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {
    private final List<Todo> todos = new ArrayList<>();
    private final ApiService api;

    public TodoAdapter(ApiService api) {
        this.api = api;
    }

    public void setItems(List<Todo> next) {
        todos.clear();
        if (next != null) {
            todos.addAll(next);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.title.setText(todo.getTitle());
        holder.done.setOnCheckedChangeListener(null);
        holder.done.setChecked(todo.isCompleted());
        Picasso.get()
                .load(todo.imageUrl())
                .resize(160, 160)
                .centerCrop()
                .placeholder(R.drawable.ph_photo)
                .error(R.drawable.err_photo)
                .into(holder.image);
        holder.done.setOnCheckedChangeListener((button, checked) -> sendUpdate(holder, todo, checked));
    }

    private void sendUpdate(TodoViewHolder holder, Todo todo, boolean checked) {
        boolean previous = !checked;
        todo.setCompleted(checked);
        api.updateTodo(todo.getId(), todo).enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(@NonNull Call<Todo> call, @NonNull Response<Todo> response) {
                if (response.isSuccessful()) {
                    return;
                }
                revert(holder, todo, previous);
            }

            @Override
            public void onFailure(@NonNull Call<Todo> call, @NonNull Throwable t) {
                revert(holder, todo, previous);
            }
        });
    }

    private void revert(TodoViewHolder holder, Todo todo, boolean previous) {
        todo.setCompleted(previous);
        int position = holder.getBindingAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            notifyItemChanged(position);
        }
        Toast.makeText(holder.itemView.getContext(), R.string.update_failed, Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }
}
