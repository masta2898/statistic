package statistic;

public class Solution {
    private String formula;
    private Float answer;

    public Solution(String formula, Float answer) {
        this.formula = formula;
        this.answer = answer;
    }

    public String getFormula() {
        return this.formula;
    }

    public Float getAnswer() {
        return this.answer;
    }
}
