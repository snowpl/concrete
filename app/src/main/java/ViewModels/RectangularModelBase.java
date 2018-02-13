package ViewModels;

import org.parceler.Parcel;

/**
 * Created by kwerema on 2018-01-17.
 */

@Parcel
public class RectangularModelBase {
    public double b;
    public double h;
    public double d;
    public double fyd;
    public double fcd;
    public double fctm;
    public double fyk;
    public double Msd;
    public double StirrupsDiameter;
    public double RodSurface;
    public int RodDiameter;
    public boolean isValid;
    public double MinDiamOfRod;
    public double MaxDiamOfRod;

    public RectangularModelBase(){}

    public RectangularModelBase(double b, double h, double stirrupsDiameter, double Msd){
        this.h = h;
        this.b = b;
        this.StirrupsDiameter = stirrupsDiameter;
        this.Msd = Msd;
    }

    public RectangularModelBase(double b, double h, double stirrupsDiameter, double d, double fyd, double fctm, double fyk, double fcd, double Msd, double rodSurface, int rodDiamater, boolean isValid, double minDiam, double maxDiam){
        this.h = h;
        this.b = b;
        this.StirrupsDiameter = stirrupsDiameter;
        this.d = d;
        this.fctm = fctm;
        this.fyk = fyk;
        this.fyd = fyd;
        this.fcd = fcd;
        this.Msd = Msd;
        this.isValid = isValid;
        this.RodDiameter = rodDiamater;
        this.RodSurface = rodSurface;
        this.MinDiamOfRod = minDiam;
        this.MaxDiamOfRod = maxDiam;
    }
}
