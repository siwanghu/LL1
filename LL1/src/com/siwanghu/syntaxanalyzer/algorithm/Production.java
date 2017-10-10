package com.siwanghu.syntaxanalyzer.algorithm;
 
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
 
import com.siwanghu.syntaxanalyzer.bean.Grammar;
 
public class Production {
    private String begin="";                                      //��ʼ����
    private List<Grammar> productions = new LinkedList<Grammar>(); // ����ʽ
    private List<Character> symbols = new ArrayList<Character>(); // ��ʼ����ʽ���ս��
    private List<String> nonTerminatingSymbol = new ArrayList<String>(); // LL(1)�ķ����ս��
    private List<String> terminatingSymbol = new ArrayList<String>(); // LL(1)�ķ��ս��
    private boolean isLL1 = false;
 
    public Production(List<Grammar> productions,String begin) {
        super();
        this.productions = productions;
        this.begin=begin;
        symbolProductions();
    }
 
    public List<Grammar> getProductions() {
        return productions;
    }
 
    public List<Character> getSymbols() {
        return symbols;
    }
 
    public List<String> getNonTerminatingSymbol() {
        return nonTerminatingSymbol;
    }
 
    public List<String> getTerminatingSymbol() {
        return terminatingSymbol;
    }
     
    public String getBegin(){
        return begin;
    }
 
    public void createLL1() {
        if (productions.size() != 0) {
            removeLeftRecursion();
            isLL1 = true;
        }
    }
 
    public void removeLeftRecursion() {
        for (int i = 0; i < symbols.size(); i++) {
            for (int j = 0; j < i; j++) {
                iterativeReplacement(symbols.get(i), symbols.get(j));
            }
            removeLeftRecursion(symbols.get(i));
        }
        no_or_is_terminatingSymbol();
    }
 
    private void symbolProductions() {
        if (productions.size() != 0) {
            for (int i = 0; i < productions.size(); i++) {
                if (!((ArrayList<Character>) symbols).contains(productions
                        .get(i).getLeft().charAt(0))) {
                    symbols.add(productions.get(i).getLeft().charAt(0));
                }
            }
        }
    }
 
    private void no_or_is_terminatingSymbol() {
        for (int i = 0; i < productions.size(); i++) {
            if (!((ArrayList<String>) nonTerminatingSymbol)
                    .contains(productions.get(i).getLeft())) {
                nonTerminatingSymbol.add(productions.get(i).getLeft());
            }
            if (productions.get(i).getLeft() == productions.get(i).getLeft()
                    .charAt(0)
                    + "'") {
                nonTerminatingSymbol.add(productions.get(i).getLeft());
            }
        }
        for (int i = 0; i < productions.size(); i++) {
            String temp = productions.get(i).getRight();
            temp = temp.replace("epsilon", "#");
            for (int j = 0; j < nonTerminatingSymbol.size(); j++) {
                temp = temp.replaceAll(nonTerminatingSymbol.get(j), "");
            }
            temp = temp.replaceAll("\\|", "");
            temp = temp.replaceAll("'", "");
            char[] chars = temp.toCharArray();
            for (int k = 0; k < chars.length; k++) {
                if (chars[k] == '#') {
                    if (!terminatingSymbol.contains("epsilon")) {
                        terminatingSymbol.add("epsilon");
                    }
                } else {
                    if (!terminatingSymbol.contains(String.valueOf(chars[k]))) {
                        terminatingSymbol.add(String.valueOf(chars[k]));
                    }
                }
            }
        }
    }
 
    private void iterativeReplacement(Character left, Character right) {
        ListIterator<Grammar> listIterator = productions.listIterator();
        while (listIterator.hasNext()) {
            String inRight = "";
            Grammar grammar = listIterator.next();
            if (grammar.getLeft().equals(left.toString())) {
                boolean isReplacement = false;
                String[] rights = grammar.getRight().split("\\|");
                for (int i = 0; i < rights.length; i++) {
                    if (rights[i].startsWith(right.toString())) {
                        isReplacement = true;
                    }
                }
                if (isReplacement) {
                    ListIterator<Grammar> _listIterator = productions
                            .listIterator();
                    while (_listIterator.hasNext()) {
                        Grammar _grammar = _listIterator.next();
                        if (_grammar.getLeft().equals(right.toString())) {
                            String[] _rights = _grammar.getRight().split("\\|");
                            for (int i = 0; i < rights.length; i++) {
                                boolean isCheck = false;
                                if (rights[i].startsWith(right.toString())) {
                                    isCheck = true;
                                    for (int j = 0; j < _rights.length; j++) {
                                        String temp = rights[i];
                                        inRight += (temp.replaceFirst(
                                                right.toString(), _rights[j]) + "|");
                                    }
                                }
                                if (!isCheck) {
                                    inRight += (rights[i] + "|");
                                }
                            }
                        }
                    }
                    if (inRight.length() != 0) {
                        listIterator.remove();
                        listIterator.add(new Grammar(left.toString(), inRight
                                .substring(0, inRight.length() - 1)));
                    }
                }
            }
        }
    }
 
    private void removeLeftRecursion(Character left) {
        ListIterator<Grammar> listIterator = productions.listIterator();
        while (listIterator.hasNext()) {
            Grammar grammar = listIterator.next();
            if (grammar.getLeft().equals(left.toString())) {
                String[] rights = grammar.getRight().split("\\|");
                boolean isLeftRecursion = false;
                for (int i = 0; i < rights.length; i++) {
                    if (rights[i].startsWith(left.toString())) {
                        isLeftRecursion = true;
                    }
                }
                if (isLeftRecursion) {
                    listIterator.remove();
                    String oneRight = "", twoRight = "";
                    for (int i = 0; i < rights.length; i++) {
                        if (!rights[i].startsWith(left.toString())) {
                            oneRight += (rights[i]
                                    .concat(left.toString() + "'") + "|");
                        } else {
                            twoRight += (rights[i].replaceFirst(
                                    left.toString(), "").concat(
                                    left.toString() + "'") + "|");
                        }
                    }
                    listIterator.add(new Grammar(left.toString(), oneRight
                            .substring(0, oneRight.length() - 1)));
                    listIterator.add(new Grammar(left.toString() + "'",
                            twoRight.concat("epsilon")));
                }
            }
        }
    }
 
    public void createTaken() {
        if (isLL1) {
            for (Grammar grammar : productions) {
                grammar.getLefts().add(grammar.getLeft());
                String right = grammar.getRight();
                if (right.equals("epsilon")) {
                    grammar.getRights().add(right);
                } else {
                    while (right.contains("epsilon")) {
                        right=right.replaceFirst("epsilon", "#");
                    }
                    if (right.length() == 1) {
                        grammar.getRights().add(right);
                    } else {
                        for (int i = 0; i < right.length(); i++) {
                            if ((i + 1 < right.length())
                                    && String.valueOf(right.charAt(i + 1))
                                            .equals("'")) {
                                grammar.getRights().add(right.charAt(i) + "'");
                            } else {
                                if (!(String.valueOf(right.charAt(i))
                                        .equals("'"))) {
                                    if (String.valueOf(right.charAt(i)).equals(
                                            "#")) {
                                        grammar.getRights().add("epsilon");
                                    } else {
                                        grammar.getRights()
                                                .add(String.valueOf(right
                                                        .charAt(i)));
                                    }
                                }
                            }
                        }
                    }
 
                }
            }
        }
    }
 
    @Override
    public String toString() {
        String temp = "���ս��: ";
        for (int i = 0; i < nonTerminatingSymbol.size(); i++) {
            temp += nonTerminatingSymbol.get(i) + " ";
        }
        temp += "  ����:" + nonTerminatingSymbol.size();
        temp += "\n�ս��: ";
        for (int i = 0; i < terminatingSymbol.size(); i++) {
            temp += terminatingSymbol.get(i) + "  ";
        }
        temp += "  ����:" + terminatingSymbol.size();
        temp += "\n������ݹ����ķ�:\n";
        for (int i = 0; i < productions.size(); i++) {
            temp += (productions.get(i) + "\n");
        }
        return temp;
    }
}