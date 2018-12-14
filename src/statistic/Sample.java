package statistic;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Sample {
    private final SimpleFloatProperty lowerBound;
    private final SimpleFloatProperty higherBound;
    private final SimpleIntegerProperty frequency;
    private final SimpleFloatProperty relativeFrequency;
    private final SimpleFloatProperty averageValue;

    public Sample(Float lowerBound, Float higherBound, Integer frequency, Float relativeFrequency, Float averageValue) {
        this.lowerBound = new SimpleFloatProperty(lowerBound);
        this.higherBound = new SimpleFloatProperty(higherBound);
        this.frequency = new SimpleIntegerProperty(frequency);
        this.relativeFrequency = new SimpleFloatProperty(relativeFrequency);
        this.averageValue = new SimpleFloatProperty(averageValue);
    }

    public Float getLowerBound() {
        return lowerBound.get();
    }

    public void setLowerBound(Float lowerBound) {
        this.lowerBound.set(lowerBound);
    }

    public Float getHigherBound() {
        return higherBound.get();
    }

    public void setHigherBound(Float higherBound) {
        this.higherBound.set(higherBound);
    }

    public int getFrequency() {
        return frequency.get();
    }

    public void setFrequency(Integer frequency) {
        this.frequency.set(frequency);
    }

    public Float getRelativeFrequency() {
        return relativeFrequency.get();
    }

    public void setRelativeFrequency(Float relativeFrequency) {
        this.relativeFrequency.set(relativeFrequency);
    }

    public Float getAverageValue() {
        return averageValue.get();
    }

    public void setAverageValue(Float averageValue) {
        this.averageValue.set(averageValue);
    }
}
