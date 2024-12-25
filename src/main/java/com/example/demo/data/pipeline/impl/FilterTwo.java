package com.example.demo.data.pipeline.impl;

import com.example.demo.model.Issuer;
import com.example.demo.repository.IssuerRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.data.Parser;
import com.example.demo.data.pipeline.Filter;
import com.example.demo.model.Stock;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterTwo implements Filter<List<Issuer>> {

    private final IssuerRepository issuerRepository;
    private final StockRepository stockRepository;
    private static final String url = "https://www.mse.mk/mk/stats/symbolhistory/";

    public FilterTwo(IssuerRepository issuerRepository, StockRepository stockRepository) {
        this.issuerRepository = issuerRepository;
        this.stockRepository = stockRepository;
    }

    public List<Issuer> execute(List<Issuer> input) throws IOException {
        List<Issuer> issuers = new ArrayList<>();
        for (Issuer issuer : input) {
            if (issuer.getLastUpdated() == null) {
                for (int i = 1; i <= 10; i++) {
                    int temp = i - 1;
                    LocalDate fromDate = LocalDate.now().minusYears(i);
                    LocalDate toDate = LocalDate.now().minusYears(temp);
                    addHistoricalData(issuer, fromDate, toDate);
                }
            } else {
                issuers.add(issuer);
            }
        }

        return issuers;
    }

    private void addHistoricalData(Issuer issuer, LocalDate fromDate, LocalDate toDate) throws IOException {
        Connection.Response response = Jsoup.connect(url + issuer.getCompanyCode())
                .data("FromDate", fromDate.toString())
                .data("ToDate", toDate.toString())
                .method(Connection.Method.POST)
                .execute();

        Document doc = response.parse();
        Element table = doc.select("table#resultsTable").first();
        if (table != null) {
            Elements rows = table.select("tbody tr");
            for (Element row : rows) {
                Elements columns = row.select("td");
                if (columns.size() > 0) {
                    LocalDate date = Parser.parseDate(columns.get(0).text(), "d.M.yyyy");
                    if (stockRepository.findByDateAndIssuer(date, issuer).isEmpty()) {

                        NumberFormat format = NumberFormat.getInstance(Locale.GERMANY);

                        Double lastTransaction = Parser.parseDouble(columns.get(1).text(), format);
                        Double max = Parser.parseDouble(columns.get(2).text(), format);
                        Double min = Parser.parseDouble(columns.get(3).text(), format);
                        Double average = Parser.parseDouble(columns.get(4).text(), format);
                        Double prom = Parser.parseDouble(columns.get(5).text(), format);
                        Integer quantity = Parser.parseInteger(columns.get(6).text(), format);
                        Integer turnover = Parser.parseInteger(columns.get(7).text(), format);
                        Integer totalTurnover = Parser.parseInteger(columns.get(8).text(), format);

                        if (max != null) {

                            if (issuer.getLastUpdated() == null || issuer.getLastUpdated().isBefore(date)) {
                                issuer.setLastUpdated(date);
                            }

                            Stock stock = new Stock(
                                    date, lastTransaction, max, min, average, prom,
                                    quantity, turnover, totalTurnover);
                            stock.setIssuer(issuer);
                            stockRepository.save(stock);
                            issuer.getHistoricalData().add(stock);
                        }
                    }
                }
            }
        }

        issuerRepository.save(issuer);
    }

}
