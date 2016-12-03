package kr.ryoo.app.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.ryoo.app.taskmanager.Models.Task;

public class TodolistActivity extends AppCompatActivity implements TodolistAdapter.OnTodolistClickListener {
    BackPressCloseHandler backPressCloseHandler;
    List<Task> tasks = new ArrayList<>();
    private TodolistAdapter.OnTodolistClickListener mListener;
    private RecyclerView.Adapter adapter;
    static final int REQUEST_EDIT = 2222;
    static final int REQUEST_ADD = 1111;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(TodolistActivity.this, TodoAddActivity.class);
            startActivityForResult(intent,REQUEST_ADD);
            }
        });
        backPressCloseHandler = new BackPressCloseHandler(this);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.todolist_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        tasks.add(new Task("test", true,121212));
        tasks.add(new Task("222", true,1212142));
        tasks.add(new Task("3333", true,121243212));
        tasks.add(new Task("3333", true,123));
        tasks.add(new Task("3333", true,4444));
        tasks.add(new Task("3333", true,2222));
        mListener = this;
        adapter = new TodolistAdapter(tasks, mListener);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_todolist, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(TodolistActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mListener = this;
    }

    @Override
    public void onStop() {
        super.onStop();
        mListener = null;
    }

    public void onRemoveButton(String name,int position) {
        //firebase
        Toast.makeText(this,"REMOVE",Toast.LENGTH_SHORT).show();
        tasks.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeRemoved(position,tasks.size());
    }

    public void onEditButton(String name) {
        Intent intent = new Intent(TodolistActivity.this, TodoEditActivity.class);
        intent.putExtra("name",name);
        startActivityForResult(intent,REQUEST_EDIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode != RESULT_OK) {
            Toast.makeText(this,"에러",Toast.LENGTH_SHORT).show();
            return;
        }
        switch (requestCode) {
            case REQUEST_ADD:
                Toast.makeText(this,"REQUEST_ADD",Toast.LENGTH_SHORT).show();
                tasks.add(new Task("adf",true,1111));
                pos = tasks.size()-1;
                adapter.notifyItemInserted(pos);
                adapter.notifyItemRangeInserted(pos,tasks.size());
                break;
            case REQUEST_EDIT:
                Toast.makeText(this,"REQUEST_EDIT",Toast.LENGTH_SHORT).show();
                pos = tasks.size()-1;
                adapter.notifyItemChanged(pos);
                adapter.notifyItemRangeChanged(pos,tasks.size());
                break;

        }
    }


}
