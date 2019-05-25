package com.bignerdranch.android.geoquiz;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;//button 需要引入的包
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class QuizActivity extends AppCompatActivity {
        private final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";
        private Button mTrueButton;
        private  Button mFalseButton;
        private Button mNextButton;
        private Button mPrevButton;
        private Button mCheatButton;
        private static final String TAG = "QuizActivity";
        private boolean mIsCheater;
        private static final int REQUEST_CODE_CHEAT = 0 ;
        private int true_count = 0;
        private int total_count = 0;
        private TextView mQuestionTextView;
        private static final String KEY_INDEX = "index";
        //定义一个问题的数组 数组内存放问题的实例
        private Question[] mQuestionBank = new Question[]{
                new Question(R.string.question1,true),
                new Question(R.string.question2,false),
                new Question(R.string.question3,true),
                new Question(R.string.question4,false),
                new Question(R.string.question5,true),
                new Question(R.string.question6,false),
                new Question(R.string.question7,true),

        };
        private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAnswer(true);

            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast toast = Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP,0,0);
//                toast.show();
                checkAnswer(false);
                //nothing
            }
        });

        mCheatButton = (Button) findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast toast = Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.TOP,0,0);
//                toast.show();
                //start cheatActivity
                Intent intent = new Intent(QuizActivity.this,CheatActivity.class);
                //第一个参数表示哪里可以找到它 第二个表示id启动那个activity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                intent.putExtra("answerIsTrue",answerIsTrue);
//                Intent intent = CheatActivity.newIntent(QuizActivity.this,answerIsTrue);
//                startActivity(intent);
                //nothing
                startActivityForResult(intent,0);
            }
        });

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        updateQuestion();

        mPrevButton = (Button) findViewById(R.id.pre_button);
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex+mQuestionBank.length-1) % mQuestionBank.length;
                updateQuestion();
                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex+1) % mQuestionBank.length;
                updateQuestion();
                mIsCheater = false;
                mTrueButton.setEnabled(true);
                mFalseButton.setEnabled(true);
            }
        });


    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;

        if(mIsCheater){
            messageResId = R.string.judgment_toast;
        }else{
            if(userPressedTrue == answerIsTrue){
                messageResId = R.string.correct_toast;
                true_count+=1;
                total_count+=1;
            }else{
                messageResId = R.string.incorrect_toast;
                total_count+=1;
            }
        }
        if(total_count % mQuestionBank.length ==0){
            float corr_rate = (float) true_count/total_count;
            DecimalFormat df = new DecimalFormat("0.00%");
            String scorr_rate = df.format(corr_rate);
            scorr_rate = "您的正确率为："+scorr_rate;
            Toast.makeText(this,scorr_rate,Toast.LENGTH_LONG).show();
        }
        mTrueButton.setEnabled(false);
        mFalseButton.setEnabled(false);
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==2){
            mIsCheater  =data.getBooleanExtra("answer_shown",false);
            Log.i(TAG, "onActivityResult: dollarRate="+mIsCheater);
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}
