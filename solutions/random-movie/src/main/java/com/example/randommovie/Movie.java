package com.example.randommovie;

public class Movie {
    private String title;
    private int year;
    private int duration;
    private String genre;
    private String director;
    private double rating;

    public Movie() {}

    public Movie(String title, int year, int duration, String genre, String director, double rating) {
        this.title = title;
        this.year = year;
        this.duration = duration;
        this.genre = genre;
        this.director = director;
        this.rating = rating;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}

