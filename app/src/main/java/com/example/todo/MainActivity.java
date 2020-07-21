package com.example.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements ItemAdapter.ItemInteraction {
    public static final String KEY_ITEM_TEXT = "item_txt";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

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

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskItem = edItem.getText().toString();
                // Add item to the model
                items.add(taskItem);
                // Notify adapter that an item has been added
                itemAdapter.notifyItemInserted(items.size() - 1);
                edItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });
    }

    @Override
    public void todoClicked(String s, int position) {
        // display text to indicate the item that has been clicked
        Toast.makeText(this, s + " Clicked", Toast.LENGTH_SHORT).show();

        // create the new activity
        Intent i = new Intent(MainActivity.this, EditActivity.class);

        // pass data being edited
        i.putExtra(KEY_ITEM_TEXT, items.get(position));
        i.putExtra(KEY_ITEM_POSITION, position);

        // display the activity
        startActivityForResult(i, EDIT_TEXT_CODE);
    }

    // this function deletes items from the list
    @Override
    public void todoLongClick(int position) {
        items.remove(position);
        // notify adapter position of deleted item
        itemAdapter.notifyItemRemoved(position);

        Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
        saveItems();
    }

    // handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            // Retrieve the updated text value
            // Check for nullptr exception
            assert data != null;
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            // Extract original position of the edited item from position key
            int position = Objects.requireNonNull(data.getExtras()).getInt(KEY_ITEM_POSITION);

            // update the model at the right most position with new item text
            items.set(position, itemText);

            // notify adapter
            itemAdapter.notifyItemChanged(position);

            // persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
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