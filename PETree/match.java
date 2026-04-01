package org.PETree;

import java.util.Arrays;
import java.util.List;

class match {
    public int[] lowerList;
    public int[] realupperList;
    public int[] upperList;
    public int[] intervalList;
    public int[] eventIdList;
    int mark;
    public List<Integer> negationLowerList;
    public List<Integer> negationUpperList;
    public List<Integer> negationIntervalList;


    public List<int[]> vlbLists;
    public List<int[]> vubLists;
    public List<int[]> intervalLists;

    public int mix4And;
    public int max4And;

    int lower;
    int upper;
    String eventList;

    public match(int[] lowerList, int[] upperList,int[] intervalList,List<Integer> negationLowerList,List<Integer> negationUpperList,List<Integer> negationIntervalList) {
        this.lowerList = lowerList;
        this.upperList = upperList;
        this.intervalList = intervalList;
        this.negationLowerList = negationLowerList;
        this.negationUpperList = negationUpperList;
        this.negationIntervalList = negationIntervalList;
    }

    public match(int[] lowerList, int[] upperList,int[] intervalList,int[] eventIdList) {
        this.lowerList = lowerList;
        this.upperList = upperList;
        this.intervalList = intervalList;
        this.eventIdList = eventIdList;
    }

    public int[] getLowerList() {
        return lowerList;
    }

    public void setLowerList(int[] lowerList) {
        this.lowerList = lowerList;
    }

    public int[] getUpperList() {
        return upperList;
    }

    public void setUpperList(int[] upperList) {
        this.upperList = upperList;
    }

    public int[] getIntervalList() {
        return intervalList;
    }

    public void setIntervalList(int[] intervalList) {
        this.intervalList = intervalList;
    }

    public int getLower() {
        return lower;
    }

    public void setLower(int lower) {
        this.lower = lower;
    }

    public int getUpper() {
        return upper;
    }

    public void setUpper(int upper) {
        this.upper = upper;
    }

    public String getEventList() {
        return eventList;
    }

    public void setEventList(String eventList) {
        this.eventList = eventList;
    }

    public List<int[]> getVubLists() {
        return vubLists;
    }

    public void setVubLists(List<int[]> vubLists) {
        this.vubLists = vubLists;
    }

    public List<int[]> getVlbLists() {
        return vlbLists;
    }

    public void setVlbLists(List<int[]> vlbLists) {
        this.vlbLists = vlbLists;
    }

    public int[] getRealupperList() {
        return realupperList;
    }

    public void setRealupperList(int[] realupperList) {
        this.realupperList = realupperList;
    }

    public List<int[]> getIntervalLists() {
        return intervalLists;
    }

    public void setIntervalLists(List<int[]> intervalLists) {
        this.intervalLists = intervalLists;
    }

    public int getMax4And() {
        return max4And;
    }

    public void setMax4And(int max4And) {
        this.max4And = max4And;
    }

    public int getMix4And() {
        return mix4And;
    }

    public void setMix4And(int mix4And) {
        this.mix4And = mix4And;
    }


    public List<Integer> getNegationLowerList() {
        return negationLowerList;
    }

    public void setNegationLowerList(List<Integer> negationLowerList) {
        this.negationLowerList = negationLowerList;
    }

    public List<Integer> getNegationUpperList() {
        return negationUpperList;
    }

    public void setNegationUpperList(List<Integer> negationUpperList) {
        this.negationUpperList = negationUpperList;
    }

    public List<Integer> getNegationIntervalList() {
        return negationIntervalList;
    }

    public void setNegationIntervalList(List<Integer> negationIntervalList) {
        this.negationIntervalList = negationIntervalList;
    }

    public int[] getEventIdList() {
        return eventIdList;
    }

    public void setEventIdList(int[] eventIdList) {
        this.eventIdList = eventIdList;
    }

    @Override
    public String toString() {
        return "match{" +
                "lowerList=" + Arrays.toString(lowerList) +
                ", upperList=" + Arrays.toString(upperList) +
                ", idList=" + Arrays.toString(eventIdList) +
                '}';
    }
}