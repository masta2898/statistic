package main;

import java.util.List;

public interface SampleParametersCalculator {
    SampleParameters calculateSampleParameters();

    void setBaseSample(Sample baseSample);

    void setSamples(List<Sample> samples);
}
