package sj.bodystats.Database;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import sj.bodystats.Model.WeightDate;

/**
 * Created by Corsair on 6-6-2016.
 */
public class Repository {
    private DatabaseContext databaseContext;
    private Activity activity;

    public Repository(Activity activity) {
        databaseContext = new MySQLContext(activity);
        this.activity = activity;
    }

    public Repository(Activity activity, DatabaseContext databaseContext) {
        this.databaseContext = databaseContext;
        this.activity = activity;
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
