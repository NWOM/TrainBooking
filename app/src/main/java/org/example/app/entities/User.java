package org.example.app.entities;

import java.util.List;

public class User {
    public User(String name, String password, String hashPasword, List<Ticket> ticketBooked,String userId) {
        this.name = name;
        this.password = password;
        this.hashPasword = hashPasword;
        this.ticketBooked = ticketBooked;
        this.userId=userId;
    }

    private String name;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    public String getPassword() {
        return password;
    }
    public void printTickets(){
        for(int i=0;i<ticketBooked.size();i++){
            System.out.println(ticketBooked.get(i).getTicketInfo());
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashPasword() {
        return hashPasword;
    }

    public void setHashPasword(String hashPasword) {
        this.hashPasword = hashPasword;
    }

    public List<Ticket> getTicketBooked() {
        return ticketBooked;
    }

    public void setTicketBooked(List<Ticket> ticketBooked) {
        this.ticketBooked = ticketBooked;
    }

    private String password;
    private String hashPasword;
    private List<Ticket> ticketBooked;
}
