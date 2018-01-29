package com.example.kwerema.concrete;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import ViewModels.ConcreteModel;
import ViewModels.RectangularModelBase;
import ViewModels.RodModel;
import ViewModels.SteelModel;

/**
 * Created by kwerema on 2018-01-19.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "concrete.db";
    public static final String CONCRETE_TABLE_NAME = "ConDurability";
    public static final String CONCRETE_COLUMN_ID = "Id";
    public static final String CONCRETE_COLUMN_CONCRETECLASS = "ConcreteClass";
    public static final String CONCRETE_COLUMN_FCD = "fck";
    public static final String CONCRETE_COLUMN_FCTM = "fctm";
    public static final String STEEL_TABLE_NAME = "SteelParams";
    public static final String STEEL_COLUMN_SteelClass = "SteelClass";
    public static final String STEEL_COLUMN_STEELGRADES = "SteelGrades";
    public static final String STEEL_COLUMN_MINDIAMETEROFROD = "MinDiamOfRod";
    public static final String STEEL_COLUMN_MAXDIAMETEROFROD = "MaxDiamOfRod";
    public static final String STEEL_COLUMN_FYK = "fyk";
    public static final String STEEL_COLUMN_FYD = "fyd";
    public static final String ROD_TABLE_NAME = "Rods";
    public static final String ROD_COLUMN_RODDIAMETER = "Diameter";
    public static final String ROD_COLUMN_SURFACE = "Surface";

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 8);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table "+CONCRETE_TABLE_NAME + " (Id integer primary key autoincrement, ConcreteClass text, fck real, fctm real)");
        db.execSQL("create table "+STEEL_TABLE_NAME + " (Id integer primary key autoincrement, SteelClass text, SteelGrades text, MinDiamOfRod real, MaxDiamOfRod real, fyk real, fyd real)");
        db.execSQL("create table " + ROD_TABLE_NAME + " (Id integer primary key autoincrement, Diameter real, Surface real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+CONCRETE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+STEEL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ROD_TABLE_NAME);
        onCreate(db);
    }

    public void addRodClass(String diameter, String surface){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROD_COLUMN_RODDIAMETER, diameter);
        values.put(ROD_COLUMN_SURFACE, surface);
        db.insertOrThrow(ROD_TABLE_NAME, null, values);
    }

    public void addConcreteClass(String className, String fck, String fctm){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CONCRETE_COLUMN_CONCRETECLASS, className);
        values.put(CONCRETE_COLUMN_FCD, fck);
        values.put(CONCRETE_COLUMN_FCTM, fctm);
        db.insertOrThrow(CONCRETE_TABLE_NAME, null, values);
    }

    public void addSteelClass(String className, String steelGrade, String MinDiamOfRod, String MaxDiamOfRod, String fyk, String fyd){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STEEL_COLUMN_SteelClass, className);
        values.put(STEEL_COLUMN_STEELGRADES, steelGrade);
        values.put(STEEL_COLUMN_MINDIAMETEROFROD, MinDiamOfRod);
        values.put(STEEL_COLUMN_MAXDIAMETEROFROD, MaxDiamOfRod);
        values.put(STEEL_COLUMN_FYD, fyd);
        values.put(STEEL_COLUMN_FYK, fyk);
        db.insertOrThrow(STEEL_TABLE_NAME, null, values);
    }

    public SteelModel getSteelModelByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + STEEL_TABLE_NAME + " WHERE " + STEEL_COLUMN_STEELGRADES + " LIKE '"+ name + "'";
        Cursor res = db.rawQuery(selectQuery, null);

        /*Cursor res = db.query(STEEL_TABLE_NAME,
                new String[]{
                STEEL_COLUMN_SteelClass,
                STEEL_COLUMN_STEELGRADES,
                STEEL_COLUMN_MINDIAMETEROFROD,
                STEEL_COLUMN_MAXDIAMETEROFROD,
                STEEL_COLUMN_FYK,
                STEEL_COLUMN_FYD},
                STEEL_COLUMN_STEELGRADES + "=?",
                new String[]{String.valueOf(name)}, null, null, null, null);*/
        if(res!=null)
            res.moveToFirst();

        SteelModel model = new SteelModel(res.getString(1), res.getString(2), Double.parseDouble(res.getString(3)),Double.parseDouble(res.getString(4)),
                Double.parseDouble(res.getString(5)) ,Double.parseDouble(res.getString(6)));

        return model;
    }

    public ConcreteModel getConcreteModelByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + CONCRETE_TABLE_NAME + " WHERE " + CONCRETE_COLUMN_CONCRETECLASS + " LIKE '"+ name + "'";
        Cursor res = db.rawQuery(selectQuery, null);
        //Cursor res = db.query(CONCRETE_TABLE_NAME, new String[]{CONCRETE_COLUMN_ID, CONCRETE_COLUMN_CONCRETECLASS, CONCRETE_COLUMN_FCD, CONCRETE_COLUMN_FCTM}, CONCRETE_COLUMN_CONCRETECLASS + " LIKE ",
          //      new String[]{String.valueOf(name)}, null, null, null, null);
        if(res!=null)
            res.moveToFirst();

        ConcreteModel model = new ConcreteModel(Integer.parseInt(res.getString(0)), Double.parseDouble(res.getString(2)), Double.parseDouble(res.getString(3)), res.getString(1));

        return model;
    }

    public RodModel getRodModelByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + ROD_TABLE_NAME + " WHERE " + ROD_COLUMN_RODDIAMETER + " = '"+ name + "'";
        Cursor res = db.rawQuery(selectQuery, null);
        if(res!=null)
            res.moveToFirst();

        RodModel model = new RodModel(Integer.parseInt(res.getString(1)), Double.parseDouble(res.getString(2)));

        return model;
    }

    public String[] getAllConcreteClassesSpinner(){
        ArrayList<String> concreteClassNames = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * FROM "+ CONCRETE_TABLE_NAME, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            concreteClassNames.add(res.getString(res.getColumnIndex(CONCRETE_COLUMN_CONCRETECLASS)));
            res.moveToNext();
        }

        String[] concreteSpinnersNames = new String[concreteClassNames.size()];
        concreteSpinnersNames = concreteClassNames.toArray(concreteSpinnersNames);

        return concreteSpinnersNames;
    }

    public String[] getAllSteelClassesSpinner(){
        ArrayList<String> steelClassNames = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * FROM "+ STEEL_TABLE_NAME, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            steelClassNames.add(res.getString(res.getColumnIndex(STEEL_COLUMN_STEELGRADES)) + " " + res.getString(res.getColumnIndex(STEEL_COLUMN_SteelClass)));
            res.moveToNext();
        }

        String[] steelSpinnersNames = new String[steelClassNames.size()];
        steelSpinnersNames = steelClassNames.toArray(steelSpinnersNames);

        return steelSpinnersNames;
    }

    public String[] getAllRodClassesSpinner(){
        ArrayList<String> rodClassNames = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("Select * FROM "+ ROD_TABLE_NAME, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            rodClassNames.add(res.getString(res.getColumnIndex(ROD_COLUMN_RODDIAMETER)));
            res.moveToNext();
        }

        String[] rodSpinnersNames = new String[rodClassNames.size()];
        rodSpinnersNames = rodClassNames.toArray(rodSpinnersNames);

        return rodSpinnersNames;
    }
}
