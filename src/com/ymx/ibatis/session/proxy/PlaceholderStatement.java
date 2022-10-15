package com.ymx.ibatis.session.proxy;

import com.ymx.ibatis.plus.wrapper.statement.OriginalStatement;

import java.util.List;

public class PlaceholderStatement {
    private OriginalStatement originalStatement;
    private List<String> paramNameList;
    public PlaceholderStatement(OriginalStatement os, List<String> paramNameList){
        this.originalStatement = os;
        this.paramNameList = paramNameList;
    }

    public void setOriginalStatement(OriginalStatement originalStatement) {
        this.originalStatement = originalStatement;
    }

    public void setParamNameList(List<String> paramNameList) {
        this.paramNameList = paramNameList;
    }

    public List<String> getParamNameList() {
        return paramNameList;
    }

    public OriginalStatement getOriginalStatement() {
        return originalStatement;
    }
}
