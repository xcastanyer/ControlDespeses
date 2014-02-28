package xco.sm.controldespeses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.Date;

public class MainActivity extends Activity {

    EditText importTxt;
    //List<Despesa> Despeses = new ArrayList<Despesa>();
    ListView despesesList;
    //DatabaseManager db;
    String ConceptoSeleccionado = "Comida";
    Date PrimeraDataDate;
    ServeiDespeses s;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        s = new ServeiDespeses(this);
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
        final TextView importMitjSetmana = (TextView)findViewById(R.id.lblImportDMSetmana);
        final TextView importMitjHORA = (TextView)findViewById(R.id.lblImportDMHora);
        final TextView importMitjMES = (TextView)findViewById(R.id.lblImportDMMes);
        final TextView importMitjANY = (TextView)findViewById(R.id.lblImportDMAny);

        afgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AfgDespesa(ConceptoSeleccionado, Double.parseDouble(importTxt.getText().toString()));
                populateList();

                Toast.makeText(getApplicationContext(), "La despesa ha estat registrada.!", Toast.LENGTH_SHORT).show();
                importTxt.setText("");
                importAvui.setText(s.GetTotalDespesesAvuiStr());
                importMitj.setText(s.GetMitjaDespeses(ServeiDespeses.TipusMitja.Dia));
                importMitjSetmana.setText(s.GetMitjaDespeses(ServeiDespeses.TipusMitja.Setmana));
                importMitjHORA.setText(s.GetMitjaDespeses(ServeiDespeses.TipusMitja.Hora));
                importMitjMES.setText(s.GetMitjaDespeses(ServeiDespeses.TipusMitja.Mes));
                importMitjANY.setText(s.GetMitjaDespeses(ServeiDespeses.TipusMitja.Any, "#,##0"));
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

        importAvui.setText(s.GetTotalDespesesAvuiStr());
        importMitj.setText(s.GetMitjaDespeses(ServeiDespeses.TipusMitja.Dia));
        importMitjSetmana.setText(s.GetMitjaDespeses(ServeiDespeses.TipusMitja.Setmana));
        importMitjHORA.setText(s.GetMitjaDespeses(ServeiDespeses.TipusMitja.Hora));
        importMitjMES.setText(s.GetMitjaDespeses(ServeiDespeses.TipusMitja.Mes));
            importMitjANY.setText(s.GetMitjaDespeses(ServeiDespeses.TipusMitja.Any, "#,##0"));
}

    private void populateList(){
        ArrayAdapter<Despesa> adaptador = new DespesaListAdapter(this);
        despesesList.setAdapter(adaptador);
    }
    private void AfgDespesa(String despesa, Double valor){

        s.AfegirDespesa(despesa, valor);
    }



    private class DespesaListAdapter extends ArrayAdapter<Despesa>{

        private Context _context;

        public DespesaListAdapter(Context context){
            super (context, R.layout.listview_item, s.Despeses  );
            _context = context;


        }



        @Override
        public View getView(int position, View view, ViewGroup parent){
            DespesaHolder holder = null;
            View row = view;
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listview_item, parent, false);

            holder = new DespesaHolder();

            Despesa currentDespesa = s.Despeses.get(position);
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
                                    s.EliminarDespesa(holder.id);
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


}
