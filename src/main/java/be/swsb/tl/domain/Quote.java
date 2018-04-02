package be.swsb.tl.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Quote {
    private final UUID id;
    private String author;
    private String text;
    private LocalDateTime creationTime;

    public Quote(String author, String text) {
        this.id = UUID.randomUUID();
        this.author = author;
        this.text = text;
        this.creationTime = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }
}
