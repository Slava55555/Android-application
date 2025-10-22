package com.example.flightbooking;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner departureSpinner;
    private Spinner arrivalSpinner;
    private EditText departureDateEditText;
    private EditText returnDateEditText;
    private EditText adultsEditText;
    private EditText childrenEditText;
    private EditText infantsEditText;
    private Button searchButton;

    private Calendar departureCalendar;
    private Calendar returnCalendar;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupSpinners();
        setupDatePickers();
        setupButton();
    }

    private void initializeViews() {
        departureSpinner = findViewById(R.id.departureSpinner);
        arrivalSpinner = findViewById(R.id.arrivalSpinner);
        departureDateEditText = findViewById(R.id.departureDateEditText);
        returnDateEditText = findViewById(R.id.returnDateEditText);
        adultsEditText = findViewById(R.id.adultsEditText);
        childrenEditText = findViewById(R.id.childrenEditText);
        infantsEditText = findViewById(R.id.infantsEditText);
        searchButton = findViewById(R.id.searchButton);

        departureCalendar = Calendar.getInstance();
        returnCalendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    }

    private void setupSpinners() {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.cities,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        departureSpinner.setAdapter(adapter);
        arrivalSpinner.setAdapter(adapter);

        departureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        arrivalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupDatePickers() {

        departureDateEditText.setOnClickListener(v -> showDatePickerDialog(departureDateEditText, departureCalendar));

        returnDateEditText.setOnClickListener(v -> showDatePickerDialog(returnDateEditText, returnCalendar));
    }

    private void showDatePickerDialog(final EditText editText, final Calendar calendar) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    String selectedDate = dateFormatter.format(calendar.getTime());
                    editText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    private void setupButton() {
        searchButton.setOnClickListener(v -> showBookingSummary());
    }

    private void showBookingSummary() {
        String departureCity = departureSpinner.getSelectedItem().toString();
        String arrivalCity = arrivalSpinner.getSelectedItem().toString();
        String departureDate = departureDateEditText.getText().toString();
        String returnDate = returnDateEditText.getText().toString();
        String adults = adultsEditText.getText().toString();
        String children = childrenEditText.getText().toString();
        String infants = infantsEditText.getText().toString();

        String summary = "Данные бронирования:\n" +
                "Вылет: " + departureCity + "\n" +
                "Прилет: " + arrivalCity + "\n" +
                "Дата вылета: " + departureDate + "\n" +
                "Дата прилета: " + returnDate + "\n" +
                "Пассажиры: " + adults + " взр., " +
                children + " дет., " +
                infants + " млад.";

        Toast.makeText(this, summary, Toast.LENGTH_LONG).show();
    }
}