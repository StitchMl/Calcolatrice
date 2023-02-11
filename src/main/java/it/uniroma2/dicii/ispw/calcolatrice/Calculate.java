package it.uniroma2.dicii.ispw.calcolatrice;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXMLLoader;

public class Calculate extends Application {
    private String num1 = "";
    private String num2 = "";
    private String op  = "";
    private double result= 0;
    private boolean oldop = false ;
    private static final String ERROR = "Errore: input non valido";

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

    private void numAction(String n){
        if (Objects.equals(op, "√") || num1.isEmpty()){
            num1 += n;
            display.setText(op + num1);
        } else if (Objects.equals(op, "log")){
            num1 += n;
            display.setText(op + "(" + num1 + ")");
        } else if (!oldop) {
            num1 += n;
            display.setText(num1);
        } else {
            num2 += n;
            display.setText(num1 + op + num2);
        }
    }
    @FXML
    protected void oneAction() {
        numAction("1");
    }
    @FXML
    protected void twoAction() {
        numAction("2");
    }
    @FXML
    protected void threeAction() {
        numAction("3");
    }
    @FXML
    protected void fourAction() {
        numAction("4");
    }
    @FXML
    protected void fiveAction() {
        numAction("5");
    }
    @FXML
    protected void sixAction() {
        numAction("6");
    }
    @FXML
    protected void sevenAction() {
        numAction("7");
    }
    @FXML
    protected void eightAction() {
        numAction("8");
    }
    @FXML
    protected void nineAction() {
        numAction("9");
    }
    @FXML
    protected void zeroAction(){
        numAction("0");
    }

    @FXML
    protected void dotAction() {
        if(!num1.isEmpty()) {
            if (!oldop) {
                num1 += '.';
                display.setText(num1);
            } else {
                num2 += '.';
                display.setText(num2);
            }
        }
    }

    @FXML
    protected void changeSignAction() {
        if (!oldop && !num1.isEmpty()) {
            if (Double.parseDouble(num1) < 0) {
                num1 = String.valueOf(Double.parseDouble(num1) * -1);
            } else {
                num1 = '-' + num1;
            }
            display.setText(num1);
        } else if (!num2.isEmpty()) {
            if (Double.parseDouble(num2) < 0) {
                num2 = String.valueOf(Double.parseDouble(num2) * -1);
            } else {
                num2 = '-' + num2;
            }
            display.setText(num1 + op + "(" + num2 + ")");
        }
    }

    @FXML
    protected void sumAction() {
        action("+");
    }

    @FXML
    protected void mineAction() {
        action("-");
    }

    @FXML
    protected void mulAction() {
        action("x");
    }

    @FXML
    protected void divideAction() {
        action("÷");
    }

    private void action(String s){
        if(!Objects.equals(op, "")){
            equalAction();
        }
        if(!num1.isEmpty()) {
            baseAction(s);
        }
    }

    private void baseAction(String s){
        if (!oldop) {
            oldop = true;
            op = s;
            display.setText(num1 + op);
        } else {
            try {
                double n1 = Double.parseDouble(num1);
                double n2 = Double.parseDouble(num2);
                if(Objects.equals(op, "÷") && n2 == 0){
                    display.setText("Error: Division by zero");
                } else {
                    result = calc(n1, n2, op);
                    num1 = String.valueOf(result);
                    num2 = "";
                    op = s;
                    display.setText(num1 + op);
                    oldop = true;
                }
            } catch (NumberFormatException e) {
                display.setText(ERROR);
            }
        }
    }

    @FXML
    protected void squaredAction() {
        if(!Objects.equals(op, "")){
            equalAction();
        }
        if(!num1.isEmpty()) {
            try {
                oldop = false;
                op = "x";
                double n1 = Double.parseDouble(num1);
                result = calc(n1, n1, op);
                num1 = String.valueOf(result);
                display.setText(num1);
                num2 = "";
            } catch (NumberFormatException e) {
                display.setText(ERROR);
            }
        }
    }

    @FXML
    protected void logAction() {
        if(!Objects.equals(op, "")){
            equalAction();
        }
        if (!oldop && num1.isEmpty()) {
            oldop = true;
            op = "log";
            display.setText(op);
        } else if (!oldop) {
            oldop = true;
            op = "log";
            display.setText(op + "(" + num1 + ")");
        } else {
            try {
                equalAction();
                num1 = String.valueOf(result);
                num2 = "";
                op = "log";
                display.setText(op + "(" + num1 + ")");
                oldop = true;
            } catch (NumberFormatException e) {
                display.setText(ERROR);
            }
        }
    }

    @FXML
    protected void sqrtAction() {
        if(!Objects.equals(op, "")){
            equalAction();
        }
        if (!oldop && num1.isEmpty()) {
            oldop = true;
            op = "√";
            display.setText(op);
        } else if (!oldop) {
            oldop = true;
            op = "√";
            display.setText(op + num1);
        } else {
            try {
                equalAction();
                num1 = String.valueOf(result);
                num2 = "";
                op = "√";
                display.setText(op + num1);
                oldop = true;
            } catch (NumberFormatException e) {
                display.setText(ERROR);
            }
        }
    }

    @FXML
    protected void equalAction() {
        if (!oldop) {
            return;
        }

        double n1 = num1.isEmpty() ? 0 : Double.parseDouble(num1);
        double n2 = num2.isEmpty() ? 0 : Double.parseDouble(num2);

        switch (op) {
            case "log" -> {
                if (n1 <= 0) {
                    display.setText("Errore: logaritmo di un numero non positivo");
                    return;
                }
                result = Math.log10(n1);
            }
            case "√" -> {
                if (n1 < 0) {
                    display.setText("Errore: radice di un numero negativo");
                    return;
                }
                result = Math.sqrt(n1);
            }
            case "÷" -> {
                if (n2 == 0) {
                    display.setText("Errore: divisione per zero");
                    return;
                }
                result = calc(n1, n2, op);
            }
            default -> result = calc(n1, n2, op);
        }

        num1 = String.valueOf(result);
        display.setText(num1);
        oldop = false;
        num2 = "";
    }

    @FXML
    protected void cAction() {
        num1 = "";
        num2 = "";
        result = 0;
        display.setText("");
        oldop = false;
    }

    @FXML
    protected void ceAction() {
        if (!oldop) {
            num1 = "";
            display.setText(op);
        } else {
            num2 = "";
            display.setText(num1 + op);
        }
        result = 0;
        display.setText("");
    }

    public double calc(double n1, double n2, String op) {
        return switch (op) {
            case "+" -> n1 + n2;
            case "-" -> n1 - n2;
            case "x" -> n1 * n2;
            case "÷" -> n1 / n2;
            default -> 0;
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}