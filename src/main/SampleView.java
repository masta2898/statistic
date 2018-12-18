package main;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SampleView {
    private SimpleFloatProperty lowerBound = new SimpleFloatProperty();
    private SimpleFloatProperty higherBound = new SimpleFloatProperty();
    private SimpleIntegerProperty frequency = new SimpleIntegerProperty();
    private SimpleFloatProperty relativeFrequency = new SimpleFloatProperty();
    private SimpleFloatProperty averageValue = new SimpleFloatProperty();

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

    void setLowerBound(float lowerBound) {
        this.lowerBound.set(lowerBound);
    }

    void setHigherBound(float higherBound) {
        this.higherBound.set(higherBound);
    }

    void setFrequency(int frequency) {
        this.frequency.set(frequency);
    }

    void setRelativeFrequency(float relativeFrequency) {
        this.relativeFrequency.set(relativeFrequency);
    }

    void setAverageValue(float averageValue) {
        this.averageValue.set(averageValue);
    }
}
