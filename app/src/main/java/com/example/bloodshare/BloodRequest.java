package com.example.bloodshare;


public class BloodRequest {

    public enum Status { PENDING, APPROVED, FULFILLED, REJECTED }
    public enum Urgency { CRITICAL, HIGH, MEDIUM, LOW }

    private String requestId;
    private String patientName;
    private String bloodGroup;
    private String hospitalName;
    private int unitsNeeded;
    private String neededBeforeDate;
    private Status status;
    private Urgency urgency;
    private String postedTimeAgo;
    private int progressStep; // 1=Submitted, 2=Verified, 3=Matched, 4=Fulfilled

    public BloodRequest(String requestId, String patientName, String bloodGroup,
                        String hospitalName, int unitsNeeded, String neededBeforeDate,
                        Status status, Urgency urgency, String postedTimeAgo, int progressStep) {
        this.requestId = requestId;
        this.patientName = patientName;
        this.bloodGroup = bloodGroup;
        this.hospitalName = hospitalName;
        this.unitsNeeded = unitsNeeded;
        this.neededBeforeDate = neededBeforeDate;
        this.status = status;
        this.urgency = urgency;
        this.postedTimeAgo = postedTimeAgo;
        this.progressStep = progressStep;
    }

    public String getRequestId() { return requestId; }
    public String getPatientName() { return patientName; }
    public String getBloodGroup() { return bloodGroup; }
    public String getHospitalName() { return hospitalName; }
    public int getUnitsNeeded() { return unitsNeeded; }
    public String getNeededBeforeDate() { return neededBeforeDate; }
    public Status getStatus() { return status; }
    public Urgency getUrgency() { return urgency; }
    public String getPostedTimeAgo() { return postedTimeAgo; }
    public int getProgressStep() { return progressStep; }
}