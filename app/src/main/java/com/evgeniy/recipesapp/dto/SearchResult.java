package com.evgeniy.recipesapp.dto;

import java.util.List;

public class SearchResult {
    private int offset;
    private int number;
    private List<RecipeMini> results;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<RecipeMini> getResults() {
        return results;
    }

    public void setResults(List<RecipeMini> results) {
        this.results = results;
    }
}
