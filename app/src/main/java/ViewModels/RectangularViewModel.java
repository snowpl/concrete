package ViewModels;

/**
 * Created by kwerema on 2018-01-25.
 */

public class RectangularViewModel {
    public RectangularModel singleReinforcedCalculations;
    public RectangularModel doubleReinforcedCalculations;
    public boolean isSingleReinforced;

    public RectangularViewModel(RectangularModel singleReinforcedCalculations, RectangularModel doubleReinforcedCalculations, boolean isSingleReinforced) {
        this.singleReinforcedCalculations = singleReinforcedCalculations;
        this.doubleReinforcedCalculations = doubleReinforcedCalculations;
        this.isSingleReinforced = isSingleReinforced;
    }
}
