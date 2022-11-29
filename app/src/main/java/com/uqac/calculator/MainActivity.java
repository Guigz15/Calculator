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

    private boolean leftBracket = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operationsText = findViewById(R.id.operation_text_view);
        resultText = findViewById(R.id.result_text_view);
    }

    private void setOperation(String value) {
        operation += value;
        operationsText.setText(operation);
    }

    public void clearOnClick(View view) {
        operation = "";
        operationsText.setText(operation);
        resultText.setText("");
        leftBracket = false;
    }

    private String getPercentageNumber(int percentPosition) {
        StringBuilder leftNumber = new StringBuilder();
        while (percentPosition >= 0 && (operation.charAt(percentPosition) == '.' || Character.isDigit(operation.charAt(percentPosition)))) {
            leftNumber.insert(0, operation.charAt(percentPosition));
            percentPosition--;
        }
        return leftNumber.toString();
    }

    private double calculate(String operation) {
        Double result = null;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");

        try {
            result = (double) engine.eval(operation);
        } catch (ScriptException e) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
        }

        if(result != null) {
            return result;
        } else {
            return Double.MAX_VALUE;
        }
    }

    public void equalsOnClick(View view) {
        double result;

        if (operation.contains("%")) {
            int percentPosition = operation.indexOf("%") - 1;
            StringBuilder percentNumber = new StringBuilder();
            while (percentPosition >= 0 && (operation.charAt(percentPosition) == '.' || Character.isDigit(operation.charAt(percentPosition)))) {
                percentNumber.insert(0, operation.charAt(percentPosition));
                percentPosition--;
            }

            int operatorPosition = percentPosition;
            if (operatorPosition == -1) {
                result = calculate(percentNumber + "/100");
            } else {
                String leftSide = operation.substring(0, operatorPosition);
                double leftResult = calculate(leftSide);
                double rightResult = leftResult * Double.parseDouble(percentNumber.toString()) / 100;
                result = calculate(leftSide + operation.charAt(operatorPosition) + rightResult);
            }
        } else {
            result = calculate(operation);
        }

        if (result != Double.MAX_VALUE) {
            resultText.setText(String.valueOf(result));
        } else {
            resultText.setText("ERROR");
        }
    }

    public void bracketsOnClick(View view) {
        if (leftBracket) {
            setOperation(")");
            leftBracket = false;
        } else {
            setOperation("(");
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
        if(operation.length() > 0)
        {
            if(operation.charAt(operation.length() - 1) == '-')
                operation = operation.substring(0, operation.length() - 1);
            else
                setOperation("");
        }
        else
            setOperation("-");
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