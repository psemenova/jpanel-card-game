import java.util.Arrays;
import java.util.Random;

public class Deck {
    private Card[] deck = {
            new Card(1, "diamond"),
            new Card(1, "hearts"),
            new Card(1, "spades"),
            new Card(1, "clubs"),

            new Card(2, "diamond"),
            new Card(2, "hearts"),
            new Card(2, "spades"),
            new Card(2, "clubs"),

            new Card(3, "diamond"),
            new Card(3, "hearts"),
            new Card(3, "spades"),
            new Card(3, "clubs"),

            new Card(4, "diamond"),
            new Card(4, "hearts"),
            new Card(4, "spades"),
            new Card(4, "clubs"),

            new Card(5, "diamond"),
            new Card(5, "hearts"),
            new Card(5, "spades"),
            new Card(5, "clubs"),

            new Card(6, "diamond"),
            new Card(6, "hearts"),
            new Card(6, "spades"),
            new Card(6, "clubs"),

            new Card(7, "diamond"),
            new Card(7, "hearts"),
            new Card(7, "spades"),
            new Card(7, "clubs"),

            new Card(8, "diamond"),
            new Card(8, "hearts"),
            new Card(8, "spades"),
            new Card(8, "clubs"),

            new Card(9, "diamond"),
            new Card(9, "hearts"),
            new Card(9, "spades"),
            new Card(9, "clubs"),

            new Card(10, "diamond"),
            new Card(10, "hearts"),
            new Card(10, "spades"),
            new Card(10, "clubs"),

            new Card(11, "diamond"),
            new Card(11, "hearts"),
            new Card(11, "spades"),
            new Card(11, "clubs"),

            new Card(12, "diamond"),
            new Card(12, "hearts"),
            new Card(12, "spades"),
            new Card(12, "clubs"),

            new Card(13, "diamond"),
            new Card(13, "hearts"),
            new Card(13, "spades"),
            new Card(13, "clubs")
    };

    private Card[] discardPile;
    private Card[] user1;
    private Card[] user2;
    private Card current;
    private int currentDeckPos;
    private int currentDisPos;

    public Deck() {
        currentDeckPos = 0;
        currentDisPos = -1;
        discardPile = new Card[32];
        user1 = new Card[10];
        user2 = new Card[10];
    }


    public void shuffleDeck() {
        Random rnd = new Random();
        for (int i = deck.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            Card a = deck[index];
            deck[index] = deck[i];
            deck[i] = a;
        }
    }

    public void dealCards() {
        for (int i = 0; i < user1.length; i++) {
            user1[i] = deck[currentDeckPos];
            deck[currentDeckPos++] = null;
        }
        for (int i = 0; i < user2.length; i++) {
            user2[i] = deck[currentDeckPos];
            deck[currentDeckPos++] = null;
        }
    }

    public void startGame() {
        discardPile[++currentDisPos] = deck[currentDeckPos];
        deck[currentDeckPos++] = null;
    }

    public void playerTurn(Card c) {

    }


    public void setCurrent(String s, int i) {
        if (s.equals("U")) {
            Card temp = current;
            current = user1[i];
            user1[i] = temp;
        } else {
            Card temp = current;
            current = user2[i];
            user2[i] = temp;
        }
    }

    public void setCurrentFromDeck() {
        current = deck[currentDeckPos];
        deck[currentDeckPos] = null;
        currentDeckPos++;
    }
    public void setCurrentFromDiscard() {
        if (currentDisPos > -1) {
            current = discardPile[currentDisPos];
            discardPile[currentDisPos] = null;
            currentDisPos++;
        }
    }

    public Card getUserCard(int i) {
        return user1[i];
    }
    public Card getCompCard(int i) {
        return user2[i];
    }

    public Card[] getDeck() {
        return deck;
    }
    public Card[] getDiscardPile() {
        return discardPile;
    }

    public Card getCurrentCard() {
        return current;
    }

    public int getCurrentDeckPos() {
        return currentDeckPos;
    }
    public int getCurrentDisPos() {
        return currentDisPos;
    }

    public Card getCard(int i) {
        return deck[i];
    }
    public Card getDiscardCard(int i) { return discardPile[i]; }


    public void addToDiscardPile(Card c) {
        discardPile[currentDisPos++] = c;
    }

    public String toString() {
        String out = "";
        for(int i = 0; i < deck.length; i++) {
            if (deck[i] == null) {
                out += "null\n";
            } else {
                out += deck[i].toString() + " \n";
            }
        }
        return out;
    }
}