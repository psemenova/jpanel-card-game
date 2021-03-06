import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GameFrame extends JFrame {
    static final int SCREEN_HEIGHT = 700;
    static final int SCREEN_WIDTH = 500;

    JButton startBtn;
    JPanel startPanel;

    GameFrame() {

        this.setTitle("Trash");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setSize(SCREEN_WIDTH,SCREEN_HEIGHT);
        this.setVisible(true);
        this.setLocationRelativeTo(null);

        startPanel = new JPanel();

        startBtn = new JButton("Start");
        startBtn.setBounds((SCREEN_WIDTH / 2) - 50, (SCREEN_HEIGHT / 2) - 25, 100, 50);

        startPanel.setLayout(new BorderLayout());
        startPanel.setBounds(0,0,SCREEN_WIDTH, SCREEN_HEIGHT);
        startPanel.add(startBtn, BorderLayout.CENTER);

        JLabel name = new JLabel("Garbage");
        name.setSize(200,200);
        name.setBounds((SCREEN_WIDTH / 2) - 50, SCREEN_HEIGHT / 3, 100, 50);
        name.setForeground(new Color(22, 111, 12));
        name.setFont(new Font("Serif", Font.BOLD, 20));
        startPanel.add(name, BorderLayout.NORTH);

        startPanel.setVisible(true);

        this.add(startPanel, BorderLayout.CENTER);

        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startPanel.setVisible(false);
                try {
                    startGame();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    public void startGame() throws IOException {
        this.add(new GamePanel(this));
    }


}
