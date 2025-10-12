package com.android.calculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.google.android.material.button.MaterialButton;
import java.text.DecimalFormat;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 * ExpressionEvaluator handles calculator input, operations,
 * real-time calculation updates, and history saving.
 * Supports numeric, operator, decimal point inputs, backspace/clear, and equals.
 */
public class ExpressionEvaluator {
    private final Context context;
    final DatabaseHelper databaseHelper;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##########");
    private final AppLogger logger = new AppLogger("ExpressionEvaluator");
    private boolean isNewInput = true;
    private boolean lastInputWasOperator = false;
    private final StringBuilder currentExpression = new StringBuilder();

    /**
     * Initializes the evaluator with context and database helper.
     * @param context application context
     */
    public ExpressionEvaluator(Context context) {
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
        logger.d("ExpressionEvaluator initialized");
    }

    /**
     * Handles numeric button input.
     * @param button        pressed numeric button
     * @param inputDisplay  TextView displaying the current input
     * @param resultDisplay TextView displaying real-time calculation
     */
    public void handleNumberInput(MaterialButton button, TextView inputDisplay, TextView resultDisplay) {
        String value = button.getText().toString();
        if (isNewInput) {
            inputDisplay.setText(value);
            currentExpression.setLength(0);
            currentExpression.append(value);
            isNewInput = false;
        } else {
            inputDisplay.append(value);
            currentExpression.append(value);
        }
        lastInputWasOperator = false;
        updateRealtimeCalculation(resultDisplay);
        logger.v("Number input handled: " + value);
    }

    /**
     * Handles decimal point input, preventing multiple dots in a number segment.
     * @param inputDisplay  TextView displaying current input
     * @param resultDisplay TextView displaying real-time calculation
     */
    public void handleDot(TextView inputDisplay, TextView resultDisplay) {
        String text = inputDisplay.getText().toString();
        String[] parts = text.split("[+\\-*/%]");
        String lastSegment = parts[parts.length - 1];

        if (!lastSegment.contains(".")) {
            inputDisplay.append(".");
            currentExpression.append(".");
            lastInputWasOperator = false;
            updateRealtimeCalculation(resultDisplay);
            logger.v("Decimal point added");
        } else {
            logger.w("Attempted multiple decimal points in a number segment");
        }
    }

    /**
     * Handles operator input (+, -, *, /, %).
     * @param operator     operator character
     * @param inputDisplay TextView displaying current input
     */
    @SuppressLint("SetTextI18n")
    public void handleOperation(char operator, TextView inputDisplay) {
        if (currentExpression.length() == 0 && operator != '-') return;

        if (lastInputWasOperator) {
            currentExpression.setCharAt(currentExpression.length() - 1, operator);
            String text = inputDisplay.getText().toString();
            inputDisplay.setText(text.substring(0, text.length() - 1) + operator);
            logger.v("Replaced last operator with: " + operator);
        } else {
            inputDisplay.append(String.valueOf(operator));
            currentExpression.append(operator);
            logger.v("Operator appended: " + operator);
        }

        lastInputWasOperator = true;
        isNewInput = false;
    }

    /**
     * Handles backspace / clear button input.
     * @param inputDisplay  TextView displaying current input
     * @param resultDisplay TextView displaying real-time calculation
     */
    public void handleClear(TextView inputDisplay, TextView resultDisplay) {
        if (currentExpression.length() > 0) {
            currentExpression.setLength(currentExpression.length() - 1);
            inputDisplay.setText(currentExpression.toString());
            lastInputWasOperator = false;
            updateRealtimeCalculation(resultDisplay);
            logger.v("Backspace applied");
        } else {
            handleAllClear(inputDisplay, resultDisplay);
        }
    }

    /**
     * Handles all-clear (AC) button input.
     * @param inputDisplay  TextView displaying current input
     * @param resultDisplay TextView displaying real-time calculation
     */
    public void handleAllClear(TextView inputDisplay, TextView resultDisplay) {
        currentExpression.setLength(0);
        inputDisplay.setText("");
        resultDisplay.setText("");
        lastInputWasOperator = false;
        isNewInput = true;
        logger.v("All-clear applied");
    }

    /**
     * Handles equal (=) button input.
     * Evaluates expression, displays result, and stores in history database.
     * @param inputDisplay  TextView displaying current input
     * @param resultDisplay TextView displaying real-time calculation
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void handleEqual(TextView inputDisplay, TextView resultDisplay) {
        if (currentExpression.length() == 0) {
            Toast.makeText(context, "Enter an expression", Toast.LENGTH_SHORT).show();
            logger.w("Equal pressed with empty expression");
            return;
        }

        String expr = currentExpression.toString();
        if (lastInputWasOperator) {
            expr = expr.substring(0, expr.length() - 1);
            logger.w("Removed trailing operator before evaluation");
        }

        try {
            expr = expr.replace("%", "/100");
            Expression expression = new ExpressionBuilder(expr).build();
            double result = expression.evaluate();
            String resultString = decimalFormat.format(result);

            String recordToSave = currentExpression + " = " + resultString;
            long recordId = databaseHelper.insertCalculation(recordToSave);
            if (recordId > 0) logger.d("Record saved → " + recordToSave);
            else logger.e("Failed to save record: " + recordToSave, (Throwable) null);

            inputDisplay.setText(resultString);
            resultDisplay.setText("");
            currentExpression.setLength(0);
            currentExpression.append(resultString);
            isNewInput = true;
            lastInputWasOperator = false;

            logger.v("Expression evaluated: " + expr + " = " + resultString);
        } catch (ArithmeticException e) {
            Toast.makeText(context, "Math error", Toast.LENGTH_SHORT).show();
            logger.e("Math error in expression: " + expr, e);
        } catch (Exception e) {
            Toast.makeText(context, "Invalid expression", Toast.LENGTH_SHORT).show();
            logger.e("Evaluation failed — expression not saved: " + expr, e);
        }
    }

    /**
     * Clears all calculation history from database.
     */
    public void clearAllHistory() {
        boolean success = databaseHelper.deleteAllRecords();
        if (success) logger.d("All history cleared");
        else logger.e("Failed to clear history", (Throwable) null);
    }

    /**
     * Updates real-time calculation result in result display.
     * @param resultDisplay TextView showing live calculation result
     */
    private void updateRealtimeCalculation(TextView resultDisplay) {
        if (currentExpression.length() == 0) {
            resultDisplay.setText("");
            return;
        }

        try {
            Expression expression = new ExpressionBuilder(currentExpression.toString()).build();
            double result = expression.evaluate();
            resultDisplay.setText(decimalFormat.format(result));
            logger.v("Realtime calculation updated: " + currentExpression + " → " + result);
        } catch (Exception e) {
            resultDisplay.setText("");
            logger.w("Realtime calculation failed for: " + currentExpression);
        }
    }
}
