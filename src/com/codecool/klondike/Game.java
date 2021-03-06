package com.codecool.klondike;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Game extends Pane {

    private List<Card> deck = new ArrayList<>();

    private Pile stockPile;
    private Pile discardPile;
    private List<Pile> foundationPiles = FXCollections.observableArrayList();
    private List<Pile> tableauPiles = FXCollections.observableArrayList();

    private double dragStartX, dragStartY;
    private List<Card> draggedCards = FXCollections.observableArrayList();

    private static double STOCK_GAP = 1;
    private static double FOUNDATION_GAP = 0;
    private static double TABLEAU_GAP = 30;

    private Card lastAutoCard;
    private Pile lastAutoPile;
    private int autoIndex = 0;
    private int autoPileIndex = 0;


    private EventHandler<MouseEvent> onMouseClickedHandler = e -> {
        Card card = (Card) e.getSource();
        Card topCard = card.getContainingPile().getTopCard();
        if (card.getContainingPile().getPileType() == Pile.PileType.STOCK && card == topCard) {
            card.moveToPile(discardPile);
            card.flip();
            card.setMouseTransparent(false);
            System.out.println("Placed " + card + " to the waste.");
        }
    };

    private EventHandler<MouseEvent> stockReverseCardsHandler = e -> {
        refillStockFromDiscard();
    };

    private EventHandler<MouseEvent> onMousePressedHandler = e -> {
        dragStartX = e.getSceneX();
        dragStartY = e.getSceneY();
    };

    private EventHandler<MouseEvent> onMouseDraggedHandler = e -> {
        Card card = (Card) e.getSource();
        Pile activePile = card.getContainingPile();
        if (activePile.getPileType() == Pile.PileType.STOCK)
            return;
        if (activePile.getPileType() == Pile.PileType.TABLEAU && card.isFaceDown())
            return;
        if (card.getContainingPile().getPileType() == Pile.PileType.DISCARD) {
            Card topDisCard = card.getContainingPile().getTopCard();
            if (card != topDisCard) {
                return;
            }
        }

        double offsetX = e.getSceneX() - dragStartX;
        double offsetY = e.getSceneY() - dragStartY;

        draggedCards.clear();
        draggedCards.add(card);
        if (card != activePile.getTopCard()) {
            List<Card> pileContent = activePile.getCards();
            int start = pileContent.indexOf(card) + 1;
            for (int i = start; i < pileContent.size(); i++) {
                Card currentCard = pileContent.get(i);
                draggedCards.add(currentCard);
            }
        }

        card.getDropShadow().setRadius(20);
        card.getDropShadow().setOffsetX(10);
        card.getDropShadow().setOffsetY(10);

        //card.toFront();
        for (Card dragEach : draggedCards) {
            dragEach.toFront();
            dragEach.setTranslateX(offsetX);
            dragEach.setTranslateY(offsetY);
        }
    };

    private EventHandler<MouseEvent> onMouseReleasedHandler = e -> {
        if (draggedCards.isEmpty())
            return;
        Card card = (Card) e.getSource();
        List<Pile> allPiles = new ArrayList<>(tableauPiles);
        allPiles.addAll(foundationPiles);
        Pile pile = getValidIntersectingPile(card, allPiles);

        // handle autoSlide separately, as autoSlide doesn't have dragged cards
        if (pile != null) {
            handleValidMove(card, pile);
        } else {
            //draggedCards.forEach(MouseUtil::slideBack);
            for (Card draggedCard : draggedCards) {
                MouseUtil.slideBack(draggedCard, new Callable() {
                    @Override
                    public void doCallback() {
                        System.out.println("Callbacking");
                        if (draggedCards.size() == 0) return;
                        int lastIndex = draggedCards.size() - 1;
                        if (draggedCards.get(lastIndex).equals(draggedCard)) {
                            System.out.println("Clearing");
                            draggedCards.clear();
                        }
                    }
                });
            }
        }

        if (isAutoCompletePossible()){
            autoComplete();
        }
    };

    private EventHandler<MouseEvent> onMouseDoubleClickedHandler = e -> {

        Card card = (Card) e.getSource();

        // cards that are not on top or are in FOUNDATION cannot be double-clicked
        if (card.getContainingPile().getTopCard() != card ||
                card.getContainingPile().getPileType() == Pile.PileType.FOUNDATION ||
                card.getContainingPile().getPileType() == Pile.PileType.STOCK) return;

        // check if click was right-click
        if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
            System.out.println("double click");
            // iterate through FOUNDATION piles and move card to pile if found valid
            for (Pile destPile : foundationPiles) {
                if (isFoundationValid(card, destPile)) {
                    handleValidMove(card, destPile);
                }
            }
        }
        if (isAutoCompletePossible()){
            autoComplete();
        }
    };

    private boolean isFoundationValid(Card card, Pile pile) {
        if (draggedCards.size() > 1) return false;
        Card topCard = pile.getTopCard();
        return (pile.isEmpty() && card.getRank().id == 1) ||
                !pile.isEmpty() && Card.isSameSuit(card, topCard) && card.getRank().id == (topCard.getRank().id + 1);
    }

    private boolean isTableauValid(Card card, Pile pile) {
        Card topCard = pile.getTopCard();
        if (topCard == null && card.getRank().id == 13) return true;
        if (topCard != null &&
                (Card.isOppositeColor(card, topCard) && topCard.getRank().id - card.getRank().id == 1)) return true;
        return false;
    }

    public boolean isGameWon() {
        int count = 0;
        for (Pile pile : foundationPiles) {
            count += pile.numOfCards();
        }
        if (count == 52) {
            return true;
        }
        return false;
    }

    public Game() {
        deck = Card.createNewDeck();
        shuffleDeck();
        initPiles();
        dealCards();
    }

    public void addMouseEventHandlers(Card card) {
        card.setOnMousePressed(onMousePressedHandler);
        card.setOnMouseDragged(onMouseDraggedHandler);
        card.setOnMouseReleased(onMouseReleasedHandler);
        card.setOnMouseClicked(onMouseClickedHandler);
        card.addEventHandler(MouseEvent.MOUSE_CLICKED, onMouseDoubleClickedHandler);
    }

    public void refillStockFromDiscard() {
        Collections.reverse(discardPile.getCards());
        for (Card discardedCard : discardPile.getCards()) {
            discardedCard.flip();
            stockPile.addCard(discardedCard);
        }
        discardPile.clear();
    }

    public boolean isMoveValid(Card card, Pile destPile) {
        return (destPile.getPileType() == Pile.PileType.FOUNDATION) ?
                isFoundationValid(card, destPile) : isTableauValid(card, destPile);
    }

    private Pile getValidIntersectingPile(Card card, List<Pile> piles) {
        for (Pile pile : piles) {
            if (!pile.equals(card.getContainingPile()) &&
                    isOverPile(card, pile) &&
                    isMoveValid(card, pile)) {
                return pile;
            }
        }
        Pile result = null;
        return result;
    }

    private boolean isOverPile(Card card, Pile pile) {
        if (pile.isEmpty())
            return card.getBoundsInParent().intersects(pile.getBoundsInParent());
        else
            return card.getBoundsInParent().intersects(pile.getTopCard().getBoundsInParent());
    }

    private void handleValidMove(Card card, Pile destPile) {
        String msg = null;
        if (destPile.isEmpty()) {
            if (destPile.getPileType().equals(Pile.PileType.FOUNDATION))
                msg = String.format("Placed %s to the foundation.", card);

            if (destPile.getPileType().equals(Pile.PileType.TABLEAU))
                msg = String.format("Placed %s to a new pile.", card);
        } else {
            msg = String.format("Placed %s to %s.", card, destPile.getTopCard());
        }
        System.out.println(msg);
        Pile origPile = card.getContainingPile();
        System.out.println(destPile.getPileType());

        Callable moveCardCallback = new Callable() {
            @Override
            public void doCallback() {
                autoFlip();
                winCheck();
            }

            private void winCheck() {
                boolean won = isGameWon();
                if (won) {
                    PopUp winPopup = new PopUp();
                    winPopup.showDialog();
                }
            }

            private void autoFlip() {
                if (origPile.isEmpty()) return;
                Card cardAbove = origPile.getTopCard();
                if (origPile.getPileType() == Pile.PileType.TABLEAU && cardAbove.isFaceDown()) {
                    cardAbove.flip();
                }
            }
        };

        if (draggedCards.isEmpty()) {
            List<Card> slideCard = new ArrayList<>();
            slideCard.add(card);
            MouseUtil.slideToDest(slideCard, destPile, moveCardCallback);
        } else {
            MouseUtil.slideToDest(draggedCards, destPile, moveCardCallback);
        }

        draggedCards.clear();
    }

    private void initPiles() {
        stockPile = new Pile(Pile.PileType.STOCK, "Stock", STOCK_GAP);
        stockPile.setBlurredBackground();
        stockPile.setLayoutX(95);
        stockPile.setLayoutY(80);
        stockPile.setOnMouseClicked(stockReverseCardsHandler);
        getChildren().add(stockPile);

        discardPile = new Pile(Pile.PileType.DISCARD, "Discard", STOCK_GAP);
        discardPile.setBlurredBackground();
        discardPile.setLayoutX(285);
        discardPile.setLayoutY(80);
        getChildren().add(discardPile);

        for (int i = 0; i < 4; i++) {
            Pile foundationPile = new Pile(Pile.PileType.FOUNDATION, "Foundation " + i, FOUNDATION_GAP);
            foundationPile.setBlurredBackground();
            foundationPile.setLayoutX(610 + i * 180);
            foundationPile.setLayoutY(80);
            foundationPiles.add(foundationPile);
            getChildren().add(foundationPile);
        }
        for (int i = 0; i < 7; i++) {
            Pile tableauPile = new Pile(Pile.PileType.TABLEAU, "Tableau " + i, TABLEAU_GAP);
            tableauPile.setBlurredBackground();
            tableauPile.setLayoutX(95 + i * 180);
            tableauPile.setLayoutY(335);
            tableauPiles.add(tableauPile);
            getChildren().add(tableauPile);
        }
    }

    public void dealCards() {
        Iterator<Card> deckIterator = deck.iterator();
        for (int i = 0; i < tableauPiles.size(); i++) {
            Pile currentTableauPile = tableauPiles.get(i);
            for (int j = 0; j < i + 1; j++) {
                Card card = deckIterator.next();
                currentTableauPile.addCard(card);
                card.moveToPile(currentTableauPile);
                addMouseEventHandlers(card);
                getChildren().add(card);
                if (j == i) {
                    card.flip();
                }
            }
        }
        deckIterator.forEachRemaining(card -> {
            stockPile.addCard(card);
            card.moveToPile(stockPile);
            addMouseEventHandlers(card);
            getChildren().add(card);
        });
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public void setTableBackground(Image tableBackground) {
        setBackground(new Background(new BackgroundImage(tableBackground,
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, BackgroundSize.DEFAULT)));
    }

    public boolean isAutoCompletePossible() {
        for (Pile pile : tableauPiles) {
            for (Card card : pile.getCards()) {
                if (card.isFaceDown()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void autoComplete() {

        Callable nextWithMove = new Callable() {
            @Override
            public void doCallback() {
                lastAutoCard.moveToPile(lastAutoPile);
                autoIndex = 0;
                if (lastAutoCard.getRank().id == 13) {
                    autoPileIndex++;
                }

                if (autoPileIndex == 4 && lastAutoCard.getRank().id == 13) {
                    System.out.println("winning");
                    PopUp winPopup = new PopUp();
                    winPopup.showDialog();
                }
                autoComplete();
            }
        };

        // gather cards that are not moved to foundation yet
        List<Card> fromCards = new ArrayList<>();
        fromCards.addAll(stockPile.getCards());
        fromCards.addAll(discardPile.getCards());
        for (Pile pile:tableauPiles) {
            fromCards.addAll(pile.getCards());
        }

        // stop the loop if all cards are up in foundation
        if (fromCards.size() == 0) return;

        // choose the next card to check and possible move
        Card card = fromCards.get(autoIndex);
        Pile destPile = foundationPiles.get(autoPileIndex);

        // create list from card for the MouseUtil method
        List<Card> slideCard = new ArrayList<>();
        slideCard.add(card);

        // check if given card can move up at this stage
        if (isFoundationValid(card, destPile)) {

            lastAutoCard = card;
            lastAutoPile = destPile;

            if (card.getContainingPile().getPileType() == Pile.PileType.STOCK) {
                card.flip();
            }
            MouseUtil.slideToDest(slideCard, destPile, nextWithMove);

        } else {
            autoIndex++;
            autoComplete();
        }
    }
}
