package sj.bodystats.Model;

import java.util.Date;

/**
 * Created by Corsair on 9-6-2016.
 */
public class WeightDate {
    private int id;
    private double weight;
    private Date date;

    public WeightDate(int id, double weight, Date date) {
        this.id = id;
        this.weight = weight;
        this.date = date;
    }

    public int getId() {
        return id;
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
