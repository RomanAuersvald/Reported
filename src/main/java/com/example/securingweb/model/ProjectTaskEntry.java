package com.example.securingweb.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ProjectTaskEntry implements Comparable<ProjectTaskEntry> {
    private LocalDateTime startDate;

    public LocalDateTime getStartDate() {
        return startDate;
    }

    private LocalDateTime endDate;

    public Long returnTaskEntryDuration() {
        if (endDate != null){
            long diff = ChronoUnit.SECONDS.between(startDate, endDate);
            return diff;
        }else{
            return null;
        }

    }

    public ProjectTaskEntry() {
        this.startDate = LocalDateTime.now();
        this.endDate = null;
    }
    public ProjectTaskEntry(LocalDateTime startDate, LocalDateTime endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void endEntry() {
        this.endDate = LocalDateTime.now();
    }

    @Override
    public int compareTo(ProjectTaskEntry o) {
        return this.startDate.compareTo(o.startDate);
    }
}
