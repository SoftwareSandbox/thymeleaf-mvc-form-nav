package be.swsb.tl.ui.quote;

import be.swsb.tl.domain.quote.Quote;

import java.util.Optional;

public class QuoteMapper {

    public static Quote toDomain(QuoteModel model) {
        return new Quote(model.getAuthor(), model.getText());
    }

    public static QuoteModel toModel(Optional<Quote> quote) {
        return quote.map(QuoteMapper::toModel).orElse(QuoteModel.initialQuote());
    }

    private static QuoteModel toModel(Quote quote){
        return new QuoteModel(quote.getAuthor(), quote.getText());
    }
}
