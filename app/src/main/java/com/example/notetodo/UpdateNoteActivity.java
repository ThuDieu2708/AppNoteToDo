package com.example.notetodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UpdateNoteActivity extends AppCompatActivity {
    EditText title, content;
    Button updateNote;
    String id;
    TextView dataTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        title = (EditText) findViewById(R.id.editTitle);
        content = (EditText) findViewById(R.id.editContent);
        updateNote = (Button) findViewById(R.id.updateNote);

        Intent i1 = getIntent();
        title.setText(i1.getStringExtra("title"));
        content.setText(i1.getStringExtra("content"));
        id = i1.getStringExtra("id");
        dataTime = (TextView) findViewById(R.id.txtDatatime);
        dataTime.setText(
                //"EEEE, dd MMMM yyyy HH:mm a" == Wednesday, 16 December 2021 22:04 PM
                new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault()).format(new Date())
        );
        updateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(content.getText().toString())){
                    DatabaseNote databaseNote = new DatabaseNote(UpdateNoteActivity.this);
                    databaseNote.updateNote(title.getText().toString(), content.getText().toString(), id);
                    Intent i2 = new Intent(UpdateNoteActivity.this, MainActivity.class);
                    i2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i2);
                    finish();
                }
                else {
                    Toast.makeText(UpdateNoteActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}