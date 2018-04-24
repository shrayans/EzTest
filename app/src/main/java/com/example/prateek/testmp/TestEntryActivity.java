package com.example.prateek.testmp;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestEntryActivity extends AppCompatActivity {

    EditText TestNameEditText,TotalQuestionEditText,TotalNoOfStudent;
    ConstraintLayout childConstraintLayout;

    ConstraintLayout constraintLayout;
    TextView QuestionNoTextView;
    EditText Question;
    EditText editTextOption1;
    EditText editTextOption2;
    EditText editTextOption3;
    EditText editTextOption4;
    Button buttonAddQuestion;
    RadioGroup radioGroupOptions;

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

     Object nameOfTestCreator;
    public void buttonClicked(View view ){

        if(questionNo<=maxQuestionNo) {

            if(buttonAddQuestion.getText().toString().equals("Add & Finish")){

                buttonAddQuestion.setEnabled(false);

                addQuestion();
try {
    String UID = firebaseAuth.getCurrentUser().getUid().toString();
    final String childString = testName + "*" + UID;

    DBManager dbManager = new DBManager();
    dbManager.getTeacherNameFromUid(UID, new MyCallback() {
        @Override
        public void onCallback(ArrayList<Object> value) {

        }

        @Override
        public void onCallbackString(String string) {
            nameOfTestCreator = string;
            Log.i("TestUpdated",nameOfTestCreator.toString());

            mDatabase.child("tests").child(childString).child("nameOfTestCreator").setValue(nameOfTestCreator.toString());
            mDatabase.child("tests").child(childString).child("totalQuestions").setValue(maxQuestionNo);
            mDatabase.child("tests").child(childString).child("totalNoOfStudents").setValue(totalNoOfStudent);

            mDatabase.child("tests").child(childString).child("testQuestionDetails").setValue(testQuestionMap);
            Log.i("TestUpdated","Test is on the firebase");
        }
    });



}catch (Exception e){
    e.printStackTrace();//mDatabase.child("tests").setValue(testQuestionMap);
}

            }
            addQuestion();
  }

    }


    public void clickOnNextButton(View view){
        try{
        testName=TestNameEditText.getText().toString();
        totalNoOfStudent=TotalNoOfStudent.getText().toString();



        if(TextUtils.isEmpty(testName)||TextUtils.isEmpty(totalNoOfStudent)||TextUtils.isEmpty(TotalQuestionEditText.getText().toString() ) ){
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }
            maxQuestionNo=0;
            maxQuestionNo=Integer.parseInt(TotalQuestionEditText.getText().toString());
        if(maxQuestionNo<=0){
            Toast.makeText(TestEntryActivity.this, "Total Question cant be less than 0", Toast.LENGTH_LONG).show();
            return;
        }

        childConstraintLayout.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(View.VISIBLE);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void XMLReferences() {

        TestNameEditText=(EditText)findViewById(R.id.TestNameEditText);
        TotalQuestionEditText=(EditText)findViewById(R.id.TotalQuestionEditText);
        TotalNoOfStudent=(EditText)findViewById(R.id.TotalNoOfStudent);

        childConstraintLayout =findViewById(R.id.ChildConstraintLayout);
        constraintLayout=(ConstraintLayout)findViewById(R.id.ConstraintLayout);


        Question = (EditText)findViewById(R.id.edit_text_question);
        editTextOption1 = (EditText)findViewById(R.id.edit_text_option1);
        editTextOption2 = (EditText)findViewById(R.id.edit_text_option2);
        editTextOption3 = (EditText)findViewById(R.id.edit_text_option3);
        editTextOption4 = (EditText)findViewById(R.id.edit_text_option4);
        radioGroupOptions = findViewById(R.id.radioGroupOptions2);
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
        String correctAns="";
        if(radioGroupOptions.getCheckedRadioButtonId()!= -1){
            correctAns = getCheckedOption();
        }
        Log.i("RadioId",radioGroupOptions.getCheckedRadioButtonId()+"");
        Log.i("CorrectAns",correctAns);

        clearAllEditText();

        if (TextUtils.isEmpty(question) || TextUtils.isEmpty(a) || TextUtils.isEmpty(b) || TextUtils.isEmpty(c) || TextUtils.isEmpty(d) ) {
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

    private String getCheckedOption() {
        int id = radioGroupOptions.getCheckedRadioButtonId();
        View RadioButton = radioGroupOptions.findViewById(id);
        String option = getResources().getResourceEntryName(id);
        Log.i("CORRECTANS**",option);
        return option;
    }

    private void clearAllEditText() {
        Question.setText("");
        editTextOption1.setText("");
        editTextOption2.setText("");
        editTextOption3.setText("");
        editTextOption4.setText("");
        radioGroupOptions.clearCheck();
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