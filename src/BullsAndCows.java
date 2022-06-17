import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

// We are going to create a Bulls and Cows game in Java
// It is also known as 4 digits game.
// It is an old code-breaking mind or paper and pencil game for two or more players, predating the commercially marketed board game Mastermind
public class BullsAndCows {

  public static final Random RANDOM = new Random();
  private int number;
  private int nbGuesses;
  private boolean guessed;
  private JTextPane textPane;
  private JTextField textField;

  // we generate a number
  private void generateNumber() {
    do {
      number = RANDOM.nextInt(9000) + 1000;
    } while ( hasDuplicates ( number ));

    System.out.println("[CHEAT] = " + number + "\n");
  }

  private boolean hasDuplicates(int number) {
    boolean[] digits = new boolean[10];

    while (number > 0) {
      int last = number % 10;

      if (digits[last]) return true;

      digits[last] = true;
      number = number / 10;
    }

    return false;
  }

  // return the number of bulls and cows for the entered number comparing to the number to guess
  private int[] bullsAndCows(int entered) {
    int bulls = 0;
    int cows = 0;

    String enteredStr = String.valueOf(entered);
    String numberStr = String.valueOf(number);

    for (int i = 0; i < numberStr.length(); i++) {
      char c = enteredStr.charAt(i);

      if (c == numberStr.charAt(i)) {
        bulls++;
      } else if (numberStr.contains(String.valueOf(c))) {
        cows++;
      }
    }

    return new int[] {bulls,cows};
  }

  private void play() {
    // Create and set up the window.
    JFrame frame = new JFrame("Bulls and Cows on SSaurel");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container contentPane = frame.getContentPane();

    // add buttons
    JPanel buttonsPanel = new JPanel();

    JLabel inputLabel = new JLabel("Input :");
    buttonsPanel.add(inputLabel, BorderLayout.LINE_START);

    textField = new JTextField(15);
    textField.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        okClick();
      }
    });
    buttonsPanel.add(textField, BorderLayout.LINE_START);

    JButton okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        okClick();
      }
    });

    buttonsPanel.add(okButton, BorderLayout.LINE_END);

    JButton newGameButton = new JButton("New Game");
    newGameButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        init();
      }
    });

    buttonsPanel.add(newGameButton, BorderLayout.LINE_END);
    contentPane.add(buttonsPanel, BorderLayout.PAGE_END);

    // add text area
    textPane = new JTextPane();
    textPane.setEditable(false);
    JScrollPane jscrollpane = new JScrollPane(textPane);

    SimpleAttributeSet bSet = new SimpleAttributeSet();
    StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_CENTER);
    StyleConstants.setFontSize(bSet, 20);
    StyledDocument doc = textPane.getStyledDocument();
    doc.setParagraphAttributes(0, doc.getLength(), bSet, false);

    contentPane.add(jscrollpane, BorderLayout.CENTER);
    frame.setMinimumSize(new Dimension(600, 400));

    // center jframe
    Dimension objDimension = Toolkit.getDefaultToolkit().getScreenSize();
    int iCoordX = (objDimension.width - frame.getWidth()) / 2;
    int iCoordY = (objDimension.height - frame.getHeight()) / 2;
    frame.setLocation(iCoordX, iCoordY);

    // Display the window.
    frame.pack();
    frame.setVisible(true);

    init();
  }

  private void okClick() {
    String userInput = textField.getText();
    int entered = -1;

    try {
      entered = Integer.parseInt(userInput);
    } catch(Exception e) {
      textField.setText("");
      JOptionPane.showMessageDialog(null, "You must enter an interger", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (hasDuplicates(entered) || entered < 1000 || entered > 9999) {
      textField.setText("");
      JOptionPane.showMessageDialog(null, "You must enter an interger with no duplicates digits and with 4-digits", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    nbGuesses++;

    int[] answer = bullsAndCows(entered);

    if (answer[0] == 4) {
      guessed = true;
    } else {
      updateText(entered + "\t\t\t\t" + answer[0] + " Bulls and " + answer[1] + " Cows");
    }

    if (guessed) {
      updateText("\n" + entered + "\n\nYou won after " + nbGuesses + " guesses!");
    }

    textField.setText("");
  }

  private void updateText(String txt) {
    textPane.setText(textPane.getText() + "\n" + txt);
  }

  private void init() {
    generateNumber();
    nbGuesses = 0;
    guessed = false;
    textPane.setText("You must guess a 4 digits number with no duplicate digits");
    textField.setText("");
  }

  public static void main(String[] args) {
    BullsAndCows bullsAndCows = new BullsAndCows();

    javax.swing.SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        bullsAndCows.play();
      }
    });
  }

}