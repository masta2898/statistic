package statistic;

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

    @FXML
    public void handleCalculateAction(ActionEvent actionEvent) {
        if (!this.samplesNumberExist()) {
            showError("Кількість вибірок повинна бути цілим числом.");
            return;
        }

        if (!this.sampleNumbersExist()) {
            showError("Числа повинні бути цілими або з плаваючою комою, розділені пробілом.");
            return;
        }

        int samplesNumber = this.getSamplesNumber();
        Sample sample = this.getSample();

        if (samplesNumber <= 0) {
            showError("Кількість вибірок повинна бути більше 1.");
            return;
        }

        if (sample.isEmpty()) {
            showError("Кількість чисел повинна бути більше 1.");
            return;
        }

        StatisticCalculator statisticCalculator = new StatisticCalculator(samplesNumber, sample);
        List<Sample> samples = statisticCalculator.getSamples();

        this.setSamplingRates(statisticCalculator.getSamplingRates());

        List<SampleView> samplesViews = new LinkedList<>();
        for (Sample s : samples) {
            samplesViews.add(new SampleView(s.getMinNumber(), s.getMaxNumber(), s.getNumberCount(),
                    s.getRelativeFrequency(sample.getNumberCount()), s.getAverageValue()));
        }

        this.buildStatTable(samplesViews);
        this.buildBarChart(samples);
    }

    private boolean sampleNumbersExist() {
        try {
            Integer.parseInt(this.samplesNumber.getText());
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
        return Integer.parseInt(this.samplesNumber.getText());
    }

    private Sample getSample() {
        Sample sample = new Sample();
        String[] data = this.numbers.getText().split(" ");
        for (String number : data) {
            sample.add(Float.parseFloat(number));
        }
        return sample;
    }

    private void setSamplingRates(SamplingRates samplingRates) {
        this.xmax.setText(String.valueOf(samplingRates.getMax()));
        this.xmin.setText(String.valueOf(samplingRates.getMin()));
        this.hop.setText(String.valueOf(samplingRates.getHop()));

        this.sampleMean.setText(samplingRates.getSampleMeanRate());
        this.mathExpectationEstimation.setText(samplingRates.getMathExpectationEstimationRate());
        this.varianceEstimation.setText(samplingRates.getVarianceEstimationRate());
        this.quadraticDeviationEstimation.setText(samplingRates.getQuadraticDeviationEstimationRate());
    }

    private void buildStatTable(List<SampleView> sampleViews) {
        try {
            this.dischargeColumn.getColumns().clear();
            this.statTable.getColumns().clear();

            this.lowColumn.setCellValueFactory(new PropertyValueFactory<>("lowerBound"));
            this.highColumn.setCellValueFactory(new PropertyValueFactory<>("higherBound"));
            this.miColumn.setCellValueFactory(new PropertyValueFactory<>("frequency"));
            this.hiColumn.setCellValueFactory(new PropertyValueFactory<>("relativeFrequency"));
            this.xiColumn.setCellValueFactory(new PropertyValueFactory<>("averageValue"));

            this.statTable.setItems(FXCollections.observableArrayList(sampleViews));

            this.dischargeColumn.getColumns().addAll(this.lowColumn, this.highColumn);
            this.statTable.getColumns().addAll(this.dischargeColumn, this.miColumn, this.hiColumn, this.xiColumn);
        } catch (NullPointerException | NoSuchElementException e) {
            this.showWarning("Одне із значень відсутнє, тому таблиця не буде відтворена.");
        }
    }

    private void buildBarChart(List<Sample> samples) {
        this.dischargeAxis.setLabel("Розряд");
        this.relativeFrequencyAxis.setLabel("Відносна чатота (hi)");

        // Выключил анимацию, чтобы тратило меньше ресурсов.
        this.dischargeAxis.setAnimated(false);
        this.relativeFrequencyAxis.setAnimated(false);

        XYChart.Series series = new XYChart.Series();
        series.setName("Значення відносної частоти у проміжку.");

        int baseSampleNumberCount = 0;
        for (Sample sample : samples) {
            baseSampleNumberCount += sample.getNumberCount();
        }

        String discharge;
        for (Sample sample : samples) {
            discharge = "з " + sample.getMinNumber().toString() + " до " + sample.getMaxNumber().toString();
            series.getData().add(new XYChart.Data(discharge, sample.getRelativeFrequency(baseSampleNumberCount)));
        }

        this.barChart.getData().clear();
        this.barChart.getData().addAll(series);
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
