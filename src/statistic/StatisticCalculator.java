package statistic;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatisticCalculator {
    private int numbersCount;
    private final List<Sample> samples;
    private float min, max;
    private float hop;
    private int accuracy;

    StatisticCalculator(int samplesNumber, int accuracy, @NotNull List<Float> numbers) {
        this.accuracy = accuracy;
        this.numbersCount = numbers.size();
        this.min = Collections.min(numbers);
        this.max = Collections.max(numbers);
        this.hop = this.round((this.max - this.min) / samplesNumber);
        this.samples = this.generateSamples(numbers);
    }

    public float getMin() {
        return this.min;
    }

    public float getMax() {
        return this.max;
    }

    public float getHop() {
        return this.hop;
    }

    public List<Sample> getSamples() {
        return this.samples;
    }

    // Средняя выборки.
    public Solution calculateSampleMean() {
        StringBuilder formula = new StringBuilder("(");
        Float answer = 0F;

        int frequency; float averageValue;
        for (Sample sample : this.samples) {
            frequency = sample.getFrequency();
            averageValue = sample.getAverageValue();
            answer += frequency * averageValue;
            formula.append(String.valueOf(frequency))
                    .append("*")
                    .append(String.valueOf(averageValue)).append("+");
        }

        answer /= this.numbersCount;

        formula.deleteCharAt(formula.length() - 1);
        formula.append(")").append("/").append(this.numbersCount);

        return new Solution(formula.toString(), answer);
    }

    // Оценка математического ожидания.
    public Solution calculateMathExpectationEstimation() {
        StringBuilder formula = new StringBuilder("(");
        Float answer = 0F;

        float relativeFrequency, averageValue;
        for (Sample sample : this.samples) {
            relativeFrequency = sample.getRelativeFrequency();
            averageValue = sample.getAverageValue();
            answer += averageValue * relativeFrequency;
            formula.append(String.valueOf(averageValue))
                    .append("*")
                    .append(String.valueOf(relativeFrequency)).append("+");
        }

        formula.deleteCharAt(formula.length() - 1);
        formula.append(")");

        return new Solution(formula.toString(), answer);
    }

    // Оценка дисперсии.
    public Solution calculateVarianceEstimation(float mathExpectationEstimation) {
        StringBuilder formula = new StringBuilder("");
        Float answer = 0F;

        int samplesNumber = this.samples.size();
        formula.append(String.valueOf(samplesNumber)).append("/").append(samplesNumber-1).append("*(");

        float averageValue, relativeFrequency;
        for (Sample sample : this.samples) {
            relativeFrequency = sample.getRelativeFrequency();
            averageValue = sample.getAverageValue();
            answer += (float) Math.pow(averageValue - mathExpectationEstimation, 2) * relativeFrequency;
            formula.append("(")
                    .append(String.valueOf(averageValue))
                    .append("-")
                    .append(String.valueOf(mathExpectationEstimation))
                    .append(")^2")
                    .append("*").append(String.valueOf(relativeFrequency)).append("+");
        }

        float offset = (float) samplesNumber / ((float) samplesNumber - 1);
        answer *= offset;

        formula.deleteCharAt(formula.length() - 1);
        formula.append(")");

        return new Solution(formula.toString(), answer);
    }

    // Оценка среднеквадратического отклонения.
    public Solution calculateQuadraticDeviationEstimation(float varianceEstimation) {
        StringBuilder formula = new StringBuilder("");
        Float answer = (float) Math.sqrt(varianceEstimation);
        formula.append("sqrt(").append(String.valueOf(varianceEstimation)).append(")");
        return new Solution(formula.toString(), answer);
    }

    private List<Sample> generateSamples(List<Float> numbers) {
        int frequency;
        float nextBound;
        List<Sample> samples = new ArrayList<>();

        for (float i = this.min; i < this.max; i += this.hop) {
            i = this.round(i); // don't try to understand why does it needed here.
            nextBound = this.round(i + this.hop);
            frequency = this.getFrequency(i, nextBound, numbers);
            samples.add(new Sample(
                    i, nextBound,
                    frequency,
                    this.round((float) frequency / (float) numbers.size()),
                    this.round((i + nextBound) / 2)));
        }

        return samples;
    }

    private int getFrequency(float from, float to, List<Float> numbers) {
        int result = 0;
        for (float number : numbers) {
            if (from == this.min) {
                if (from <= number && number <= to) {
                    result++;
                }
            } else {
                if (from < number && number <= to) {
                    result++;
                }
            }
        }
        return result;
    }

    private float round(float number) {
        float coefficient = (float) Math.pow(10, this.accuracy);
        return Math.round(number * coefficient) / coefficient;
    }
}
