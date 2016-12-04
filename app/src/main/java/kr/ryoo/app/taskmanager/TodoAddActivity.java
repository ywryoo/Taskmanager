package kr.ryoo.app.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import kr.ryoo.app.taskmanager.Models.Task;

public class TodoAddActivity extends AppCompatActivity {
    Button button;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_add);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editText = (EditText) findViewById(R.id.text_to_add);
        button = (Button) findViewById(R.id.button_to_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().length() == 0) {
                    return;
                }

                DatabaseReference userRef = FirebaseUtil.getCurrentUserRef();
                assert userRef != null;

                DatabaseReference taskRef = userRef.child("tasks");
                String key = taskRef.push().getKey();

                Task task = new Task(editText.getText().toString(),false,System.currentTimeMillis()/1000);

                Map<String, Object> addValues = task.toMap();

                Map<String, Object> updates = new HashMap<>();
                updates.put(key,addValues);
                taskRef.updateChildren(updates);

                finish();


            }
        });
    }
}
