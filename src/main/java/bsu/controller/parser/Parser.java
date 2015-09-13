package bsu.controller.parser;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

public class Parser {
    private static String X = "x_{";
    private static String PROCESS = "}^{";

    public static File parse(MultipartFile file) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
        StringBuffer output = new StringBuffer();
        String line;

        line = br.readLine();
        int variableCount = getLastNumber(line);

        line = br.readLine(); //arc pow
        int arcPow = getLastNumber(line);

        List<Arc> arcList = new ArrayList<Arc>(arcPow);
        int processCount = 0;
        while ((line = br.readLine()) != null) {
            if (!line.contains("/*|K|*/")) {
                arcList.add(getArc(line));
            } else {
                processCount = getLastNumber(line);
                break;
            }
        }

        List<ProcessArc> processArcList = new ArrayList(processCount);
        for (int i = 0; i < processCount; i++) {
            line = br.readLine();
            ProcessArc processArc = new ProcessArc();
            processArc.setProcess(i + 1);
            for (int j = 0; j < arcList.size(); j++) {
                line = br.readLine();
                Arc acr = getArc(line);
                int contain = getLastNumber(line);
                if (contain == 0) {
                    processArc.getProcessArcMap().put(acr, Boolean.FALSE);
                } else {
                    processArc.getProcessArcMap().put(acr, Boolean.TRUE);
                }
            }
            processArcList.add(processArc);
        }

        //last equations
        List<LastEquation> lastEquationList = new ArrayList<LastEquation>();
        line = br.readLine();
        for (int i = 0; i < arcList.size(); i++) {
            line = br.readLine();
            int contain = getLastNumberLength(line);
            if (contain > 1) {
                LastEquation lastEquation = new LastEquation();
                Arc arc = getArc(line);
                lastEquation.setArc(arc);
                lastEquation.setProcessList(getProcessForLastEquation(line));
                lastEquation.setValue(Integer.parseInt(line.substring(line.indexOf("-") + 1)));
                lastEquationList.add(lastEquation);
            }
        }

        //middle equations
        line = br.readLine();
        int middleEquationCount = getLastNumber(line);
        int currentMiddleEquationCount = 1;
        Map<Integer, MiddleEquations> middleEquationsMap = new HashMap<Integer, MiddleEquations>(middleEquationCount);
        for (int i = 0; i < middleEquationCount; i++) {
            middleEquationsMap.put(i + 1, new MiddleEquations());
        }
        while ((line = br.readLine()) != null) {
            if (!line.contains("/*a_")) {
                MiddleEquationElement middleEquationElement = parseMiddleEquationElement(line);
                middleEquationsMap.get(middleEquationElement.getEquation()).getMiddleEquationElementList().add(middleEquationElement);
            } else {
                break;
            }
        }

        //first equation value
        List<FirstEquationValue> firstEquationValueList = new ArrayList<FirstEquationValue>();
        while (true) {
            firstEquationValueList.add(parseFirstValue(line));
            line = br.readLine();
            if (line.contains("/*alpha_")) {
                break;
            }
        }

        //middle equation value
        List<MiddleEquationValue> middleEquationValueList = new ArrayList<MiddleEquationValue>();
        while (true) {
            middleEquationValueList.add(parseMiddleValue(line));
            line = br.readLine();
            if (line == null) {
                break;
            }
        }


        // to tex

        PrintWriter writer = new PrintWriter("output.tex");
        writer.println("\\documentclass{article}");
        writer.println("\\begin{document}");
        writer.println("\\begin{center}");

        for (int i = 1; i <= processCount; i++) {
            ProcessArc processArc = processArcList.get(i - 1);
            for (int j = 1; j <= variableCount; j++) {
                writer.print("$");
                boolean isBegin = true;
                for (Map.Entry<Arc, Boolean> entry : processArc.getProcessArcMap().entrySet()) {
                    if (entry.getValue()) {
                        if (j == entry.getKey().getBegin()) {
                            if (isBegin) {
                                isBegin = false;
                            } else {
                                writer.print("+");
                            }
                            writer.print(generateOutputElement(entry.getKey(), i));
                        }
                        if (j == entry.getKey().getEnd()) {
                            isBegin = false;
                            writer.print("-");
                            writer.print(generateOutputElement(entry.getKey(), i));
                        }
                    }
                }
                writer.print("=");
                writer.print(findFirstEquationValue(firstEquationValueList, j, i));
                writer.println("$\\\\");
            }
            writer.println("\\bigskip");
        }

        for (Map.Entry<Integer, MiddleEquations> entry : middleEquationsMap.entrySet()) {
            writer.print("$");
            boolean isBegin = true;
            for (MiddleEquationElement middleEquationElement : entry.getValue().getMiddleEquationElementList()) {
                if (isBegin) {
                    isBegin = false;
                } else {
                    if (middleEquationElement.getCoefficient() > 0) {
                        writer.print("+");
                    }
                }
                if( Math.abs(middleEquationElement.getCoefficient()) == 1){
                    if(middleEquationElement.getCoefficient() < 0){
                        writer.print("-");
                    }
                } else {
                    writer.print(middleEquationElement.getCoefficient());
                }
                writer.print(generateOutputElement(middleEquationElement.getArc(), middleEquationElement.getProcess()));
            }
            writer.print("=");
            writer.print(findMiddleEquationValue(middleEquationValueList, entry.getKey()));
            writer.println("$\\\\");
        }

        writer.println("\\bigskip");

        for(LastEquation lastEquation : lastEquationList){
            writer.print("$");
            boolean isBegin = true;
            for(Integer process : lastEquation.getProcessList()){
                if(isBegin){
                    isBegin = false;
                } else {
                    writer.print("+");
                }
                writer.print(generateOutputElement(lastEquation.getArc(), process));
            }
            writer.print("=");
            writer.print(lastEquation.getValue());
            writer.println("$\\\\");
        }


        writer.println("\\end{center}");
        writer.println("\\end{document}");
        writer.close();

        return new File("output.tex");
    }

    private static Arc getArc(String line) {
        int begin = line.indexOf("{");
        int end = line.indexOf("}");
        String arcString = line.substring(begin + 1, end);
        StringTokenizer stringTokenizer = new StringTokenizer(arcString, ",");
        int arcBegin = Integer.parseInt(stringTokenizer.nextToken());
        int arcEnd = Integer.parseInt(stringTokenizer.nextToken());
        return new Arc(arcBegin, arcEnd);
    }

    private static int getLastNumber(String line) {
        int begin = line.indexOf("*/");
        return Integer.parseInt(line.substring(begin + 2));
    }

    private static int getLastNumberLength(String line) {
        int begin = line.indexOf("*/");
        return line.substring(begin + 2).length();
    }

    private static List<Integer> getProcessForLastEquation(String line) {
        int begin = line.indexOf("*/1 ");
        int end = line.indexOf("-");
        String processString = line.substring(begin + 4, end);
        StringTokenizer stringTokenizer = new StringTokenizer(processString, ",");
        List<Integer> processList = new ArrayList<Integer>();
        while (stringTokenizer.hasMoreTokens()) {
            processList.add(Integer.parseInt(stringTokenizer.nextToken()));
        }
        return processList;
    }

    private static MiddleEquationElement parseMiddleEquationElement(String line) {
        line = line.replace("/*lambda_", "");
        int beginArc = Integer.parseInt(line.substring(0, 1));
        int endArc = Integer.parseInt(line.substring(1, 2));
        Arc arc = new Arc(beginArc, endArc);
        int process = Integer.parseInt(line.substring(3, 4));
        int equation = Integer.parseInt(line.substring(4, 5));
        int coefficient = getLastNumber(line);
        MiddleEquationElement middleEquationElement = new MiddleEquationElement();
        middleEquationElement.setArc(arc);
        middleEquationElement.setProcess(process);
        middleEquationElement.setEquation(equation);
        middleEquationElement.setCoefficient(coefficient);
        return middleEquationElement;
    }

    private static FirstEquationValue parseFirstValue(String line) {
        String beginArcStr = line.substring(line.indexOf("/*a_") + 4, line.indexOf("^"));
        int beginArc = Integer.parseInt(beginArcStr);
        String processStr = line.substring(line.indexOf("^") + 1, line.indexOf("*/"));
        int process = Integer.parseInt(processStr);
        int value = getLastNumber(line);
        FirstEquationValue firstEquationValue = new FirstEquationValue();
        firstEquationValue.setValue(value);
        firstEquationValue.setProcess(process);
        firstEquationValue.setBeginArc(beginArc);
        return firstEquationValue;
    }

    private static MiddleEquationValue parseMiddleValue(String line) {
        String equationStr = line.substring(line.indexOf("/*alpha_") + 8, line.indexOf("*/"));
        int equation = Integer.parseInt(equationStr);
        int value = getLastNumber(line);
        MiddleEquationValue middleEquationValue = new MiddleEquationValue();
        middleEquationValue.setValue(value);
        middleEquationValue.setEquation(equation);
        return middleEquationValue;
    }

    private static String generateOutputElement(Arc entry, int process) {
        return "x_{" + entry.getBegin() + entry.getEnd() + "}^{" + process + "}";
    }

    private static int findFirstEquationValue(List<FirstEquationValue> firstEquationValueList, int beginArc, int process) {
        for (FirstEquationValue firstEquationValue : firstEquationValueList) {
            if (firstEquationValue.getBeginArc() == beginArc && firstEquationValue.getProcess() == process) {
                return firstEquationValue.getValue();
            }
        }
        return 0;
    }

    private static int findMiddleEquationValue(List<MiddleEquationValue> middleEquationValueList, int equation) {
        for (MiddleEquationValue middleEquationValue : middleEquationValueList) {
            if (middleEquationValue.getEquation() == equation) {
                return middleEquationValue.getValue();
            }
        }
        return 0;
    }
}
