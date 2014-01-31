package xco.sm.controldespeses;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import java.util.GregorianCalendar;
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
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        Calendar c  = Calendar.getInstance();
        _data = c.getTime();
        _id = 0;
    }
    public Despesa(int id, String despesa, Double valor, String fecha){
        _despesa = despesa;
        _valor = valor;
        _id = id;
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        try {

            _data =  formatter.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
    public String getDataFormateado()
    {
        if (_data == null) return "";
        SimpleDateFormat formatter = new SimpleDateFormat("EEE d MMM HH:mm:ss");
        return formatter.format(_data);
    }
    public String getDataBD()
    {
        //SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        if (_data == null) return "";
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
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
}
