package main;

import java.util.List;

public interface SampleParametersCalculator {
    void setBaseSample(Sample baseSample);
    void setSamples(List<Sample> samples);

    Solution calculateSampleMean();

    Solution calculateMathExpectationEstimation();

    Solution calculateVarianceEstimation(float mathExpectationEstimation);

    Solution calculateQuadraticDeviationEstimation(float varianceEstimation);
}
