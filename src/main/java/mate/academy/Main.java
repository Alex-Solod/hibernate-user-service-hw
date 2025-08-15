package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;

public class Main {
    public static void main(String[] args) {
        Injector injector = Injector.getInstance("mate.academy");

        AuthenticationService authService =
                (AuthenticationService) injector.getInstance(AuthenticationService.class);

        // Test register
        try {
            User newUser = authService.register("test@example.com", "12345");
            System.out.println("Registered: " + newUser);
        } catch (RegistrationException e) {
            System.out.println("Registration error: " + e.getMessage());
        }

        // Test login
        try {
            User loggedIn = authService.login("test@example.com", "12345");
            System.out.println("Logged in: " + loggedIn);
        } catch (AuthenticationException e) {
            System.out.println("Login error: " + e.getMessage());
        }

        // Create and add movie
        MovieService movieService =
                (MovieService) injector.getInstance(MovieService.class);
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println("Movie by ID: " + movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        // Create and add cinema halls
        CinemaHallService cinemaHallService =
                (CinemaHallService) injector.getInstance(CinemaHallService.class);
        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");
        cinemaHallService.add(firstCinemaHall);

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");
        cinemaHallService.add(secondCinemaHall);

        System.out.println("All cinema halls: " + cinemaHallService.getAll());
        System.out.println("First hall by ID: " + cinemaHallService.get(firstCinemaHall.getId()));

        // Create and add movie sessions
        final MovieSessionService movieSessionService =
                (MovieSessionService) injector.getInstance(MovieSessionService.class);
        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1));
        movieSessionService.add(tomorrowMovieSession);

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1));
        movieSessionService.add(yesterdayMovieSession);

        System.out.println("Yesterday session by ID: "
                + movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println("Available sessions: "
                + movieSessionService.findAvailableSessions(
                fastAndFurious.getId(), LocalDate.now()));
    }
}
