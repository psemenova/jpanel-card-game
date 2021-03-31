import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 500;
    static final int SCREEN_HEIGHT = 700;

    boolean running;
    Deck deck;
    boolean user_turn;
    Random rand;
    boolean king;

    JButton deckBtn;
    JButton discardBtn = new JButton();
    JButton currentCard;

    JPanel compPanel;
    JPanel userPanel;
    JPanel deckPanel;

    JButton[] userCards;
    JButton[] compCards;

    JLabel turnTag;




    GamePanel() {
        //determine who goes first
        rand = new Random();
        user_turn = rand.nextInt(2) == 0;

        king = false;

        //TODO - temp start with player
        user_turn = true;

        //initialize the panels
        compPanel = new JPanel(new GridLayout(2,5));
        userPanel = new JPanel(new GridLayout(2,5));
        deckPanel = new JPanel();

        //initialize card decks (displayed empty buttons)
        userCards = new JButton[10];
        compCards = new JButton[10];

        deck = new Deck();
        startGame();

        //populate the displayed buttons
        for (int i = 0; i < userCards.length; i++) {

            userCards[i] = new JButton();
            compCards[i] = new JButton();
//            compCards[i].setIcon(new ImageIcon(this.getClass().getResource("backOfCard.png")));


//            userCards[i] = populateImg(deck.getUserCard(i));
//            compCards[i] = populateImg(deck.getCompCard(i));

            compPanel.add(compCards[i]);
            userPanel.add(userCards[i]);

            userCards[i].addActionListener(this);
        }

        //middle of screen design
        deckPanel.setLayout(null);

        //Deck card button
        deckBtn = new JButton();
        deckBtn.setIcon(new ImageIcon(this.getClass().getResource("backOfCard.png")));
        deckBtn.setBounds(400, 250,90,120);
        JLabel lb = new JLabel("Deck");
        lb.setBounds(430, 380, 100,30);
        deckBtn.addActionListener(this);

        //Discard pile button
        populateImg(deck.getDiscardCard(0), discardBtn);
        discardBtn.setBounds(300, 250, 90, 120);
        JLabel lb1 = new JLabel("Discard");
        lb1.setBounds(320, 380, 100,30);
        discardBtn.addActionListener(this);

        //Current card button
        currentCard = new JButton();
        currentCard.setBounds(140, 250, 90, 120);
        JLabel lb2 = new JLabel("Current");
        lb2.setBounds(150, 380, 100,30);
        currentCard.addActionListener(this);

        //Player and Computer Tags
        JLabel playerTag = new JLabel("PLAYER");
        playerTag.setBounds(10,380,100,100);
        JLabel compTag = new JLabel("COMPUTER");
        compTag.setBounds(10,200,100,100);

        //Turn Tag
        turnTag = new JLabel();
        if(user_turn) turnTag.setText("User Turn");
        else turnTag.setText("Computer Turn");
        turnTag.setBounds(20, 290, 100,100);

        //add all buttons and tags to deck panel
        deckPanel.add(turnTag);
        deckPanel.add(lb);
        deckPanel.add(lb1);
        deckPanel.add(lb2);
        deckPanel.add(playerTag);
        deckPanel.add(compTag);
        deckPanel.add(currentCard);
        deckPanel.add(deckBtn);
        deckPanel.add(discardBtn);

        compPanel.setBackground(new Color(65, 156, 74));
        userPanel.setBackground(new Color(65, 156, 74));
        deckPanel.setBackground(new Color(65, 156, 74));

        compPanel.setBounds(0,0, SCREEN_WIDTH, SCREEN_HEIGHT/3 - 10);
        deckPanel.setBounds(0,(SCREEN_HEIGHT/3) - 15, SCREEN_WIDTH, SCREEN_HEIGHT/3);
        userPanel.setBounds(0,(SCREEN_HEIGHT/3) * 2 - 15, SCREEN_WIDTH, SCREEN_HEIGHT/3 - 10);

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(65, 156, 74));


        //add user, computer, and deck panels to main panel
        this.add(userPanel);
        this.add(compPanel);
        this.add(deckPanel);

        userPanel.setVisible(true);
        compPanel.setVisible(true);
        this.setVisible(true);
    }

    public void startGame() {
        running = true;
        deck.shuffleDeck();
        deck.dealCards();
        deck.startGame();
    }

    public void playerTurn(ActionEvent e) {
        turnTag.setText("User Turn");
        if (currentCard.getText().equals("")) {
            if (e.getSource() == deckBtn) {
                deck.setCurrentFromDeck();
                populateImg(deck.getCurrentCard(), currentCard);
            } else if (e.getSource() == discardBtn) {
                if(
                        deck.getDiscardCard(deck.getCurrentDisPos()).getNum() != 11 ||
                                deck.getDiscardCard(deck.getCurrentDisPos()).getNum() != 12
                ) {
                    if (deck.getCurrentDisPos() > 0) {
                        deck.setCurrentFromDiscard();
                        populateImg(deck.getCurrentCard(), currentCard);
                        populateImg(deck.getDiscardCard(deck.getCurrentDisPos()), discardBtn);
                    } else {
                        deck.setCurrentFromDiscard();
                        populateImg(deck.getCurrentCard(), currentCard);
                        deck.takeFromDiscard();
                        discardBtn.setText("");
                    }
                }

            }
        }
        if (e.getSource() == currentCard) {
            //excluding jack or queen
            if (deck.getCurrentCard().getNum() == 11 || deck.getCurrentCard().getNum() == 12) {
                user_turn = false;
                changeTurns();
            }
            //if king user can click any location that is not taken
            else if (deck.getCurrentCard().getNum() == 13){
                king = true;
            //if not the king then the rest of the cards
            } else if (userCards[deck.getCurrentCard().getNum() - 1].getText().equals("")) {
                int pos = deck.getCurrentCard().getNum() - 1;
                populateImg(deck.getCurrentCard(), userCards[pos]);
                populateImg(deck.getUserCard(pos), currentCard);
                deck.setCurrent("U", pos);
            } else {
                user_turn = false;
                changeTurns();
            }
        }
    }

    public void changeTurns() {
        if (user_turn) {
            turnTag.setText("User Turn");
        } else {
            turnTag.setText("Computer Turn");
        }
        populateImg(deck.getCurrentCard(), discardBtn);
        deck.addToDiscardPile(deck.getCurrentCard());
        deck.setCurrent("", 0);
        currentCard.setText("");
    }


    private void kingPlay(ActionEvent e) {
        if (king = true) {
            for (int i = 0; i < userCards.length; i++) {
                if (e.getSource() == userCards[i] && userCards[i].getText().equals("")) {
                    populateImg(deck.getCurrentCard(), userCards[i]);
                    populateImg(deck.getUserCard(i), currentCard);
                    deck.setCurrent("U", i);
                }
            }
        }
    }

    public void computerTurn(ActionEvent e) {
        turnTag.setText("Computer Turn");
        //TODO - automate computer turn
    }

    public void populateImg(Card card, JButton button) {
        if (card.getSuit().equals("diamond") || card.getSuit().equals("hearts")){
            button.setForeground(Color.red);
        } else {
            button.setForeground(Color.BLACK);
        }

        switch (card.getNum()) {
            case 1:
                button.setText("Ace");
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
                button.setText("" + card.getNum());
                break;
            case 11:
                button.setText("J");
                break;
            case 12:
                button.setText("Q");
                break;
            case 13:
                button.setText("K");
                break;
        }

        if(card.getSuit().equals("diamond")) {
            button.setText(button.getText() + " ♦");
        }
        if(card.getSuit().equals("hearts")) {
            button.setText(button.getText() + " ♥");
        }
        if(card.getSuit().equals("spades")) {
            button.setText(button.getText() + " ♠");
        }
        if(card.getSuit().equals("clubs")) {
            button.setText(button.getText() + " ♣");
        }

    }

    public void winGame() {
        //TODO - win the game
        for (int i = 0; i < 10; i++){
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            if (user_turn) {
                playerTurn(e);
                if (king) {
                    kingPlay(e);
                }
            } else {
                computerTurn(e);
            }
            winGame();
        }
    }
}