package bsu.controller.parser;

import java.util.*;

public class ProcessArc {
    private Map<Arc, Boolean> processArcMap = new TreeMap<Arc, Boolean>(new MyComp());
    private int process;

    public Map<Arc, Boolean> getProcessArcMap() {
        return processArcMap;
    }

    public void setProcessArcMap(Map<Arc, Boolean> processArcMap) {
        this.processArcMap = processArcMap;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    private class MyComp implements Comparator<Arc>{

        @Override
        public int compare(Arc o1, Arc o2) {
            return o1.compareTo(o2);
        }
    }
}
