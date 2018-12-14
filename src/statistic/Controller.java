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
    public TableColumn lowColumn;
    public TableColumn highColumn;
    public TableColumn miColumn;
    public TableColumn hiColumn;
    public TableColumn xiColumn;

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
        try {
            int samplesNumber = this.getSamplesNumber();
            int accuracy = this.getAccuracy();
            List<Float> sampleNumbers = this.getSampleNumbers();

            if (samplesNumber > 0 && !sampleNumbers.isEmpty()) {
                this.statisticCalculator = new StatisticCalculator(samplesNumber, accuracy, sampleNumbers);
                List<Sample> samples = this.statisticCalculator.getSamples();

                setSamplingRates();

                buildStatTable(samples);
                buildBarChart(samples);
            }
        } catch (Exception e) {
            showError("Неочікувана помилка! Відправте це розробнику: " + e.toString());
        }
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

    private int getSamplesNumber() {
        int samplesNumber = 0;

        try {
            samplesNumber = Integer.parseInt(this.samplesNumber.getText());
        } catch (NumberFormatException e) {
            showError("Кількість вибірок повинна бути цілим числом.");
        }

        return samplesNumber;
    }

    private int getAccuracy() {
        int accuracy;

        try {
            accuracy = Integer.parseInt(this.accuracy.getText());
        } catch (NumberFormatException e) {
            showError("Кількість знаків після коми повинна бути цілм числом. "
                    + "Використовуеться значення за змовчуванням (2 знаки)");
            accuracy = 2;
        }

        return accuracy;
    }

    private List<Float> getSampleNumbers() {
        List<Float> nums = new ArrayList<>();

        try {
            String data[] = numbers.getText().split(" ");
            for (String aData : data) {
                nums.add(Float.parseFloat(aData));
            }
        } catch (NumberFormatException e) {
            showError("Числа повинні бути цілими або з плаваючою комою, розділені пробілом.");
        }

        return nums;
    }

    private void showWarning(String text) {
        Alert warningAlert = new Alert(Alert.AlertType.WARNING);
        warningAlert.setHeaderText(null);
        warningAlert.setContentText(text);
        warningAlert.showAndWait();
    }

    private void showError(String text) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(text);
        errorAlert.showAndWait();
    }
}
