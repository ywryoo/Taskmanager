package kr.ryoo.app.taskmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class TodoEditActivity extends AppCompatActivity {
    Button button;
    EditText editText;
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        editText = (EditText) findViewById(R.id.text_to_edit);
        button = (Button) findViewById(R.id.button_to_edit);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            if(bundle.getString("name") != null) {
                editText.setText(bundle.getString("name"));
            }
            key = bundle.getString("key");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().length() == 0) {
                    return;
                }

                DatabaseReference userRef = FirebaseUtil.getCurrentUserRef();
                assert userRef != null;

                Log.d("sdf",key);

                DatabaseReference taskRef = userRef.child("tasks").child(key);

                Map<String, Object> updates = new HashMap<>();
                updates.put("name",editText.getText().toString());
                taskRef.updateChildren(updates);
                finish();


            }
        });
    }
}
