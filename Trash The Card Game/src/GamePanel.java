import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

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
        populateImg(deck.getDiscardCard(), discardBtn);
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
        //if current starts empty
        if (currentCard.getText().equals("")) {
            if (e.getSource() == deckBtn) {
                deck.setCurrentFromDeck();
                populateImg(deck.getCurrentCard(), currentCard);
                //if user chooses discard pile
            } else if (e.getSource() == discardBtn) {
                //if discard pile isn't empty
                if(!deck.discardPileEmpty()) {
                    //if top discard card is not J or Q
                    if (
                            deck.getCurrentDiscard().getNum() != 11 &&
                                deck.getCurrentDiscard().getNum() != 12
                    ) {
                        if (deck.getCurrentDiscard().getNum() == 13 || userCards[deck.getCurrentDiscard().getNum() - 1].getText().equals("")) {
                            deck.setCurrentFromDiscard();
                            populateImg(deck.getCurrentCard(), currentCard);
                            if (!deck.discardPileEmpty()) {
                                populateImg(deck.getDiscardCard(), discardBtn);
                            } else {
                                discardBtn.setText("");
                            }
                        }
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
        populateImg(deck.getCurrentCard(), discardBtn);
        deck.addToDiscardPile(deck.getCurrentCard());
        deck.resetCurrent();
        currentCard.setText("");
        if (user_turn) {
            turnTag.setText("User Turn");
        } else {
            turnTag.setText("Computer Turn");
            computerTurn();
        }

//        CountDownLatch latch = new CountDownLatch(1);
//        pause();
//        latch.countDown();
//        computerTurn();
//        try { latch.await(); } catch (InterruptedException e) { }
    }

    private void pause(int seconds)  {
        CountDownLatch latch = new CountDownLatch(seconds);
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            int s = seconds;
            @Override
            public void run() {
                System.out.println(s--);
                latch.countDown();
                System.out.println("Latch: " + latch.getCount());
                if (s == 0) {
                  timer.cancel();
                }
            }
        },0, 1000);
        try { latch.await(); } catch (InterruptedException e) { }
    }


    private void kingPlay(ActionEvent e) {
        if (king = true) {
            for (int i = 0; i < userCards.length; i++) {
                if (e.getSource() == userCards[i] && userCards[i].getText().equals("")) {
                    populateImg(deck.getCurrentCard(), userCards[i]);
                    populateImg(deck.getUserCard(i), currentCard);
                    deck.setCurrent("U", i);
                    king = false;
                }
            }
        }
    }

    public void computerTurn() {
        System.out.println(Arrays.toString(deck.getCompHand()));


        if (!user_turn) {
            //if discard pile doesn't contain J or Q

            //TODO - computer king turn
            //temporary exclusion of king
            if (deck.getCurrentCard() == null) {
                if (deck.getCurrentDiscard().getNum() != 11 &&
                        deck.getCurrentDiscard().getNum() != 12 &&
                        deck.getCurrentDiscard().getNum() != 13 &&
                        compCards[deck.getCurrentDiscard().getNum() - 1].getText().equals("")
                ) {
                    //setting up the current and discard at beginning of turn
                    deck.setCurrentFromDiscard();
                    populateImg(deck.getCurrentCard(), currentCard);
                    if (!deck.discardPileEmpty()) {
                        populateImg(deck.getDiscardCard(), discardBtn);
                    } else {
                        discardBtn.setText("");
                    }
                } else {
                    //TODO - take from deck instead of discard pile
                    //temp fix
                    deck.setCurrentFromDeck();
                    populateImg(deck.getCurrentCard(), currentCard);
//                user_turn = true;
//                turnTag.setText("User Turn");
                }
            }
        }
        System.out.println("Current Card: " + currentCard.getText());
        //while it is still the computer's turn
        while(!user_turn) {
            if (deck.getCurrentCard().getNum() == 11 || deck.getCurrentCard().getNum() == 12) {
                user_turn = true;
                changeTurns();
            }
            //if king user can click any location that is not taken
            else if (deck.getCurrentCard().getNum() == 13){
                //TODO - King's turn
                user_turn = true;
                changeTurns();
                //if not the king then the rest of the cards
            } else if (compCards[deck.getCurrentCard().getNum() - 1].getText().equals("")) {
                int pos = deck.getCurrentCard().getNum() - 1;
                populateImg(deck.getCurrentCard(), compCards[pos]);
                populateImg(deck.getCompCard(pos), currentCard);
                deck.setCurrent("C", pos);
            } else {
                user_turn = true;
                changeTurns();
            }
        }


        //TODO - automate computer turn
    }

    public boolean populateImg(Card card, JButton button) {
        CountDownLatch latch = new CountDownLatch(3);
        if (card.getSuit().equals("diamond") || card.getSuit().equals("hearts")){
            button.setForeground(Color.red);
        } else {
            button.setForeground(Color.BLACK);
        }
        latch.countDown();
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
        latch.countDown();
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
        latch.countDown();
        try { latch.await(); } catch (InterruptedException e) { }
        return true;

    }

    public void winGame() {
        boolean userWin = false;
        boolean compWin = false;
        //TODO - win the game
        int i;
        for (i = 0; i < userCards.length; i++){
            if (userCards[i].getText().equals("") /*|| deck.getUserCard(i).getNum() == 13*/) {
                break;
            }
        }

        if (i == userCards.length) {
            userWin = true;
        }

        for (i = 0; i < compCards.length; i++){
            if (compCards[i].getText().equals("") || deck.getCompCard(i).getNum() == 13) {
                break;
            }
        }

        if (i == compCards.length) {
            compWin = true;
        }

        if (user_turn && userWin) {
            System.out.println("User WON");
            running = false;
        } else if (!user_turn && compWin) {
            System.out.println("Computer WON");
            running = false;
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
            }
            winGame();
        }
    }
}