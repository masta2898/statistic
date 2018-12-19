package main;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Controller {
    @FXML
    public TextArea numbers;
    public TextField samplesNumber;

    public Label xmax, xmin, hop;

    public TableView<SampleView> statTable;
    public TableColumn dischargeColumn;
    public TableColumn<SampleView, Double> lowColumn;
    public TableColumn<SampleView, Double> highColumn;
    public TableColumn<SampleView, Integer> miColumn;
    public TableColumn<SampleView, Double> hiColumn;
    public TableColumn<SampleView, Double> xiColumn;

    public BarChart<String, Float> barChart;
    public CategoryAxis dischargeAxis;
    public NumberAxis relativeFrequencyAxis;

    public TextField sampleMean;
    public TextField mathExpectationEstimation;
    public TextField varianceEstimation;
    public TextField quadraticDeviationEstimation;

    private SampleParametersCalculator sampleParametersCalculator;

    Controller(SampleParametersCalculator sampleParametersCalculator) {
        this.sampleParametersCalculator = sampleParametersCalculator;
    }

    @FXML
    public void handleCalculateAction(ActionEvent actionEvent) {
        if (!samplesNumberExist()) {
            showError("Кількість вибірок повинна бути цілим числом.");
            return;
        }

        if (!sampleNumbersExist()) {
            showError("Числа повинні бути цілими або з плаваючою комою, розділені пробілом.");
            return;
        }

        int samplesNumber = getSamplesNumber();
        Sample baseSample = new Sample(this.getSampleNumbers());

        if (samplesNumber <= 0) {
            showError("Кількість вибірок повинна бути більше 1.");
            return;
        }

        if (baseSample.isEmpty()) {
            showError("Кількість чисел повинна бути більше 1.");
            return;
        }

        List<Sample> samples = baseSample.divideToParts(samplesNumber);
        List<SampleView> sampleViews = getSampleViews(baseSample, samples);
        SampleParameters sampleParameters = calculateSampleParameters(baseSample, samples);

        setSampleParameters(sampleParameters);
        buildStatTable(sampleViews);
        buildBarChart(samples, baseSample.getNumberCount());
    }

    private SampleParameters calculateSampleParameters(Sample baseSample, List<Sample> samples) {
        sampleParametersCalculator.setBaseSample(baseSample);
        sampleParametersCalculator.setSamples(samples);

        Solution sampleMean = sampleParametersCalculator.calculateSampleMean();
        Solution mathExpectationEstimation = sampleParametersCalculator.calculateMathExpectationEstimation();
        Solution varianceEstimation = sampleParametersCalculator.calculateVarianceEstimation(
                mathExpectationEstimation.getAnswer());
        Solution quadraticDeviationEstimation = sampleParametersCalculator.calculateQuadraticDeviationEstimation(
                varianceEstimation.getAnswer());

        SampleParameters sampleParameters = new SampleParameters();

        sampleParameters.setMax(baseSample.getMaxNumber());
        sampleParameters.setMin(baseSample.getMinNumber());
        sampleParameters.setHop(baseSample.getHopNumber());

        sampleParameters.setSampleMean(sampleMean);
        sampleParameters.setMathExpectationEstimation(mathExpectationEstimation);
        sampleParameters.setVarianceEstimation(varianceEstimation);
        sampleParameters.setQuadraticDeviationEstimation(quadraticDeviationEstimation);

        return sampleParameters;
    }

    private List<SampleView> getSampleViews(Sample baseSample, List<Sample> samples) {
        List<SampleView> sampleViews = new LinkedList<>();
        for (Sample sample : samples) {
            SampleView sampleView = new SampleView();
            sampleView.setHigherBound(sample.getMaxNumber());
            sampleView.setLowerBound(sample.getMinNumber());
            sampleView.setFrequency(sample.getNumberCount());
            sampleView.setRelativeFrequency(sample.getRelativeFrequency(baseSample.getNumberCount()));
            sampleView.setAverageValue(sample.getAverageValue());
            sampleViews.add(sampleView);
        }
        return sampleViews;
    }

    private boolean sampleNumbersExist() {
        try {
            Integer.parseInt(samplesNumber.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean samplesNumberExist() {
        try {
            String[] data = numbers.getText().split(" ");
            for (String aData : data) {
                Float.parseFloat(aData);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private int getSamplesNumber() {
        return Integer.parseInt(samplesNumber.getText());
    }

    private List<Float> getSampleNumbers() {
        List<Float> sampleNumbers = new LinkedList<>();
        String[] data = numbers.getText().split(" ");
        for (String number : data) {
            sampleNumbers.add(Float.parseFloat(number));
        }
        return sampleNumbers;
    }

    private void setSampleParameters(SampleParameters sampleParameters) {
        xmax.setText(String.valueOf(sampleParameters.getMax()));
        xmin.setText(String.valueOf(sampleParameters.getMin()));
        hop.setText(String.valueOf(sampleParameters.getHop()));

        sampleMean.setText(sampleParameters.getSampleMeanCalculation());
        mathExpectationEstimation.setText(sampleParameters.getMathExpectationEstimationCalculation());
        varianceEstimation.setText(sampleParameters.getVarianceEstimationCalculation());
        quadraticDeviationEstimation.setText(sampleParameters.getQuadraticDeviationEstimationCalculation());
    }

    private void buildStatTable(List<SampleView> sampleViews) {
        try {
            dischargeColumn.getColumns().clear();
            statTable.getColumns().clear();

            lowColumn.setCellValueFactory(new PropertyValueFactory<>("lowerBound"));
            highColumn.setCellValueFactory(new PropertyValueFactory<>("higherBound"));
            miColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
            hiColumn.setCellValueFactory(new PropertyValueFactory<>("relativeFrequency"));
            xiColumn.setCellValueFactory(new PropertyValueFactory<>("averageValue"));

            statTable.setItems(FXCollections.observableArrayList(sampleViews));

            dischargeColumn.getColumns().addAll(lowColumn, highColumn);
            statTable.getColumns().addAll(dischargeColumn, miColumn, hiColumn, xiColumn);
        } catch (NullPointerException | NoSuchElementException e) {
            showWarning("Одне із значень відсутнє, тому таблиця не буде відтворена.");
        }
    }

    private void buildBarChart(List<Sample> samples, int baseSampleNumberCount) {
        dischargeAxis.setLabel("Розряд");
        relativeFrequencyAxis.setLabel("Відносна чатота (hi)");

        // Выключил анимацию, чтобы тратило меньше ресурсов.
        dischargeAxis.setAnimated(false);
        relativeFrequencyAxis.setAnimated(false);

        XYChart.Series series = new XYChart.Series();
        series.setName("Значення відносної частоти у проміжку.");

        String discharge;
        for (Sample sample : samples) {
            discharge = sample.getMinNumber().toString() + " - " + sample.getMaxNumber().toString();
            series.getData().add(new XYChart.Data(discharge, sample.getRelativeFrequency(baseSampleNumberCount)));
        }

        barChart.getData().clear();
        barChart.getData().addAll(series);
    }

    private void showWarning(String text) {
        showAlert(new Alert(Alert.AlertType.WARNING), text);
    }

    private void showError(String text) {
        showAlert(new Alert(Alert.AlertType.ERROR), text);
    }

    private void showAlert(Alert alert, String text) {
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
