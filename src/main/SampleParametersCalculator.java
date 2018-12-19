package main;

import java.util.List;

public interface SampleParametersCalculator {
    void setBaseSample(Sample baseSample);
    void setSamples(List<Sample> samples);

    SampleParameters calculateSampleParameters();
}
