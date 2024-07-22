package org.example.app.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.app.entities.Ticket;
import org.example.app.entities.Train;
import org.example.app.entities.User;
import org.example.app.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class userBookingService
{
    private User user;
    private List<User> userList;
    private ObjectMapper objectMapper=new ObjectMapper();//used to de-serialize the JSON content
    private static final String USERS_PATH="app/src/main/java/org/example/app/localDB/users.json";
    public userBookingService(User user1)throws IOException {
         this.user=user1;
         loadUsers();
    }

    public static List<Train> getTrains(String source, String dest) {
        try{
            trainService trainService=new trainService();
            return trainService.searchTrains(source,dest);
        }catch (IOException ex){
            return new ArrayList<>();
        }
    }

    public Boolean loginUser(){
        Optional<User> foundUser=userList.stream().filter(user1 -> {
            return user1.getName().equalsIgnoreCase(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(),user1.getHashPasword());
        }).findFirst();
        return foundUser.isPresent();
    }
    public userBookingService()throws IOException{

       loadUsers();
    }
    public List<User> loadUsers()throws  IOException{
        File users=new File(USERS_PATH);
        return objectMapper.readValue(users, new TypeReference<List<User>>(){});
    }

    public Boolean signUp(User user1){
        try{
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;
        }catch(Exception ex){
            return Boolean.FALSE;
        }
    }

    private void saveUserListToFile()throws IOException {
        File usersFile=new File(USERS_PATH);
        objectMapper.writeValue(usersFile,userList); //serialization
        //json --> Object(User) Deserialization
        //object --> json Serialize
    }
    public void fetchBooking(){
        Optional<User>  userFetched=userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(),user1.getHashPasword());
        }).findFirst();//findFirst() returns an optional<User> containing the first 'User' that matches the filter criteria
        if(userFetched.isPresent()){
            userFetched.get().printTickets();
        }
    }
    public boolean cancelBooking(String ticketId){
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter the ticketID to cancel");
        ticketId=sc.next();
        if(ticketId==null || ticketId.isEmpty()){
            System.out.println("TicketID cannot  be vacant");
            return Boolean.FALSE;
        }
        String finalTicketId=ticketId;
        boolean removed=user.getTicketBooked().removeIf(ticket -> ticket.getTicketId().equals(finalTicketId));
        //removeIf method is called on the users ticketBooked list.It iterates throught the list and
        //removes any ticket that matches the ticketID
        if(removed){
            System.out.println("Ticket ID"+ticketId+"is cancelled");
            return Boolean.TRUE;
        }else{
            System.out.println("No Ticket found with ID "+ticketId);
            return Boolean.FALSE;
        }
    }
    public Boolean bookseat(Train train,int row,int seat){
        try{
            trainService trainservice=new trainService();
            List<List<Integer>> seats=train.getSeats();
            if(row>=0 && row<seats.size() &&   seat>=0 && seat<seats.get(row).size()){
                if(seats.get(row).get(seat)==0){
                    seats.get(row).set(seat,1);
                    train.setSeats(seats);
                    trainservice.addTrain(train);
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }catch (IOException ex){
            return Boolean.FALSE;
        }
    }
    public List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
    }


}
