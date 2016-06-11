package sj.bodystats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

        updateWeightsList();
        setupEditWeight();
        setupDecreaseButton();
        setupIncreaseButton();
        setupUploadButton();
    }

    private void updateWeightsList() {
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
        listView.setSelection(adapter.getCount() - 1);
    }

    private void setupEditWeight() {
        final EditText editWeight = (EditText)findViewById(R.id.editWeight);
        editWeight.setGravity(Gravity.CENTER_HORIZONTAL);
        double lastInsertedWeight = new Repository().getLastInsertedWeight();
        editWeight.setText(Double.toString(lastInsertedWeight));
    }

    private void setupIncreaseButton() {
        final Button increaseWeight = (Button) findViewById(R.id.buttonIncreaseWeight);
        increaseWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editWeight = (EditText)findViewById(R.id.editWeight);
                double newValue = Double.parseDouble(editWeight.getText().toString()) + 0.1;
                editWeight.setText(String.format("%.1f", newValue));
            }
        });
    }

    private void setupDecreaseButton() {
        final Button decreaseWeight = (Button) findViewById(R.id.buttonDecreaseWeight);
        decreaseWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editWeight = (EditText)findViewById(R.id.editWeight);
                double newValue = Double.parseDouble(editWeight.getText().toString()) - 0.1;
                editWeight.setText(String.format("%.1f", newValue));
            }
        });
    }

    public void setupUploadButton() {
        final Button uploadButton = (Button)findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setWeightInputEnabled(false);
                final EditText editWeight = (EditText) findViewById(R.id.editWeight);
                double weight = Double.parseDouble(editWeight.getText().toString());
                Repository repo = new Repository();
                repo.insertWeight(weight);
                updateWeightsList();

                // Confirmation toast.
                Toast toast = Toast.makeText(getApplicationContext(), "Uploaded weight: " + weight, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 500);
                toast.show();
            }
        });
    }

    public void setWeightInputEnabled(boolean enabled) {
        final Button uploadButton = (Button)findViewById(R.id.uploadButton);
        final Button increaseButton = (Button)findViewById(R.id.buttonIncreaseWeight);
        final Button decreaseButton = (Button)findViewById(R.id.buttonDecreaseWeight);
        final EditText editText = (EditText) findViewById(R.id.editWeight);

        uploadButton.setEnabled(enabled);
        increaseButton.setEnabled(enabled);
        decreaseButton.setEnabled(enabled);
        editText.setEnabled(enabled);
    }
}
