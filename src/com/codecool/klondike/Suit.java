package com.codecool.klondike;

public enum  Suit {
    HEARTS("hearts", "red", 1),
    CLUBS("clubs", "black", 2),
    SPADES("spades", "black", 3),
    DIAMONDS("diamonds", "red", 4);

    public final String name;
    public boolean color;
    public int id;

    Suit(String name, String color, int id){
        this.name = name;
        this.color = color.equals("red");
        this.id = id;
    }

    public boolean isSameColor(Suit suit){
        return this.color == suit.color;
    }

};

