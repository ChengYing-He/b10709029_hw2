package com.example.b10709029_hw2;

import android.provider.BaseColumns;

public class WaitlistContract {
    //Create an inner class named WaitlistEntry class that implements the BaseColumns interface
    public static final class waitlistEntry implements BaseColumns{
        public static final String TABLE_NAME = "waitlist";
        public static final String COLUMN_GUEST_NAME = "guestName";
        public static final String COLUMN_PARTY_SIZE = "partySize";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
