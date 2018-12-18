package tests;

import main.Sample;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SampleTest {

    @Test
    void divideToPartsEmptyList() {
        Sample emptySample = new Sample(new LinkedList<>());
        assertTrue(emptySample.isEmpty(), "Sample must be empty if initializing list was empty!");
        List<Sample> samples = emptySample.divideToParts(2);
        assertTrue(samples.isEmpty(), "Empty sample can't divide into non empty list of samples!");
    }

    @Test
    void divideToPartsSimpleList() {

    }

    @Test
    void divideToPartsRandomGeneratedList() {

    }
}