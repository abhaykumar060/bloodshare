package com.example.bloodshare;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodshare.R;
import com.example.bloodshare.BloodRequest;

import java.util.List;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.RequestViewHolder> {

    public interface OnRequestClickListener {
        void onRequestClick(BloodRequest request);
    }

    private final Context context;
    private List<BloodRequest> requestList;
    private final OnRequestClickListener listener;

    public BloodRequestAdapter(Context context, List<BloodRequest> requestList, OnRequestClickListener listener) {
        this.context = context;
        this.requestList = requestList;
        this.listener = listener;
    }

    public void updateList(List<BloodRequest> newList) {
        this.requestList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_blood_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        BloodRequest item = requestList.get(position);

        holder.tvBloodGroupChip.setText(item.getBloodGroup());
        holder.tvPatientName.setText(item.getPatientName());
        holder.tvRequestId.setText("Request #" + item.getRequestId());
        holder.tvHospitalName.setText(item.getHospitalName());
        holder.tvUnits.setText(item.getUnitsNeeded() + (item.getUnitsNeeded() == 1 ? " Unit" : " Units"));
        holder.tvNeededBefore.setText("Before " + item.getNeededBeforeDate());
        holder.tvPostedTime.setText("Posted " + item.getPostedTimeAgo());

        bindStatus(holder, item.getStatus());
        bindUrgency(holder, item.getUrgency());
        bindProgress(holder, item.getProgressStep());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onRequestClick(item);
        });
    }

    private void bindStatus(RequestViewHolder holder, BloodRequest.Status status) {
        switch (status) {
            case PENDING:
                holder.tvStatus.setText("Pending");
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_pending);
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.status_pending_text));
                break;
            case APPROVED:
                holder.tvStatus.setText("Approved");
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_approved);
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.status_approved_text));
                break;
            case FULFILLED:
                holder.tvStatus.setText("Fulfilled");
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_fulfilled);
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.status_fulfilled_text));
                break;
            case REJECTED:
                holder.tvStatus.setText("Rejected");
                holder.tvStatus.setBackgroundResource(R.drawable.bg_status_rejected);
                holder.tvStatus.setTextColor(context.getResources().getColor(R.color.status_rejected_text));
                break;
        }
    }

    private void bindUrgency(RequestViewHolder holder, BloodRequest.Urgency urgency) {
        switch (urgency) {
            case CRITICAL:
                holder.tvUrgencyBadge.setText("🔴 Critical");
                break;
            case HIGH:
                holder.tvUrgencyBadge.setText("🟠 High");
                break;
            case MEDIUM:
                holder.tvUrgencyBadge.setText("🟡 Medium");
                break;
            case LOW:
                holder.tvUrgencyBadge.setText("🟢 Low");
                break;
        }
    }

    private void bindProgress(RequestViewHolder holder, int step) {
        int activeColor = R.drawable.bg_status_approved;
        int inactiveColor = R.color.field_border;

        holder.stepDotSubmitted.setBackgroundResource(step >= 1 ? activeColor : inactiveColor);
        holder.stepDotVerified.setBackgroundResource(step >= 2 ? activeColor : inactiveColor);
        holder.stepDotMatched.setBackgroundResource(step >= 3 ? activeColor : inactiveColor);
        holder.stepDotFulfilled.setBackgroundResource(step >= 4 ? activeColor : inactiveColor);
    }

    @Override
    public int getItemCount() {
        return requestList == null ? 0 : requestList.size();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView tvBloodGroupChip, tvPatientName, tvRequestId, tvStatus,
                tvHospitalName, tvUnits, tvNeededBefore, tvUrgencyBadge, tvPostedTime;
        View stepDotSubmitted, stepDotVerified, stepDotMatched, stepDotFulfilled;

        RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBloodGroupChip = itemView.findViewById(R.id.tvBloodGroupChip);
            tvPatientName = itemView.findViewById(R.id.tvPatientName);
            tvRequestId = itemView.findViewById(R.id.tvRequestId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            tvUnits = itemView.findViewById(R.id.tvUnits);
            tvNeededBefore = itemView.findViewById(R.id.tvNeededBefore);
            tvUrgencyBadge = itemView.findViewById(R.id.tvUrgencyBadge);
            tvPostedTime = itemView.findViewById(R.id.tvPostedTime);
            stepDotSubmitted = itemView.findViewById(R.id.stepDotSubmitted);
            stepDotVerified = itemView.findViewById(R.id.stepDotVerified);
            stepDotMatched = itemView.findViewById(R.id.stepDotMatched);
            stepDotFulfilled = itemView.findViewById(R.id.stepDotFulfilled);
        }
    }
}