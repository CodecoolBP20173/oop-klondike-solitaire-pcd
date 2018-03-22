package com.codecool.klondike;

public enum Rank {
    ACE(1, "Ace"),
    TWO(2, "2"),
    THREE(3, "3"),
    FOUR(4, "4"),
    FIVE(5, "5"),
    SIX(6, "6"),
    SEVEN(7, "7"),
    EIGHT(8, "8"),
    NINE(9, "9"),
    TEN(10, "10"),
	JACK(11, "Jack"),
    QUEEN(12, "Queen"),
    KING(13, "King");

    public int id;
    public  String name;

    Rank(int id, String name) {
        this.id = id;
        this.name = name;
    }


}
