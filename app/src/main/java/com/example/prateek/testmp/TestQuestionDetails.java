package com.example.prateek.testmp;

/**
 * Created by prateek on 2/14/18.
 */

public class TestQuestionDetails {


    public String question;
    public String a;
    public String b;
    public String c;
    public String d;
    public String correctAns;
    //String questionNo;

    public TestQuestionDetails(){

    }

    public TestQuestionDetails(String question,String a,String b,String c,String d,String correctAns){

        this.question=question;
        this.a=a;
        this.b=b;
        this.c=c;
        this.d=d;
        this.correctAns=correctAns;

    }

}
