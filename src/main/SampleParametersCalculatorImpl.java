package main;

import java.util.LinkedList;
import java.util.List;

public class SampleParametersCalculatorImpl implements SampleParametersCalculator {

    private Sample sample;
    private List<Sample> samples;

    SampleParametersCalculatorImpl() {
        this.sample = new Sample(new LinkedList<>());
        this.samples = sample.divideToParts(0);
    }

    @Override
    public SampleParameters calculateSampleParameters() {
        Solution sampleMean = this.calculateSampleMean();
        Solution mathExpectationEstimation = this.calculateMathExpectationEstimation();
        Solution varianceEstimation = this.calculateVarianceEstimation(
                mathExpectationEstimation.getAnswer());
        Solution quadraticDeviationEstimation = this.calculateQuadraticDeviationEstimation(
                varianceEstimation.getAnswer());

        SampleParameters sampleParameters = new SampleParameters();
        sampleParameters.setMax(this.sample.getMaxNumber());
        sampleParameters.setMin(this.sample.getMinNumber());
        sampleParameters.setHop(this.sample.getHopNumber());

        sampleParameters.setSampleMean(sampleMean);
        sampleParameters.setMathExpectationEstimation(mathExpectationEstimation);
        sampleParameters.setVarianceEstimation(varianceEstimation);
        sampleParameters.setQuadraticDeviationEstimation(quadraticDeviationEstimation);

        return sampleParameters;
    }

    @Override
    public void setBaseSample(Sample baseSample) {
        this.sample = baseSample;
    }

    @Override
    public void setSamples(List<Sample> samples) {
        this.samples = samples;
    }

    // Средняя выборки.
    Solution calculateSampleMean() {
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

        answer /= sample.getNumberCount();

        formula.deleteCharAt(formula.length() - 1);
        formula.append(")").append("/").append(sample.getNumberCount());

        return new Solution(formula.toString(), answer);
    }

    // Оценка математического ожидания.
    Solution calculateMathExpectationEstimation() {
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
    Solution calculateVarianceEstimation(float mathExpectationEstimation) {
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
    Solution calculateQuadraticDeviationEstimation(float varianceEstimation) {
        StringBuilder formula = new StringBuilder();
        Float answer = (float) Math.sqrt(varianceEstimation);
        formula.append("sqrt(").append(varianceEstimation).append(")");
        return new Solution(formula.toString(), answer);
    }
}