package com.example.b10709029_hw2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.*;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private TextView mTextView;
    public static GuestListAdapter mAdapter;
    public static SQLiteDatabase mDb;
    public static WaitlistDbHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTextView = findViewById(R.id.party_size_text_view);
        setUpSharedPreferences();

        RecyclerView waitlistRecyclerView;
        // Set local attributes to corresponding views
        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new WaitlistDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getAllGuests();
        // Create an adapter for that cursor to display the data
        mAdapter = new GuestListAdapter(this, cursor);

        // Link the adapter to the RecyclerView
        waitlistRecyclerView.setAdapter(mAdapter);



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                long id = (long) viewHolder.itemView.getTag();
                removeGuest(id);
                mAdapter.swapCursor(getAllGuests());
            }
        }).attachToRecyclerView(waitlistRecyclerView);
    }

    /**
     * Methods for setting up the menu
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our visualizer_menu layout to this menu */
        inflater.inflate(R.menu.mpage_menu, menu);
        /*Return ture so that the mainpage_menu will display on toolbar*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent startAddActivity = new Intent(this, AddActivity.class);
                startActivity(startAddActivity);
                return true;
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private Cursor getAllGuests(){
        return mDb.query(WaitlistContract.waitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.waitlistEntry.COLUMN_TIMESTAMP);
    }
    private boolean removeGuest(long id) {
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(WaitlistContract.waitlistEntry.TABLE_NAME,
                WaitlistContract.waitlistEntry._ID + "=" + id, null) > 0;
    }

    private void changeColor(String pref_color_value){
        Log.d("color_change", pref_color_value);
        /*if(pref_color_value=="red"){
            mTextView.setBackgroundResource(R.color.red);
        }else if(pref_color_value == "green"){
            mTextView.setBackgroundResource(R.color.green);
        }else{
            mTextView.setBackgroundResource(R.color.blue);
        }*/
    }
    private void loadColorFromPref(SharedPreferences sharedPreferences){
        Log.d("color_load",sharedPreferences.getString(getString(R.string.pref_color_key),getString(R.string.pref_color_blue_value)));
        changeColor(sharedPreferences.getString(getString(R.string.pref_color_key), getString(R.string.pref_color_blue_value)));
    }

    private void setUpSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        loadColorFromPref(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }




    // Updates the screen if the shared preferences change. This method is required when you make a
    // class implement OnSharedPreferenceChangedListener
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.pref_color_key))){
            loadColorFromPref(sharedPreferences);
        }
    }
}

