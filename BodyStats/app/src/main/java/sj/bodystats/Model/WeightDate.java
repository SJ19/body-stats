package sj.bodystats.Model;

import java.util.Date;

/**
 * Created by Corsair on 9-6-2016.
 */
public class WeightDate {
    private double weight;
    private Date date;

    public WeightDate(double weight, Date date) {
        this.weight = weight;
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "weight: " + weight + ", date: " + date.toString();
    }
}
