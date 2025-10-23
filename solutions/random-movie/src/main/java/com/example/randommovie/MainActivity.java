package com.example.randommovie;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnRandomMovie;
    private TextView tvMovieInfo;
    private List<Movie> availableMovies;
    private SharedPreferences prefs;

    private static final String PREFS_NAME = "RandomMoviePrefs";
    private static final String USED_MOVIES_KEY = "usedMovies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        loadMovies();
        setupButtonClickListener();
    }

    private void initializeViews() {
        btnRandomMovie = findViewById(R.id.btnRandomMovie);
        tvMovieInfo = findViewById(R.id.tvMovieInfo);
        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void loadMovies() {
        try {
            InputStream stream = getAssets().open("movies.json");
            InputStreamReader reader = new InputStreamReader(stream);
            Gson gson = new Gson();

            Movies moviesContainer = gson.fromJson(reader, Movies.class);
            List<Movie> allMovies = moviesContainer.getMovies();

            String usedMoviesJson = prefs.getString(USED_MOVIES_KEY, "");
            List<Movie> usedMovies = new ArrayList<>();

            if (!usedMoviesJson.isEmpty()) {
                Movies usedMoviesContainer = gson.fromJson(usedMoviesJson, Movies.class);
                if (usedMoviesContainer != null && usedMoviesContainer.getMovies() != null) {
                    usedMovies = usedMoviesContainer.getMovies();
                }
            }

            availableMovies = new ArrayList<>();
            for (Movie movie : allMovies) {
                if (!isMovieUsed(movie, usedMovies)) {
                    availableMovies.add(movie);
                }
            }

            Collections.shuffle(availableMovies);

            updateUI();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Ошибка загрузки фильмов", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isMovieUsed(Movie movie, List<Movie> usedMovies) {
        for (Movie usedMovie : usedMovies) {
            if (usedMovie.getTitle().equals(movie.getTitle()) &&
                    usedMovie.getYear() == movie.getYear()) {
                return true;
            }
        }
        return false;
    }

    private void setupButtonClickListener() {
        btnRandomMovie.setOnClickListener(v -> selectRandomMovie());
    }

    private void selectRandomMovie() {
        if (availableMovies.isEmpty()) {
            tvMovieInfo.setText("Все фильмы были просмотрены!\n\nНажмите кнопку ниже, чтобы начать заново.");
            btnRandomMovie.setText("Начать заново");
            btnRandomMovie.setOnClickListener(v -> resetMovieSelection());
            return;
        }

        Movie selectedMovie = availableMovies.remove(0);
        displayMovieInfo(selectedMovie);
        saveUsedMovie(selectedMovie);
    }

    private void displayMovieInfo(Movie movie) {
        String movieInfo = String.format(
                "🎬 %s\n\n" +
                        "📅 Год выпуска: %d\n" +
                        "⏱ Длительность: %d мин\n" +
                        "🎭 Жанр: %s\n" +
                        "🎬 Режиссер: %s\n" +
                        "⭐ Рейтинг: %.1f/10",
                movie.getTitle(),
                movie.getYear(),
                movie.getDuration(),
                movie.getGenre(),
                movie.getDirector(),
                movie.getRating()
        );

        tvMovieInfo.setText(movieInfo);

        if (availableMovies.isEmpty()) {
            btnRandomMovie.setText("Начать заново");
        }
    }

    private void saveUsedMovie(Movie movie) {
        String usedMoviesJson = prefs.getString(USED_MOVIES_KEY, "");
        Gson gson = new Gson();
        Movies usedMoviesContainer;

        if (usedMoviesJson.isEmpty()) {
            usedMoviesContainer = new Movies();
            usedMoviesContainer.setMovies(new ArrayList<>());
        } else {
            usedMoviesContainer = gson.fromJson(usedMoviesJson, Movies.class);
        }

        usedMoviesContainer.getMovies().add(movie);
        String updatedJson = gson.toJson(usedMoviesContainer);
        prefs.edit().putString(USED_MOVIES_KEY, updatedJson).apply();
    }

    private void resetMovieSelection() {
        prefs.edit().remove(USED_MOVIES_KEY).apply();
        loadMovies();
        btnRandomMovie.setText("Выбрать случайный фильм");
        btnRandomMovie.setOnClickListener(v -> selectRandomMovie());
    }

    private void updateUI() {
        if (availableMovies.isEmpty()) {
            tvMovieInfo.setText("Все фильмы были просмотрены!\n\nНажмите кнопку ниже, чтобы начать заново.");
            btnRandomMovie.setText("Начать заново");
        } else {
            tvMovieInfo.setText("Нажмите кнопку, чтобы выбрать случайный фильм!\n\nОсталось фильмов: " + availableMovies.size());
            btnRandomMovie.setText("Выбрать случайный фильм");
        }
    }
}