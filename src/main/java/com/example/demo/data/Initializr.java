package com.example.demo.data;

import com.example.demo.repository.IssuerRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.data.pipeline.Pipe;
import com.example.demo.data.pipeline.impl.FilterOne;
import com.example.demo.data.pipeline.impl.FilterThree;
import com.example.demo.data.pipeline.impl.FilterTwo;
import com.example.demo.model.Issuer;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Initializr {

    private final IssuerRepository IssuerRepository;
    private final StockRepository stockRepository;

    @PostConstruct
    private void initializeData() throws IOException, ParseException {
       long startTime = System.nanoTime();

        Pipe<List<Issuer>> pipe = new Pipe<>();
        pipe.addFilter(new FilterOne(IssuerRepository));
        pipe.addFilter(new FilterTwo(IssuerRepository, stockRepository));
        pipe.addFilter(new FilterThree(IssuerRepository, stockRepository));
        pipe.runFilter(null);

        long endTime = System.nanoTime();
        long durationInMillis = (endTime - startTime) / 1_000_000;

        long hours = durationInMillis / 3_600_000;
        long minutes = (durationInMillis % 3_600_000) / 60_000;
        long seconds = (durationInMillis % 60_000) / 1_000;

        System.out.printf("Total time for all filters to complete: %02d hours, %02d minutes, %02d seconds%n", hours, minutes, seconds);
    }

}
