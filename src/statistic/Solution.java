package statistic;

class Solution {
    private String formula;
    private Float answer;

    Solution(String formula, Float answer) {
        this.formula = formula;
        this.answer = answer;
    }

    String getFormula() {
        return this.formula;
    }

    Float getAnswer() {
        return this.answer;
    }

    @Override
    public String toString() {
        return this.formula + " = " + this.answer;
    }
}
