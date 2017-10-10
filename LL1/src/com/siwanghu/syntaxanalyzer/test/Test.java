package com.siwanghu.syntaxanalyzer.test;
 
import java.util.LinkedList;
import java.util.List;
 
import com.siwanghu.syntaxanalyzer.algorithm.AnalysisTable;
import com.siwanghu.syntaxanalyzer.algorithm.Production;
import com.siwanghu.syntaxanalyzer.algorithm.SyntaxAnalyzer;
import com.siwanghu.syntaxanalyzer.bean.Grammar;
 
public class Test {
    public static void main(String[] args) {
        /*
         * System.out.println("The Productions of G"); Grammar g1 = new
         * Grammar("S", "Qc|c"); Grammar g2 = new Grammar("Q", "Rb|b"); Grammar
         * g3 = new Grammar("R", "Sa|a");
         * 
         * List<Grammar> g_productions = new LinkedList<Grammar>();
         * g_productions.add(g3); g_productions.add(g2); g_productions.add(g1);
         * 
         * Production g_production = new Production(g_productions);
         * g_production.createLL1(); System.out.print(g_production);
         * System.out.println("end G\n");
         * 
         * AnalysisTable g_analysisTable=new AnalysisTable(g_production);
         * g_analysisTable.createAnalysisTable();
         * System.out.println(g_analysisTable);
         */
 
        Grammar h1 = new Grammar("E", "E+T|T");
        Grammar h2 = new Grammar("T", "T*F|F");
        Grammar h3 = new Grammar("F", "(E)|i");
 
        List<Grammar> h_productions = new LinkedList<Grammar>();
        h_productions.add(h1);
        h_productions.add(h2);
        h_productions.add(h3);
 
        Production h_production = new Production(h_productions, "E");
        h_production.createLL1();
 
        AnalysisTable h_analysisTable = new AnalysisTable(h_production);
        h_analysisTable.createAnalysisTable();
 
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer(h_analysisTable);
        try {
            if (syntaxAnalyzer.syntaxAnalyer("i*i+i")) {
                System.out.println("语法正确!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
 
        try {
            if (syntaxAnalyzer.syntaxAnalyer("(i*i)+(i+i)")) {
                System.out.println("语法正确!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
 
        try {
            if (syntaxAnalyzer.syntaxAnalyer("(i*i)(+(i+i)")) {
                System.out.println("语法正确!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
 
    }
 
}