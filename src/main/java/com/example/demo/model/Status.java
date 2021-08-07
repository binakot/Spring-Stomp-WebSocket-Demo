package com.example.demo.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.Objects;
import java.util.Random;

public final class Status {

    private Integer id;
    private Double value;
    private Instant timestamp;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(final Double value) {
        this.value = value;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Status that = (Status) o;
        return Objects.equals(id, that.id)
            && Objects.equals(value, that.value)
            && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, timestamp);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("id", id)
            .append("value", value)
            .append("timestamp", timestamp)
            .toString();
    }

    public static Status of(final Integer id) {
        final Status status = new Status();
        status.setId(id);
        status.setValue(new Random().nextDouble());
        status.setTimestamp(Instant.now());
        return status;
    }
}
