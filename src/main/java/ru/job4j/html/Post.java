package ru.job4j.html;

public class Post {

    private String text;
    private String date;

    public Post(String text, String date) {
        this.text = text;
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }
}
