package be.swsb.tl.ui.quote;

import java.util.Objects;

public class QuoteModel {
    private String author;
    private String text;

    private QuoteModel(){}

    QuoteModel(String author, String text) {
        this.author = author;
        this.text = text;
    }

    public static QuoteModel initialQuote(){
        return new QuoteModel("", "");
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuoteModel that = (QuoteModel) o;
        return Objects.equals(author, that.author) &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {

        return Objects.hash(author, text);
    }
}
