package com.example.demo.data.pipeline;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class Pipe<T> {

    private List<Filter<T>> listFilters = new ArrayList<>();

    public void addFilter(Filter<T> filter) {
        listFilters.add(filter);
    }

    public T runFilter(T input) throws IOException, ParseException {
        for (Filter<T> filter : listFilters) {
            input = filter.execute(input);
        }
        return input;
    }

}