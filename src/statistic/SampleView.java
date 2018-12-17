package statistic;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SampleView {
    private final SimpleFloatProperty lowerBound;
    private final SimpleFloatProperty higherBound;
    private final SimpleIntegerProperty frequency;
    private final SimpleFloatProperty relativeFrequency;
    private final SimpleFloatProperty averageValue;

    SampleView(Sample sample, int baseSampleNumberCount) {
        this.lowerBound = new SimpleFloatProperty(sample.getMinNumber());
        this.higherBound = new SimpleFloatProperty(sample.getMaxNumber());
        this.frequency = new SimpleIntegerProperty(sample.getNumberCount());
        this.relativeFrequency = new SimpleFloatProperty(sample.getRelativeFrequency(baseSampleNumberCount));
        this.averageValue = new SimpleFloatProperty(sample.getAverageValue());
    }

    public float getLowerBound() {
        return lowerBound.get();
    }

    public SimpleFloatProperty lowerBoundProperty() {
        return lowerBound;
    }

    public float getHigherBound() {
        return higherBound.get();
    }

    public SimpleFloatProperty higherBoundProperty() {
        return higherBound;
    }

    public int getFrequency() {
        return frequency.get();
    }

    public SimpleIntegerProperty frequencyProperty() {
        return frequency;
    }

    public float getRelativeFrequency() {
        return relativeFrequency.get();
    }

    public SimpleFloatProperty relativeFrequencyProperty() {
        return relativeFrequency;
    }

    public float getAverageValue() {
        return averageValue.get();
    }

    public SimpleFloatProperty averageValueProperty() {
        return averageValue;
    }
}
