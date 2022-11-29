package com.uqac.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MainActivity extends AppCompatActivity {

    private TextView resultText;
    private TextView operationsText;

    private String operation = "";

    private int nbbrackets = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operationsText = findViewById(R.id.operation_text_view);
        resultText = findViewById(R.id.result_text_view);
    }

    private void setOperation(String value) {
        if(!resultText.getText().toString().isEmpty()) {
            System.out.println(resultText.getText().toString());
            operationsText.setText(resultText.getText().toString());
            operation = resultText.getText().toString();
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
        nbbrackets = 0;
    }

    public void clearOperation() {
        operation = "";
        operationsText.setText(operation);
    }

    public void equalsOnClick(View view) {
        Double result = null;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");

        try {
            result = (double) engine.eval(operation);
        } catch (ScriptException e) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
        }

        if(result != null)
            resultText.setText(String.valueOf(result.doubleValue()));
    }

    public void bracketsOnClick(View view) {
        if(nbbrackets == 0 && operation.isEmpty()) {
            setOperation("(");
            nbbrackets++;
        } else {
            if (Character.isDigit(operation.charAt(operation.length() - 1)) && nbbrackets > 0) {
                setOperation(")");
                nbbrackets--;
            } else if (operation.charAt(operation.length() - 1) == ')' && nbbrackets > 0) {
                setOperation(")");
                nbbrackets--;
            } else if (operation.charAt(operation.length() - 1) == ')' && nbbrackets == 0) {
                setOperation("*(");
                nbbrackets++;
            } else {
                setOperation("(");
                nbbrackets++;
            }
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
        setOperation("+");
    }

    public void timesOnClick(View view)
    {
        setOperation("*");
    }

    public void minusOnClick(View view)
    {
        setOperation("-");
    }

    public void plusMinusOnClick(View view)
    {
        int lastSignPosition = lastSignInOperation();
        StringBuilder newOperation = new StringBuilder();

        if (lastSignPosition == -1) {
            newOperation.append("(");
            newOperation.append("-");
            newOperation.append(operation);
            nbbrackets++;
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
                nbbrackets++;
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
        setOperation("/");
    }

    public void decimalOnClick(View view)
    {
        setOperation(".");
    }

    public void percentOnClick(View view)
    {
        setOperation("%");
    }
}