package main;

class SampleParameters {
    private Float max;
    private Float hop;
    private Float min;

    private Solution sampleMean;
    private Solution mathExpectationEstimation;
    private Solution varianceEstimation;
    private Solution quadraticDeviationEstimation;

    SampleParameters() {
    }

    Float getMax() {
        return this.max;
    }

    void setMax(Float max) {
        this.max = max;
    }

    Float getMin() {
        return this.min;
    }

    void setMin(Float min) {
        this.min = min;
    }

    Float getHop() {
        return this.hop;
    }

    void setHop(Float hop) {
        this.hop = hop;
    }

    void setSampleMean(Solution sampleMean) {
        this.sampleMean = sampleMean;
    }

    void setMathExpectationEstimation(Solution mathExpectationEstimation) {
        this.mathExpectationEstimation = mathExpectationEstimation;
    }

    void setVarianceEstimation(Solution varianceEstimation) {
        this.varianceEstimation = varianceEstimation;
    }

    void setQuadraticDeviationEstimation(Solution quadraticDeviationEstimation) {
        this.quadraticDeviationEstimation = quadraticDeviationEstimation;
    }

    String getSampleMeanCalculation() {
        return this.sampleMean.toString();
    }

    String getMathExpectationEstimationCalculation() {
        return this.mathExpectationEstimation.toString();
    }

    String getVarianceEstimationCalculation() {
        return this.varianceEstimation.toString();
    }

    String getQuadraticDeviationEstimationCalculation() {
        return this.quadraticDeviationEstimation.toString();
    }
}
