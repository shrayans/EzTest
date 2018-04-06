package com.example.prateek.testmp;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestEntryActivity extends AppCompatActivity {

    EditText TestNameEditText,TotalQuestionEditText,TotalNoOfStudent;
    RelativeLayout relativeLayout;

    ConstraintLayout constraintLayout;
    TextView QuestionNoTextView;
    EditText Question;
    EditText editTextOption1;
    EditText editTextOption2;
    EditText editTextOption3;
    EditText editTextOption4;
    EditText editTextCorrectOption;
    Button buttonAddQuestion;

    String testName,totalNoOfStudent;

    int questionNo=1;
    int maxQuestionNo;

    public FirebaseAuth firebaseAuth;

    private DatabaseReference mDatabase;

    Map<String, Object> testQuestionMap = new HashMap<>();

    ArrayList<TestQuestionDetails> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_entry);
        this.setTitle("Fill the details");
        XMLReferences();

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
       // Log.i("Check",arrayList.size()+"");


    }


    public void buttonClicked(View view ){

        if(questionNo<=maxQuestionNo) {

            if(buttonAddQuestion.getText().toString().equals("Add & Finish")){

                buttonAddQuestion.setEnabled(false);

                addQuestion();

                String UID=firebaseAuth.getCurrentUser().getUid().toString();
                String childString=testName+"*"+UID;

                mDatabase.child("tests").child(childString).child("totalQuestions").setValue(maxQuestionNo);
                mDatabase.child("tests").child(childString).child("totalNoOfStudents").setValue(totalNoOfStudent);

                mDatabase.child("tests").child(childString).child("testQuestionDetails").setValue(testQuestionMap);

                //mDatabase.child("tests").setValue(testQuestionMap);



            }
            addQuestion();
  }

    }


    public void clickOnNextButton(View view){

        testName=TestNameEditText.getText().toString();
        totalNoOfStudent=TotalNoOfStudent.getText().toString();
        maxQuestionNo=Integer.parseInt(TotalQuestionEditText.getText().toString());

        if(TextUtils.isEmpty(testName)||TextUtils.isEmpty(totalNoOfStudent)){
            Toast.makeText(TestEntryActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
            if(maxQuestionNo<=0){
                Toast.makeText(TestEntryActivity.this, "Total Question cant be less than 0", Toast.LENGTH_LONG).show();
                return;
            }
            return;
        }

        relativeLayout.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(View.VISIBLE);

    }

    private void XMLReferences() {

        TestNameEditText=(EditText)findViewById(R.id.TestNameEditText);
        TotalQuestionEditText=(EditText)findViewById(R.id.TotalQuestionEditText);
        TotalNoOfStudent=(EditText)findViewById(R.id.TotalNoOfStudent);

        relativeLayout=(RelativeLayout)findViewById(R.id.RelativeLayout);
        constraintLayout=(ConstraintLayout)findViewById(R.id.ConstraintLayout);


        Question = (EditText)findViewById(R.id.edit_text_question);
        editTextOption1 = (EditText)findViewById(R.id.edit_text_option1);
        editTextOption2 = (EditText)findViewById(R.id.edit_text_option2);
        editTextOption3 = (EditText)findViewById(R.id.edit_text_option3);
        editTextOption4 = (EditText)findViewById(R.id.edit_text_option4);
        editTextCorrectOption = (EditText)findViewById(R.id.edit_text_correctOption);
        buttonAddQuestion = (Button)findViewById(R.id.button_addQuestion);
        QuestionNoTextView= (TextView) findViewById(R.id.QuestionNoTextView);
        QuestionNoTextView.setText("Question No:-"+questionNo);

    }

    public void addQuestion(){
        String question = Question.getText().toString();
        String a = editTextOption1.getText().toString();
        String b = editTextOption2.getText().toString();
        String c = editTextOption3.getText().toString();
        String d = editTextOption4.getText().toString();
        String correctAns = editTextCorrectOption.getText().toString();

        clearAllEditText();

        if (TextUtils.isEmpty(question) || TextUtils.isEmpty(a) || TextUtils.isEmpty(b) || TextUtils.isEmpty(c) || TextUtils.isEmpty(d) || TextUtils.isEmpty(correctAns)) {
            Toast.makeText(TestEntryActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        } else
        {

            TestQuestionDetails testQuestionDetail = new TestQuestionDetails(question, a, b, c, d, correctAns);

            testQuestionMap.put(questionNo+"",testQuestionDetail);

            questionNo = questionNo + 1;
            QuestionNoTextView.setText("Question No:-" + questionNo);

            if(questionNo==maxQuestionNo) {
                buttonAddQuestion.setText("Add & Finish");

            }
        }
    }

    private void clearAllEditText() {
        Question.setText("");
        editTextOption1.setText("");
        editTextOption2.setText("");
        editTextOption3.setText("");
        editTextOption4.setText("");
        editTextCorrectOption.setText("");

    }

}


/*.addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            //H.toast(c, task.getException().getMessage());
                            Log.e("Signup Error", "onCancelled", task.getException());
                            Log.i("dbERROR",task.getException().getMessage());
                        }else{
                            Log.i("dbERROR","All Good");

                        }
                    }
                })*/