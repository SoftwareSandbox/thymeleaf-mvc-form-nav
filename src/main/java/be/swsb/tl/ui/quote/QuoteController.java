package be.swsb.tl.ui.quote;

import be.swsb.tl.domain.quote.DomainValidationException;
import be.swsb.tl.domain.quote.Quote;
import be.swsb.tl.service.quote.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Optional;

@Controller
@RequestMapping("quote")
public class QuoteController {

    private static final String MODEL_REF = "quote";

    private QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("create")
    public ModelAndView showQuoteForm(@ModelAttribute(value = MODEL_REF) Optional<QuoteModel> defaultQuote) {
        String templateName = "quote-form";
        return new ModelAndView(templateName, MODEL_REF, defaultQuote.orElse(QuoteModel.initialQuote()));
    }

    @PostMapping("create")
    public RedirectView createQuote(@ModelAttribute(value = MODEL_REF) QuoteModel enteredQuote, RedirectAttributes rats) {
        try {
            Quote savedQuote = quoteService.save(QuoteMapper.toDomain(enteredQuote));

            rats.addFlashAttribute("message", "Quote saved");

            return new RedirectView(savedQuote.getId().toString(),true);
        } catch (DomainValidationException e) {
            rats.addFlashAttribute("message", e.getMessage());
            rats.addFlashAttribute(MODEL_REF, enteredQuote);
            return new RedirectView("create",true);
        }
    }

    @GetMapping("{id}")
    public ModelAndView showQuote(@PathVariable("id") String quoteId) {
        Optional<Quote> quote = quoteService.findQuote(quoteId);

        String templateName = "quote-detail";

        return new ModelAndView(templateName, MODEL_REF, QuoteMapper.toModel(quote));
    }
}
