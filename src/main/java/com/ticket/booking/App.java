package com.ticket.booking;

import com.ticket.booking.Service.UserBookingService;
import com.ticket.booking.Service.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {

    public static void main(String[] args) {

        System.out.println("Running Train Booking System");

        Scanner scanner = new Scanner(System.in);
        int option = 0;

        UserBookingService userBookingService;

        try {
            userBookingService = new UserBookingService();
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        Train trainSelectedForBooking = null;

        while (option != 7) {

            System.out.println("Choose option");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");

            option = scanner.nextInt();

            switch (option) {

                case 1:
                    System.out.println("Enter the username to signup");
                    String nameToSignUp = scanner.next();

                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = scanner.next();

                    User userToSignup = new User(
                            nameToSignUp,
                            passwordToSignUp,
                            UserServiceUtil.hashPassword(passwordToSignUp),
                            new ArrayList<>(),
                            UUID.randomUUID().toString()
                    );

                    userBookingService.signUp(userToSignup);
                    break;

                case 2:
                    System.out.println("Enter the username to Login");
                    String nameToLogin = scanner.next();

                    System.out.println("Enter the password to Login");
                    String passwordToLogin = scanner.next();

                    User userToLogin = new User();
                    userToLogin.setName(nameToLogin);
                    userToLogin.setPassword(passwordToLogin);

                    try {
                        userBookingService = new UserBookingService(userToLogin);
                        if (userBookingService.loginUser()) {
                            System.out.println("Login successful!");
                        } else {
                            System.out.println("Invalid credentials");
                        }
                    } catch (IOException ex) {
                        System.out.println("Login failed due to system error");
                    }

                    break;

                case 3:
                    System.out.println("Fetching your bookings");
                    userBookingService.fetchBookings();
                    break;

                case 4:
                    System.out.println("Type your source station");
                    String source = scanner.next().toLowerCase();

                    System.out.println("Type your destination station");
                    String dest = scanner.next().toLowerCase();

                    List<Train> trains = userBookingService.getTrains(source, dest);

                    if (trains.isEmpty()) {
                        System.out.println("No trains found between " + source + " and " + dest);
                        break;
                    }

                    int index = 1;
                    for (Train t : trains) {
                        System.out.println(index + " Train id : " + t.getTrainId());
                        index++;
                    }

                    System.out.println("Select a train by typing 1,2,3...");

                    int choice = scanner.nextInt();

                    if (choice < 1 || choice > trains.size()) {
                        System.out.println("Invalid selection");
                        break;
                    }

                    trainSelectedForBooking = trains.get(choice - 1);
                    break;

                case 5:
                    if (trainSelectedForBooking == null) {
                        System.out.println("Please search and select a train first.");
                        break;
                    }

                    System.out.println("Select a seat out of these seats");

                    List<List<Integer>> seats =
                            userBookingService.fetchSeats(trainSelectedForBooking);

                    for (List<Integer> row : seats) {
                        for (Integer val : row) {
                            System.out.print(val + " ");
                        }
                        System.out.println();
                    }

                    System.out.println("Enter the row");
                    int row = scanner.nextInt();

                    System.out.println("Enter the column");
                    int col = scanner.nextInt();

                    boolean booked =
                            userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);

                    if (booked) {
                        System.out.println("Booked! Enjoy your journey");
                    } else {
                        System.out.println("Can't book this seat");
                    }

                    break;

                case 6:
                    System.out.println("Enter ticket id to cancel:");
                    String ticketId = scanner.next();
                    userBookingService.cancelBooking(ticketId);
                    break;

                default:
                    break;
            }
        }

        scanner.close();
        System.out.println("Exiting App");
    }
}


