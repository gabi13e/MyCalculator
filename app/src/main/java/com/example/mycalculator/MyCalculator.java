package com.example.mycalculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MyCalculator extends AppCompatActivity {
    private EditText number1EditText, number2EditText;
    private TextView resultTextView;
    private Button addButton, subtractButton, multiplyButton, divideButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        setupUIComponents();

        // Set button listeners
        setupButtonListeners();
    }

    private void setupUIComponents() {
        number1EditText = findViewById(R.id.number1_edit_text);
        number2EditText = findViewById(R.id.number2_edit_text);
        resultTextView = findViewById(R.id.result_text_view);
        addButton = findViewById(R.id.add_button);
        subtractButton = findViewById(R.id.subtract_button);
        multiplyButton = findViewById(R.id.multiply_button);
        divideButton = findViewById(R.id.divide_button);
    }

    private void setupButtonListeners() {
        addButton.setOnClickListener(v -> {
            if (validateInputs()) calculate("+");
        });
        subtractButton.setOnClickListener(v -> {
            if (validateInputs()) calculate("-");
        });
        multiplyButton.setOnClickListener(v -> {
            if (validateInputs()) calculate("*");
        });
        divideButton.setOnClickListener(v -> {
            if (validateInputs()) calculate("/");
        });
    }

    private void calculate(String operator) {
        try {
            // Get input values with better validation
            String input1 = number1EditText.getText().toString().trim();
            String input2 = number2EditText.getText().toString().trim();

            // Validate inputs are not empty
            if (input1.isEmpty() || input2.isEmpty()) {
                showError("Please enter both numbers");
                return;
            }

            // Parse to double for better precision
            double number1 = parseDouble(input1);
            double number2 = parseDouble(input2);
            double result = 0;

            // Perform calculation
            switch (operator) {
                case "+":
                    result = number1 + number2;
                    break;
                case "-":
                    result = number1 - number2;
                    break;
                case "*":
                    result = number1 * number2;
                    break;
                case "/":
                    if (number2 == 0) {
                        showError("Cannot divide by zero");
                        return;
                    }
                    result = number1 / number2;
                    break;
                default:
                    showError("Invalid operator");
                    return;
            }

            // Format and display result
            String formattedResult = formatResult(result);
            showResult("Result: " + formattedResult);

            // Add animation for result
            animateResult();

        } catch (NumberFormatException e) {
            showError("Invalid number format");
        } catch (ArithmeticException e) {
            showError("Math error occurred");
        } catch (Exception e) {
            showError("Something went wrong");
        }
    }

    // Enhanced parsing method with better error handling
    private double parseDouble(String str) throws NumberFormatException {
        if (str == null || str.trim().isEmpty()) {
            throw new NumberFormatException("Empty input");
        }

        // Handle common input variations
        str = str.trim().replace(",", ""); // Remove commas

        return Double.parseDouble(str);
    }

    // Smart result formatting
    private String formatResult(double result) {
        // Check if result is effectively an integer
        if (result == Math.floor(result) && !Double.isInfinite(result)) {
            // For very large integers, use scientific notation
            if (Math.abs(result) >= 1000000000) {
                return String.format("%.2e", result);
            }
            return String.format("%.0f", result);
        } else {
            // For decimals, limit to reasonable precision
            if (Math.abs(result) >= 1000000) {
                // Scientific notation for very large numbers
                return String.format("%.2e", result);
            } else if (Math.abs(result) < 0.001 && result != 0) {
                // Scientific notation for very small numbers
                return String.format("%.2e", result);
            } else {
                // Regular decimal format, remove trailing zeros
                String formatted = String.format("%.6f", result);
                formatted = formatted.replaceAll("0+$", ""); // Remove trailing zeros
                formatted = formatted.replaceAll("\\.$", ""); // Remove trailing dot
                return formatted;
            }
        }
    }

    // Add subtle animation to result
    private void animateResult() {
        resultTextView.setAlpha(0f);
        resultTextView.animate()
                .alpha(1f)
                .setDuration(300)
                .start();
    }

    // Enhanced input validation
    private boolean validateInputs() {
        String input1 = number1EditText.getText().toString().trim();
        String input2 = number2EditText.getText().toString().trim();

        // Clear previous errors
        number1EditText.setError(null);
        number2EditText.setError(null);

        if (input1.isEmpty()) {
            number1EditText.setError("Enter first number");
            number1EditText.requestFocus();
            return false;
        }

        if (input2.isEmpty()) {
            number2EditText.setError("Enter second number");
            number2EditText.requestFocus();
            return false;
        }

        try {
            parseDouble(input1);
        } catch (NumberFormatException e) {
            number1EditText.setError("Invalid number");
            number1EditText.requestFocus();
            return false;
        }

        try {
            parseDouble(input2);
        } catch (NumberFormatException e) {
            number2EditText.setError("Invalid number");
            number2EditText.requestFocus();
            return false;
        }

        return true;
    }

    // Helper method to show errors
    private void showError(String message) {
        resultTextView.setText("Error: " + message);
        resultTextView.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
    }

    // Helper method to show results
    private void showResult(String message) {
        resultTextView.setText(message);
        resultTextView.setTextColor(ContextCompat.getColor(this, android.R.color.black));
    }

    // Keep original parseInt method for backward compatibility (if needed elsewhere)
    private int parseInt(String str) {
        if (str == null || str.isEmpty()) {
            return 0;
        }
        try {
            return (int) Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}