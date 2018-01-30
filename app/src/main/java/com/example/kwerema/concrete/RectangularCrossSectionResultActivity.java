package com.example.kwerema.concrete;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

import org.parceler.Parcels;

import ILogic.RectangularCrossSectionCalculations;
import ViewModels.RectangularModel;
import ViewModels.RectangularModelBase;
import ViewModels.RectangularViewModel;

/**
 * Created by kwerema on 2018-01-17.
 */

public class RectangularCrossSectionResultActivity extends Activity {
    //@InjectExtra RectangularModelBase userInput;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rect_cross_section_result);
        //Dart.bind(this);
        RectangularModelBase rectangularUserInput = Parcels.unwrap(getIntent().getParcelableExtra("rectangularModelBase"));
        RectangularViewModel completedModel = countResults(rectangularUserInput);
        prepareModelForView(completedModel);
        TextView t = (TextView) findViewById(R.id.textView7);
        if(completedModel.singleReinforcedCalculations.isProjectedGood){
            t.setText("fine");
        }else{
            t.setText("NOT");
        }
    }

    public RectangularViewModel countResults(RectangularModelBase baseModel){
        RectangularCrossSectionCalculations calucaltions = new RectangularCrossSectionCalculations();
        RectangularViewModel calculatedModel = calucaltions.CalculateCrossSection(baseModel);
        return calculatedModel;
    }

    public void prepareModelForView(RectangularViewModel model){
        TextView t1 = (TextView) findViewById(R.id.textView8);
        TextView t2 = (TextView) findViewById(R.id.textView9);
        TextView t3 = (TextView) findViewById(R.id.textView10);
        TextView t4 = (TextView) findViewById(R.id.textView11);
        TextView t5 = (TextView) findViewById(R.id.textView12);
        if(model.isSingleReinforced){
            t1.append(" pojedyńczo zbrojony");
            t2.append(String.valueOf(model.singleReinforcedCalculations.As1));
        }else{
            t1.append(" podwójnie zbrojony");
        }
        t3.append(String.valueOf(model.singleReinforcedCalculations.Xeff));
        t4.append(String.valueOf(model.singleReinforcedCalculations.Mieff));
        t5.append(String.valueOf(model.singleReinforcedCalculations.Capacity));
    }

}
