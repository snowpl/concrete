package ViewModels;

/**
 * Created by kwerema on 2018-01-19.
 */

public class ConcreteModel {
    public double fcd;
    public double fctm;
    public Integer Id;
    public String ConcreteClass;

    public ConcreteModel(){}

    public ConcreteModel(Integer Id, double fcd, double fctm, String ConcreteClass){
        this.ConcreteClass = ConcreteClass;
        this.Id = Id;
        this.fcd = fcd;
        this.fctm = fctm;
    }
}
