import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

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

    private Card[] user1;
    private Card[] user2;
    private Card current;
    private int currentDeckPos;
    private int currentDisPos;
    private Stack<Card> discardPileNew;

    public Deck() {
        currentDeckPos = 0;
        currentDisPos = 0;
        discardPileNew = new Stack<>();
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
        discardPileNew.push(deck[currentDeckPos]);
        deck[currentDeckPos++] = null;
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
    public void resetCurrent() {
        current = null;
    }

    public void setCurrentFromDeck() {
        current = deck[currentDeckPos];
        deck[currentDeckPos++] = null;
    }

    public void shuffleDiscardIntoDeck() {

    }

    public void setCurrentFromDiscard() {
        if (!discardPileNew.isEmpty()) {
            current = discardPileNew.peek();
            discardPileNew.pop();
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

    public Card getCurrentCard() {
        return current;
    }

    public int getCurrentDeckPos() {
        return currentDeckPos;
    }
    public int getCurrentDisPos() {
        return currentDisPos;
    }

    public Card getCurrentDiscard() {
        return discardPileNew.peek();
    }

    public void takeFromDeck() {
        currentDeckPos--;
    }

    public Card getCard(int i) {
        return deck[i];
    }
    public Card getDiscardCard() {
        return discardPileNew.peek();
    }

    public boolean discardPileEmpty() {
        return discardPileNew.isEmpty();
    }

    public void addToDiscardPile(Card c) {
        discardPileNew.push(c);
    }

    public Card[] getCompHand() {
        return user2;
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