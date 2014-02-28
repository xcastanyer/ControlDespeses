package xco.sm.controldespeses;

import android.content.Context;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by XCO on 27/02/14.
 */
public class ServeiDespeses {


        List<Despesa> Despeses = new ArrayList<Despesa>();
        DatabaseManager db;
    Date PrimeraDataDate;
    Context Contexto;
    public ServeiDespeses(Context context)
    {

        Contexto = context;
        db = new DatabaseManager(context);
        Despeses = db.getAllRows();
        PrimeraDataDate    = Despeses.get(Despeses.size()-1).getDataDate();


    }
    public void AfegirDespesa(String despesa, Double valor)
    {

        db.insertar(new Despesa(despesa, valor));
        Despeses.add(new Despesa(despesa, valor));
    }
    public void EliminarDespesa(Integer id)
    {
        db.eliminar(id);
    }
    public  String GetTotalDespesesAvuiStr()
    {
        DecimalFormat formato = new DecimalFormat("#,##0.00");
        return formato.format(GetTotalDespesesAvui());

    }
    private Double GetTotalDespesesAvui()
    {
        Double totalHoy = 0d;
        for(Object o: Despeses){
            if (isToday(((Despesa)o).getDataDate()))            {
                totalHoy+=((Despesa)o).getImport();
            }

        }
        return totalHoy;
    }
    public String GetMitjaDespeses(TipusMitja tipo, String strformato)
    {
        DecimalFormat formato = new DecimalFormat(strformato);
        return formato.format(GetDespesaMitja(tipo));
    }
    public String GetMitjaDespeses(TipusMitja tipo)
    {
        return GetMitjaDespeses(tipo, "#,##0.000");

    }
    private Double GetDespesaMitja(TipusMitja tipo)
    {
        Double total = 0d;

        List<DespesesDia> desDia = new ArrayList<DespesesDia>();

        for(Object o: Despeses){
            Date d =((Despesa)o).getDataDate();

            AgregarDespesesDia(((Despesa)o), desDia);

        }
        for(Object o: desDia){

            total+=((DespesesDia)o).getImport();
        }
        Calendar c  = Calendar.getInstance();

        Date Avui;
        Avui = c.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String AvuiStr =  formatter.format(Avui);

        double  diferenciaTiempo = DiferenciaFechasDias(Avui, PrimeraDataDate, tipo);

        if (diferenciaTiempo!=0)
            return total/diferenciaTiempo;
        else
            return 0d;
    }
    private void AgregarDespesesDia(Despesa des, List<DespesesDia> lista)
    {
        String dia = des.getDataFormateadoDia();

        //Buscar dia en lista
        for(Object o: lista){
            if (((DespesesDia)o).getDia().equals(dia))
            {
                ((DespesesDia)o).AgregarImporte(des.getImport());
                return;
            }

        }
        lista.add(new DespesesDia(dia, des.getImport()));

    }


    public static boolean isToday(Date date) {
        if (date == null) return false;
        Calendar today = Calendar.getInstance();
        today.setTime(new Date());
        Calendar otherday = Calendar.getInstance();
        otherday.setTime(date);
        return otherday.get(Calendar.YEAR) == today.get(Calendar.YEAR)
                && otherday.get(Calendar.MONTH) == today.get(Calendar.MONTH)
                && otherday.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
    }

    public static double DiferenciaFechasDias(Date dinicio, Date dfinal, TipusMitja tipo){

        double milis1, milis2, diff;

        //INSTANCIA DEL CALENDARIO GREGORIANO
        Calendar cinicio = Calendar.getInstance();
        Calendar cfinal = Calendar.getInstance();

        //ESTABLECEMOS LA FECHA DEL CALENDARIO CON EL DATE GENERADO ANTERIORMENTE
        cinicio.setTime(dinicio);
        cfinal.setTime(dfinal);


        milis1 = cinicio.getTimeInMillis();

        milis2 = cfinal.getTimeInMillis();


        diff = milis2-milis1;


        // calcular la diferencia en segundos

        double diffSegundos =  Math.abs (diff / 1000);


        // calcular la diferencia en minutos

        double diffMinutos =  Math.abs (diff / (60 * 1000));


        double restominutos = diffMinutos%60;



        // calcular la diferencia en horas

        double diffHoras =   (diff / (60 * 60 * 1000));



        // calcular la diferencia en dias

        double diffdias = Math.abs ( diff / (24 * 60 * 60 * 1000) );

        if (tipo == TipusMitja.Any)
            return diffdias/365;
        if (tipo == TipusMitja.Mes)
            return diffdias/30;
        if (tipo == TipusMitja.Setmana)
            return diffdias/7;
        if (tipo == TipusMitja.Dia)
            return diffdias;
        if (tipo == TipusMitja.Hora)
            return diffMinutos/60;
        if (tipo == TipusMitja.Minut)
            return diffMinutos;

        return diffdias;
        /*String devolver = String.valueOf(diffdias);

        return devolver;*/
    }


    private class DespesesDia
    {
        String _dia;
        Double _TotalDia;
        public DespesesDia(String dia, Double importe)
        {
            _dia = dia;
            _TotalDia = importe;
        }
        void AgregarImporte(Double importe)
        {
            _TotalDia+=importe;

        }
        public String getDia()
        {
            return _dia;
        }
        public Double getImport()
        {
            return _TotalDia;
        }

    }
    public enum TipusMitja{Minut, Hora, Dia, Setmana, Mes, Any}

}
