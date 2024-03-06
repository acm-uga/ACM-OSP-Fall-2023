package edu.uga.acm.osp.data.routeSchedule;

import java.util.Objects;

/**
 * Represents all the components of a "predefined" Session
 *
 * @param indicator    the "predefined session indicator," any lowercase letter
 * @param fullName     the full name of the predefined session (e.g. "Fall Semester")
 * @param encodedDates a {@code String} of encoded OperatingDates included in the predefined session
 * @see PredefinedSession
 * @see "README"
 */
final class PredefinedSessionTemplate {
    private final char indicator;
    private final String fullName;
    private final String encodedDates;

    PredefinedSessionTemplate(char indicator, String fullName, String encodedDates) {
        this.indicator = indicator;
        this.fullName = fullName;
        this.encodedDates = encodedDates;
    }

    public char indicator() {
        return indicator;
    }

    public String fullName() {
        return fullName;
    }

    public String encodedDates() {
        return encodedDates;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        PredefinedSessionTemplate that = (PredefinedSessionTemplate) obj;
        return this.indicator == that.indicator &&
                Objects.equals(this.fullName, that.fullName) &&
                Objects.equals(this.encodedDates, that.encodedDates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(indicator, fullName, encodedDates);
    }

    @Override
    public String toString() {
        return "PredefinedSessionTemplate[" +
                "indicator=" + indicator + ", " +
                "fullName=" + fullName + ", " +
                "encodedDates=" + encodedDates + ']';
    }
}