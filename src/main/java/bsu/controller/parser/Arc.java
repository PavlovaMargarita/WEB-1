package bsu.controller.parser;

public class Arc  implements Comparable{
    private int begin;
    private int end;

    public Arc(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Arc arc = (Arc) o;

        if (begin != arc.begin) return false;
        if (end != arc.end) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = begin;
        result = 31 * result + end;
        return result;
    }

    public int getBegin() {

        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    @Override
    public int compareTo(Object o) {
        Arc arc = (Arc) o;
        if(this.getBegin() == arc.getBegin()){
            return  this.getEnd() - arc.getBegin();
        }
        return this.getBegin()- arc.getBegin();
    }
}
