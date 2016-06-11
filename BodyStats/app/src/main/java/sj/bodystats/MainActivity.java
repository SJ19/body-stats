package sj.bodystats;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sj.bodystats.Database.Repository;
import sj.bodystats.Model.WeightDate;

public class MainActivity extends AppCompatActivity {

    private Repository repository = new Repository(this);

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
        Repository repo = new Repository(this);
        List<WeightDate> weightDates = repo.getAllWeights();
        List<String> listItems = new ArrayList<>();

        // The actual weightdates.
        final List<WeightDate> listItemWeightDates = new ArrayList<>();

        for (WeightDate weightDate : weightDates) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = dateFormat.format(weightDate.getDate());
            listItems.add(weightDate.getWeight() + " KG [ " + dateString + " ]");
            listItemWeightDates.add(weightDate);
        }
        ListAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_rows, listItems);

        final ListView listView = (ListView) findViewById(R.id.listWeights);
        listView.setAdapter(adapter);
        listView.setSelection(adapter.getCount() - 1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // Confirmation snackbar with undo button.
                Snackbar snackbar = Snackbar.make(view, "Delete weight?", Snackbar.LENGTH_LONG);

                // Undo insert, so delete from db again.
                snackbar.setAction("Confirm", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        WeightDate weightDate = listItemWeightDates.get(position);
                        int id = weightDate.getId();
                        repository.deleteWeight(id);
                        updateWeightsList();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String dateString = dateFormat.format(weightDate.getDate());
                        Snackbar snackbar = Snackbar.make(v, "Deleted " + weightDate.getWeight() + " on " + dateString, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
                snackbar.show();
            }
        });
    }

    private void setupEditWeight() {
        final EditText editWeight = (EditText) findViewById(R.id.editWeight);
        editWeight.setGravity(Gravity.CENTER_HORIZONTAL);
        double lastInsertedWeight = new Repository(this).getLastInsertedWeight();
        editWeight.setText(Double.toString(lastInsertedWeight));
    }

    private void setupIncreaseButton() {
        final Button increaseWeight = (Button) findViewById(R.id.buttonIncreaseWeight);
        increaseWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editWeight = (EditText) findViewById(R.id.editWeight);
                double newValue = Double.parseDouble(editWeight.getText().toString()) + 0.1;
                editWeight.setText(String.format(Locale.ENGLISH, "%.1f", newValue));
            }
        });
    }

    private void setupDecreaseButton() {
        final Button decreaseWeight = (Button) findViewById(R.id.buttonDecreaseWeight);
        decreaseWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editWeight = (EditText) findViewById(R.id.editWeight);
                double newValue = Double.parseDouble(editWeight.getText().toString()) - 0.1;
                editWeight.setText(String.format(Locale.ENGLISH, "%.1f", newValue));
            }
        });
    }

    public void setupUploadButton() {
        final Button uploadButton = (Button) findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWeightInputEnabled(false);
                final EditText editWeight = (EditText) findViewById(R.id.editWeight);
                double weight = Double.parseDouble(editWeight.getText().toString());
                repository.insertWeight(weight);
                updateWeightsList();

                // Confirmation toast.
                /*Toast toast = Toast.makeText(getApplicationContext(), "Uploaded weight: " + weight, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 500);
                toast.show();*/

                // Confirmation snackbar with undo button.
                Snackbar snackbar = Snackbar.make(v, "Weight " + weight + " uploaded", Snackbar.LENGTH_LONG);

                // Undo insert, so delete from db again.
                snackbar.setAction("Undo", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Utility.println("DELETE LAST SUCCESS: " + repository.deleteLastInsertedWeight());
                        updateWeightsList();
                        Snackbar snackbar = Snackbar.make(v, "Weight removed", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
                snackbar.show();
            }
        });
    }

    public void setWeightInputEnabled(boolean enabled) {
        final Button uploadButton = (Button) findViewById(R.id.uploadButton);
        final Button increaseButton = (Button) findViewById(R.id.buttonIncreaseWeight);
        final Button decreaseButton = (Button) findViewById(R.id.buttonDecreaseWeight);
        final EditText editText = (EditText) findViewById(R.id.editWeight);

        uploadButton.setEnabled(enabled);
        increaseButton.setEnabled(enabled);
        decreaseButton.setEnabled(enabled);
        editText.setEnabled(enabled);
    }
}
