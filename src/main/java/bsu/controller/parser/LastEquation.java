package bsu.controller.parser;

import java.util.ArrayList;
import java.util.List;

public class LastEquation {
    private Arc arc;
    private List<Integer> processList = new ArrayList<Integer>();
    private int value;

    public Arc getArc() {
        return arc;
    }

    public void setArc(Arc arc) {
        this.arc = arc;
    }

    public List<Integer> getProcessList() {
        return processList;
    }

    public void setProcessList(List<Integer> processList) {
        this.processList = processList;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
