package com.example.kwerema.concrete;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.parceler.Parcels;

import ViewModels.ConcreteModel;
import ViewModels.RectangularModelBase;
import ViewModels.RodModel;
import ViewModels.SteelModel;

/**
 * Created by kwerema on 2018-01-16.
 */

public class RectangularCrossSectionActivity extends Activity {

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rect_cross_section_main);
        //prepareDb();

        final DBHelper dbh = new DBHelper(this);
        Button next = findViewById(R.id.rectangular_check);
        next.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                RectangularModelBase userInput = new RectangularModelBase();

                EditText numD = (EditText) findViewById(R.id.modelD);
                double StirrupsDiameter = Double.parseDouble(numD.getText().toString());
                userInput.StirrupsDiameter = StirrupsDiameter/1000;
                EditText numH = (EditText) findViewById(R.id.modelH);
                double h = Double.parseDouble(numH.getText().toString());
                userInput.h = h/1000;
                EditText numB = (EditText) findViewById(R.id.modelB);
                double b = Double.parseDouble(numB.getText().toString());
                userInput.b = b/1000;
                EditText numMsd = (EditText) findViewById(R.id.modelMsd);
                double Msd = Double.parseDouble(numMsd.getText().toString());
                userInput.Msd = Msd;
                Spinner concrete = (Spinner) findViewById(R.id.concrete_classes);
                String concreteClassName = concrete.getSelectedItem().toString();
                ConcreteModel model = dbh.getConcreteModelByName(concreteClassName);
                userInput.fcd = model.fcd;
                userInput.fctm = model.fctm;
                Spinner steel = (Spinner) findViewById(R.id.steel_classes);
                String steelClassName = steel.getSelectedItem().toString();
                steelClassName = steelClassName.substring(0, steelClassName.indexOf(" "));
                SteelModel Smodel = dbh.getSteelModelByName(steelClassName);
                userInput.fyd = Smodel.fyd;
                userInput.fyk = Smodel.fyk;
                userInput.isValid = true;

                Spinner rod = (Spinner) findViewById(R.id.rod_classes);
                String rodClassName = rod.getSelectedItem().toString();
                RodModel Rmodel = dbh.getRodModelByName(rodClassName);
                userInput.RodSurface = Rmodel.surface;
                userInput.RodDiameter = Rmodel.diameter;

                Bundle bundle = new Bundle();
                bundle.putParcelable("rectangularModelBase", Parcels.wrap(userInput));

                Intent intent = new Intent(view.getContext(), RectangularCrossSectionResultActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                //ustawiamy pusty intent, żeby wrócić do głównej strony, zrobić tak ze strzałką <
                //setResult(RESULT_OK, intent);
                //finish();
            }
        });

        String[] concreteSpinnerList = dbh.getAllConcreteClassesSpinner();
        setSpinner(concreteSpinnerList, R.id.concrete_classes);

        String[] steelSpinnerList = dbh.getAllSteelClassesSpinner();
        setSpinner(steelSpinnerList, R.id.steel_classes);

        String[] rodSpinnerList = dbh.getAllRodClassesSpinner();
        setSpinner(rodSpinnerList, R.id.rod_classes);

    }

    private void setSpinner(String[] spinnerItemList, int id){
        Spinner spinnerById = (Spinner) findViewById(id);
        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(RectangularCrossSectionActivity.this,
                android.R.layout.simple_list_item_1, spinnerItemList);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerById.setAdapter(classAdapter);
    }

    private void prepareDb(){
        DBHelper dbh = new DBHelper(this);
        dbh.addConcreteClass("C12/15", "12", "1.6");
        dbh.addConcreteClass("C16/20", "16", "1.9");
        dbh.addConcreteClass("C20/25", "20", "2.2");
        dbh.addConcreteClass("C25/30", "25", "2.6");
        dbh.addConcreteClass("C30/37", "30", "2.9");
        dbh.addConcreteClass("C35/45", "35", "3.2");

        dbh.addSteelClass("A-0", "St0S-b", "5.5", "40", "220", "190");
        dbh.addSteelClass("A-I", "St3S-b", "6", "40", "240", "210");
        dbh.addSteelClass("A-II", "20GY-b", "6", "28", "355", "310");
        dbh.addSteelClass("A-III", "RB400", "6", "40", "400", "350");
        dbh.addSteelClass("A-IIIN", "RB500", "6", "40", "500", "420");

        dbh.addRodClass("6", "0.28");
        dbh.addRodClass("8", "0.50");
        dbh.addRodClass("10", "0.79");
        dbh.addRodClass("12", "1.13");
        dbh.addRodClass("14", "1.54");
        dbh.addRodClass("16", "2.01");
        dbh.addRodClass("18", "2.54");
        dbh.addRodClass("20", "3.14");
        dbh.addRodClass("22", "3.80");
        dbh.addRodClass("25", "4.91");
        dbh.addRodClass("32", "8.04");
    }
}
