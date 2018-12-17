package statistic;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class SampleView {
    private final SimpleFloatProperty lowerBound;
    private final SimpleFloatProperty higherBound;
    private final SimpleIntegerProperty frequency;
    private final SimpleFloatProperty relativeFrequency;
    private final SimpleFloatProperty averageValue;

    SampleView(float lowerBound, float higherBound, int frequency, float relativeFrequency, float averageValue) {
        this.lowerBound = new SimpleFloatProperty(lowerBound);
        this.higherBound = new SimpleFloatProperty(higherBound);
        this.frequency = new SimpleIntegerProperty(frequency);
        this.relativeFrequency = new SimpleFloatProperty(relativeFrequency);
        this.averageValue = new SimpleFloatProperty(averageValue);
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
