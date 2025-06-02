package com.nhom22.studentmanagement.data.model;

import com.google.gson.annotations.SerializedName;

public class Grade {
    private Double process;
    private Double midterm;
    @SerializedName("final")
    private Double finalGrade;

    public Grade(Double process, Double midterm, Double finalGrade) {
        this.process = process;
        this.midterm = midterm;
        this.finalGrade = finalGrade;
    }

    public Grade(Grade other) {
        this.process = other.getProcess();
        this.midterm = other.getMidterm();
        this.finalGrade = other.getFinalGrade();
    }

    public Double getProcess() {
        return process;
    }

    public void setProcess(Double process) {
        this.process = process;
    }

    public  Double getMidterm() {
        return midterm;
    }

    public void setMidterm(Double midterm) {
        this.midterm = midterm;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(Double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public Double getAverageGrade() {
        return process * 0.2 + midterm * 0.3 + finalGrade * 0.5;
    }
}
