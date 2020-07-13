# NightMovieApp for the Nanodegree in Android development


# Meets Specifications
## User Interface - Layout
- Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
- UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated, and favorites.
- UI contains a screen for displaying the details for a selected movie.
- Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.
- Movie Details layout contains a section for displaying trailer videos and user reviews
- Tablet UI uses a Master-Detail layout implemented using fragments. The left fragment is for discovering movies. The right fragment displays the movie details view for the currently selected movie.

## User Interface - Function
- When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.
- When a movie poster thumbnail is selected, the movie details screen is launched \[Phone] or displayed in a fragment \[Tablet].
- When a trailer is selected, app uses an Intent to launch the trailer.
- In the movies detail screen, a user can tap a button(for example, a star) to mark it as a Favorite.

## Network API Implementation
- App queries the /discover/movies endpoint in a background thread to retrieve movies for main activity, sorted (from left to right) by criteria selected in the settings menu. This query can also be used to fetch the related metadata needed for the detail view.
- App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.
- App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

## Data Persistence
- App saves a "Favorited" movie to SharedPreferences using the movie’s id.
- When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie IDs stored in SharedPreferences or a database.

## General Project Guidelines
- App conforms to common standards found in the Android Nanodegree [General Project Guidelines](http://udacity.github.io/android-nanodegree-guidelines/core.html).
