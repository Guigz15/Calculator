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

    private String getLeftNumberFromPosition(int position) {
        StringBuilder leftNumber = new StringBuilder();
        while (position >= 0 && (operation.charAt(position) == '.' || Character.isDigit(operation.charAt(position)))) {
            leftNumber.insert(0, operation.charAt(position));
            position--;
        }
        return leftNumber.toString();
    }

    public void equalsOnClick(View view) {
        Double result = null;
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("rhino");

        if (operation.contains("%")) {
            System.out.println(getLeftNumberFromPosition(operation.indexOf("%") - 1));
        }

        try {
            result = (double) engine.eval(operation);
        } catch (ScriptException e) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();
        }

        if(result != null)
            resultText.setText(String.valueOf(result.doubleValue()));
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