package com.ticket.booking.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticket.booking.Train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TrainService {

    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String TRAIN_DB_PATH = "localDB/trains.json";

    public TrainService() throws IOException {
        File trains = new File(TRAIN_DB_PATH);

        System.out.println("DEBUG: Looking for trains file at -> " + trains.getAbsolutePath());

        if (!trains.exists()) {
            System.out.println("DEBUG: trains.json FILE DOES NOT EXIST!");
            trainList = new ArrayList<>();
            return;
        }

        if (trains.length() == 0) {
            System.out.println("DEBUG: trains.json is EMPTY!");
            trainList = new ArrayList<>();
            return;
        }

        trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {});

        System.out.println("DEBUG: Number of trains loaded = " + trainList.size());
    }


    public List<Train> searchTrains(String source, String destination) {

        System.out.println("DEBUG: Searching for source = " + source + " destination = " + destination);

        System.out.println("DEBUG: Total trains available = " + trainList.size());

        for (Train t : trainList) {
            System.out.println("DEBUG: Train -> " + t.getTrainId());
            System.out.println("DEBUG: Stations -> " + t.getStations());
        }

        List<Train> result = trainList.stream()
                .filter(train -> validTrain(train, source, destination))
                .collect(Collectors.toList());

        System.out.println("DEBUG: Trains matched = " + result.size());

        return result;
    }


    public void addTrain(Train newTrain) {
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            updateTrain(newTrain);
        } else {
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }

    public void updateTrain(Train updatedTrain) {
        OptionalInt index = IntStream.range(0, trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();

        if (index.isPresent()) {
            trainList.set(index.getAsInt(), updatedTrain);
            saveTrainListToFile();
        } else {
            addTrain(updatedTrain);
        }
    }

    private void saveTrainListToFile() {
        try {
            objectMapper.writeValue(new File(TRAIN_DB_PATH), trainList);
        } catch (IOException e) {
            System.out.println("Failed to save train data: " + e.getMessage());
        }
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations();

        List<String> normalized = stationOrder.stream()
                .map(String::toLowerCase)
                .toList();

        int sourceIndex = normalized.indexOf(source.toLowerCase());
        int destinationIndex = normalized.indexOf(destination.toLowerCase());

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }
}
