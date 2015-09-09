package bsu.controller.parser;

public class MiddleEquationElement {
    private Arc arc;
    private int process;
    private int coefficient;
    private int equation;

    public Arc getArc() {
        return arc;
    }

    public void setArc(Arc arc) {
        this.arc = arc;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public int getEquation() {
        return equation;
    }

    public void setEquation(int equation) {
        this.equation = equation;
    }
}
