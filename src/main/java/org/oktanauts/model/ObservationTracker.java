package org.oktanauts.model;

import java.sql.Timestamp;
import java.util.ArrayList;

class ObservationTracker {
    private Observation[] records;
    private String observationCode;
    private int maxNumOfRecords;
    private int count;

    public ObservationTracker(String observationCode, int numberOfRecords) {
        this.observationCode = observationCode;
        this.maxNumOfRecords = numberOfRecords;
        this.records = new Observation[maxNumOfRecords];
        this.count = 0;
    }

    /**
     * Adds the new latest measurement
     *
     * @param observation the latest measurement
     */
    public void addObservation(Observation observation, int position) {
        for (int i = this.count - 1; i > position; i--) {
            this.records[i] = this.records[i-1];
        }

        this.records[position] = observation;

        if (this.count < this.maxNumOfRecords) {
            this.count++;
        }
    }

    public String getObservationCode() {
        return this.observationCode;
    }

    public int getCount() {
        return this.count;
    }

    public Observation getLatest() {
        return this.records[0];
    }

    public Timestamp getLastUpdated() {
        if (this.count > 0) {
            return this.records[0].getTimestamp();
        }
        else {
            return null;
        }
    }

    public int getMaxNumOfRecords() {
        return this.maxNumOfRecords;
    }

}
