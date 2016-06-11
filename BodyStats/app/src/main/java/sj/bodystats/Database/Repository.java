package sj.bodystats.Database;

import java.util.List;

import sj.bodystats.Model.WeightDate;

/**
 * Created by Corsair on 6-6-2016.
 */
public class Repository {
    private DatabaseContext databaseContext;

    public Repository() {
        databaseContext = new MySQLContext();
    }

    public Repository(DatabaseContext databaseContext) {
        this.databaseContext = databaseContext;
    }

    public boolean insertWeight(double weight) {
        return databaseContext.insertWeight(weight);
    }

    public List<WeightDate> getAllWeights() {
        return databaseContext.getAllWeights();
    }

    public double getLastInsertedWeight() {
        return databaseContext.getLastInsertedWeight();
    }
}
