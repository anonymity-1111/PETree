package org.PETree;

public class assembledMatch {
    public int[] vlbList;
    public int[] vubList;
    public int[] intervalList;
    double pro;
    public assembledMatch(int length) {
        this.vlbList = new int[length];
        this.vubList = new int[length];
        this.intervalList = new int[length];
    }

    public int[] getVlbList() {
        return vlbList;
    }
    public int getVlbListValue(int index){
        return vlbList[index];
    }

    public void setVlbList(int[] VlbList) {
        this.vlbList = VlbList;
    }
    public void setVlbListValue(int index, int value) {
        this.vlbList[index] = value;
    }

    public int[] getVubList() {
        return vubList;
    }

    public int getVubListValue(int index){
        return vubList[index];
    }

    public void setVubList(int[] vubList) {
        this.vubList = vubList;
    }
    public void setVubListValue(int index, int value)  {
        this.vubList[index] = value;
    }

    public int[] getIntervalList() {
        return intervalList;
    }
    public int getIntervalListValue(int index) {
        return intervalList[index];
    }

    public void setIntervalList(int[] intervalList) {
        this.intervalList = intervalList;
    }

    public void setIntervalListValue(int index, int value) {
        this.intervalList[index] = value;
    }

    public double getPro() {
        return pro;
    }

    public void setPro(double pro) {
        this.pro = pro;
    }
}
