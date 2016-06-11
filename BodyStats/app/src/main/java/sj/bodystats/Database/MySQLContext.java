package sj.bodystats.Database;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import sj.bodystats.Model.WeightDate;
import sj.bodystats.Utility;

/**
 * Created by Corsair on 6-6-2016.
 */
public class MySQLContext implements DatabaseContext {
    private Activity activity;

    public MySQLContext(Activity activity) {
        this.activity = activity;
    }

    @Override
    public boolean insertWeight(double weight) {
        Utility.println("INSERTING WEIGHT: " + Double.toString(weight));
        String result = null;
        try {
            result = new DatabaseTask(activity, "INSERT INTO Weight(weight,date)VALUES(" + weight + ",convert_tz(now(),@@session.time_zone,'+02:00'))", QueryTypes.EXECUTE, true).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return (result == "1" ? true : false);
    }

    @Override
    public List<WeightDate> getAllWeights() {
        List<WeightDate> weightDates = new ArrayList<>();
        try {
            String result = new DatabaseTask(activity, Queries.GET_ALL_WEIGHTS).execute().get();
            JSONArray jsonArray = parseJSON(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = Integer.parseInt(jsonObject.getString("id"));
                double weight = Double.parseDouble(jsonObject.getString("weight"));
                String dateString = jsonObject.getString("date");
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = format.parse(dateString);

                WeightDate weightDate = new WeightDate(weight, date);
                weightDates.add(weightDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weightDates;
    }

    @Override
    public double getLastInsertedWeight() {
        double weight = 0;
        try {
            String result = new DatabaseTask(activity, Queries.GET_LAST_INSERTED_WEIGHT).execute().get();
            Utility.println("LAST INSERTED WEIGHT STRING: " + result);
            JSONArray jsonArray = parseJSON(result);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            weight = Double.parseDouble(jsonObject.getString("weight"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weight;
    }

    @Override
    public boolean deleteLastInsertedWeight() {
        String result = null;
        try {
             result = new DatabaseTask(activity, Queries.DELETE_LAST_INSERTED_WEIGHT).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return (result == "1" ? true : false);
    }

    private JSONArray parseJSON(String jsonString) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
