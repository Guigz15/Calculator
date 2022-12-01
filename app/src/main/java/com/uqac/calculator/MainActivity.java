package com.uqac.calculator;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private TextView resultText;
    private EditText operationsText;
    private double result;
    SharedPreferences.Editor prefsEditor;
    private Gson gson;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operationsText = findViewById(R.id.operation_text_view);
        resultText = findViewById(R.id.result_text_view);
        Button guessButton = findViewById(R.id.guess_result);
        ImageButton historyButton = findViewById(R.id.historyButton);
        ImageButton infoButton = findViewById(R.id.infoButton);

        SharedPreferences mPrefs = getSharedPreferences("History", MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        gson = new Gson();

        operationsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    resultText.setBackgroundResource(R.drawable.result_text_bg);
                    resultText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        guessButton.setOnClickListener(v -> {
            result = checkEntryValidity();
            if (result != Double.MAX_VALUE) {
                saveCalculationInHistory();
                showDialog();
            }
        });

        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            launcher.launch(intent);
        });

        ImageButton resultUp = findViewById(R.id.result_up);
        resultUp.setOnClickListener(v -> {
            operationsText.setText(resultText.getText());
            operationsText.setSelection(operationsText.getText().length());
            resultText.setText("");
        });
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                String operationResult = data.getStringExtra("result");
                if (Double.parseDouble(operationResult) % 1 != 0) {
                    operationsText.setText(operationResult);
                } else {
                    operationsText.setText(operationResult.substring(0, operationResult.indexOf(".")));
                }
                operationsText.setSelection(operationsText.getText().length());
            }
        }
    });

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
                        throw new RuntimeException("Missing ')'");
                    }
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) throw new RuntimeException("Missing ')'");
                    } else {
                        x = parseFactor();
                    }
                    switch (func) {
                        case "sqrt":
                            x = Math.sqrt(x);
                            break;
                        case "sin":
                            x = Math.sin(Math.toRadians(x));
                            break;
                        case "cos":
                            x = Math.cos(Math.toRadians(x));
                            break;
                        case "tan":
                            x = Math.tan(Math.toRadians(x));
                            break;
                        default:
                            throw new RuntimeException("Unknown function: " + func);
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

    private double checkEntryValidity() {
        double result = Double.MAX_VALUE;

        try {
            result = eval(operationsText.getText().toString());
        } catch (RuntimeException e) {
            String errorMessage = e.getMessage();
            if (errorMessage != null) {
                if (errorMessage.equals("Missing ')'")) {
                    Toast.makeText(this, "Manque parenthèse fermante", Toast.LENGTH_LONG).show();
                    operationsText.setError("Manque parenthèse fermante");
                } else if (errorMessage.startsWith("Unexpected: ")) {
                    Toast.makeText(this, "Caractère inattendu: " + errorMessage.substring(12), Toast.LENGTH_LONG).show();
                    operationsText.setError("Caractère inattendu: " + errorMessage.substring(12));
                } else if (errorMessage.startsWith("Unknown function: ")) {
                    Toast.makeText(this, "Fonction inconnue: " + errorMessage.substring(18), Toast.LENGTH_LONG).show();
                    operationsText.setError("Fonction inconnue: " + errorMessage.substring(18));
                }
            } else {
                e.printStackTrace();
            }
        }

        return result;
    }

    public void equalsOnClick(View view) {
        if (operationsText.getText().length() > 0) {
            result = checkEntryValidity();

            if (result != Double.MAX_VALUE) {
                if (doubleToInt(result))
                    resultText.setText(String.valueOf(result));
                saveCalculationInHistory();
            } else {
                resultText.setText("ERROR");
            }
        }
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_enter_result);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        EditText resultEditText = dialog.findViewById(R.id.result_edit_text);
        Button validateButton = dialog.findViewById(R.id.validate_button);

        validateButton.setOnClickListener(v -> {
            if (resultEditText.getText().length() > 0) {
                if (result == Double.parseDouble(resultEditText.getText().toString())) {
                    Toast.makeText(this, "Bonne réponse !", Toast.LENGTH_LONG).show();
                    resultText.setBackgroundResource(R.drawable.result_correct_answer);
                } else {
                    Toast.makeText(this, "Mauvaise réponse !", Toast.LENGTH_LONG).show();
                    resultText.setBackgroundResource(R.drawable.result_wrong_answer);
                }
                dialog.dismiss();
                if (doubleToInt(result))
                    resultText.setText(String.valueOf(result));
            } else {
                Toast.makeText(this, "Veuillez entrer un résultat", Toast.LENGTH_LONG).show();
            }
        });

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private void saveCalculationInHistory() {
        Calculation calculation = new Calculation(operationsText.getText().toString(), String.valueOf(result));
        String json = gson.toJson(calculation);
        prefsEditor.putString(calculation.getId(), json);
        prefsEditor.commit();
    }

    private boolean doubleToInt(double toConvert) {
        if (toConvert % 1 == 0) {
            resultText.setText(String.valueOf((int) toConvert));
            return false;
        } else {
            return true;
        }
    }
}