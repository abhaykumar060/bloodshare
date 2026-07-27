package com.example.bloodshare;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Update user data from SharedPreferences based on current UID
        TextView tvGreeting = findViewById(R.id.tvGreeting);
        TextView tvBloodGroup = findViewById(R.id.tvBloodGroup);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = (user != null) ? user.getUid() : "";

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = prefs.getString("userName_" + uid, "User");
        String bloodGroup = prefs.getString("bloodGroup_" + uid, "N/A");

        tvGreeting.setText(getString(R.string.greeting_format, name));
        tvBloodGroup.setText(bloodGroup);

        findViewById(R.id.cardRequestBlood).setOnClickListener(v ->
                startActivity(new Intent(this, Request_Blood_Activity.class)));

        findViewById(R.id.cardBecomeDonor).setOnClickListener(v ->
                Toast.makeText(this, "Become a Donor clicked", Toast.LENGTH_SHORT).show());

        findViewById(R.id.cardNearbyDonors).setOnClickListener(v ->
                Toast.makeText(this, "Nearby Donors clicked", Toast.LENGTH_SHORT).show());

        findViewById(R.id.cardBloodBanks).setOnClickListener(v ->
                Toast.makeText(this, "Blood Banks clicked", Toast.LENGTH_SHORT).show());

        findViewById(R.id.cardMyRequests).setOnClickListener(v ->
                startActivity(new Intent(this, MyRequestsActivity.class)));

        findViewById(R.id.cardMyProfile).setOnClickListener(v ->
                Toast.makeText(this, "My Profile clicked", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnEmergency).setOnClickListener(v ->
                Toast.makeText(this, "Emergency alert sent!", Toast.LENGTH_SHORT).show());

        findViewById(R.id.navHome).setOnClickListener(v -> { /* already here */ });
        findViewById(R.id.navDonors).setOnClickListener(v ->
                Toast.makeText(this, "Donors tab", Toast.LENGTH_SHORT).show());
        findViewById(R.id.navNotifications).setOnClickListener(v ->
                Toast.makeText(this, "Notifications tab", Toast.LENGTH_SHORT).show());
        findViewById(R.id.navProfile).setOnClickListener(v ->
                Toast.makeText(this, "Profile tab", Toast.LENGTH_SHORT).show());
        findViewById(R.id.fabDonate).setOnClickListener(v ->
                Toast.makeText(this, "Quick donate action", Toast.LENGTH_SHORT).show());
    }
}