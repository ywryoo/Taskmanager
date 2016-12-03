package kr.ryoo.app.taskmanager;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import kr.ryoo.app.taskmanager.Models.Task;

/**
 * Created by ywryo on 2016-12-03.
 */

public class TodolistAdapter extends RecyclerView.Adapter<TodolistAdapter.TodoViewHolder> {
    List<Task> tasks;

    TodolistAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_todolist,viewGroup,false);
        TodolistAdapter.TodoViewHolder viewHolder = new TodoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodolistAdapter.TodoViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.taskName.setText(tasks.get(position).getTaskName());

    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView taskName;
        TodoViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_todolist);
            taskName = (TextView) itemView.findViewById(R.id.card_task_name);
        }
    }
}
