package com.siwanghu.syntaxanalyzer.bean;
 
public class TableItem {
    private String nonTerminatingSymbol;
    private String terminatingSymbol;
    private Grammar grammar;
 
    public String getNonTerminatingSymbol() {
        return nonTerminatingSymbol;
    }
 
    public TableItem setNonTerminatingSymbol(String nonTerminatingSymbol) {
        this.nonTerminatingSymbol = nonTerminatingSymbol;
        return this;
    }
 
    public String getTerminatingSymbol() {
        return terminatingSymbol;
    }
 
    public TableItem setTerminatingSymbol(String terminatingSymbol) {
        this.terminatingSymbol = terminatingSymbol;
        return this;
    }
 
    public Grammar getGrammar() {
        return grammar;
    }
 
    public TableItem setGrammar(Grammar grammar) {
        this.grammar = grammar;
        return this;
    }
     
    public TableItem createTaken(){
        grammar.createTaken();
        return this;
    }
 
    @Override
    public String toString() {
        String temp="\n";
        temp+="��:"+grammar.getLefts().get(0)+"\n";
        temp+="�Ҳ�:";
        for(int i=0;i<grammar.getRights().size();i++){
            temp+=grammar.getRights().get(i)+"  ";
        }
        temp+="���� "+grammar.getRights().size();
        return "TableItem [nonTerminatingSymbol=" + nonTerminatingSymbol
                + ", terminatingSymbol=" + terminatingSymbol + "]"+temp;
    }
 
}
