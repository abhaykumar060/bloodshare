package com.example.bloodshare;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.bloodshare.BloodRequestAdapter;
import com.example.bloodshare.BloodRequest;

import java.util.ArrayList;
import java.util.List;

public class MyRequestsActivity extends AppCompatActivity {

    private RecyclerView rvRequests;
    private SwipeRefreshLayout swipeRefresh;
    private LinearLayout layoutEmptyState, btnEmptyNewRequest;
    private ImageButton btnBack, btnAddRequest;
    private TextView chipAll, chipPending, chipApproved, chipFulfilled, chipRejected;

    private BloodRequestAdapter adapter;
    private List<BloodRequest> allRequests = new ArrayList<>();
    private TextView selectedChip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        initViews();
        loadDummyData();
        setupRecyclerView();
        setupListeners();
    }

    private void initViews() {
        rvRequests = findViewById(R.id.rvRequests);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        btnEmptyNewRequest = findViewById(R.id.btnEmptyNewRequest);
        btnBack = findViewById(R.id.btnBack);
        btnAddRequest = findViewById(R.id.btnAddRequest);

        chipAll = findViewById(R.id.chipAll);
        chipPending = findViewById(R.id.chipPending);
        chipApproved = findViewById(R.id.chipApproved);
        chipFulfilled = findViewById(R.id.chipFulfilled);
        chipRejected = findViewById(R.id.chipRejected);

        selectedChip = chipAll;
    }

    private void loadDummyData() {
        allRequests.add(new BloodRequest("BL10234", "Ramesh Kumar", "AB+",
                "City General Hospital", 2, "24/07", BloodRequest.Status.PENDING,
                BloodRequest.Urgency.CRITICAL, "2 hrs ago", 2));

        allRequests.add(new BloodRequest("BL10198", "Sunita Sharma", "O+",
                "Apollo Hospital", 1, "20/07", BloodRequest.Status.APPROVED,
                BloodRequest.Urgency.HIGH, "1 day ago", 3));

        allRequests.add(new BloodRequest("BL10150", "Arjun Mehta", "B-",
                "Fortis Hospital", 3, "18/07", BloodRequest.Status.FULFILLED,
                BloodRequest.Urgency.MEDIUM, "3 days ago", 4));

        allRequests.add(new BloodRequest("BL10122", "Priya Nair", "A+",
                "Manipal Hospital", 1, "15/07", BloodRequest.Status.REJECTED,
                BloodRequest.Urgency.LOW, "6 days ago", 1));
    }

    private void setupRecyclerView() {
        adapter = new BloodRequestAdapter(this, allRequests, request ->
                Toast.makeText(this, "Opening details for " + request.getRequestId(), Toast.LENGTH_SHORT).show());

        rvRequests.setLayoutManager(new LinearLayoutManager(this));
        rvRequests.setAdapter(adapter);

        toggleEmptyState(allRequests.isEmpty());
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnAddRequest.setOnClickListener(v ->
                startActivity(new Intent(this, Request_Blood_Activity.class)));

        btnEmptyNewRequest.setOnClickListener(v ->
                startActivity(new Intent(this, Request_Blood_Activity.class)));

        swipeRefresh.setOnRefreshListener(() ->
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    adapter.updateList(allRequests);
                    swipeRefresh.setRefreshing(false);
                }, 800));

        chipAll.setOnClickListener(v -> filterByStatus(null, chipAll));
        chipPending.setOnClickListener(v -> filterByStatus(BloodRequest.Status.PENDING, chipPending));
        chipApproved.setOnClickListener(v -> filterByStatus(BloodRequest.Status.APPROVED, chipApproved));
        chipFulfilled.setOnClickListener(v -> filterByStatus(BloodRequest.Status.FULFILLED, chipFulfilled));
        chipRejected.setOnClickListener(v -> filterByStatus(BloodRequest.Status.REJECTED, chipRejected));
    }

    private void filterByStatus(BloodRequest.Status status, TextView clickedChip) {
        resetChipStyles();
        clickedChip.setBackgroundResource(R.drawable.bg_filter_chip_selected);
        clickedChip.setTextColor(getResources().getColor(R.color.white));
        selectedChip = clickedChip;

        List<BloodRequest> filtered = new ArrayList<>();
        if (status == null) {
            filtered.addAll(allRequests);
        } else {
            for (BloodRequest r : allRequests) {
                if (r.getStatus() == status) filtered.add(r);
            }
        }

        adapter.updateList(filtered);
        toggleEmptyState(filtered.isEmpty());
    }

    private void resetChipStyles() {
        TextView[] chips = {chipAll, chipPending, chipApproved, chipFulfilled, chipRejected};
        for (TextView chip : chips) {
            chip.setBackgroundResource(R.drawable.bg_filter_chip_unselected);
            chip.setTextColor(getResources().getColor(R.color.text_secondary));
        }
    }

    private void toggleEmptyState(boolean isEmpty) {
        layoutEmptyState.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        rvRequests.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }
}