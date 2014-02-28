package xco.sm.controldespeses;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by XCO on 29/01/14.
 */
public class Despesa {
    private Integer _id;
    private String _despesa;
    private Double _valor;
    private Date _data;

    public Despesa(String despesa, Double valor){
        _despesa = despesa;
        _valor = valor;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c  = Calendar.getInstance();
        _data = c.getTime();
       // c.set(114 + 1900, 1, 3, 0, 0, 0);
       // _data = c.getTime();
        _id = 0;
    }
    public Despesa(int id, String despesa, Double valor, String fecha){
        _despesa = despesa;
        _valor = valor;
        _id = id;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {

            _data =  formatter.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public Despesa(int id, String despesa, Double valor, Date fecha){
        _despesa = despesa;
        _valor = valor;
        _id = id;


            _data =  fecha;

    }
    public int getAny()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(_data);

        return calendar.get(Calendar.YEAR);
    }
    public int getMes(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(_data);

        return calendar.get(Calendar.MONTH) + 1;
    }
    public int geDias(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(_data);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    public String getDespesa()
    {
        return _despesa;

    }
    public Double getImport()
    {
        return _valor;
    }
    public String getImportStr()
    {
        DecimalFormat formato = new DecimalFormat("#,##0.00");
        return formato.format(getImport());
    }
    public String getImportStrEdit()
    {

        return getImport().toString();
    }
    public String getDataFormateado()
    {
        if (_data == null) return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(_data);
    }
    public String getDataFormateadoDia()
    {
        if (_data == null) return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(_data);
    }
    public String getDataBD()
    {
        //SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        if (_data == null) return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat formatter = new SimpleDateFormat("EEE d MMM HH:mm:ss");
        return formatter.format(_data);
    }
    public Date getDataDate()
    {
     return _data   ;

    }
    public Integer getId()
    {
        return _id;
    }

    public String getDataBDNewFormat() {
        if (_data == null) return "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //SimpleDateFormat formatter = new SimpleDateFormat("EEE d MMM HH:mm:ss");
        return formatter.format(_data);
    }
}
