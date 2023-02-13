package it.uniroma2.dicii.ispw.calcolatrice;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Calculate extends Application {
    private String num1 = "";
    private String num2 = "";
    private String op  = "";
    private double result= 0;
    private boolean oldop = false ;
    @FXML
    private Label display;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Calculate.class.getResource("Calcolatrice.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 460);
        stage.setTitle("Calcolatrice");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void numAction(ActionEvent event) {
        String n = ((Button) event.getSource()).getText();
        if (Objects.equals(op, "√") || num1.isEmpty()) {
            num1 += n;
            updateDisplay(op + num1);
        } else if (Objects.equals(op, "log")) {
            num1 += n;
            updateDisplay(op + "(" + num1 + ")");
        } else if (!oldop) {
            num1 += n;
            updateDisplay(num1);
        } else {
            num2 += n;
            updateDisplay(num1 + op + num2);
        }
    }

    @FXML
    protected void dotAction() {
        String currentNum = oldop ? num2 : num1;
        if (!currentNum.isEmpty()) {
            currentNum += '.';
            updateDisplay(oldop ? num1 + op + currentNum : currentNum);
        }
    }

    @FXML
    protected void changeSignAction() {
        String num = oldop && !num2.isEmpty() ? num2 : num1;
        if (!num.isEmpty()) {
            double n = Double.parseDouble(num);
            num = String.valueOf(n * -1);
        }
        if (oldop && !num2.isEmpty()) {
            num2 = num;
            updateDisplay(num1 + op + "(" + num2 + ")");
        } else {
            num1 = num;
            updateDisplay(num1);
        }
    }

    @FXML
    private void opBaseAction(ActionEvent event) {
        String s = ((Button) event.getSource()).getText();
        if (!s.isEmpty() && !num1.isEmpty()) {
            equalAction();
            oldop = true;
            op = s;
            updateDisplay(num1 + op);
        }
    }

    @FXML
    private void opExpertAction(ActionEvent event) {
        String s = ((Button) event.getSource()).getText();
        if (!op.isEmpty()) {
            equalAction();
        }
        oldop = true;
        op = s;
        switch (op) {
            case "x²" -> {
                op = "²";
                updateDisplay(num1 + op);
            }
            case "log" -> updateDisplay(op + "(" + num1 + ")");
            default -> updateDisplay(op + num1);
        }
    }

    @FXML
    protected void equalAction() {
        if (op.isEmpty() || num1.isEmpty()) {
            return;
        }

        double n1, n2 = 0;
        try {
            n1 = Double.parseDouble(num1);
            if (!num2.isEmpty()) {
                n2 = Double.parseDouble(num2);
            }
        } catch (NumberFormatException e) {
            updateDisplay("Errore: input non valido");
            return;
        }

        if (op.equals("÷") && n2 == 0) {
            updateDisplay("Error: Division by zero");
        } else if(op.equals("√") && n1 < 0){
            updateDisplay("Error: Square root of a negative number");
        }else {
            result = calc(n1, n2, op);
            num1 = String.valueOf(result);
            updateDisplay(num1);
            num2 = "";
            op = "";
            oldop = false;
        }
    }

    @FXML
    protected void cAction() {
        num1 = "";
        num2 = "";
        op = "";
        result = 0;
        oldop = false;
        updateDisplay("");
    }

    @FXML
    protected void ceAction() {
        if (!oldop) {
            num1 = "";
            updateDisplay(op);
        } else {
            num2 = "";
            updateDisplay(num1 + op);
        }
        result = 0;
    }

    private void updateDisplay(String text) {
        display.setText(text);
    }

    public double calc(double n1, double n2, String op) {
        return switch (op) {
            case "+" -> n1 + n2;
            case "-" -> n1 - n2;
            case "x" -> n1 * n2;
            case "÷" -> n1 / n2;
            case "log" -> Math.log10(n1);
            case "√" -> Math.sqrt(n1);
            case "²" -> n1 * n1;
            default -> 0;
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}