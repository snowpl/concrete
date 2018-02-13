package ViewModels;

/**
 * Created by kwerema on 2018-01-17.
 */

public class RectangularModel extends RectangularModelBase {

    public RectangularModel(RectangularModelBase baseModel){
        this.b = baseModel.b;
        this.d = baseModel.d;
        this.fcd = baseModel.fcd;
        this.h = baseModel.h;
        this.fyd = baseModel.fyd;
        this.Msd = baseModel.Msd;
    }

    public double Sceff, Mieff, Es, MieffLim, Xeff, As1, Capacity, a1;
    public boolean isProjectedGood;
    public String message;
}
