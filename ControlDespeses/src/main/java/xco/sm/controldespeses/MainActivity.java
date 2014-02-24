package xco.sm.controldespeses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity {

    EditText importTxt;
    List<Despesa> Despeses = new ArrayList<Despesa>();
    ListView despesesList;
    DatabaseManager db;
    String ConceptoSeleccionado = "Comida";




        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseManager(this);
        Despeses = db.getAllRows();






        importTxt = (EditText) findViewById(R.id.txtImport);
        despesesList = (ListView)findViewById(R.id.listView);
        populateList();
        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);

        Spinner listaConceptos = (Spinner)findViewById(R.id.spLlistaConceptes);

        listaConceptos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ConceptoSeleccionado = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this, R.array.conceptos_array,android.R.layout.simple_list_item_1);
        listaConceptos.setAdapter(adaptador);

        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.afegirTab);
        tabSpec.setIndicator("Creador");
        tabHost.addTab(tabSpec);

         tabSpec = tabHost.newTabSpec("store");
        tabSpec.setContent(R.id.listTab);
        tabSpec.setIndicator("Llista");
        tabHost.addTab(tabSpec);

        final Button afgBtn = (Button) findViewById(R.id.btAfegir);
        final TextView importAvui = (TextView) findViewById(R.id.lblImportDespesaAvui);
        final TextView importMitj = (TextView)findViewById(R.id.lblImportDespesaMitja);

        afgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AfgDespesa(ConceptoSeleccionado, Double.parseDouble(importTxt.getText().toString()));
                populateList();

                Toast.makeText(getApplicationContext(), "La despesa ha estat registrada.!", Toast.LENGTH_SHORT).show();
                importTxt.setText("");
                importAvui.setText(GetTotalDespesesAvuiStr());
                importMitj.setText(GetMitjaDespeses());
            }
        });
        importTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                afgBtn.setEnabled(!importTxt.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        importAvui.setText(GetTotalDespesesAvuiStr());
        importMitj.setText(GetMitjaDespeses());

    }

    private void populateList(){
        ArrayAdapter<Despesa> adaptador = new DespesaListAdapter(this);
        despesesList.setAdapter(adaptador);
    }
    private void AfgDespesa(String despesa, Double valor){
        db.insertar(new Despesa(despesa, valor));
        Despeses.add(new Despesa(despesa, valor));

    }

    private String GetTotalDespesesAvuiStr()
    {
        DecimalFormat formato = new DecimalFormat("#,##0.00");
        return formato.format(GetTotalDespesesAvui());

    }
    public String GetMitjaDespeses()
    {
        DecimalFormat formato = new DecimalFormat("#,##0.00");
        return formato.format(GetDespesaMitja());
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
    private Double GetDespesaMitja()
    {
        Double total = 0d;
        Integer contador = 0;
        List<DespesesDia> desDia = new ArrayList<DespesesDia>();

        for(Object o: Despeses){
            Date d =((Despesa)o).getDataDate();

            AgregarDespesesDia(((Despesa)o), desDia);

        }
        for(Object o: desDia){
            contador++;
            total+=((DespesesDia)o).getImport();
        }
        Calendar c  = Calendar.getInstance();

        Date Avui;
        Avui = c.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String AvuiStr =  formatter.format(Avui);

        String PrimeraDataStr = Despeses.get(Despeses.size()-1).getDataFormateadoDia();

        long  dias = DiferenciaFechasDias(AvuiStr, PrimeraDataStr);
        contador = (int)dias;
        if (contador!=0)
            return total/contador;
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

    private class DespesaListAdapter extends ArrayAdapter<Despesa>{

        private Context _context;

        public DespesaListAdapter(Context context){
            super (context, R.layout.listview_item, Despeses  );
            _context = context;


        }



        @Override
        public View getView(int position, View view, ViewGroup parent){
            DespesaHolder holder = null;
            View row = view;
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            holder = new DespesaHolder();

            Despesa currentDespesa = Despeses.get(position);
            holder.Descripcion = (TextView)view.findViewById(R.id.Descripcion);
            holder.Descripcion.setText(currentDespesa.getDespesa());
            holder.Import = (TextView)view.findViewById(R.id.txtImport);
            holder.Import.setText(currentDespesa.getImportStr());
            holder.Data = (TextView)view.findViewById(R.id.txtData);
            holder.Data.setText(currentDespesa.getDataBDNewFormat());
            holder.id  =  currentDespesa.getId();
            holder.btEliminar = (ImageButton)view.findViewById(R.id.iBtnDelete);
            holder.view = view;
            setBtEliminarClickListener(holder);



            return view;

        }
        private void setBtEliminarClickListener(final DespesaHolder holder){

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =
                            new Intent(MainActivity.this, EditActivity.class);
                    //Creamos la información a pasar entre actividades
                    Bundle b = new Bundle();
                    b.putInt("ID", holder.id);

                    //Añadimos la información al intent
                    intent.putExtras(b);
                    startActivity(intent);

                }
            });


            holder.btEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder alert;

                    alert = new AlertDialog.Builder(_context);
                    // set title
                    alert.setTitle("Eliminar despesa");

                    // set dialog message
                    alert
                            .setMessage("Desitjes eliminar la despesa?")
                            .setCancelable(false)
                            .setPositiveButton("Sí",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, close
                                    // current activity
                                    Toast.makeText(getApplicationContext(), "S'ha eliminat el registre: " +  holder.id.toString(), Toast.LENGTH_SHORT).show();
                                    db.eliminar(holder.id);
                                    populateList();
                                }
                            })
                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    AlertDialog a = alert.create();
                    a.show();
                }
            });
        }
    }
    public static class DespesaHolder{
        Integer id;
        TextView Descripcion;
        TextView Import;
        TextView Data;
        ImageButton btEliminar;
        View view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public static long DiferenciaFechasDias(String vinicio, String vfinal){

        Date dinicio = null, dfinal = null;
        long milis1, milis2, diff;

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");

        try {
            // PARSEO STRING A DATE
            dinicio = sdf.parse(vinicio);
            dfinal = sdf.parse(vfinal);

        } catch (ParseException e) {

            System.out.println("Se ha producido un error en el parseo");
        }

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

        long diffSegundos =  Math.abs (diff / 1000);


        // calcular la diferencia en minutos

        long diffMinutos =  Math.abs (diff / (60 * 1000));


        long restominutos = diffMinutos%60;



        // calcular la diferencia en horas

        long diffHoras =   (diff / (60 * 60 * 1000));



        // calcular la diferencia en dias

        long diffdias = Math.abs ( diff / (24 * 60 * 60 * 1000) );


     /*
     System.out.println("En segundos: " + diffSegundos + " segundos.");

     System.out.println("En minutos: " + diffMinutos + " minutos.");

     System.out.println("En horas: " + diffHoras + " horas.");

     System.out.println("En dias: " + diffdias + " dias.");
     */

        return diffdias;
        /*String devolver = String.valueOf(diffdias);

        return devolver;*/
    }

}
