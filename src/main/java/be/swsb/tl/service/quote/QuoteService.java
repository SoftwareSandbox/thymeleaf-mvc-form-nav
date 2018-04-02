package be.swsb.tl.service.quote;

import be.swsb.tl.domain.Quote;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class QuoteService {

    private static final Logger logger = Logger.getLogger(QuoteService.class.getName());
    private Map<UUID, Quote> quotes = new HashMap<>();

    public Quote save(Quote newQuote) {
        quotes.put(newQuote.getId(), newQuote);

        logger.info(String.format("Saved Quote[%s, %s] with id: %s", newQuote.getAuthor(), newQuote.getText(), newQuote.getId()));

        return newQuote;
    }

    public Optional<Quote> findQuote(String id) {
        return Optional.ofNullable(quotes.get(UUID.fromString(id)));
    }
}
