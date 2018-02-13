package ILogic;

import ViewModels.RectangularModel;
import ViewModels.RectangularModelBase;
import ViewModels.RectangularViewModel;

import static java.lang.Math.round;

/**
 * Created by kwerema on 2018-01-16.
 */

public class RectangularCrossSectionCalculations implements IRectangularCrossSection {

    public RectangularViewModel CalculateCrossSection(RectangularModelBase baseModel){
        RectangularViewModel result = new RectangularViewModel();
        RectangularModel model = new RectangularModel(baseModel);
        //Dodać warunek na dg + delta dg
        double SlMin = CheckSlMin(baseModel.RodDiameter);
        //Pobierać na podstawie klasy ekspozycji
        double CNom = 0.025;
        //Położenie osi ciężkości zbrojenia głównego
        model.a1 = (CNom + baseModel.RodDiameter/2*0.001 + baseModel.StirrupsDiameter * 0.001);
        //Wysokość użyteczna przekroju
        model.d = model.h - model.a1;
        //Bezwymiarowa wartość momentu zginającego przekrój
        model.Sceff = CountSceff(model.Msd, (model.fcd/1.5), model.b, model.d);
        //Względna efektywna wysokość strefy ściskanej i porównanie z wartością graniczną
        model.Mieff = CountMieff(model.Sceff);
        result.isSingleReinforced = IsMieffGreaterThanLim(model.Mieff, model.fyd, model.Es);
        //Mieff < Mieff,lim
        if(result.isSingleReinforced){
            //Umowne położenie osi obojętnej przekroju
            model.Xeff = CountXEff(model.Mieff, model.d);
            //łączne pole przekroju podłużnego zbrojenia
            model.As1 = CountAs1((model.fcd/1.5), model.b, model.Xeff, model.fyd);
            //Rozmieszczenie prętów zbrojenia głównego w 1 rzędzie
            ////Liczba prętów
            int numberOfRods = CountRodNumber(model.As1, baseModel.RodSurface);
            double Asprov = numberOfRods * baseModel.RodSurface;
            //sprawdzenie czy zostało zapewnione minimalne normowe zbrojenie
            double AsMin = GetAsMin(model);
            model.As1 = Asprov;
            if(model.As1 < AsMin) {
                model.message  = "Minimalne zbrojenie nie jest zapewnione, proszę zmienić warunki zbrojenia";
                model.isProjectedGood = false;
                result.singleReinforcedCalculations = model;
                return result;
            }
            //rozmieszczenie prętów zbrojenia w jednym rzędzie
            double Sl = (model.b - 2*CNom - 2*baseModel.StirrupsDiameter-numberOfRods*baseModel.RodDiameter*0.001);
            Sl = Sl/(numberOfRods-1);
            //Sprawdź czy rozstaw między prętami będzie odpowiedni
            //SPRAWDZIC POPRAWNOSC SL I SLMIN!!!!
            if(Sl < SlMin) {
                model.message = "Uwaga: Pręty nie zmieszczą się w jednym rzędzie, obliczenia należy powtórzyć dla dwóch rzędów zbrojenia.";
                //Implementacja dla modelu podwójnie zbrojonego
                result.isSingleReinforced = false;
                result.singleReinforcedCalculations = model;
                result.doubleReinforcedCalculations = CalculateDoubleReinforcementCrossSection(model);
                return result;
            }else{
            //sprawdzamy nośność przekroju
                result.singleReinforcedCalculations = model;
                result = CheckBearingCapacity(result);
                return result;
            }
        }
        //Implementacja dla modelu podwójnie zbrojonego
        else{
            result.isSingleReinforced = false;
            result.doubleReinforcedCalculations = CalculateDoubleReinforcementCrossSection(model);
        }
        return result;
    }

    public RectangularViewModel CheckBearingCapacity(RectangularViewModel result){
        RectangularModel model = new RectangularModel(result.singleReinforcedCalculations);
        double xeff = CountRealXEff(model);
        double Mrd = CountMrd(model, xeff);
        model.Capacity = model.Msd / Mrd;
        //Zwróć model z zapasem nośnością
        if(model.Msd < Mrd){
            model.isProjectedGood = true;
            int capLimit = (int)round((1 - model.Capacity)*100);
            model.message = String.format("Przekrój zapropojektowany poprawnie z %d %% zapasem nośności.", capLimit);
        }else{
            //Zwróć model z błędem nośności
            int capLimit = (int) round((model.Capacity - 1) * 100);
            model.message = String.format("Warunek nośności nie jest spełniony. Nośność przekroczona o %d %%.", capLimit);
            model.isProjectedGood = false;
        }
        result.singleReinforcedCalculations = model;
        result.isSingleReinforced = true;
        return result;
    }

    public RectangularModel CalculateDoubleReinforcementCrossSection(RectangularModel model){
        RectangularModel doubleRModel = new RectangularModel(model);
        //Liczmy Mlin
        //Liczymy As1'
        //Liczymy DeltaM
        //Liczymy As2
        //Liczymy As1
        return doubleRModel;
    }

    public double CountMlim(double fcd, double b, double xefflim, double d){
        return fcd*b*xefflim*(d-0.5*xefflim);
    }

    public double CheckSlMin(int rodDiam) {
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
        return (int) round((As1/rodSurface)+0.5);
    }

    public double GetAsMin(RectangularModel model){
        double AsMin = model.As1;
        double asMin2 = 0.26*model.fctm/model.fyk * model.b * model.d;
        double asMin3 = 0.0013*model.b*model.d;
        AsMin = Math.max(asMin3, Math.max(AsMin, asMin2));
        return AsMin;
    }

    private double CountRealXEff(RectangularModel model){
        double xeff = model.fyd*model.As1/model.fcd/model.b/10000;
        double xeffLim = model.d*model.Mieff;
        xeff = xeff < xeffLim ? xeff : xeffLim;
        return xeff;
    }

    private double CountMrd(RectangularModel model, double xeff){
        return model.fcd*model.b*xeff*(model.d-0.5*xeff)*1000;
    }
}
