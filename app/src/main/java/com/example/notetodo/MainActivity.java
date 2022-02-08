package com.example.notetodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton actionButton;
    List<ModelNote> noteList;
    Adapter adapter;
    DatabaseNote dbNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.list_item);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.layoutMain);
        actionButton = (FloatingActionButton) findViewById(R.id.addfb);

        // khi nhấn vô 1 floatingActionButton nào sẽ chuyển tới trang để viết nội dung
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        noteList = new ArrayList<>();
        dbNote = new DatabaseNote(this);
        findAllNotesFromDB();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter( this, MainActivity.this, noteList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    private void findAllNotesFromDB() {
        Cursor cursor = dbNote.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No Data to show", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()){
                noteList.add(new ModelNote(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
            System.out.println(cursor.getCount());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.searchNote);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Notes Here");
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.deleteNote){
            deleteAll();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAll() {
        DatabaseNote databaseNote = new DatabaseNote(MainActivity.this);
        databaseNote.deleteAllNote();
        // phương pháp tạo lại hoạt động, tái tạo một hoạt động.
        recreate();
    }

    // vuốt để xóa note....
    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int index = viewHolder.getAdapterPosition();
            ModelNote noteItem = adapter.getList().get(index);
            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(coordinatorLayout,"Đã Xóa", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.restoreItem(noteItem, index);
                    recyclerView.scrollToPosition(index);
                }
            }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if(!(event == DISMISS_EVENT_ACTION)){
                        DatabaseNote db = new DatabaseNote(MainActivity.this);
                        db.deleteItem(noteItem.getId());
                    }
                }
            });
            snackbar.setActionTextColor(Color.rgb(50,205,50));
            snackbar.show();
        }
    };
}