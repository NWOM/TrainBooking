package org.example.app.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.app.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class trainService {
    private List<Train> trainList;
    private ObjectMapper objectMapper=new ObjectMapper();
    private static final String TRAIN_DB_PATH="../localDB/trains.json";
    public trainService()throws IOException{
        File trains=new File(TRAIN_DB_PATH);
        trainList=objectMapper.readValue(trains, new TypeReference<List<Train>>() {
        });
    }
    public List<Train> searchTrains(String source, String dest) {
        return trainList.stream().filter(train -> validTrain(train,source,dest)).collect(Collectors.toList());
    }
    public void addTrain(Train newTrain){
        Optional<Train> existingTrain=trainList.stream()
                .filter(train -> train.getTrainId().equalsIgnoreCase(newTrain.getTrainId()))
                .findFirst();
        if(existingTrain.isPresent()){
            updateTrain(newTrain);
        }else{
            trainList.add(newTrain);
            saveTrainListToFile();
        }
    }
    public void updateTrain(Train updatedTrain){
        OptionalInt index= IntStream.range(0,trainList.size())
                .filter(i -> trainList.get(i).getTrainId().equalsIgnoreCase(updatedTrain.getTrainId()))
                .findFirst();
        if(index.isPresent()){
            trainList.set(index.getAsInt(),
                    updatedTrain);
            saveTrainListToFile();
        }else{
            addTrain(updatedTrain);
        }
    }
    private void saveTrainListToFile()
    {
        try{
            objectMapper.writeValue(new File(TRAIN_DB_PATH),trainList);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    private boolean validTrain(Train train,String source,String dest){
        List<String> stationOrder=train.getStation();
        int sourceIndex=stationOrder.indexOf(source.toLowerCase());
        int destinationIndex=stationOrder.indexOf(dest.toLowerCase());
        return sourceIndex !=-1 && destinationIndex!=-1 && sourceIndex<destinationIndex;
    }
}
