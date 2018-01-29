package ILogic;

import ViewModels.RectangularModel;

/**
 * Created by kwerema on 2018-01-16.
 */

public interface IRectangularCrossSection {
    double CountSceff(double Msd, double fcd, double b, double d);
    double CountMieff(double Sceff);
    boolean IsMieffGreaterThanLim(double Mieff, double fyd, double Es);
    double CountXEff(double Mieff, double d);
    double CountAs1(double fcd, double b, double xeff, double fyd);
}
