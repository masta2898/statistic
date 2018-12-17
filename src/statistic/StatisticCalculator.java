package statistic;

import java.util.List;

class StatisticCalculator {
    private final Float sampleMaxNumber, sampleMinNumber, sampleHopNumber;

    private final Integer baseSampleNumberCount;

    private final List<Sample> samples;

    StatisticCalculator(int samplesNumber, Sample sample) {
        this.sampleMaxNumber = sample.getMaxNumber();
        this.sampleMinNumber = sample.getMinNumber();
        this.sampleHopNumber = (this.sampleMaxNumber - this.sampleMinNumber) / samplesNumber;

        this.baseSampleNumberCount = sample.getNumberCount();

        this.samples = sample.divideToParts(samplesNumber);
    }

    List<Sample> getSamples() {
        return this.samples;
    }

    SamplingRates getSamplingRates() {
        Solution mathExpectationEstimation = this.calculateMathExpectationEstimation();
        Solution varianceEstimation = this.calculateVarianceEstimation(mathExpectationEstimation.getAnswer());
        return new SamplingRates(this.sampleMaxNumber, this.sampleMinNumber, this.sampleHopNumber,
                this.calculateSampleMean(), mathExpectationEstimation,
                varianceEstimation, this.calculateQuadraticDeviationEstimation(varianceEstimation.getAnswer()));
    }

    // Средняя выборки.
    private Solution calculateSampleMean() {
        StringBuilder formula = new StringBuilder("(");
        Float answer = 0F;

        int numberCount;
        float averageValue;
        for (Sample sample : this.samples) {
            numberCount = sample.getNumberCount();
            averageValue = sample.getAverageValue();
            answer += numberCount * averageValue;
            formula.append(numberCount)
                    .append("*")
                    .append(averageValue).append("+");
        }

        answer /= this.baseSampleNumberCount;

        formula.deleteCharAt(formula.length() - 1);
        formula.append(")").append("/").append(this.baseSampleNumberCount);

        return new Solution(formula.toString(), answer);
    }

    // Оценка математического ожидания.
    private Solution calculateMathExpectationEstimation() {
        StringBuilder formula = new StringBuilder("(");
        Float answer = 0F;

        float relativeFrequency, averageValue;
        for (Sample sample : this.samples) {
            relativeFrequency = sample.getRelativeFrequency(this.baseSampleNumberCount);
            averageValue = sample.getAverageValue();
            answer += averageValue * relativeFrequency;
            formula.append(averageValue)
                    .append("*")
                    .append(relativeFrequency).append("+");
        }

        formula.deleteCharAt(formula.length() - 1);
        formula.append(")");

        return new Solution(formula.toString(), answer);
    }

    // Оценка дисперсии.
    private Solution calculateVarianceEstimation(float mathExpectationEstimation) {
        StringBuilder formula = new StringBuilder();
        Float answer = 0F;

        int samplesNumber = this.samples.size();
        formula.append(samplesNumber).append("/").append(samplesNumber - 1).append("*(");

        float averageValue, relativeFrequency;
        for (Sample sample : this.samples) {
            relativeFrequency = sample.getRelativeFrequency(this.baseSampleNumberCount);
            averageValue = sample.getAverageValue();
            answer += (float) Math.pow(averageValue - mathExpectationEstimation, 2) * relativeFrequency;
            formula.append("(")
                    .append(averageValue)
                    .append("-")
                    .append(mathExpectationEstimation)
                    .append(")^2")
                    .append("*").append(relativeFrequency).append("+");
        }

        float offset = (float) samplesNumber / ((float) samplesNumber - 1);
        answer *= offset;

        formula.deleteCharAt(formula.length() - 1);
        formula.append(")");

        return new Solution(formula.toString(), answer);
    }

    // Оценка среднеквадратического отклонения.
    private Solution calculateQuadraticDeviationEstimation(float varianceEstimation) {
        StringBuilder formula = new StringBuilder();
        Float answer = (float) Math.sqrt(varianceEstimation);
        formula.append("sqrt(").append(varianceEstimation).append(")");
        return new Solution(formula.toString(), answer);
    }
}