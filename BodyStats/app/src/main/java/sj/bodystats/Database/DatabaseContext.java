package sj.bodystats.Database;

import java.util.List;

import sj.bodystats.Model.WeightDate;

/**
 * Created by Corsair on 6-6-2016.
 */
public interface DatabaseContext {
    boolean insertWeight(int weight);

    List<WeightDate> getAllWeights();
}
