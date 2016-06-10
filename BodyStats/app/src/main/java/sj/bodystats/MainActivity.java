package sj.bodystats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import sj.bodystats.Database.Repository;
import sj.bodystats.Model.WeightDate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //testInsertWeight();
        setupWeightsList();
    }

    private void setupWeightsList() {
        Repository repo = new Repository();
        List<WeightDate> weightDates = repo.getAllWeights();
        List<String> listItems = new ArrayList<>();

        for (WeightDate weightDate : weightDates) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = dateFormat.format(weightDate.getDate());
            listItems.add(weightDate.getWeight() + " KG [ " + dateString + " ]");
        }

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listItems);
        ListView listView = (ListView)findViewById(R.id.listWeights);
        listView.setAdapter(adapter);
    }

    private void testInsertWeight() {
        Repository repo = new Repository();
        repo.insertWeight(7609);
    }
}
