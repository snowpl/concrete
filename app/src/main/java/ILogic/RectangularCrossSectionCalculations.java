package ILogic;

import ViewModels.RectangularModel;
import ViewModels.RectangularModelBase;

/**
 * Created by kwerema on 2018-01-16.
 */

public class RectangularCrossSectionCalculations implements IRectangularCrossSection {

    public RectangularModel CalculateCrossSection(RectangularModelBase baseModel){
        RectangularModel model = new RectangularModel(baseModel);
        double SlMin = CheckSlMin(baseModel.RodDiameter);
        //Pobierać na podstawie klasy ekspozycji
        double CNom = 0.025;
        model.Sceff = CountSceff(model.Msd, (model.fcd/1.5), model.b, model.d);
        model.Mieff = CountMieff(model.Sceff);
        model.isSingleReinforced = IsMieffGreaterThanLim(model.Mieff, model.fyd, model.Es);
        if(model.isSingleReinforced){
            model.Xeff = CountXEff(model.Mieff, model.d);
            model.As1 = CountAs1((model.fcd/1.5), model.b, model.Xeff, model.fyd);
            int numberOfRods = CountRodNumber(model.As1, baseModel.RodSurface);
            double Asprov = numberOfRods * baseModel.RodSurface;
            model.As1 = Asprov;
            //zamiast baseModel.RodDiameter trzeba średnicę strzemion
            double Sl = (model.b - 2*CNom - baseModel.RodDiameter*0.001-numberOfRods*baseModel.RodDiameter*0.001)/(numberOfRods-1);
            if(Sl < SlMin) {
                model.message = "Uwaga: Pręty nie zmieszczą się w jednym rzędzie, obliczenia należy powtórzyć dla dwóch rzędów zbrojenia.";
                //Implementacja dla modelu podwójnie zbrojonego
                model.isProjectedGood = false;
                model.isSingleReinforced = false;
            }else{
            model.isProjectedGood = true;
            }
        }
        else{
            //Implementacja dla modelu podwójnie zbrojonego
            model.isProjectedGood = false;
            model.isSingleReinforced = false;
        }
        return model;
    }

    public double CheckSlMin(int rodDiam){
        return rodDiam > 20 ? rodDiam*0.001 : 0.02;
    }

    public double CountSceff(double Msd, double fcd, double b, double d) {
        double bottom = fcd*1000*b*d*d;
        double result = Msd/bottom;
        return result;
    }

    public double CountMieff(double Sceff) {
        return 1 - Math.sqrt(1-(2*Sceff));
    }

    public boolean IsMieffGreaterThanLim(double Mieff, double fyd, double Es) {
        double MieffLim = 0.8*(0.0035/(0.0035+(fyd/200000)));
        return Mieff < MieffLim;
    }

    public double CountXEff(double Mieff, double d){
        return Mieff*d;
    }

    public double CountAs1(double fcd, double b, double xeff, double fyd){
        return fcd*b*xeff*10000/fyd;
    }

    private int CountRodNumber(double As1, double rodSurface){
        return (int)Math.round((As1/rodSurface)+0.5);
    }
}