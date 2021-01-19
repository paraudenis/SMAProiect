package com.example.blackjack;

import java.util.Comparator;

public class User {

    public String username;
    public Integer score;

    public User(){
    }

    public User(String username, Integer score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}

class SortByScore implements Comparator<User> {
    public int compare(User a, User b) {
        return b.getScore()-a.getScore();
    }
}
