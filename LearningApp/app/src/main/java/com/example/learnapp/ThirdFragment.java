package com.example.learnapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class ThirdFragment extends Fragment {

    Boolean answered;
    Button nextButton;
    EditText eq, ans;
    int a, b, state;
    String arith;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eq = view.findViewById(R.id.editTextQuestion);
        ans = view.findViewById(R.id.editTextAnswer);
        nextButton = view.findViewById(R.id.nextQ);
        nextButton.setVisibility(View.INVISIBLE);

        state = ((MainActivity)getActivity()).getState();

        answered = false;

        // This triggers the keyboard to appear
        ans.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        setArith();
        nextQuestion();
        setListeners();

    }

    private void setListeners() {
        ans.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (answered == false) {
                    if (i == KeyEvent.KEYCODE_ENTER) {
                        if (ans.getText().toString().trim().length() > 0) {
                            if (checkAnswer(a, b, ans.getText().toString())) {
                                eq.setBackgroundColor(Color.GREEN);
                            } else {
                                eq.setBackgroundColor(Color.RED);
                            }
                            nextButton.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                    return false;
                } else if (answered){
                    nextQuestion();
                    answered = false;
                    return true;
                }
                return false;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextQuestion();
            }
        });
    }

    // set the arithmetic sign
    public void setArith() {
        if (state == 1) { // addition
            arith = " + ";
        } else if (state == 2) { // multiplication
            arith = " X ";
        } else if (state == 3) { // division
            arith = " / ";
        }
        Log.d("arith", arith);
    }

    // Return an integer between 0 and 9
    private int generateNumber() {
        Random random = new Random();
        return random.nextInt(9);
    }

    // Check if a * b is equal to ans
    private boolean checkAnswer(int a, int b, String ans) {
        int answer = Integer.parseInt(ans);
        if (state == 1 && a+b == answer) { // addition
            return true;
        } else if (state == 2 && a*b == answer) { // multiplication
            return true;
        } else if (state == 3 && a/b == answer) { // division
            return true;
        }
        return false;
    }

    // Replace the question with another
    private void nextQuestion() {
        ans.getText().clear();
        eq.setBackgroundColor(Color.WHITE);
        nextButton.setVisibility(View.INVISIBLE);

        a = generateNumber();
        b = generateNumber();

        eq.setText(a + arith + b);
    }
}