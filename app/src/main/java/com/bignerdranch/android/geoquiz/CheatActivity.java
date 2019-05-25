package com.bignerdranch.android.geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
	private boolean mAnswerIsTrue;
	private TextView mAnswerTextView;
	private Button mShowAnswerButton;
	private static final String EXTRA_ANSWER_SHOWN = "com.bignerdranch.android.geoquiz.answer_shown";

	private static final String EXTRA_ANSWER_IS_TRUE = "com.bignerdranch.android.geoquiz.answer_is_true";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);

		Intent intent = getIntent();//获取之前的数据
		mAnswerIsTrue = intent.getBooleanExtra("answerIsTrue",true);
		mAnswerTextView = (TextView)findViewById(R.id.answer_text_view);

		mShowAnswerButton = (Button) findViewById(R.id.show_answer_button);
		mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
			@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
			@Override
			public void onClick(View v) {
				if (mAnswerIsTrue) {
					mAnswerTextView.setText(R.string.true_button);
				} else {
					mAnswerTextView.setText(R.string.false_button);
				}
				AnswerShown(true);
				int cx = mShowAnswerButton.getWidth() / 2;
				int cy = mShowAnswerButton.getHeight() / 2;
				float radius = mShowAnswerButton.getWidth();
				Animator anim = ViewAnimationUtils
						.createCircularReveal(mShowAnswerButton, cx, cy, radius, 0);
				anim.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						super.onAnimationEnd(animation);
						mShowAnswerButton.setVisibility(View.VISIBLE);
					}
				});
			}
		});
	}
	private void AnswerShown(boolean isAnswerShown) {
		//保存到Bundle或者Extra中
		Intent intent = getIntent();
		intent.putExtra("answer_shown", isAnswerShown);
		setResult(2, intent);
		finish();
	}
}