package model;

public class User {
    private String Name;
    private int Score;

    public User(String Name, int score) {
        this.Name = Name;
        this.Score = score;
    }

    public User() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }
}
