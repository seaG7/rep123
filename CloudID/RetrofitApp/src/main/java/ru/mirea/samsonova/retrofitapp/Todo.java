package ru.mirea.samsonova.retrofitapp;

public class Todo {
    private int userId;
    private int id;
    private String title;
    private boolean completed;

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String imageUrl() {
        int[] photos = {1015, 1016, 1025, 1035, 1043, 106, 1074, 1080, 1084, 129};
        int photo = photos[Math.floorMod(id, photos.length)];
        return "https://picsum.photos/id/" + photo + "/200/200";
    }
}
