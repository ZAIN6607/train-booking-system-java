package com.ticket.booking.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket.booking.User;
import com.ticket.booking.Train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private User user;
    private List<User> userList;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String USERS_PATH = "localDB/users.json";

    public UserBookingService(User user1) throws IOException {
        this.user = user1;

        File users = new File(USERS_PATH);

        if (!users.exists() || users.length() == 0) {
            userList = new ArrayList<>();
        } else {
            userList = OBJECT_MAPPER.readValue(users, new TypeReference<List<User>>() {});
        }
    }

    public UserBookingService() throws IOException {
        File users = new File(USERS_PATH);

        if (!users.exists() || users.length() == 0) {
            userList = new ArrayList<>();
        } else {
            userList = OBJECT_MAPPER.readValue(users, new TypeReference<List<User>>() {});
        }
    }

    public boolean loginUser() {
        if (userList == null || user == null) {
            return false;
        }

        return userList.stream().anyMatch(user1 ->
                user1.getName().equalsIgnoreCase(user.getName()) &&
                        UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword())
        );
    }

    public boolean signUp(User user1) {
        try {
            if (userList == null) {
                return false;
            }

            userList.add(user1);
            saveUserListToFile();
            return true;

        } catch (IOException ex) {
            return false;
        }
    }

    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        OBJECT_MAPPER.writeValue(usersFile, userList);
    }

    public void fetchBookings() {
        Optional<User> userFetched = userList.stream().filter(user1 ->
                user1.getName().equalsIgnoreCase(user.getName()) &&
                        UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword())
        ).findFirst();

        userFetched.ifPresent(User::printTickets);
    }

    public boolean cancelBooking(String ticketId) {

        if (user == null || user.getTicketsBooked() == null) {
            return false;
        }

        boolean removed = user.getTicketsBooked()
                .removeIf(ticket -> ticket.getTicketId().equals(ticketId));

        if (removed) {
            try {
                saveUserListToFile();
            } catch (IOException e) {
                return false;
            }

            System.out.println("Ticket with ID " + ticketId + " has been canceled.");
            return true;
        } else {
            System.out.println("No ticket found with ID " + ticketId);
            return false;
        }
    }

    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException ex) {
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public boolean bookTrainSeat(Train train, int row, int seat) {
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();

            if (row >= 0 && row < seats.size() &&
                    seat >= 0 && seat < seats.get(row).size()) {

                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    return true;
                }
            }
            return false;

        } catch (IOException ex) {
            return false;
        }
    }
}
