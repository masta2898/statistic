package statistic;

class SamplingRates {
    private final Float max;
    private final Float min;
    private final Float hop;

    private final Solution sampleMean;
    private final Solution mathExpectationEstimation;
    private final Solution varianceEstimation;
    private final Solution quadraticDeviationEstimation;

    SamplingRates(Float max, Float min, Float hop, Solution sampleMean, Solution mathExpectationEstimation,
                  Solution varianceEstimation, Solution quadraticDeviationEstimation) {
        this.max = max;
        this.min = min;
        this.hop = hop;

        this.sampleMean = sampleMean;
        this.mathExpectationEstimation = mathExpectationEstimation;
        this.varianceEstimation = varianceEstimation;
        this.quadraticDeviationEstimation = quadraticDeviationEstimation;
    }

    Float getMax() {
        return this.max;
    }

    Float getMin() {
        return this.min;
    }

    Float getHop() {
        return this.hop;
    }

    String getSampleMeanRate() {
        return this.sampleMean.getFormula()
                + " = "
                + this.sampleMean.getAnswer().toString();
    }

    String getMathExpectationEstimationRate() {
        return this.mathExpectationEstimation.getFormula()
                + " = "
                + this.mathExpectationEstimation.getAnswer().toString();
    }

    String getVarianceEstimationRate() {
        return this.varianceEstimation.getFormula()
                + " = "
                + this.varianceEstimation.getAnswer().toString();
    }

    String getQuadraticDeviationEstimationRate() {
        return this.quadraticDeviationEstimation.getFormula()
                + " = "
                + this.quadraticDeviationEstimation.getAnswer().toString();
    }
}
