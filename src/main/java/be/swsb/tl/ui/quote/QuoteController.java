package be.swsb.tl.ui.quote;

import be.swsb.tl.domain.Quote;
import be.swsb.tl.service.quote.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.websocket.server.PathParam;
import java.util.Optional;

@Controller
@RequestMapping("/quote")
public class QuoteController {

    private static final String MODEL_REF = "quote";

    private QuoteService quoteService;

    @Autowired
    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @GetMapping("/create")
    public ModelAndView showQuoteForm() {
        String templateName = "quote-form";
        return new ModelAndView(templateName, MODEL_REF, QuoteModel.initialQuote());
    }

    @PostMapping("/createQuote")
    public RedirectView createQuote(@ModelAttribute(value = MODEL_REF) QuoteModel enteredQuote, RedirectAttributes rats) {
        Quote savedQuote = quoteService.save(QuoteMapper.toDomain(enteredQuote));

        rats.addFlashAttribute("message", "Quote saved");

        return new RedirectView(String.format("/quote/%s", savedQuote.getId().toString()));
    }

    @GetMapping("/{id}")
    public ModelAndView showQuote(@PathVariable("id") String quoteId) {
        Optional<Quote> quote = quoteService.findQuote(quoteId);

        String templateName = "quote-detail";

        return new ModelAndView(templateName, MODEL_REF, QuoteMapper.toModel(quote));
    }
}
