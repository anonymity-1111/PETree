package org.PETree;

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class function {
    public static  int countMinBound4LeafNode(int i, int j, int beLower, int timeWindow, @NotNull String[][] pattern, @NotNull String[] patternType){
        int occupied = 0;
        if (patternType[i].equals("NSEQ")){
            for (int index = 0; index < i; index++){
                if (index!=i){
                    if (pattern[index].equals("OR")) {
                        occupied++;
                    }
                    else if (pattern[index].equals("NSEQ")) {
                        occupied = occupied + 2;
                    }
                    else {
                        occupied = occupied + pattern[index].length;
                    }
                }
            }
            if (j == 2){
                occupied++;
            }
            return beLower + occupied;

        }
        else if (patternType[i].equals("SEQ")){
            for (int index = 0; index < i; index++){
                if (index!=i){
                    if (pattern[index].equals("OR")) {
                        occupied++;
                    }
                    else if (pattern[index].equals("NSEQ")) {
                        occupied = occupied + 2;
                    }
                    else {
                        occupied = occupied + pattern[index].length;
                    }
                }
            }

            return beLower + occupied +j;
        }
        else {
            if (i == 0) {
                if (patternType[i].equals("AND")) {
                    for (int index = 1; index < pattern.length; index++) {
                        if (pattern[index].equals("OR")) {
                            occupied++;
                        } else if (pattern[index].equals("NSEQ")) {
                            occupied = occupied + 2;
                        } else {
                            occupied = occupied + pattern[index].length;
                        }
                    }
                    return beLower - (timeWindow - occupied - 1);
                }
            } else  {
                for (int index = 0; index < i; index++){
                    if (index!=i){
                        if (pattern[index].equals("OR")) {
                            occupied++;
                        }
                        else if (pattern[index].equals("NSEQ")) {
                            occupied = occupied + 2;
                        }
                        else {
                            occupied = occupied + pattern[index].length;
                        }
                    }
                }
                return beLower + occupied;
            }
        }

        return 0;
    }

    static boolean checkAllBuffer(List<TreeNode> operatorLists) {
        for (int index = 0; index < operatorLists.size(); index++) {
            TreeNode operatorsNode = operatorLists.get(index);
            List<LeafNode> leafNodes = operatorsNode.getChildren();
            if (index ==0){
                for (int index2 = 1; index2 < leafNodes.size(); index2++) {
                    if (leafNodes.get(index2).getEventList().size() == 0) {
                        return false;
                    }
                }
            } else {
                for (int index2 = 0; index2 < leafNodes.size(); index2++) {
                    if (leafNodes.get(index2).getEventList().size() == 0) {
                        return false;
                    }
                }
            }

        }
        return true;
    }

    public static void  AndMatch(AndNode andNode, Boolean benchMark, Event benchmarkEvent, int timeWindow, String[][] pattern, String[] patternType) {
        List<match> partialMatchs = new ArrayList<>();
        List<LeafNode> andChildren = andNode.getChildren();
        if (benchMark) {
            benchmarkEvent.setVlb(benchmarkEvent.getLower());
            benchmarkEvent.setVub(benchmarkEvent.getUpper());
            for (int index = 1; index <= andChildren.size() - 1 ; index++) {
                int maxBoundForChildNode = benchmarkEvent.getUpper() + timeWindow - countInterval(0, index, pattern, patternType);
                List<Event> matchableEventForChildNode = new ArrayList<Event>();

                for (Event e : andChildren.get(index).getEventList()) {
                    e.setVlb(e.getLower());
                    if (e.getUpper() <= maxBoundForChildNode) {
                        e.setVub(e.getUpper());
                        matchableEventForChildNode.add(e);
                    } else if (e.getUpper() > maxBoundForChildNode && e.getVlb() <= maxBoundForChildNode) {
                        e.setVub(maxBoundForChildNode);
                        matchableEventForChildNode.add(e);
                    }
                }
                if (index == 1) {
                    for (Event e : matchableEventForChildNode) {

                        int[] vlbList = new int[andChildren.size()];
                        int[] vubList = new int[andChildren.size()];
                        int[] intervalList = new int[andChildren.size()];
                        int[] eventIdList = new int[andChildren.size()];
                        vlbList[0] = benchmarkEvent.getVlb();
                        vubList[0] = benchmarkEvent.getVub();
                        intervalList[0] = benchmarkEvent.getUpper() - benchmarkEvent.getLower() + 1;
                        eventIdList[0] = benchmarkEvent.getId();

                        vlbList[1] = e.getVlb();
                        vubList[1] = e.getVub();
                        if (canFormUniqueSequence(vlbList,vubList)){
                            intervalList[1] = e.getUpper() - benchmarkEvent.getUpper() + 1;
                            eventIdList[1] = e.getId();
                            match newMatch = new match(vlbList, vubList, intervalList,eventIdList);
                            partialMatchs.add(newMatch);
                        }
                    }
                }
                else {
                    if (partialMatchs.size() > 0) {
                        List<match> pms = new ArrayList<>();
                        for (Event e : matchableEventForChildNode) {
                            for (match m : partialMatchs) {
                                int[] vlbList = new int[andChildren.size()];
                                int[] vubList = new int[andChildren.size()];
                                int[] intervalList = new int[andChildren.size()];
                                int[] eventIdList = new int[andChildren.size()];
                                for (int k = 0; k <= index - 1; k++) {
                                    vlbList[k] = m.getLowerList()[k];
                                    vubList[k] =  m.getUpperList()[k];
                                    intervalList[k] = m.getIntervalList()[k];
                                    eventIdList[k] = m.eventIdList[k];
                                }

                                vlbList[index] = e.getVlb();
                                vubList[index] = e.getVub();

                                if (canFormUniqueSequence(vlbList,vubList)){
                                    intervalList[index] = e.getUpper() - benchmarkEvent.getUpper() + 1;
                                    eventIdList[index] = e.getId();
                                    match newMatch = new match(vlbList, vubList, intervalList,eventIdList);
                                    pms.add(newMatch);
                                }
                            }
                        }
                        partialMatchs.clear();
                        partialMatchs.addAll(pms);
                    }
                    else {
                        break;
                    }
                }
            }
        }
        else {
            for (int index = 0; index <= andChildren.size()-1 ; index++) {
                List<match> tempoPartialMatchs = new ArrayList<>();
                int maxBoundForChildNode = benchmarkEvent.getUpper() + timeWindow - countInterval(andNode.getI(), index, pattern, patternType);
                if (index == 0) {
                    for (Event e : andChildren.get(index).getEventList()) {
                        int[] vlbList = new int[andChildren.size()];
                        int[] vubList = new int[andChildren.size()];
                        int[] intervalList = new int[andChildren.size()];
                        int[] eventIdList = new int[andChildren.size()];
                        e.setVlb(e.getLower());
                        if (e.getUpper() <= maxBoundForChildNode) {
                            e.setVub(e.getUpper());
                            vlbList[0] = e.getVlb();
                            vubList[0] = e.getVub();
                            intervalList[0] = e.getUpper() - e.getLower() + 1;
                            eventIdList[0] = e.getId();
                            match newMatch = new match(vlbList, vubList, intervalList,eventIdList);
                            partialMatchs.add(newMatch);
                        }
                        else if (e.getUpper() > maxBoundForChildNode && e.getVlb() <= maxBoundForChildNode) {
                            e.setVub(maxBoundForChildNode);
                            vlbList[0] = e.getVlb();
                            vubList[0] = e.getVub();
                            intervalList[0] = e.getUpper() - e.getLower() + 1;
                            eventIdList[0] = e.getId();
                            match newMatch = new match(vlbList, vubList, intervalList,eventIdList);
                            partialMatchs.add(newMatch);
                        }
                    }
                }
                else {
                    if (partialMatchs.size() > 0) {
                        List<Event> matchableEventForChildNode = new ArrayList<Event>();
                        for (Event e : andChildren.get(index).getEventList()) {
                            e.setVlb(e.getLower());
                            if (e.getUpper() <= maxBoundForChildNode) {
                                e.setVub(e.getUpper());
                                matchableEventForChildNode.add(e);
                            } else if (e.getUpper() > maxBoundForChildNode && e.getVlb() <= maxBoundForChildNode) {
                                e.setVub(maxBoundForChildNode);
                                matchableEventForChildNode.add(e);
                            }
                        }
                        for (Event e : matchableEventForChildNode) {
                            for (match m : partialMatchs) {
                                int[] vlbList = new int[andChildren.size()];
                                int[] vubList = new int[andChildren.size()];
                                int[] intervalList = new int[andChildren.size()];
                                int[] eventIdList = new int[andChildren.size()];

                                for (int k = 0; k <= index - 1; k++) {
                                    vlbList[k] = m.getLowerList()[k];
                                    vubList[k] = m.getUpperList()[k];
                                    intervalList[k] = m.getIntervalList()[k];
                                    eventIdList[k] = m.getEventIdList()[k];
                                }
                                vlbList[index] = e.getVlb();
                                vubList[index] = e.getVub();

                                if (canFormUniqueSequence(vlbList,vubList)){
                                    intervalList[index] = e.getUpper() - e.getLower() + 1;
                                    eventIdList[index] = e.getId();
                                    trimInnerMatch4SEQ(vlbList, vubList, index);
                                    match newMatch = new match(vlbList, vubList, intervalList,eventIdList);
                                    tempoPartialMatchs.add(newMatch);
                                }
                            }
                        }
                        partialMatchs.clear();
                        partialMatchs.addAll(tempoPartialMatchs);
                    }
                }
            }
        }

        andNode.setPartialmatchs(partialMatchs);
    }

    public static void  SeqMatch(SeqNode seqNode, Boolean benchMark, Event benchmarkEvent, int timeWindow, String[][] pattern, String[] patternType) {
        List<match> partialMatchs = new ArrayList<>();
        List<LeafNode> seqChildren = seqNode.getChildren();
        if (benchMark) {
            benchmarkEvent.setVlb(benchmarkEvent.getLower());
            benchmarkEvent.setVub(benchmarkEvent.getUpper());
            for (int index = 1; index <= seqChildren.size() - 1 ; index++) {
                int maxBoundForChildNode = benchmarkEvent.getUpper() + timeWindow - countInterval(0, index, pattern, patternType);
                List<Event> matchableEventForChildNode = new ArrayList<Event>();
                for (Event e : seqChildren.get(index).getEventList()) {
                    e.setVlb(e.getLower());
                    if (e.getUpper() <= maxBoundForChildNode) {
                        e.setVub(e.getUpper());
                        matchableEventForChildNode.add(e);
                    } else if (e.getUpper() > maxBoundForChildNode && e.getVlb() <= maxBoundForChildNode) {
                        e.setVub(maxBoundForChildNode);
                        matchableEventForChildNode.add(e);
                    }
                }

                if (index == 1) {
                    for (Event e : matchableEventForChildNode) {
                        int[] vlbList = new int[seqChildren.size()];
                        int[] vubList = new int[seqChildren.size()];
                        int[] intervalList = new int[seqChildren.size()];
                        int[] eventIdList = new int[seqChildren.size()];
                        vlbList[0] = benchmarkEvent.getVlb();
                        vubList[0] = benchmarkEvent.getVub();
                        intervalList[0] = benchmarkEvent.getUpper() - benchmarkEvent.getLower() + 1;
                        eventIdList[0] = benchmarkEvent.getId();
                        if (benchmarkEvent.getVlb() < e.getVub()) {
                            vlbList[1] = e.getVlb();
                            vubList[1] = e.getVub();
                            eventIdList[1] = e.getId();
                            intervalList[1] = e.getUpper() - e.getLower() + 1;
                            if (vubList[1] <= vubList[0]) {
                                vubList[0] = vubList[1] - 1;
                            }
                            if (vlbList[0] >= vlbList[1]) {
                                vlbList[1] = vlbList[0] + 1;
                            }
                            trimInnerMatch4SEQ(vlbList,vubList,index);
                            match newMatch = new match(vlbList, vubList, intervalList,eventIdList);
                            partialMatchs.add(newMatch);
                        }
                    }
                }
                else {
                    if (partialMatchs.size() > 0) {
                        List<match> pms = new ArrayList<>();
                        for (match m : partialMatchs) {
                            for (Event e : matchableEventForChildNode) {
                                int[] vlbList = new int[seqChildren.size()];
                                int[] vubList = new int[seqChildren.size()];
                                int[] intervalList = new int[seqChildren.size()];
                                int[] eventIdList = new int[seqChildren.size()];
                                for (int k = 0; k <= index - 1; k++) {
                                    vlbList[k] = m.getLowerList()[k];
                                    vubList[k] =  m.getUpperList()[k];
                                    intervalList[k] = m.getIntervalList()[k];
                                    eventIdList[k] = m.eventIdList[k];
                                }
                                if (vlbList[index-1] < e.getVub()){
                                    vlbList[index] = e.getVlb();
                                    vubList[index] = e.getVub();
                                    eventIdList[index] = e.getId();
                                    intervalList[index] = e.getUpper() - e.getLower() + 1;
                                    trimInnerMatch4SEQ(vlbList,vubList,index);
                                    match newMatch = new match(vlbList, vubList, intervalList,eventIdList);
                                    pms.add(newMatch);
                                }
                            }
                        }
                        partialMatchs.clear();
                        partialMatchs.addAll(pms);
                    }
                    else {
                        break;
                    }
                }
            }
        }
        else {
            for (int index = 0; index <= seqChildren.size()-1 ; index++) {
                List<match> tempoPartialMatchs = new ArrayList<>();
                int maxBoundForChildNode = benchmarkEvent.getUpper() + timeWindow - countInterval(seqNode.getI(), index, pattern, patternType);
                if (index == 0) {
                    for (Event e : seqChildren.get(index).getEventList()) {
                        int[] vlbList = new int[seqChildren.size()];
                        int[] vubList = new int[seqChildren.size()];
                        int[] intervalList = new int[seqChildren.size()];
                        int[] eventIdList = new int[seqChildren.size()];
                        e.setVlb(e.getLower());
                        if (e.getUpper() <= maxBoundForChildNode) {
                            e.setVub(e.getUpper());
                            vlbList[0] = e.getVlb();
                            vubList[0] = e.getVub();
                            intervalList[0] = e.getUpper() - e.getLower() + 1;
                            eventIdList[0] = e.getId();
                            match newMatch = new match(vlbList, vubList, intervalList,eventIdList);
                            partialMatchs.add(newMatch);
                        }
                        else if (e.getUpper() > maxBoundForChildNode && e.getVlb() <= maxBoundForChildNode) {
                            e.setVub(maxBoundForChildNode);
                            vlbList[0] = e.getVlb();
                            vubList[0] = e.getVub();
                            intervalList[0] = e.getUpper() - e.getLower() + 1;
                            eventIdList[0] = e.getId();
                            match newMatch = new match(vlbList, vubList, intervalList,eventIdList);
                            partialMatchs.add(newMatch);
                        }
                    }
                }
                else {
                    if (partialMatchs.size() > 0) {
                        List<Event> matchableEventForChildNode = new ArrayList<Event>();
                        for (Event e : seqChildren.get(index).getEventList()) {
                            e.setVlb(e.getLower());
                            if (e.getUpper() <= maxBoundForChildNode) {
                                e.setVub(e.getUpper());
                                matchableEventForChildNode.add(e);
                            } else if (e.getUpper() > maxBoundForChildNode && e.getVlb() <= maxBoundForChildNode) {
                                e.setVub(maxBoundForChildNode);
                                matchableEventForChildNode.add(e);
                            }
                        }
                        for (Event e : matchableEventForChildNode) {
                            for (match m : partialMatchs) {
                                int[] vlbList = new int[seqChildren.size()];
                                int[] vubList = new int[seqChildren.size()];
                                int[] intervalList = new int[seqChildren.size()];
                                int[] eventIdList = new int[seqChildren.size()];
                                if (m.getLowerList()[index - 1] < e.getVub()) {
                                    for (int k = 0; k <= index - 1; k++) {
                                        vlbList[k] = m.getLowerList()[k];
                                        vubList[k] = m.getUpperList()[k];
                                        intervalList[k] = m.getIntervalList()[k];
                                        eventIdList[k] = m.eventIdList[k];
                                    }
                                    vlbList[index] = e.getVlb();
                                    vubList[index] = e.getVub();
                                    intervalList[index] = e.getUpper() - e.getLower() + 1;
                                    eventIdList[index] = e.getId();
                                    trimInnerMatch4SEQ(vlbList, vubList, index);
                                    match newMatch = new match(vlbList, vubList, intervalList,eventIdList);
                                    tempoPartialMatchs.add(newMatch);
                                }
                            }
                        }
                        partialMatchs.clear();
                        partialMatchs.addAll(tempoPartialMatchs);
                    }
                }
            }
        }

        seqNode.setPartialmatchs(partialMatchs);
    }

    public static void  NseqMatch(NSEQNode nseqNode, Boolean benchMark, Event benchmarkEvent, int timeWindow, String[][] pattern, String[] patternType) {
        List<match> partialmatchs = new ArrayList<>();
        if (benchMark) {
            benchmarkEvent.setVlb(benchmarkEvent.getLower());
            benchmarkEvent.setVub(benchmarkEvent.getUpper());
            LeafNode negation = ((LeafNode) nseqNode.getChild(1));
            LeafNode childNode = ((LeafNode) nseqNode.getChild(2));
            int maxBoundForChildNode = benchmarkEvent.getUpper() + timeWindow - countInterval(0, 2, pattern, patternType);
            List<Event> matchableEvent = new ArrayList<Event>();
            for (Event e : childNode.getEventList()) {
                e.setVlb(e.getLower());
                if (e.getUpper() <= maxBoundForChildNode) {
                    e.setVub(e.getUpper());
                    matchableEvent.add(e);
                } else if (e.getUpper() > maxBoundForChildNode && e.getVlb() <= maxBoundForChildNode) {
                    e.setVub(maxBoundForChildNode);
                    matchableEvent.add(e);
                }
            }
            for (Event rightBufEvent : matchableEvent) {
                int[] vlbList = new int[2];
                int[] vubList = new int[2];
                int[] intervalList = new int[2];
                List<Integer> negationLowerList = new ArrayList<>();
                List<Integer> negationUpperList = new ArrayList<>();
                List<Integer> negationIntervalList = new ArrayList<>();
                vlbList[0] = benchmarkEvent.getVlb();
                vubList[0] = benchmarkEvent.getVub();
                intervalList[0] = benchmarkEvent.getUpper() - benchmarkEvent.getLower() + 1;
                if (benchmarkEvent.getVlb() < rightBufEvent.getVub()) {
                    vlbList[1] = rightBufEvent.getVlb();
                    vubList[1] = rightBufEvent.getVub();
                    intervalList[1] = rightBufEvent.getUpper() - rightBufEvent.getLower() + 1;
                    if (vubList[1] <= vubList[0]) {
                        vubList[0] = vubList[1] - 1;
                    }
                    if (vlbList[0] >= vlbList[1]) {
                        vlbList[1] = vlbList[0] + 1;
                    }
                    int[][] eventTimestamps = {
                            {vlbList[0], vubList[0]},
                            {vlbList[1], vubList[1]},
                    };
                    List<Event> negationEventList = negation.getEventList();
                    Boolean existNegEvent = false;
                    if (negationEventList.size() > 0) {
                        outerLoop:
                        for(Event negEvent: negationEventList) {
                            for (int[] eventTimestamp : eventTimestamps) {
                                int lower = eventTimestamp[0];
                                int upper = eventTimestamp[1];
                                if (isOverlap(negEvent, lower, upper)) {
                                    negationLowerList.add(negEvent.getLower());
                                    negationUpperList.add(negEvent.getUpper());
                                    negationIntervalList.add(negEvent.getUpper() - negEvent.getLower() + 1);
                                    break;
                                }
                                else if (negEvent.getLower() > vubList[0] && negEvent.getUpper() < vlbList[1]) {
                                    existNegEvent = true;
                                    break outerLoop;
                                }
                            }
                        }
                    }
                    if (!existNegEvent) {
                        match newMatch = new match(vlbList, vubList, intervalList, negationLowerList, negationUpperList, negationIntervalList);
                        partialmatchs.add(newMatch);
                    }
                }
            }
        }
        else {
            LeafNode leftNode = ((LeafNode) nseqNode.getChild(0));
            LeafNode negationNode = ((LeafNode) nseqNode.getChild(1));
            LeafNode rightNode = ((LeafNode) nseqNode.getChild(2));

            List<Event> matchableEventLeftNode = new ArrayList<Event>();
            List<Event> matchableEventRightNode = new ArrayList<Event>();

            int maxBoundForLeftNode = benchmarkEvent.getUpper() + timeWindow - countInterval(nseqNode.getI(), 0, pattern, patternType);
            int maxBoundForRightNode = benchmarkEvent.getUpper() + timeWindow - countInterval(nseqNode.getI(), 2, pattern, patternType);

            for (Event e : leftNode.getEventList()) {
                e.setVlb(e.getLower());
                if (e.getUpper() <= maxBoundForLeftNode) {
                    e.setVub(e.getUpper());
                    matchableEventLeftNode.add(e);
                } else if (e.getUpper() >  maxBoundForLeftNode && e.getVlb() <=  maxBoundForLeftNode) {
                    e.setVub( maxBoundForLeftNode);
                    matchableEventLeftNode.add(e);
                }
            }
            for (Event e : rightNode.getEventList()) {
                e.setVlb(e.getLower());
                if (e.getUpper() <= maxBoundForRightNode) {
                    e.setVub(e.getUpper());
                    matchableEventRightNode.add(e);
                } else if (e.getUpper() >  maxBoundForRightNode && e.getVlb() <=  maxBoundForRightNode) {
                    e.setVub( maxBoundForRightNode);
                    matchableEventRightNode.add(e);
                }
            }

            for (Event eL : matchableEventLeftNode) {
                for (Event eR : matchableEventRightNode) {

                    int[] vlbList = new int[2];
                    int[] vubList = new int[2];
                    int[] intervalList = new int[2];
                    List<Integer> negationLowerList = new ArrayList<>();
                    List<Integer> negationUpperList = new ArrayList<>();
                    List<Integer> negationIntervalList = new ArrayList<>();
                    vlbList[0] = eL.getVlb();
                    vubList[0] = eL.getVub();
                    intervalList[0] = eL.getUpper() - eL.getLower() + 1;
                    if (eL.getVlb() < eR.getVub()) {
                        vlbList[1] = eR.getVlb();
                        vubList[1] = eR.getVub();
                        intervalList[1] = eR.getUpper() - eR.getLower() + 1;
                        if (vubList[1] <= vubList[0]) {
                            vubList[0] = vubList[1] - 1;
                        }
                        if (vlbList[0] >= vlbList[1]) {
                            vlbList[1] = vlbList[0] + 1;
                        }
                        int[][] eventTimestamps = {
                                {vlbList[0], vubList[0]},
                                {vlbList[1], vubList[1]},
                        };
                        List<Event> negationEventList = negationNode.getEventList();
                        Boolean existNegEvent = false;
                        if (negationEventList.size() > 0) {
                            outerLoop:
                            for(Event negEvent: negationNode.getEventList()) {
                                for (int[] timeStamp : eventTimestamps) {
                                    int lower = timeStamp[0];
                                    int upper = timeStamp[1];
                                    if (isOverlap(negEvent, lower, upper)) {
                                        negationLowerList.add(negEvent.getLower());
                                        negationUpperList.add(negEvent.getUpper());
                                        negationIntervalList.add(negEvent.getUpper() - negEvent.getLower() + 1);
                                        break;
                                    }
                                    else if (negEvent.getLower() > vubList[0] && negEvent.getUpper() < vlbList[1]) {
                                        existNegEvent = true;
                                        break outerLoop;
                                    }
                                }
                            }
                        }
                        if (!existNegEvent) {
                            match newMatch = new match(vlbList, vubList, intervalList, negationLowerList, negationUpperList, negationIntervalList);
                            partialmatchs.add(newMatch);
                        }
                    }

                }
            }
        }
        nseqNode.setPartialmatchs(partialmatchs);
    }

    public static  int countInterval(int i, int j, @NotNull String[][] pattern, @NotNull String[] operatorType) {
        int occupied = 0;
        for (int k = pattern.length - 1; k > i; k--) {
            if (operatorType[k].equals("OR")) {
                occupied++;
            }
            else if (operatorType[k].equals("NSEQ")) {
                occupied = occupied + 2;
            }
            else {
                occupied = occupied + pattern[k].length;
            }
        }


        if (operatorType[i].equals("OR") || operatorType[i].equals("AND")){
            occupied++;
        }
        else if (operatorType[i].equals("NSEQ")) {
            if (j ==2){
                occupied++;
            } else {
                occupied = occupied + 2;
            }
        }
        else {
            occupied = occupied + pattern[i].length - j;
        }
        return occupied;
    }


    public static boolean canFormUniqueSequence(int[] lowerList, int[] upperList) {
        int n = 0;
        for (int i = 0; i < lowerList.length; i++) {
            if (lowerList[i] != 0 || upperList[i] != 0) {
                n = i + 1;
            }
        }

        if (n == 0) {
            return true;
        }


        int[][] ranges = new int[n][2];
        for (int i = 0; i < n; i++) {
            ranges[i][0] = lowerList[i];
            ranges[i][1] = upperList[i];
        }


        return backtrack(ranges, 0, new HashSet<>());
    }

    private static boolean backtrack(int[][] ranges, int index, Set<Integer> chosenElements) {
        if (index == ranges.length) {
            return true;
        }

        int start = ranges[index][0];
        int end = ranges[index][1];

        if (start > end) {
            return false;
        }

        for (int time = start; time <= end; time++) {
            if (!chosenElements.contains(time)) {
                chosenElements.add(time);
                if (backtrack(ranges, index + 1, chosenElements)) {
                    return true;
                }
                chosenElements.remove(time);
            }
        }

        return false;
    }

    public static boolean isOverlap(Event event, int lower, int upper) {

        return Math.max(event.lower, lower) <= Math.min(event.upper, upper);
    }

    public static void trimInnerMatch4SEQ(int[] vlbList,int[] vubList,int index){

        if (vlbList[index-1]>= vlbList[index]){
            vlbList[index] = vlbList[index-1] + 1;
        }
        while (index >= 1){
            if (vubList[index] <= vubList[index-1]){
                vubList[index-1]  =  vubList[index]-1;
            }
            else {
                break;
            }
            index--;
        }
    }

    static int countPatthernLenghth(String[][] pattern, String[] patternType){
        int count = 0;
        for (int index = 0; index < pattern.length; index++) {
            if (patternType[index].equals("NSEQ")){
                count += 2;
            }else {
                count += pattern[index].length;
            }

        }
        return count;
    }

        public static double AverageMemory(
            String inputFilePath, TreeRoot root, String[][] pattern, String[] patternType, int timeWindow, String latencyFile,
            int rounds
    ) throws InterruptedException, IOException {

        Runtime rt = Runtime.getRuntime();
        long totalMemoryCost = 0;

     
        for (int i = 0; i < 3; i++) {
            PETreeSystem(inputFilePath, root, pattern, patternType, timeWindow, latencyFile);
        }

        for (int i = 0; i < rounds; i++) {
            System.gc();                
            Thread.sleep(100);         

            long before = getUsedMemory();

            PETreeSystem(inputFilePath, root, pattern, patternType, timeWindow, latencyFile);

            long after = getUsedMemory();
            long diff = after - before;

            totalMemoryCost += diff;
        }
        return totalMemoryCost / (double) rounds / 1024 / 1024;
    }


}
