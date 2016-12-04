package kr.ryoo.app.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ryoo.app.taskmanager.Models.Task;

public class TodolistActivity extends AppCompatActivity implements TodolistAdapter.OnTodolistClickListener {
    BackPressCloseHandler backPressCloseHandler;
    List<Task> tasks = new ArrayList<>();
    private TodolistAdapter.OnTodolistClickListener mListener;
    private RecyclerView.Adapter adapter;

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
            startActivity(intent);
            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.todolist_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        mListener = this;
        adapter = new TodolistAdapter(tasks, mListener);
        recyclerView.setAdapter(adapter);

        DatabaseReference userRef = FirebaseUtil.getCurrentUserRef();
        assert userRef != null;
        DatabaseReference taskRef = userRef.child("tasks");

        Query taskQuery = taskRef.orderByChild("done").equalTo(false);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("FirebaseDB", "onChildAdded:" + dataSnapshot.getKey());
                Task task = dataSnapshot.getValue(Task.class);
                task.setKey(dataSnapshot.getKey());
                tasks.add(task);
                Collections.sort(tasks, new Comparator<Task>() {
                    @Override
                    public int compare(Task task, Task t1) {
                        return Long.compare(t1.getTimestamp(),task.getTimestamp());
                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("FirebaseDB", "onChildChanged:" + dataSnapshot.getKey());
                Task task = dataSnapshot.getValue(Task.class);
                task.setKey(dataSnapshot.getKey());
                int i = tasks.indexOf(task);
                if(i != -1) {
                    tasks.set(i,task);
                    Collections.sort(tasks, new Comparator<Task>() {
                        @Override
                        public int compare(Task task, Task t1) {
                            return Long.compare(t1.getTimestamp(),task.getTimestamp());
                        }
                    });
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("FirebaseDB", "onChildRemoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("FirebaseDB", "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FirebaseDB", "postComments:onCancelled", databaseError.toException());
                Toast.makeText(TodolistActivity.this, "Failed to load.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        taskQuery.addChildEventListener(childEventListener);
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

    public void onRemoveButton(int position) {
        //firebase
        DatabaseReference userRef = FirebaseUtil.getCurrentUserRef();
        assert userRef != null;
        DatabaseReference taskRef = userRef.child("tasks").child(tasks.get(position).getKey());

        Map<String, Object> updates = new HashMap<>();
        updates.put("done",true);

        taskRef.updateChildren(updates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(TodolistActivity.this,"삭제 완료", Toast.LENGTH_SHORT).show();
            }
        });
        tasks.remove(position);
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task task, Task t1) {
                return Long.compare(t1.getTimestamp(),task.getTimestamp());
            }
        });
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeRemoved(position,tasks.size());
    }

    public void onEditButton(int position) {
        Intent intent = new Intent(TodolistActivity.this, TodoEditActivity.class);
        intent.putExtra("name",tasks.get(position).getName());
        intent.putExtra("key",tasks.get(position).getKey());
        startActivity(intent);
    }

}
