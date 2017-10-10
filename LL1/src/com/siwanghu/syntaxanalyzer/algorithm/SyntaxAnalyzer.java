package com.siwanghu.syntaxanalyzer.algorithm;
 
import java.util.List;
import java.util.Stack;
 
import com.siwanghu.syntaxanalyzer.bean.Grammar;
import com.siwanghu.syntaxanalyzer.bean.TableItem;
 
public class SyntaxAnalyzer {
    private AnalysisTable analysisTable;
    private Stack<String> stack = new Stack<String>();
 
    public SyntaxAnalyzer(AnalysisTable analysisTable) {
        super();
        this.analysisTable = analysisTable;
    }
 
    public boolean syntaxAnalyer(String syntax) {
        char[] syntaxs = (syntax + "#").replaceAll(" ", "").toCharArray();
        int index = 0;
        stack.clear();
        stack.push("#");
        stack.push(analysisTable.getBegin());
        while (!(stack.peek().equals("#"))) {
            if (stack.peek().equals(String.valueOf(syntaxs[index]))) {
                index++;
                stack.pop();
            } else {
                TableItem tableItem = getTableItem(stack.peek(),
                        String.valueOf(syntaxs[index]));
                if (tableItem == null) {
                    throw new RuntimeException("输入的句子语法错误!");
                } else {
                    List<String> nextList = tableItem.getGrammar().getRights();
                    if (nextList.size() == 1
                            && isterminatingSymbol(nextList.get(0))) {
                        stack.pop();
                        index++;
                    } else {
                        if (nextList.size() == 1 && isEpsilon(nextList.get(0))) {
                            stack.pop();
                        } else {
                            stack.pop();
                            for (int i = nextList.size() - 1; i >= 0; i--) {
                                stack.push(nextList.get(i));
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
 
    private boolean isEpsilon(String symbol) {
        if (symbol.equals("epsilon"))
            return true;
        else {
            return false;
        }
    }
 
    private boolean isterminatingSymbol(String symbol) {
        if (analysisTable.getProduction().getTerminatingSymbol()
                .contains(symbol)&&!isEpsilon(symbol))
            return true;
        else {
            return false;
        }
    }
 
    private TableItem getTableItem(String symbol, String syntax) {
        for (TableItem tableItem : analysisTable.getAnalysisTable()) {
            if (tableItem.getNonTerminatingSymbol().equals(symbol)
                    && tableItem.getTerminatingSymbol().equals(syntax)) {
                return tableItem;
            }
        }
        return null;
    }
}