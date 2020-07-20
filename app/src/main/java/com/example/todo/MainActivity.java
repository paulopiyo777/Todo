package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemAdapter.ItemInteraction {

    private List<String> items;
    private Button btnAdd;
    private EditText edItem;
    private RecyclerView rvItems;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        edItem = findViewById(R.id.edItem);
        rvItems = findViewById(R.id.rvItems);

        itemAdapter = new ItemAdapter(this);

        loadItems();


        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setHasFixedSize(true);
        itemAdapter.setData(items);
        rvItems.setAdapter(itemAdapter);

//        final ItemsAdapter itemAdapter = new ItemsAdapter(items, onLongClickListener);
//        rvItems.setAdapter(itemAdapter);
//        rvItems.setLayoutManager(new LinearLayoutManager(this));
//
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskItem = edItem.getText().toString();
                // Add item to the model
                items.add(taskItem);
                // Notify adapter that an item has been added
                // itemAdapter.setData(items);
                itemAdapter.notifyItemInserted(items.size() - 1);
                edItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    @Override
    public void todoClicked(String s) {
        Toast.makeText(this, s + " Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void todoLongClick(int position) {
        items.remove(position);
        // notify adapter position of deleted item
        itemAdapter.notifyItemRemoved(position);
        Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
        saveItems();
    }

    private File getDataFile() {
        return new File(getFilesDir(), "data.txt");
    }

    // this function loads items by reading every line of the data file
    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    // this function saves items by writing them into the data file
    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }

}