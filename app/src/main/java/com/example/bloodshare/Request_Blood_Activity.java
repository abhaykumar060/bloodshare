package com.example.bloodshare;

import android.app.DatePickerDialog;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Request_Blood_Activity extends AppCompatActivity {

    private EditText etPatientName, etAge, etHospitalName, etMedicalCondition,
            etLocation, etAdditionalNotes;
    private Spinner spinnerBloodGroup, spinnerUnits, spinnerUrgency;
    private TextView tvBloodGroup, tvUnitsNeeded, tvUrgency, tvNeededBefore,
            tvCharCount, tvFileName;
    private LinearLayout layoutNeededBefore, btnUploadCertificate, btnSubmitRequest,
            btnUseCurrentLocation, btnMyRequests;
    private ImageButton btnBack;
    private CheckBox cbConfirm;

    private Uri selectedCertificateUri = null;

    private final ActivityResultLauncher<String> filePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedCertificateUri = uri;
                    tvFileName.setText("File selected: " + getFileNameFromUri(uri));
                    tvFileName.setVisibility(View.VISIBLE);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);

        initViews();
        setupSpinners();
        setupListeners();
    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnMyRequests = findViewById(R.id.btnMyRequests);

        etPatientName = findViewById(R.id.etPatientName);
        etAge = findViewById(R.id.etAge);
        etHospitalName = findViewById(R.id.etHospitalName);
        etMedicalCondition = findViewById(R.id.etMedicalCondition);
        etLocation = findViewById(R.id.etLocation);
        etAdditionalNotes = findViewById(R.id.etAdditionalNotes);

        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup);
        spinnerUnits = findViewById(R.id.spinnerUnits);
        spinnerUrgency = findViewById(R.id.spinnerUrgency);

        tvBloodGroup = findViewById(R.id.tvBloodGroup);
        tvUnitsNeeded = findViewById(R.id.tvUnitsNeeded);
        tvUrgency = findViewById(R.id.tvUrgency);
        tvNeededBefore = findViewById(R.id.tvNeededBefore);
        tvCharCount = findViewById(R.id.tvCharCount);
        tvFileName = findViewById(R.id.tvFileName);

        layoutNeededBefore = findViewById(R.id.layoutNeededBefore);
        btnUploadCertificate = findViewById(R.id.btnUploadCertificate);
        btnSubmitRequest = findViewById(R.id.btnSubmitRequest);
        btnUseCurrentLocation = findViewById(R.id.btnUseCurrentLocation);

        cbConfirm = findViewById(R.id.cbConfirm);
    }

    private void setupSpinners() {
        String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        String[] units = {"1 Unit", "2 Units", "3 Units", "4 Units", "5+ Units"};
        String[] urgencyLevels = {"Critical", "High", "Medium", "Low"};

        ArrayAdapter<String> bgAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
        spinnerBloodGroup.setAdapter(bgAdapter);

        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        spinnerUnits.setAdapter(unitsAdapter);

        ArrayAdapter<String> urgencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, urgencyLevels);
        spinnerUrgency.setAdapter(urgencyAdapter);

        spinnerBloodGroup.setOnItemSelectedListener(new SimpleSelectListener(tvBloodGroup, bloodGroups));
        spinnerUnits.setOnItemSelectedListener(new SimpleSelectListener(tvUnitsNeeded, units));
        spinnerUrgency.setOnItemSelectedListener(new SimpleSelectListener(tvUrgency, urgencyLevels));
    }

    private class SimpleSelectListener implements android.widget.AdapterView.OnItemSelectedListener {
        private final TextView target;
        private final String[] values;

        SimpleSelectListener(TextView target, String[] values) {
            this.target = target;
            this.values = values;
        }

        @Override
        public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
            target.setText(values[position]);
            target.setTextColor(getResources().getColor(R.color.text_dark));
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) {}
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnMyRequests.setOnClickListener(v ->
                Toast.makeText(this, "Opening My Requests...", Toast.LENGTH_SHORT).show());

        layoutNeededBefore.setOnClickListener(v -> showDatePicker());

        btnUploadCertificate.setOnClickListener(v -> filePickerLauncher.launch("*/*"));

        btnUseCurrentLocation.setOnClickListener(v ->
                Toast.makeText(this, "Fetching current location...", Toast.LENGTH_SHORT).show());

        etAdditionalNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvCharCount.setText(s.length() + "/250");
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSubmitRequest.setOnClickListener(v -> validateAndSubmit());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                    tvNeededBefore.setText(date);
                    tvNeededBefore.setTextColor(getResources().getColor(R.color.text_dark));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    private String getFileNameFromUri(Uri uri) {
        String result = "certificate";
        try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                if (nameIndex >= 0) {
                    result = cursor.getString(nameIndex);
                }
            }
        } catch (Exception ignored) {}
        return result;
    }

    private void validateAndSubmit() {
        if (etPatientName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter patient name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etAge.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter age", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etHospitalName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter hospital name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvBloodGroup.getText().toString().equals("Select blood group")) {
            Toast.makeText(this, "Please select blood group", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvUnitsNeeded.getText().toString().equals("Select units")) {
            Toast.makeText(this, "Please select units needed", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvUrgency.getText().toString().equals("Select urgency")) {
            Toast.makeText(this, "Please select urgency level", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvNeededBefore.getText().toString().equals("Select date")) {
            Toast.makeText(this, "Please select needed-before date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etLocation.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter location", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedCertificateUri == null) {
            Toast.makeText(this, "Please upload blood group certificate", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cbConfirm.isChecked()) {
            Toast.makeText(this, "Please confirm the certificate details", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Blood request submitted successfully!", Toast.LENGTH_LONG).show();
        finish();
    }
}