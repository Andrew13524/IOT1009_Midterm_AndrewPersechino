// Andrew Persechino
// A00228632

package com.example.iot1009_midterm_andrewpersechino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Dictionary;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView inputTextView;
    TextView outputTextView;

    Button[] numberButtons;
    Button addButton;
    Button subtractButton;
    Button multiplyButton;
    Button divideButton;
    Button decimalButton;
    Button clearButton;
    Button equalsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numberButtons = new Button[10];

        // Setting view components based on ID
        inputTextView = findViewById(R.id.inputTextView);
        outputTextView = findViewById(R.id.outputTextView);

        numberButtons[0] = findViewById(R.id.zeroButton);
        numberButtons[1] = findViewById(R.id.oneButton);
        numberButtons[2] = findViewById(R.id.twoButton);
        numberButtons[3] = findViewById(R.id.threeButton);
        numberButtons[4] = findViewById(R.id.fourButton);
        numberButtons[5] = findViewById(R.id.fiveButton);
        numberButtons[6] = findViewById(R.id.sixButton);
        numberButtons[7] = findViewById(R.id.sevenButton);
        numberButtons[8] = findViewById(R.id.eightButton);
        numberButtons[9] = findViewById(R.id.nineButton);

        addButton = findViewById(R.id.addButton);
        subtractButton = findViewById(R.id.subtractButton);
        multiplyButton = findViewById(R.id.multiplyButton);
        divideButton = findViewById(R.id.divideButton);

        decimalButton = findViewById(R.id.decimalButton);
        clearButton = findViewById(R.id.clearButton);
        equalsButton = findViewById(R.id.equalsButton);

        // Using current instance as listeners
        for(int i = 0; i < numberButtons.length; i++){
            numberButtons[i].setOnClickListener(this);
        }
        addButton.setOnClickListener(this);
        subtractButton.setOnClickListener(this);
        multiplyButton.setOnClickListener(this);
        divideButton.setOnClickListener(this);
        decimalButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        equalsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(isNumberButton(view.getId())) {
            try {
                // Add button number to input text
                inputTextView.append(String.valueOf(getButtonNumber(view.getId())));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(isOperandButton(view.getId())) {
            try {
                // Add operator only if it is the only operator in input text
                // If operator does not follow a number (example: "1.") it will append a zero first (example output: "1.0")
                if(operandExists() == false){
                    if(followsANumber(inputTextView.getText()) == false) inputTextView.append("0");
                    inputTextView.append(" " + getButtonOperand(view.getId()) + " ");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if(view.getId() == R.id.decimalButton && isDecimalPlaceable()) {
            // Add decimal only if it is the only decimal in current number
            // If decimal does not follow a number (example "1 + ") it will append a zero first (example output: "1 + 0.")
            if(followsANumber(inputTextView.getText()) == false) inputTextView.append("0");
            inputTextView.append(".");
        }
        else if(view.getId() == R.id.clearButton) {
            inputTextView.setText("");
            outputTextView.setText("");
        }
        else if(view.getId() == R.id.equalsButton) {
            if(followsANumber(inputTextView.getText()) == false) inputTextView.append("0");
            outputTextView.setText("");
            float x = getFirstNumber();
            float y = getSecondNumber();

            // After getting both numbers in equation, switch case used depending on the operator used
            switch(getOperand()){
                case '+':
                    outputTextView.append(String.valueOf(x + y));
                    break;
                case '-':
                    outputTextView.append(String.valueOf(x - y));
                    break;
                case '*':
                    outputTextView.append(String.valueOf(x * y));
                    break;
                case '/':
                    outputTextView.append(String.valueOf(x / y));
                    break;
            }
        }
    }

    // Checks if id matches a number in the numberButtons array
    private boolean isNumberButton(int id){
        for (int i = 0; i < numberButtons.length; i++){
            if(numberButtons[i] == findViewById(id)) return true;
        }
        return false;
    }
    // Checks if id matches any of the possible operator buttons
    private boolean isOperandButton(int id){
        if(id == R.id.addButton || id == R.id.subtractButton || id == R.id.multiplyButton || id == R.id.divideButton) return true;
        else return false;
    }
    // Checks if decimal is placeable on number (only can be one decimal in a number)
    // This is done by splitting the text in the inputTextView, and checking if the last segment contains a decimal
    private boolean isDecimalPlaceable(){
       String inputText = inputTextView.getText().toString();
       String splitInputText[] = inputText.split(" ");
       if(splitInputText[splitInputText.length - 1].contains(".")) return false;
       return true;
    }
    // Returns first number in equation by splitting the text in the inputTextView
    private float getFirstNumber(){
        String inputText = inputTextView.getText().toString();
        String splitInputText[] = inputText.split(" ");
        return Float.parseFloat(splitInputText[0]);
    }
    // Returns second number in equation by splitting the text in the inputTextView
    private float getSecondNumber(){
        String inputText = inputTextView.getText().toString();
        String splitInputText[] = inputText.split(" ");
        return Float.parseFloat(splitInputText[splitInputText.length - 1]);
    }
    // Returns the number associated with the button id
    private int getButtonNumber(int id) throws ClassNotFoundException {
        for (int i = 0; i < numberButtons.length; i++){
            if(numberButtons[i] == findViewById(id)) return i;
        }
        throw new ClassNotFoundException();
    }
    // Returns the operand associated with the button id
    private String getButtonOperand(int id) throws ClassNotFoundException {
        switch (id){
            case R.id.addButton:
                return "+";
            case R.id.subtractButton:
                return "-";
            case R.id.multiplyButton:
                return "*";
            case R.id.divideButton:
                return "/";
            default:
                throw new ClassNotFoundException();
        }
    }
    // Returns the operand contained in the inputTextView
    // If it returns ' ', it means that there is no operand within the equation
    private char getOperand(){
        if(inputTextView.getText().length() <= 0) return ' ';
        String inputText = inputTextView.getText().toString();
        String inputTextSplits[] = inputText.split(" ");
        if(inputTextSplits.length < 2) return ' ';
        String operand = inputTextSplits[1];

        switch(operand){
            case "+":
                return '+';
            case "-":
                return '-';
            case "*":
                return '*';
            case "/":
                return '/';
            default:
                return ' ';
        }
    }
    // Checks if, based on the CharSequence, if it ends with a number
    private boolean followsANumber(CharSequence cs) {
        if(cs.length() <= 0) return false;
        char c = cs.charAt(cs.length() - 1);
        for(int i = 0; i < 10; i++) {
            if(c == (char)i + '0') return true;
        }
        return false;
    }
    // Checks if an operand exists within the equation
    private boolean operandExists(){
        if(getOperand() != ' ') return true;
        else return false;
    }
}