package com.uqac.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView resultText;
    private TextView operationsText;
    private String operation = "";
    private boolean leftBracket = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operationsText = findViewById(R.id.operation_text_view);
        resultText = findViewById(R.id.result_text_view);
    }

    private void setOperation(String value) {
        if(!resultText.getText().toString().isEmpty()) {
            if (Character.isDigit(value.charAt(0))) {
                clearOperation();
            } else {
                operationsText.setText(resultText.getText().toString());
                operation = resultText.getText().toString();
            }
            clearResult();
        }
        operation += value;
        operationsText.setText(operation);
    }

    public void clearOnClick(View view) {
        clearOperation();
        clearResult();
    }

    public void clearResult() {
        resultText.setText("");
        leftBracket = false;
    }

    public void clearOperation() {
        operation = "";
        operationsText.setText(operation);
    }

    private double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    if (!eat(')')) {
                        Toast.makeText(getApplicationContext(), "Missing right bracket", Toast.LENGTH_SHORT).show();
                    }
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    public void equalsOnClick(View view) {
        if (operation.length() > 0) {
            double result = eval(operation);

            if (result != Double.MAX_VALUE) {
                resultText.setText(String.valueOf(result));
            } else {
                resultText.setText("ERROR");
            }
        }
    }

    public void bracketsOnClick(View view) {
        if (leftBracket) {
            setOperation(")");
            leftBracket = false;
        } else {
            if (operation.length() > 0) {
                if (Character.isDigit(operation.charAt(operation.length() - 1)) ||
                    operation.charAt(operation.length() - 1) == ')') {
                    setOperation("*(");
                } else {
                    setOperation("(");
                }
            } else {
                setOperation("(");
            }
            leftBracket = true;
        }
    }

    public void zeroOnClick(View view)
    {
        setOperation("0");
    }

    public void oneOnClick(View view)
    {
        setOperation("1");
    }

    public void twoOnClick(View view)
    {
        setOperation("2");
    }

    public void threeOnClick(View view)
    {
        setOperation("3");
    }

    public void fourOnClick(View view)
    {
        setOperation("4");
    }

    public void fiveOnClick(View view)
    {
        setOperation("5");
    }

    public void sixOnClick(View view)
    {
        setOperation("6");
    }

    public void sevenOnClick(View view)
    {
        setOperation("7");
    }

    public void eightOnClick(View view)
    {
        setOperation("8");
    }

    public void nineOnClick(View view)
    {
        setOperation("9");
    }

    public void plusOnClick(View view)
    {
        if (operation.length() > 0) {
            if (Character.isDigit(operation.charAt(operation.length() - 1)) ||
                operation.charAt(operation.length() - 1) == '(' ||
                operation.charAt(operation.length() - 1) == ')' ||
                operation.charAt(operation.length() - 1) == '^') {
                setOperation("+");
            } else {
                operation = operation.substring(0, operation.length() - 1);
                setOperation("+");
            }
        }
    }

    public void timesOnClick(View view)
    {
        if (operation.length() > 0 && operation.charAt(operation.length() - 1) != '(' && operation.charAt(operation.length() - 1) != '^') {
            if (Character.isDigit(operation.charAt(operation.length() - 1)) ||
                    operation.charAt(operation.length() - 1) == ')') {
                setOperation("*");
            } else if ((operation.charAt(operation.length() - 2) == '(' ||
                    operation.charAt(operation.length() - 2) == '^') &&
                    !Character.isDigit(operation.charAt(operation.length() - 1))) {
                operation = operation.substring(0, operation.length() - 1);
                operationsText.setText(operation);
            } else {
                operation = operation.substring(0, operation.length() - 1);
                setOperation("*");
            }
        }
    }

    public void minusOnClick(View view)
    {
        if (operation.length() > 0) {
            if (Character.isDigit(operation.charAt(operation.length() - 1)) ||
                    operation.charAt(operation.length() - 1) == '(' ||
                    operation.charAt(operation.length() - 1) == ')' ||
                    operation.charAt(operation.length() - 1) == '^') {
                setOperation("-");
            } else {
                operation = operation.substring(0, operation.length() - 1);
                setOperation("-");
            }
        }
    }

    public void plusMinusOnClick(View view)
    {
        int lastSignPosition = lastSignInOperation();
        StringBuilder newOperation = new StringBuilder();

        if (lastSignPosition == -1) {
            newOperation.append("(");
            newOperation.append("-");
            newOperation.append(operation);
            leftBracket = true;
        } else {
            if ((operation.charAt(lastSignPosition) == '-' && lastSignPosition == operation.length() - 1 && operation.charAt(lastSignPosition - 1) == '(')
                    || (operation.charAt(lastSignPosition) == '-' && operation.charAt(lastSignPosition - 1) == '(')) {
                newOperation.append(operation.substring(0, lastSignPosition-1));
                newOperation.append(operation.substring(lastSignPosition + 1));
            } else {
                newOperation.append(operation.substring(0, lastSignPosition));
                newOperation.append(operation.charAt(lastSignPosition));
                newOperation.append("(");
                newOperation.append("-");
                newOperation.append(operation.substring(lastSignPosition + 1));
                leftBracket = true;
            }
        }
        operation = newOperation.toString();
        operationsText.setText(operation);
    }

    public int lastSignInOperation()
    {
        int position = operation.length() - 1;
        while(position >= 0 && Character.isDigit(operation.charAt(position)))
        {
            System.out.println("position : " + position);
            System.out.println("char : " + operation.charAt(position));
            position--;
        }
        return position;
    }

    public void divisionOnClick(View view)
    {
        if (operation.length() > 0 && operation.charAt(operation.length() - 1) != '(' && operation.charAt(operation.length() - 1) != '^') {
            if (Character.isDigit(operation.charAt(operation.length() - 1)) ||
                    operation.charAt(operation.length() - 1) == ')') {
                setOperation("/");
            } else if ((operation.charAt(operation.length() - 2) == '(' ||
                    operation.charAt(operation.length() - 2) == '^') &&
                    !Character.isDigit(operation.charAt(operation.length() - 1))) {
                operation = operation.substring(0, operation.length() - 1);
                operationsText.setText(operation);
            } else {
                operation = operation.substring(0, operation.length() - 1);
                setOperation("/");
            }
        }
    }

    public void decimalOnClick(View view)
    {
        if (operation.length() > 0) {
            if (Character.isDigit(operation.charAt(operation.length() - 1))) {
                setOperation(".");
            } else {
                operation = operation.substring(0, operation.length() - 1);
                setOperation(".");
            }
        }
    }

    public void powOnClick(View view)
    {
        if (operation.length() > 0 && Character.isDigit(operation.charAt(operation.length() - 1))) {
            setOperation("^(");
            leftBracket = true;
        }
    }
}