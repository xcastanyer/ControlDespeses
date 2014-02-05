package xco.sm.controldespeses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XCO on 29/01/14.
 */
public class DatabaseManager {
    public static final String TABLE_DESPESES = "Despeses",
    KEY_ID = "ID",
    KEY_CONCEPTE = "Concepte",
    KEY_IMPORT = "Import",
    KEY_DATA = "Data";


    private DatabaseHelper helper;

    private SQLiteDatabase db;
   public static final  String CREATE_TABLE = "CREATE TABLE " + TABLE_DESPESES + " ( "
           + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
           + KEY_CONCEPTE + " TEXT , "
           + KEY_IMPORT + " NUMERIC ,  "
           + KEY_DATA + " TIMESTAMP NOT NULL DEFAULT current_timestamp )";

    public static final String[] ALL_KEYS = new String[] {KEY_ID, KEY_CONCEPTE, KEY_IMPORT, KEY_DATA};

    private Context _context;
    public DatabaseManager(Context context)
    {
        helper = new DatabaseHelper(context);

        db = helper.getWritableDatabase();
        _context = context;
    }
    public void insertar(Despesa despesa)
    {
        ContentValues valores = new ContentValues();
        valores.put(KEY_CONCEPTE, despesa.getDespesa());
        valores.put(KEY_IMPORT, despesa.getImport());
        valores.put(KEY_DATA, despesa.getDataBDNewFormat());
        if (db.insert(TABLE_DESPESES,null, valores) != -1)
            Toast.makeText(_context, "Insertado correctamente en base de datos", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(_context, "Error al insertar en base de datos", Toast.LENGTH_SHORT).show();

    }
    public void eliminar(Integer id){
        String[] v = new String[1];
        v[0] = id.toString();

        db.delete(TABLE_DESPESES,"ID=?",v);

    }
    public List<Despesa> getAllRows()
    {
        List<Despesa> lstD = new ArrayList<Despesa>();
        Cursor c = db.query(true, TABLE_DESPESES, ALL_KEYS, null,null,null,null,"ID DESC",null);
        if (c!=null){
            if (c.moveToFirst()){
                do{
                    Despesa d = new Despesa(c.getInt(0), c.getString(1),c.getDouble(2),c.getString(3));
                    lstD.add(d);
                }while(c.moveToNext());
            }

        }


        return lstD;
     }
    public Despesa getDespesa(Integer id)
    {
        Cursor c = db.rawQuery("SELECT " + KEY_ID + ", " +  KEY_CONCEPTE + ", " + KEY_IMPORT + ", " +  KEY_DATA + " FROM " + TABLE_DESPESES + " WHERE ID=" + id.toString(),null);
        if (c!=null){
            if (c.moveToFirst()){
                do{
                    Despesa d = new Despesa(c.getInt(0), c.getString(1),c.getDouble(2),c.getString(3));
                   return d;
                }while(c.moveToNext());
            }

        }
        return null;
    }

    public void actualizarFecha(Despesa despesa)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_DATA, despesa.getDataBDNewFormat());
        db.update(TABLE_DESPESES, args, "ID=" + despesa.getId(), null);
    }
    public void actualizarDespesa(Despesa despesa)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_DATA, despesa.getDataBDNewFormat());
        args.put(KEY_CONCEPTE, despesa.getDespesa());
        args.put(KEY_IMPORT, despesa.getImport());
        if (db.update(TABLE_DESPESES, args, "ID=" + despesa.getId(), null)!=-1)
        Toast.makeText(_context, "Actualizado correctamente en base de datos", Toast.LENGTH_SHORT).show();
            else
        Toast.makeText(_context, "Error al actualizar en base de datos", Toast.LENGTH_SHORT).show();

    }


}
