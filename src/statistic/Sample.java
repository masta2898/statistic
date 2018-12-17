package statistic;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class Sample {
    private List<Float> numbers = new LinkedList<Float>();

    Sample() {
    }

    void add(Float number) {
        this.numbers.add(number);
    }

    boolean isEmpty() {
        return this.numbers.isEmpty();
    }

    Float getMaxNumber() {
        return Collections.max(this.numbers);
    }

    Float getMinNumber() {
        return Collections.min(this.numbers);
    }

    Integer getNumberCount() {
        return this.numbers.size();
    }

    Float getRelativeFrequency(int baseSampleNumberCount) {
        return (float) this.getNumberCount() / (float) baseSampleNumberCount;
    }

    Float getAverageValue() {
        Float averageValue = 0F;
        for (Float number : this.numbers) {
            averageValue += number;
        }
        return averageValue / this.getNumberCount();
    }

    List<Sample> divideToParts(int partsNumber) {
        Collections.sort(this.numbers);

        List<Sample> parts = new LinkedList<>();
        Float max = this.getMaxNumber();
        Float min = this.getMinNumber();
        Float measurementScale = max - min;
        Float hop = measurementScale / partsNumber;

        for (float i = min; i < max; i += hop) {
            Sample sample = new Sample();
            for (Float number : this.numbers) {
                if (i == min) {
                    if (i <= number && number <= i + hop) {
                        sample.add(number);
                    }
                } else {
                    if (i < number && number <= i + hop) {
                        sample.add(number);
                    }
                }
            }
            if (!sample.isEmpty()) {
                parts.add(sample);
            }
        }

        return parts;
    }
}
