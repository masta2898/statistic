package main;

import java.util.LinkedList;
import java.util.List;

public class SampleParametersCalculatorImpl implements SampleParametersCalculator {

    private Sample baseSample;
    private List<Sample> samples;

    SampleParametersCalculatorImpl() {
        this.baseSample = new Sample(new LinkedList<>());
        this.samples = baseSample.divideToParts(0);
    }

    @Override
    public void setBaseSample(Sample baseSample) {
        this.baseSample = baseSample;
    }

    @Override
    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    // Средняя выборки.
    @Override
    public Solution calculateSampleMean() {
        StringBuilder formula = new StringBuilder("(");
        float answer = 0F;

        int numberCount;
        float averageValue;
        for (Sample sample : samples) {
            numberCount = sample.getNumberCount();
            averageValue = sample.getAverageValue();
            answer += numberCount * averageValue;
            formula.append(numberCount).append("*").append(averageValue).append("+");
        }

        answer /= baseSample.getNumberCount();

        formula.deleteCharAt(formula.length() - 1);
        formula.append(")").append("/").append(baseSample.getNumberCount());

        return new Solution(formula.toString(), answer);
    }

    // Оценка математического ожидания.
    @Override
    public Solution calculateMathExpectationEstimation() {
        StringBuilder formula = new StringBuilder("(");
        float answer = 0F;

        float relativeFrequency, averageValue;
        for (Sample sample : samples) {
            relativeFrequency = sample.getRelativeFrequency(sample.getNumberCount());
            averageValue = sample.getAverageValue();
            answer += averageValue * relativeFrequency;
            formula.append(averageValue).append("*").append(relativeFrequency).append("+");
        }

        formula.deleteCharAt(formula.length() - 1);
        formula.append(")");

        return new Solution(formula.toString(), answer);
    }

    // Оценка дисперсии.
    @Override
    public Solution calculateVarianceEstimation(float mathExpectationEstimation) {
        StringBuilder formula = new StringBuilder();
        float answer = 0F;

        int samplesNumber = samples.size();
        formula.append(samplesNumber).append("/").append(samplesNumber - 1).append("*(");

        float averageValue, relativeFrequency;
        for (Sample sample : samples) {
            relativeFrequency = sample.getRelativeFrequency(sample.getNumberCount());
            averageValue = sample.getAverageValue();
            answer += (float) Math.pow(averageValue - mathExpectationEstimation, 2) * relativeFrequency;
            formula.append("(").append(averageValue).append("-").append(mathExpectationEstimation)
                    .append(")^2").append("*").append(relativeFrequency).append("+");
        }

        float offset = (float) samplesNumber / ((float) samplesNumber - 1);
        answer *= offset;

        formula.deleteCharAt(formula.length() - 1);
        formula.append(")");

        return new Solution(formula.toString(), answer);
    }

    // Оценка среднеквадратического отклонения.
    @Override
    public Solution calculateQuadraticDeviationEstimation(float varianceEstimation) {
        StringBuilder formula = new StringBuilder();
        Float answer = (float) Math.sqrt(varianceEstimation);
        formula.append("sqrt(").append(varianceEstimation).append(")");
        return new Solution(formula.toString(), answer);
    }
}