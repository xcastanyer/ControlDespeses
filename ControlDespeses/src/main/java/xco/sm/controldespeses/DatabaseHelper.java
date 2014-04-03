package xco.sm.controldespeses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XCO on 29/01/14.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "AdministradorDespeses";
    private static  Context _context;
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        _context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){

       db.execSQL(DatabaseManager.CREATE_TABLE);
    }

    @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS Despeses" );
            onCreate(db);
    }
    public static void BD_backup() throws IOException {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        final String inFileName = "/data/data/xco.sm.controldespeses/databases/"+DATABASE_NAME;
        File dbFile = new File(inFileName);
        FileInputStream fis = null;

        fis = new FileInputStream(dbFile);
        String outFileName = DATABASE_NAME + "_" + timeStamp;

        File directorio =  Environment.getExternalStorageDirectory();
        File d = new File(directorio.getAbsolutePath(), outFileName );


        OutputStream output = new FileOutputStream(d);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();

        //

         fis.close();
        //

    }
    public static void BD_Restore() throws IOException {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());

        File directorio =  Environment.getExternalStorageDirectory();
        final String inFileName = DATABASE_NAME;
        File dbFile = new File(directorio, inFileName);
        FileInputStream fis = null;

        fis = new FileInputStream(dbFile);



        File d = new File("/data/data/xco.sm.controldespeses/databases/", DATABASE_NAME );


        OutputStream output = new FileOutputStream(d);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        fis.close();

    }

}


