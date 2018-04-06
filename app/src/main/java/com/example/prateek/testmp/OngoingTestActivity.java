package com.example.prateek.testmp;


import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.DoubleStream;

public class OngoingTestActivity extends AppCompatActivity {

    TextView textViewQuestion, textViewResult;
    RadioGroup radioGroupOptions;
    RadioButton radioButtonOptionA, radioButtonOptionB, radioButtonOptionC, radioButtonOptionD;
    Button buttonNext;
    ConstraintLayout testConstraintLayout, resultConstraintLayout;

    String test_uid;

    int questionNo = 0;

    ArrayList<TestQuestionDetails> testQuestionDetailsArrayList = new ArrayList<>();
    int[] checkedButtonIdArray;
    double[] marks;

    public FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_test);

        XMLReferences();

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        test_uid = intent.getStringExtra("test_uid");
        Log.i("####Value===", test_uid);

        getQuestions();
    }

    //To display question.
    private void displayQuestion() {
        if(questionNo<(testQuestionDetailsArrayList.size()-1))
            buttonNext.setText("Next");
        try {
            TestQuestionDetails testQuestionDetails = testQuestionDetailsArrayList.get(questionNo);
            textViewQuestion.append(testQuestionDetails.question);
            radioButtonOptionA.setText("A. " + testQuestionDetails.a);
            radioButtonOptionB.setText("B. " + testQuestionDetails.b);
            radioButtonOptionC.setText("C. " + testQuestionDetails.c);
            radioButtonOptionD.setText("D. " + testQuestionDetails.d);
            radioGroupOptions.check(checkedButtonIdArray[questionNo]);
        }
        catch (Exception e){
            questionNo=0;
            textViewQuestion.setText("");
            displayQuestion();
            e.printStackTrace();}
    }

    /*To get the questions from database into an ArrayList.
    Creates the array containing checked options and marks.*/
    private void getQuestions() {


        try {
            mDatabase.child("tests").child(test_uid).child("testQuestionDetails").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        TestQuestionDetails testQuestionDetails = ds.getValue(TestQuestionDetails.class);

                        ds.child("");//To go a step deep into database table.

                        try {

                            testQuestionDetailsArrayList.add(testQuestionDetails);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    Log.i("size of array: ", testQuestionDetailsArrayList.size()+"");

                    int size = testQuestionDetailsArrayList.size();

                    //Array to store ids of checked radio buttons and marks.
                    checkedButtonIdArray = new int[size];
                    Arrays.fill(checkedButtonIdArray, -1);

                    marks = new double[size];
                    Arrays.fill(marks, 0);

                    //Displays the first question.
                    displayQuestion();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){e.printStackTrace();}
    }

    public void onNextButtonClicked(View view){

        //See if any radio button is selected or not.
        if(radioGroupOptions.getCheckedRadioButtonId()!= -1){
            checkCorrectOption();
        }

        /*If already on last question, shows result.
        If not, then increaments the questionNo.*/
        if(buttonNext.getText().equals("Submit")){
            onSubmit();
        }
        else
            questionNo++;

        //If the last question hasn't been reached then display question.
        if(questionNo < (testQuestionDetailsArrayList.size()-1)) {
            textViewQuestion.setText("");
            displayQuestion();
        }
        else{
            textViewQuestion.setText("");
            buttonNext.setText("Submit");
            displayQuestion();
        }
    }

    public void onPreviousButtonClicked(View view){

        //See if any radio button is selected or not.
        if(radioGroupOptions.getCheckedRadioButtonId()!= -1){
            checkCorrectOption();
        }

        //If its not the first question, decrement questionNo.
        if(questionNo>=1){
            questionNo--;
            textViewQuestion.setText("");
            displayQuestion();
        }

    }

    private void checkCorrectOption() {
        int id = radioGroupOptions.getCheckedRadioButtonId();
        checkedButtonIdArray[questionNo] = id;

        RadioButton checkedButton = findViewById(id);

        String option = checkedButton.getText().toString();
        String correctOption = testQuestionDetailsArrayList.get(questionNo).correctAns;

        option = option.substring(0,1);
        if(option.equalsIgnoreCase(correctOption)) {
            marks[questionNo] = 1;
            Log.i("### MSG : ", marks[questionNo]+"");
        }
        else {
            marks[questionNo] = -0.25;
            Log.i("### MSG : ", marks[questionNo] + "");
        }
        Log.i("### Option : ", option);
    }


    //Shows total marks.
    private void onSubmit() {

        double totalMarks = 0;
        for(int i = 0; i < marks.length; i++)
            totalMarks = totalMarks + marks[i];
        Log.i("### Total Score :", totalMarks+"");

        textViewResult.setText(totalMarks+"");

        testConstraintLayout.setVisibility(View.INVISIBLE);
        resultConstraintLayout.setVisibility((View.VISIBLE));
    }

    private void XMLReferences() {

        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewResult = findViewById(R.id.textViewResult);
        radioGroupOptions = findViewById(R.id.radioGroupOptions);
        radioButtonOptionA = findViewById(R.id.radioButtonOptionA);
        radioButtonOptionB = findViewById(R.id.radioButtonOptionB);
        radioButtonOptionC = findViewById(R.id.radioButtonOptionC);
        radioButtonOptionD = findViewById(R.id.radioButtonOptionD);
        buttonNext = findViewById(R.id.buttonNext);
        testConstraintLayout = findViewById(R.id.testConstraintLayout);
        resultConstraintLayout = findViewById(R.id.resultConstraintLayout);
    }
}