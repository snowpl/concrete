package ViewModels;

/**
 * Created by kwerema on 2018-01-22.
 */

public class SteelModel {
    public String className;
    public String steelGrade;
    public double MinDiamOfRod;
    public double MaxDiamOfRod;
    public double fyk;
    public double fyd;


    public SteelModel(String className, String steelGrade, double minDiamOfRod, double maxDiamOfRod, double fyk, double fyd) {
        this.className = className;
        this.steelGrade = steelGrade;
        this.MinDiamOfRod = minDiamOfRod;
        this.MaxDiamOfRod = maxDiamOfRod;
        this.fyk = fyk;
        this.fyd = fyd;
    }
}
