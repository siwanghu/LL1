package com.siwanghu.syntaxanalyzer.algorithm;
 
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
 
import com.siwanghu.syntaxanalyzer.bean.Grammar;
import com.siwanghu.syntaxanalyzer.bean.TableItem;
 
public class AnalysisTable {
    private Production production;
    private Map<String, List<String>> firstMap;
    private Map<String, List<String>> followMap;
    private List<TableItem> analysisTable;
 
    public AnalysisTable(Production production) {
        super();
        this.production = production;
        firstMap = new LinkedHashMap<String, List<String>>();
        followMap = new LinkedHashMap<String, List<String>>();
    }
 
    public void createAnalysisTable() {
        if (production.getProductions().size() != 0) {
            production.createTaken();
            calculateFirstSet();
            calculateFollowSet();
            createTable();
        }
    }
 
    private void initFollowSet() {
        for (String symbol : production.getNonTerminatingSymbol()) {
            List<String> list = new ArrayList<String>();
            list.add("#");
            followMap.put(symbol, list);
        }
    }
 
    private void calculateFollowSet() {
        initFollowSet();
        int count = 100;
        for (int i = 0, j = 0; i < production.getNonTerminatingSymbol().size()
                && j < count; i = (i + 1)
                % (production.getNonTerminatingSymbol().size()), j++) {
            String symbol = production.getNonTerminatingSymbol().get(i);
            for (Grammar grammar : production.getProductions()) {
                if (grammar.getRight().contains(symbol)) {
                    for (int k = 0; k < grammar.getRights().size(); k++) {
                        if (grammar.getRights().get(k).equals(symbol)) {
                            // 从此开始
                            boolean canDo = false, meDo = false;
                            for (int n = k; n < grammar.getRights().size()
                                    && !(grammar.getRights().get(n).equals("|"))
                                    && !canDo; n++) {
                                if (n + 1 < grammar.getRights().size()) {
                                    String rightString = grammar.getRights()
                                            .get(n + 1);
                                    if (production.getNonTerminatingSymbol()
                                            .contains(rightString)) {
                                        if (!canDo && !meDo) {
                                            meDo = true;
                                            List<String> leftFirst = firstMap
                                                    .get(rightString);
                                            List<String> symbolFollow = followMap
                                                    .get(symbol);
                                            for (int m = 0; m < leftFirst
                                                    .size(); m++) {
                                                if (!(symbolFollow
                                                        .contains(leftFirst
                                                                .get(m)))
 
                                                ) {
                                                    if (!(leftFirst.get(m)
                                                            .equals("epsilon")))
                                                        if (!(leftFirst.get(m)
                                                                .equals("|")))
                                                            symbolFollow
                                                                    .add(leftFirst
                                                                            .get(m));
                                                }
                                            }
                                            followMap.put(symbol, symbolFollow);
                                        }
                                    } else {
                                        List<String> symbolFollow = followMap
                                                .get(symbol);
                                        if (!(symbolFollow
                                                .contains(rightString))
                                                && !(rightString.equals("|"))) {
                                            symbolFollow.add(rightString);
                                            followMap.put(symbol, symbolFollow);
                                            canDo = true;
                                        }
                                    }
                                }
                            }
                            if (k == grammar.getRights().size() - 1
                                    || grammar.getRights().get(k + 1)
                                            .equals("|") && !canDo) {
                                String leftSymbol = grammar.getLeft();
                                List<String> leftFollow = followMap
                                        .get(leftSymbol);
                                List<String> symbolFollow = followMap
                                        .get(symbol);
                                for (int m = 0; m < leftFollow.size(); m++) {
                                    if (!(symbolFollow.contains(leftFollow
                                            .get(m)))) {
                                        if (!(leftFollow.get(m)
                                                .equals("epsilon")))
                                            if (!(leftFollow.get(m).equals("|")))
                                                symbolFollow.add(leftFollow
                                                        .get(m));
                                    }
                                }
                                followMap.put(symbol, symbolFollow);
                                canDo = true;
                            } else {
                                int nonTerminatingSymbol = 0;
                                int isepsilon = 0;
                                boolean isMe = false;
                                for (int n = k; n < grammar.getRights().size()
                                        && !(grammar.getRights().get(n)
                                                .equals("|")); n++) {
                                    if (n + 1 < grammar.getRights().size()) {
                                        String rightString = grammar
                                                .getRights().get(n + 1);
                                        if (production.getTerminatingSymbol()
                                                .contains(rightString)) {
                                            isMe = true;
                                        }
                                        if (production
                                                .getNonTerminatingSymbol()
                                                .contains(rightString)) {
                                            nonTerminatingSymbol++;
                                            if (hasepsilon(rightString)) {
                                                isepsilon++;
                                            }
                                        }
                                    }
                                }
                                if (nonTerminatingSymbol == isepsilon && !canDo
                                        && !isMe) {
                                    String leftSymbol = grammar.getLeft();
                                    List<String> leftFollow = followMap
                                            .get(leftSymbol);
                                    List<String> symbolFollow = followMap
                                            .get(symbol);
                                    for (int m = 0; m < leftFollow.size(); m++) {
                                        if (!(symbolFollow.contains(leftFollow
                                                .get(m)))) {
                                            if (!(leftFollow.get(m)
                                                    .equals("epsilon")))
                                                if (!(leftFollow.get(m)
                                                        .equals("|")))
                                                    symbolFollow.add(leftFollow
                                                            .get(m));
                                        }
                                    }
                                    followMap.put(symbol, symbolFollow);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
 
    private boolean hasepsilon(String symbol) {
        for (Grammar grammar : production.getProductions()) {
            if (grammar.getLeft().equals(symbol)) {
                int index = grammar.getRights().indexOf("epsilon");
                if (index < 0) {
                    return false;
                } else {
                    if (grammar.getRights().size() == 1
                            && grammar.getRights().contains("epsilon")) {
                        return true;
                    } else {
                        if (index == grammar.getRights().size() - 1
                                && grammar.getRights().get(index - 1)
                                        .equals("|")) {
                            return true;
                        } else {
                            if (index + 1 < grammar.getRights().size()
                                    && grammar.getRights().get(index + 1)
                                            .equals("|") && index == 0) {
                                return true;
                            } else {
                                if (index + 1 < grammar.getRights().size()
                                        && grammar.getRights().get(index + 1)
                                                .equals("|")
                                        && index - 1 > 0
                                        && grammar.getRights().get(index - 1)
                                                .equals("|")) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
 
    private void calculateFirstSet() {
        for (String symbol : production.getNonTerminatingSymbol()) {
            List<String> listFirst = new ArrayList<String>();
            iterativeToFirst(symbol, listFirst);
        }
    }
 
    private void iterativeToFirst(String symbol, List<String> listFirst) {
        for (Grammar grammar : production.getProductions()) {
            if (grammar.getLeft().equals(symbol)) {
                String[] rights = grammar.getRight().split("\\|");
                for (String right : rights) {
                    if (!isStartWithSymbol(right)) {
                        listFirst.add(getStartWithSymbol(right));
                    } else {
                        iterativeToFirst(getStartWithSymbol(right), listFirst);
                    }
                }
            }
        }
        firstMap.put(symbol, listFirst);
    }
 
    private boolean isStartWithSymbol(String symbol) {
        for (String nonTerminatingSymbol : production.getNonTerminatingSymbol()) {
            if (symbol.startsWith(nonTerminatingSymbol)) {
                return true;
            }
        }
        return false;
    }
 
    private String getStartWithSymbol(String symbol) {
        for (String nonTerminatingSymbol : production.getNonTerminatingSymbol()) {
            if (symbol.startsWith(nonTerminatingSymbol)) {
                return nonTerminatingSymbol;
            }
        }
        if (symbol.equals("epsilon")) {
            return "epsilon";
        } else {
            return String.valueOf(symbol.charAt(0));
        }
    }
 
    private void createTable() {
        analysisTable = new ArrayList<TableItem>();
        for (Map.Entry<String, List<String>> entry : firstMap.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            Grammar _grammar = null;
            for (Grammar grammar : production.getProductions()) {
                if (grammar.getLeft().equals(key)) {
                    _grammar = grammar;
                }
            }
            String[] rights = _grammar.getRight().split("\\|");
            for (int i = 0; i < values.size(); i++) {
                if (values.get(i).equals("epsilon")) {
                    List<String> followList = followMap.get(key);
                    for (String follow : followList) {
                        TableItem tableItem = new TableItem();
                        tableItem.setNonTerminatingSymbol(key)
                                .setTerminatingSymbol(follow)
                                .setGrammar(new Grammar(key, "epsilon"))
                                .createTaken();
                        if (!analysisTable.contains(tableItem)) {
                            analysisTable.add(tableItem);
                        }
                    }
                } else {
                    TableItem tableItem = new TableItem();
                    tableItem
                            .setNonTerminatingSymbol(key)
                            .setTerminatingSymbol(values.get(i))
                            .setGrammar(
                                    new Grammar(key, getRight(rights,
                                            values.get(i)))).createTaken();
                    if (!analysisTable.contains(tableItem)) {
                        analysisTable.add(tableItem);
                    }
                }
            }
        }
    }
 
    private String getRight(String[] rights, String right) {
        for (int i = 0; i < rights.length; i++) {
            if (rights[i].startsWith(right)) {
                return rights[i];
            }
        }
        for (int i = 0; i < rights.length; i++) {
            for (int j = 0; j < production.getNonTerminatingSymbol().size(); j++) {
                if (rights[i].startsWith(production.getTerminatingSymbol().get(
                        j))) {
                    return rights[i];
                }
            }
        }
        return rights[0];
    }
     
    public String getBegin(){
        return production.getBegin();
    }
     
    public List<TableItem> getAnalysisTable(){
        return analysisTable;
    }
     
    public Production getProduction(){
        return production;
    }
     
    public void print(){
        for(int i=0;i<analysisTable.size();i++){
            TableItem tableItem=analysisTable.get(i);
            System.out.println(tableItem.getNonTerminatingSymbol()+" "+tableItem.getTerminatingSymbol());
            System.out.println(tableItem.getGrammar());
        }
    }
 
    @Override
    public String toString() {
        String temp = "";
        for (Grammar grammar : production.getProductions()) {
            System.out.println(grammar);
            System.out
                    .println("左部:" + grammar.getLefts().get(0) + "\n" + "右部:");
            for (int i = 0; i < grammar.getRights().size(); i++) {
                System.out.print(grammar.getRights().get(i) + "    ");
            }
            System.out.print("  个数:" + grammar.getRights().size() + "\n");
        }
        temp += "\nFirst集:\n";
        for (Map.Entry<String, List<String>> entry : firstMap.entrySet()) {
            temp += "First(" + entry.getKey() + ")" + "={";
            List<String> firstList = entry.getValue();
            for (String first : firstList) {
                temp += first + " ";
            }
            temp += "}\n";
        }
        temp += "\nFollow集:\n";
        for (Map.Entry<String, List<String>> entry : followMap.entrySet()) {
            temp += "Follow(" + entry.getKey() + ")" + "={";
            List<String> followList = entry.getValue();
            for (String follow : followList) {
                temp += follow + " ";
            }
            temp += "}\n";
        }
        String table="";
        for(int i=0;i<analysisTable.size();i++){
            table+=analysisTable.get(i).toString();
        }
        return temp+table;
    }
}
 