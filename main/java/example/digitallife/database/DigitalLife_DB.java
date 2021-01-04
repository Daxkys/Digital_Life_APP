package example.digitallife.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Account.class}, version = 1, exportSchema = false)
public abstract class DigitalLife_DB extends RoomDatabase {

    private static DigitalLife_DB instance;

    public abstract Account_DAO accountDAO();

    public static DigitalLife_DB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DigitalLife_DB.class, "digitalLife_db").allowMainThreadQueries().build();
        }
        return instance;
    }

    /*
    public static void destroyInstance() {
        instance = null;
    }
     */
}
