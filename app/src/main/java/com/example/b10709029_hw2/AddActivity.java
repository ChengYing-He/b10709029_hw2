package com.example.b10709029_hw2;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private Button btn_ok;
    private Button btn_cancel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mNewGuestNameEditText = (EditText) this.findViewById(R.id.person_name_edit_text);
        mNewPartySizeEditText = (EditText) this.findViewById(R.id.party_count_edit_text);
        btn_ok = (Button) this.findViewById(R.id.btn_ok_and_add);
        btn_cancel = (Button) this.findViewById(R.id.btn_cancel_and_return);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /**
     * This method is called when user clicks on the Add to waitlist button
     *
     * @param view The calling view (button)
     */
    public void addToWaitlist(View view) {
        // COMPLETED (9) First thing, check if any of the EditTexts are empty, return if so
        if (mNewGuestNameEditText.getText().length() == 0 ||
                mNewPartySizeEditText.getText().length() == 0) {
            return;
        }
        // COMPLETED (10) Create an integer to store the party size and initialize to 1
        //default party size to 1
        int partySize = 1;
        // COMPLETED (11) Use Integer.parseInt to parse mNewPartySizeEditText.getText to an integer
        try {
            //mNewPartyCountEditText inputType="number", so this should always work
            partySize = Integer.parseInt(mNewPartySizeEditText.getText().toString());
        } catch (NumberFormatException ex) {
            // COMPLETED (12) Make sure you surround the Integer.parseInt with a try catch and log any exception
            Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
        }
        addNewGuest(mNewGuestNameEditText.getText().toString(), partySize);
        MainActivity.mAdapter.swapCursor(getAllGuests());

        // COMPLETED (20) To make the UI look nice, call .getText().clear() on both EditTexts, also call clearFocus() on mNewPartySizeEditText
        //clear UI text fields
        mNewPartySizeEditText.clearFocus();
        mNewGuestNameEditText.getText().clear();
        mNewPartySizeEditText.getText().clear();
    }

    public long addNewGuest(String name, int partySize) {
        ContentValues cv = new ContentValues();
        cv.put(WaitlistContract.waitlistEntry.COLUMN_GUEST_NAME, name);
        cv.put(WaitlistContract.waitlistEntry.COLUMN_PARTY_SIZE, partySize);
        return MainActivity.mDb.insert(WaitlistContract.waitlistEntry.TABLE_NAME, null, cv);
    }
    private Cursor getAllGuests(){
        return MainActivity.mDb.query(WaitlistContract.waitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.waitlistEntry.COLUMN_TIMESTAMP);
    }
}
