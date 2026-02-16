package org.sparta.aistudy.prompt;

public record TextClassification(
        String text, Sentiment classification
) {
    public enum Sentiment {
        POSITIVE, NEUTRAL, NEGATIVE
    }
}
