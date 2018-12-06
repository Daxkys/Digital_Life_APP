package example.digitallife.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Account.class}, version = 1)
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

    public static void destroyInstance() {
        instance = null;
    }
}
