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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Controller {
    @FXML
    public TextArea numbers;
    public TextField accuracy;
    public TextField samplesNumber;

    public Label xmax;
    public Label xmin;
    public Label hop;

    public TableView<Sample> statTable;
    public TableColumn dischargeColumn;
    public TableColumn<Sample, Double> lowColumn;
    public TableColumn<Sample, Double> highColumn;
    public TableColumn<Sample, Integer> miColumn;
    public TableColumn<Sample, Double> hiColumn;
    public TableColumn<Sample, Double> xiColumn;

    public BarChart<String, Float> barChart;
    public CategoryAxis dischargeAxis;
    public NumberAxis relativeFrequencyAxis;

    public TextField sampleMean;
    public TextField mathExpectationEstimation;
    public TextField varianceEstimation;
    public TextField quadraticDeviationEstimation;

    private StatisticCalculator statisticCalculator;

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

        int accuracy;
        if (!this.accuracyExist()) {
            showError("Кількість знаків після коми повинна бути цілм числом. "
                    + "Використовуеться значення за змовчуванням (2 знаки)");
            accuracy = 2;
        } else {
            accuracy = this.getAccuracy();
        }

        int samplesNumber = this.getSamplesNumber();
        List<Float> sampleNumbers = this.getSampleNumbers();

        if (samplesNumber <= 0) {
            showError("Кількість вибірок повинна бути більше 1.");
            return;
        }
        if (sampleNumbers.isEmpty()) {
            showError("Кількість чисел повинна бути більше 1.");
            return;
        }

        this.statisticCalculator = new StatisticCalculator(samplesNumber, accuracy, sampleNumbers);
        List<Sample> samples = this.statisticCalculator.getSamples();

        this.setSamplingRates();

        this.buildStatTable(samples);
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

    private boolean accuracyExist() {
        try {
            Integer.parseInt(this.accuracy.getText());
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

    private int getAccuracy() {
        return Integer.parseInt(this.accuracy.getText());
    }

    private List<Float> getSampleNumbers() {
        List<Float> nums = new ArrayList<>();
        String[] data = numbers.getText().split(" ");
        for (String aData : data) {
            nums.add(Float.parseFloat(aData));
        }
        return nums;
    }

    private void setSamplingRates() {
        this.xmax.setText(String.valueOf(this.statisticCalculator.getMax()));
        this.xmin.setText(String.valueOf(this.statisticCalculator.getMin()));
        this.hop.setText(String.valueOf(this.statisticCalculator.getHop()));

        Solution sampleMean = this.statisticCalculator.calculateSampleMean();
        Solution mathExpectationEstimation = this.statisticCalculator.calculateMathExpectationEstimation();
        Solution varianceEstimation = this.statisticCalculator.calculateVarianceEstimation(
                mathExpectationEstimation.getAnswer());
        Solution quadraticDeviationEstimation = this.statisticCalculator.calculateQuadraticDeviationEstimation(
                varianceEstimation.getAnswer());

        this.sampleMean.setText(sampleMean.getFormula() + "=" + sampleMean.getAnswer().toString());
        this.mathExpectationEstimation.setText(mathExpectationEstimation.getFormula() + "="
                + mathExpectationEstimation.getAnswer().toString());
        this.varianceEstimation.setText(varianceEstimation.getFormula() + "=" + varianceEstimation.getAnswer());
        this.quadraticDeviationEstimation.setText(quadraticDeviationEstimation.getFormula() + "="
                + quadraticDeviationEstimation.getAnswer());
    }

    private void buildStatTable(List<Sample> samples) {
        try {
            this.dischargeColumn.getColumns().clear();
            this.statTable.getColumns().clear();

            this.lowColumn.setCellValueFactory(new PropertyValueFactory<Sample, Double>("lowerBound"));
            this.highColumn.setCellValueFactory(new PropertyValueFactory<Sample, Double>("higherBound"));
            this.miColumn.setCellValueFactory(new PropertyValueFactory<Sample, Integer>("frequency"));
            this.hiColumn.setCellValueFactory(new PropertyValueFactory<Sample, Double>("relativeFrequency"));
            this.xiColumn.setCellValueFactory(new PropertyValueFactory<Sample, Double>("averageValue"));

            this.statTable.setItems(FXCollections.observableArrayList(samples));

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

        try {
            String discharge;
            for (Sample sample : samples) {
                discharge = sample.getLowerBound().toString() + "-" + sample.getHigherBound().toString();
                series.getData().add(new XYChart.Data(discharge, sample.getRelativeFrequency()));
            }

            this.barChart.getData().clear();
            this.barChart.getData().addAll(series);
        } catch (Exception e) {
            showError("Результати не можуть бути відображені на гістограмі через помилку:" + e.toString());
        }
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
