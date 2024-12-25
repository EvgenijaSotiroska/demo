package com.example.demo.data.pipeline.impl;

import com.example.demo.model.Issuer;
import com.example.demo.repository.IssuerRepository;
import com.example.demo.data.pipeline.Filter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

public class FilterOne implements Filter<List<Issuer>> {

    private final IssuerRepository issuerRepository;
    private static final String url = "https://www.mse.mk/mk/stats/symbolhistory/kmb";

    public FilterOne(IssuerRepository issuerRepository) {
        this.issuerRepository = issuerRepository;
    }

    @Override
    public List<Issuer> execute(List<Issuer> input) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Element el = doc.select("select#Code").first();
        if (el != null) {
            Elements options = el.select("option");
            for (Element option : options) {
                String code = option.attr("value");
                if (!code.isEmpty() && code.matches("^[a-zA-Z]+$")) {
                    if (issuerRepository.findByCompanyCode(code).isEmpty()) {
                        issuerRepository.save(new Issuer(code));
                    }
                }
            }
        }

        return issuerRepository.findAll();
    }
}
