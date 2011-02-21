package com.bubblebot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.bubblebot.jni.Processor;
import com.opencv.jni.Mat;

public class BubbleProcess extends Activity  {
	
    private class ProcessFormTask extends AsyncTask<Void, Void, Void> {
        private Activity parent;
        private ProgressDialog dialog;

        // Constructor
        public ProcessFormTask(Activity parent) {
            this.parent = parent;
        }

        // Before the task executes, display a progress dialog to indicate that
        // the program is reading from the sensors.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(parent);
            dialog.setMessage(getResources().getText(com.bubblebot.R.string.ProcessForm));
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }

        // When the task completes, remove the progress dialog
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            dialog.dismiss();
            parent.finish();
            
            //Display the processed form
            Intent intent = new Intent(getApplication(), DisplayProcessedForm.class);
            startActivity(intent); 
        }

		@Override
		protected Void doInBackground(Void... arg) {
			mProcessor.ProcessForm();
			return null;
		}
    }
 
	private final Processor mProcessor = new Processor();
	private ProcessFormTask mTask = null;
	
	// Initialize the application
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTask = new ProcessFormTask(this);
		
		FrameLayout frame = new FrameLayout(this);
		setContentView(frame);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mTask.cancel(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		// Use null for now. Eventually we will pass in a real image to the processor.
		Void[] arg = null;
		mTask.execute(arg);
	}
}
	