package bsu.controller.parser;

import java.util.ArrayList;
import java.util.List;

public class MiddleEquations {
    private List<MiddleEquationElement> middleEquationElementList = new ArrayList<MiddleEquationElement>();

    public List<MiddleEquationElement> getMiddleEquationElementList() {
        return middleEquationElementList;
    }

    public void setMiddleEquationElementList(List<MiddleEquationElement> middleEquationElementList) {
        this.middleEquationElementList = middleEquationElementList;
    }
}
