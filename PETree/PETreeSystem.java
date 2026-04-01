package org.PETree;

import com.sun.istack.internal.NotNull;
import org.com.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static org.PETree.function.*;

public class PETreeSystem {
    public static Event eventProcess(String line) {
        String[] parts = line.split(",");

        String eventType = parts[0].trim();
        int lower = Integer.parseInt(parts[3].trim());
        int upper = Integer.parseInt(parts[4].trim());
        return new Event(lower, upper, eventType);
    }
    public static boolean storeEvent(TreeRoot root, Event event, String[][] pattern) {
        int i = 0;
        int j = 0;
        outerLoop:
        while (i < pattern.length) {
            for (; j < pattern[i].length; j++) {
                if (pattern[i][j].equals(event.getEventType())) {
                    break outerLoop;
                }
            }
            i++;
            j = 0;
        }
        if ( i < pattern.length) {
            List<TreeNode> operatorLists = root.getOperatorLists();
            TreeNode targetOpetators = operatorLists.get(i);
            LeafNode targetLeafs = (LeafNode) targetOpetators.getChild(j);
            targetLeafs.getEventList().add(event);
            return  true;
        }
        return false;
    }

    public static void updateEvent(List<TreeNode> operatorLists, int beLower, @NotNull String[][] pattern, @NotNull String[] patternType, int timeWindow) {
        for (int i = 0; i < operatorLists.size(); i++) {
            TreeNode operatorNode = operatorLists.get(i);
            for (int j = 0; j < operatorNode.getChildren().size(); j++) {
                LeafNode leafNodes = (LeafNode)   operatorLists.get(0).getChildren().get(j);

                if (i != 0 || j !=0) {
                    int minBound = countMinBound4LeafNode(i, j, beLower,timeWindow,pattern, patternType);
                    ListIterator<Event> listIterator = leafNodes.getEventList().listIterator();
                    while (listIterator.hasNext()) {
                        Event event = listIterator.next();
                        if (event.getUpper() < minBound) {
                            listIterator.remove();
                        }
                    }
                }
            }
        }
    }

    public static void matchPhase(Event benchMarkEvent, List<TreeNode> operatorLists, String[][] pattern, String[] patternType, int timeWindow) {
        Boolean existNullMatch = false;
        int index4Stop = -1;
        for (int index = 0; index < operatorLists.size(); index++) {
            TreeNode currentOperator = operatorLists.get(index);
            if (index != 0){
                if (currentOperator instanceof NSEQNode) {
                    NseqMatch((NSEQNode) currentOperator,false,benchMarkEvent,timeWindow,pattern, patternType);
                    if (((NSEQNode) currentOperator).getPartialmatchs().size() == 0){
                        existNullMatch = true;
                        index4Stop = index;
                        break;
                    }
                }
                else if (currentOperator instanceof AndNode) {
                    AndMatch((AndNode) currentOperator,false,benchMarkEvent,timeWindow,pattern, patternType);
                    if (((AndNode) currentOperator).getPartialmatchs().size() == 0){
                        existNullMatch = true;
                        index4Stop = index;
                        break;
                    }
                }
                else {
                    SeqMatch((SeqNode) currentOperator,false,benchMarkEvent,timeWindow,pattern, patternType);
                    if (((SeqNode) currentOperator).getPartialmatchs().size() == 0){
                        existNullMatch = true;
                        index4Stop = index;
                        break;
                    }
                }
            }

            else {
                if (currentOperator instanceof NSEQNode) {
                    NseqMatch((NSEQNode) currentOperator,true,benchMarkEvent,timeWindow,pattern, patternType);
                    if (((NSEQNode) currentOperator).getPartialmatchs().size() == 0){
                        existNullMatch = true;
                        index4Stop = index;
                        break;
                    }
                }
                else if (currentOperator instanceof AndNode) {
                    AndMatch((AndNode) currentOperator,true,benchMarkEvent,timeWindow,pattern, patternType);
                    if (((AndNode) currentOperator).getPartialmatchs().size() == 0){
                        existNullMatch = true;
                        index4Stop = index;
                        break;
                    }
                }
                else {
                    SeqMatch((SeqNode) currentOperator,true,benchMarkEvent,timeWindow,pattern, patternType);
                    if (((SeqNode) currentOperator).getPartialmatchs().size() == 0){
                        existNullMatch = true;
                        index4Stop = index;
                        break;
                    }
                }
            }
        }

        if (existNullMatch){
            for (int index = 0; index < operatorLists.size(); index++) {
                operatorLists.get(index).clearMatch();
            }
        }
        else {
            if(pattern.length>1){
                List<m> mL = countPro1(operatorLists);
                assembleRound(operatorLists, pattern, patternType);
            }
            else {
                List<m> mL =  countPro1(operatorLists);

                for (int index = 0; index < operatorLists.size(); index++) {
                    operatorLists.get(index).clearMatch();
                }
            }
        }
    }

    private static void assembleRound(List<TreeNode> operatorLists,String[][] pattern, String[] patternType) {
        int patternTypeIndex = operatorLists.size()-1;
        TreeNode firstOperator = operatorLists.get(0);
        String preType = "";
        List<assembledMatch> matchList = null;
        List<assembledMatch> temoMatchList = null;
        int patternLength = countPatthernLenghth(pattern, patternType);
        int currentIndexStart = 0;
        for (int index = 0; index < operatorLists.size(); index++) {
            TreeNode currentOperator = operatorLists.get(index);
            List<match> matches =  currentOperator.getMatches();
            if (index == 0) {
                for (match m: matches) {
                    assembledMatch newMatch = new assembledMatch(patternLength);
                    for (int i = 0; i< pattern[index].length; i++){
                        newMatch.setVlbListValue(i,m.getLowerList()[i]);
                        newMatch.setVubListValue(i,m.getUpperList()[i]);
                        newMatch.setIntervalListValue(i,m.getIntervalList()[i]);
                    }
                    matchList.add(newMatch);
                }
                currentIndexStart = currentIndexStart + pattern[index].length;
            }
            else {
                if (pattern[index-1].equals("SEQ") ){
                    if (pattern[index].equals("AND")){
                        for (assembledMatch totalmatch: matchList) {
                            for (match m: matches) {
                                if (totalmatch.getVlbList()[currentIndexStart - 1] < m.getUpperList()[pattern[index].length-1]){
                                    assembledMatch newMatch = new assembledMatch(patternLength);
                                    for (int i = 0; i< pattern[index].length; i++){
                                        newMatch.setVlbListValue(i, totalmatch.getVlbListValue(i));
                                        newMatch.setVubListValue(i,totalmatch.getVubListValue(i));
                                        newMatch.setIntervalListValue(i, totalmatch.getIntervalListValue(i));
                                    }
                                    for (int i = currentIndexStart; i< pattern[index].length; i++){
                                        newMatch.setVlbListValue(i,m.getLowerList()[i]);
                                        newMatch.setVubListValue(i,m.getUpperList()[i]);
                                        newMatch.setIntervalListValue(i,m.getIntervalList()[i]);
                                    }
                                    temoMatchList.add(newMatch);
                                }
                            }
                        }
                        matchList.clear();
                        matchList.addAll(temoMatchList);
                        temoMatchList.clear();
                    }
                    else {
                        for (assembledMatch totalmatch: matchList) {
                            for (match m: matches) {
                                if (totalmatch.getVlbList()[currentIndexStart - 1] < m.getUpperList()[0]){
                                    assembledMatch newMatch = new assembledMatch(patternLength);
                                    for (int i = 0; i< pattern[index].length; i++){
                                        newMatch.setVlbListValue(i, totalmatch.getVlbListValue(i));
                                        newMatch.setVubListValue(i,totalmatch.getVubListValue(i));
                                        newMatch.setIntervalListValue(i, totalmatch.getIntervalListValue(i));
                                    }
                                    for (int i = currentIndexStart; i< pattern[index].length; i++){
                                        newMatch.setVlbListValue(i,m.getLowerList()[i]);
                                        newMatch.setVubListValue(i,m.getUpperList()[i]);
                                        newMatch.setIntervalListValue(i,m.getIntervalList()[i]);
                                    }
                                    temoMatchList.add(newMatch);
                                }
                            }
                        }
                        matchList.clear();
                        matchList.addAll(temoMatchList);
                        temoMatchList.clear();
                    }
                }
                else if(pattern[index-1].equals("NSEQ")){
                    if (pattern[index].equals("AND")){
                        for (assembledMatch totalmatch: matchList) {
                            for (match m: matches) {
                                if (totalmatch.getVlbList()[currentIndexStart - 1] < m.getUpperList()[pattern[index].length-1]){
                                    assembledMatch newMatch = new assembledMatch(patternLength);
                                    for (int i = 0; i< pattern[index].length; i++){
                                        newMatch.setVlbListValue(i, totalmatch.getVlbListValue(i));
                                        newMatch.setVubListValue(i,totalmatch.getVubListValue(i));
                                        newMatch.setIntervalListValue(i, totalmatch.getIntervalListValue(i));
                                    }
                                    for (int i = currentIndexStart; i< pattern[index].length; i++){
                                        newMatch.setVlbListValue(i,m.getLowerList()[i]);
                                        newMatch.setVubListValue(i,m.getUpperList()[i]);
                                        newMatch.setIntervalListValue(i,m.getIntervalList()[i]);
                                    }
                                    temoMatchList.add(newMatch);
                                }
                            }
                        }
                        matchList.clear();
                        matchList.addAll(temoMatchList);
                        temoMatchList.clear();
                    }
                    else {
                        for (assembledMatch totalmatch: matchList) {
                            for (match m: matches) {
                                if (totalmatch.getVlbList()[currentIndexStart - 1] < m.getUpperList()[0]){
                                    assembledMatch newMatch = new assembledMatch(patternLength);
                                    for (int i = 0; i< pattern[index].length; i++){
                                        newMatch.setVlbListValue(i, totalmatch.getVlbListValue(i));
                                        newMatch.setVubListValue(i,totalmatch.getVubListValue(i));
                                        newMatch.setIntervalListValue(i, totalmatch.getIntervalListValue(i));
                                    }
                                    for (int i = currentIndexStart; i< pattern[index].length; i++){
                                        newMatch.setVlbListValue(i,m.getLowerList()[i]);
                                        newMatch.setVubListValue(i,m.getUpperList()[i]);
                                        newMatch.setIntervalListValue(i,m.getIntervalList()[i]);
                                    }
                                    temoMatchList.add(newMatch);
                                }
                            }
                        }
                        matchList.clear();
                        matchList.addAll(temoMatchList);
                        temoMatchList.clear();

                    }
                }
                else if (pattern[index-1].equals("AND")){
                    for (assembledMatch totalmatch: matchList) {
                        for (match m: matches) {
                            if (m.getLowerList()[0] > totalmatch.getVubList()[pattern[index-1].length-1]) {
                                assembledMatch newMatch = new assembledMatch(patternLength);
                                for (int i = 0; i< pattern[index].length; i++){
                                    newMatch.setVlbListValue(i, totalmatch.getVlbListValue(i));
                                    newMatch.setVubListValue(i,totalmatch.getVubListValue(i));
                                    newMatch.setIntervalListValue(i, totalmatch.getIntervalListValue(i));
                                }
                                for (int i = currentIndexStart; i< pattern[index].length; i++){
                                    newMatch.setVlbListValue(i,m.getLowerList()[i]);
                                    newMatch.setVubListValue(i,m.getUpperList()[i]);
                                    newMatch.setIntervalListValue(i,m.getIntervalList()[i]);
                                }
                                temoMatchList.add(newMatch);
                            }
                        }
                    }
                    matchList.clear();
                    matchList.addAll(temoMatchList);
                    temoMatchList.clear();
                }
                currentIndexStart = currentIndexStart + pattern[index].length;
            }
        }
    }



    public static boolean storeEvent1(TreeRoot root, Event event, String[][] pattern) {
        int i = 0;
        int j = 0;
        outerLoop:
        while (i < pattern.length) {
            for (; j < pattern[i].length; j++) {
                if (pattern[i][j].equals(event.getEventType())) {
                    break outerLoop;
                }
            }
            i++;
            j = 0;
        }
        if ( i < pattern.length) {
            List<TreeNode> operatorLists = root.getOperatorLists();
            TreeNode targetOpetators = operatorLists.get(i);
            LeafNode targetLeafs = (LeafNode) targetOpetators.getChild(j);
            targetLeafs.getEventList().add(event);
            System.out.println(targetLeafs.getEventList().get(0).toString() + " add");
            return  true;
        }
        return false;
    }


    public static List<m>  countPro1(List<TreeNode> operatorLists){
        List<m> partialMatchLists = new ArrayList<>();
        List<m> tempoSubPatternTotalMatchLists = new ArrayList<>();
        List<m> SubPatternTotalMatchLists = new ArrayList<>();
        for (int opIndex = operatorLists.size()-1; opIndex>=0; opIndex--){
            TreeNode currentOperator = operatorLists.get(opIndex);
            List<match> subMatchLists = currentOperator.getMatches();
            System.out.println(opIndex +" "+ subMatchLists.size());
        }
        for (int opIndex = operatorLists.size()-1; opIndex>=0; opIndex--){
            TreeNode currentOperator = operatorLists.get(opIndex);
            List<match> subMatchLists = currentOperator.getMatches();
            if (opIndex == operatorLists.size() - 1){
                if (currentOperator instanceof SeqNode) {
                    for (match subMatch: subMatchLists) {
                        int[] vlbList = subMatch.getLowerList();
                        int[] vubList = subMatch.getUpperList();
                        int[] intervalList = subMatch.getIntervalList();
                        int[] eventIdList = subMatch.getEventIdList();
                        for (int eventIndex = vlbList.length-1; eventIndex>=0; eventIndex--){
                            if (eventIndex == vlbList.length-1) {
                                m m1 = new m();
                                for (int i = vlbList[eventIndex]; i <= vubList[eventIndex]; i++){
                                    List<Integer> lastTimPointList = new ArrayList<>();
                                    lastTimPointList.add(i);
                                    m1.addLastTimePointLIST(lastTimPointList);
                                    m1.addCount(1);
                                    m1.addCurrentTimePoint(i);
                                }
                                int x = vubList[eventIndex]-vlbList[eventIndex]+1;
                                m1.xInterval(x);
                                m1.addMatchInfo(String.valueOf(eventIdList[eventIndex]));
                                if (vlbList.length > 1) {
                                    partialMatchLists.add(m1);
                                } else{
                                    SubPatternTotalMatchLists.add(m1);
                                }
                            }
                            else {
                                List<m> tempoPartialMatchLists = new ArrayList<>();
                                for (m pm : partialMatchLists) {
                                    m m1 = new m();
                                    for (int i = vlbList[eventIndex]; i <= vubList[eventIndex]; i++){
                                        int count = 0;
                                        List<Integer> lastTimePointList = new ArrayList<>();
                                        for ( int j = 0; j < pm.getCurrentTimePoint().size(); j++) { //???边界是什么
                                            if (i < pm.getCurrentTimePoint().get(j)) {
                                                count = count + pm.getCount().get(j);
                                                lastTimePointList.addAll(pm.getLastTimePointLIST().get(j));
                                            }
                                        }
                                        if (count>0) {
                                            m1.addLastTimePointLIST(lastTimePointList);
                                            m1.addCount(count);
                                            m1.addCurrentTimePoint(i);
                                        }
                                    }
                                    int x = (vubList[eventIndex]-vlbList[eventIndex]+1) * pm.getTrimInterval();
                                    m1.xInterval((vubList[eventIndex]-vlbList[eventIndex]+1) * pm.getTrimInterval());
                                    m1.addMatchInfo(pm.getMatchInfo());
                                    m1.addMatchInfo(String.valueOf(eventIdList[eventIndex])+",");
                                    if (m1.getCount().size()>0){

                                        if (eventIndex == 0){
                                            SubPatternTotalMatchLists.add(m1);
                                        } else {
                                            tempoPartialMatchLists.add(m1);
                                        }
                                    }
                                }
                                partialMatchLists.clear();
                                partialMatchLists.addAll(tempoPartialMatchLists);
                            }
                        }
                    }
                }
            }

            //不是最尾的事件做的事
            else {
                if(currentOperator instanceof SeqNode) {
                    for (match subMatch: subMatchLists) {
                        int[] vlbList = subMatch.getLowerList();
                        int[] vubList = subMatch.getUpperList();
                        int[] eventId = subMatch.getEventIdList();
                        for (int eventIndex = vlbList.length-1; eventIndex>=0; eventIndex--){
                            if (eventIndex == vlbList.length-1) {
                                List<m> tempoPartialMatchLists = new ArrayList<>();
                                for (m pm : SubPatternTotalMatchLists){
                                    m m1 = new m();
                                    for (int i = vlbList[eventIndex]; i <= vubList[eventIndex]; i++){
                                        int count = 0;
                                        List<Integer> lastTimePointList = new ArrayList<>();
                                        for ( int j = 0; j < pm.getCurrentTimePoint().size(); j++) {
                                            if (i < pm.getCurrentTimePoint().get(j)) {
                                                count = count + pm.getCount().get(j);
                                                lastTimePointList.addAll(pm.getLastTimePointLIST().get(j));
                                            }
                                        }
                                        if (count > 0) {
                                            m1.addLastTimePointLIST(lastTimePointList);
                                            m1.addCount(count);
                                            m1.addCurrentTimePoint(i);
                                        }
                                    }
                                    if (m1.getCount().size()>0){
                                        m1.addMatchInfo(pm.getMatchInfo());
                                        m1.addMatchInfo(String.valueOf(eventId[eventIndex])+",");
                                        int x =(vubList[eventIndex]-vlbList[eventIndex]+1) * pm.getTrimInterval();
                                        m1.xInterval((vubList[eventIndex]-vlbList[eventIndex]+1) * pm.getTrimInterval());
                                        tempoPartialMatchLists.add(m1);
                                    }
                                }
                                if (vlbList.length == 1){
                                    SubPatternTotalMatchLists.clear();
                                    SubPatternTotalMatchLists.addAll(tempoPartialMatchLists);
                                }else {
                                    partialMatchLists.clear();
                                    partialMatchLists.addAll(tempoPartialMatchLists);
                                }
                            }
                            else {
                                List<m> tempoPartialMatchLists = new ArrayList<>();
                                for (m pm : partialMatchLists) {
                                    m m1 = new m();
                                    for (int i = vlbList[eventIndex]; i <= vubList[eventIndex]; i++){
                                        int count = 0;
                                        List<Integer> lastTimePointList = new ArrayList<>();
                                        for ( int j = 0; j < pm.getCurrentTimePoint().size(); j++) { //???边界是什么
                                            if (i < pm.getCurrentTimePoint().get(j)) {
                                                count = count + pm.getCount().get(j);
                                                List<Integer> copy =
                                                        new ArrayList<>(pm.getLastTimePointLIST().get(j));
                                                lastTimePointList.addAll(copy);
                                            }
                                        }
                                        if (count>0) {
                                            m1.addLastTimePointLIST(lastTimePointList);
                                            m1.addCount(count);
                                            m1.addCurrentTimePoint(i);
                                        }
                                    }
                                    int x = (vubList[eventIndex]-vlbList[eventIndex]+1) * pm.getTrimInterval();
                                    m1.xInterval((vubList[eventIndex]-vlbList[eventIndex]+1) * pm.getTrimInterval());
                                    m1.addMatchInfo(pm.getMatchInfo());
                                    m1.addMatchInfo(String.valueOf(eventId[eventIndex])+",");
                                    if (m1.getCount().size()>0){
                                        if (eventIndex == 0){
                                            tempoSubPatternTotalMatchLists.add(m1);
                                        } else {
                                            tempoPartialMatchLists.add(m1);
                                        }
                                    }
                                }
                                partialMatchLists.clear();
                                partialMatchLists.addAll(tempoPartialMatchLists);
                            }
                        }
                    }
                    SubPatternTotalMatchLists.clear();
                    SubPatternTotalMatchLists.addAll(tempoSubPatternTotalMatchLists);
                    tempoSubPatternTotalMatchLists.clear();
                }
            }
        }
        return SubPatternTotalMatchLists;
    }

    public static Long PETreeSystem(String inputFilePath, TreeRoot root, String[][] pattern, String[] patternType, int timeWindow) throws IOException {

        List<TreeNode> operatorLists = root.getOperatorLists();
        int matchingTime = -1;
        Event benchmarkEvent = null;
        long totalTime = 0;
        int intoMatchTimes = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                TreeNode targetOpetators = operatorLists.get(0);
                LeafNode targetLeafs = (LeafNode) targetOpetators.getChild(0);
                Event e = eventProcess2(line);
                System.out.println("now time "+ e.toString());
                if (benchmarkEvent == null && e.getEventType().equals(pattern[0][0])) {
                    benchmarkEvent = e;
                    System.out.println(e.getEventType()+ e.getId() +" is be now...");
                    matchingTime = benchmarkEvent.getUpper() + timeWindow -1;
                }

                if (e.getLower() > matchingTime && matchingTime != -1) {
                    updateEvent(operatorLists, benchmarkEvent.lower, pattern, patternType, timeWindow);
                    deleteBenchMarkEvent(operatorLists);
                    if (checkAllBuffer(operatorLists)){
                        int eventNum = 0;
                        for (int index = 0; index < operatorLists.size(); index++) {
                            TreeNode operatorsNode = operatorLists.get(index);
                            List<LeafNode> leafNodes = operatorsNode.getChildren();
                            if (index ==0){
                                for (int index2 = 1; index2 < leafNodes.size(); index2++) {
                                    eventNum = eventNum + leafNodes.get(index2).getEventList().size();
                                }
                            }
                            else {
                                for (int index2 = 0; index2 < leafNodes.size(); index2++) {
                                    eventNum = eventNum + leafNodes.get(index2).getEventList().size();
                                }
                            }
                        }
                        matchPhase(benchmarkEvent, operatorLists, pattern, patternType, timeWindow);
                        benchmarkEvent = null;
                        if (operatorLists.get(0).getChildren().get(0).getEventList().size() == 0 ) {
                            matchingTime = -1;
                        }
                        else {
                            benchmarkEvent = ((LeafNode) operatorLists.get(0).getChild(0)).getEventList().get(0);
                            System.out.println(benchmarkEvent.getEventType()+ benchmarkEvent.getId() +" is be now...");
                            matchingTime = benchmarkEvent.getUpper() +timeWindow -1;
                        }
                    }
                    else {
                        if (operatorLists.get(0).getChildren().get(0).getEventList().size() == 0) {
                            matchingTime = -1;
                            benchmarkEvent = null;
                        } else {
                            benchmarkEvent = ((LeafNode) operatorLists.get(0).getChild(0)).getEventList().get(0);
                            matchingTime = benchmarkEvent.getUpper() +timeWindow -1;
                            System.out.println(benchmarkEvent.getEventType()+ benchmarkEvent.getId() +" is be now...");
                        }
                    }
                }
                storeEvent1(root,e, pattern);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return totalTime;
    }


}
