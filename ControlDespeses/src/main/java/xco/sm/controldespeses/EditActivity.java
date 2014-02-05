package xco.sm.controldespeses;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class EditActivity extends ActionBarActivity {

    String ConceptoSeleccionado = "Comida";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit);
       // Integer identificador = 0;
        final TextView txtImport = (TextView)findViewById(R.id.etImport);
        final DatePicker dpFecha = (DatePicker) findViewById(R.id.dpFecha);
        //Recuperamos la información pasada en el intent
        Bundle bundle = this.getIntent().getExtras();
        final Integer identificador = bundle.getInt("ID");
        //Construimos el mensaje a mostrar

        Spinner listaConceptos = (Spinner)findViewById(R.id.spListaConceptosEdit);

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

        final DatabaseManager db;
        db = new DatabaseManager(this);
        Despesa despesa = db.getDespesa(bundle.getInt(("ID")));
        txtImport.setText(despesa.getImportStrEdit());
        SeleccionarElementoSpinner(despesa.getDespesa(),listaConceptos);
        dpFecha.updateDate(despesa.getAny(),despesa.getMes()-1,despesa.geDias());

        Button btGuardar = (Button)findViewById(R.id.btGuardar);

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int any =  dpFecha.getYear();
                int mes =  dpFecha.getMonth();
                int dia = dpFecha.getDayOfMonth();
                Calendar c  = Calendar.getInstance();
                Date _data;
                 c.set(any, mes, dia, 0, 0, 0);
                 _data = c.getTime();
                Double importe = Double.parseDouble(txtImport.getText().toString());

                Despesa d = new Despesa(identificador,ConceptoSeleccionado,importe,_data);
                db.actualizarDespesa(d);
            }
        });


    }
    private void SeleccionarElementoSpinner(String elemnto, Spinner lista)
    {
        String myString = elemnto; //the value you want the position for

        ArrayAdapter myAdap = (ArrayAdapter) lista.getAdapter(); //cast to an ArrayAdapter

        int spinnerPosition = myAdap.getPosition(myString);

//set the default according to value
        lista.setSelection(spinnerPosition);
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_edit, container, false);
            return rootView;
        }
    }

}
