package com.cadnunsdev.salaoautomacao;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import cadnunsdev.tasks.SocketAsynkTask;
import cadnunsdev.tasks.SocketListener;
import cadnunsdev.utils.DateTimeUtils;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.NetworkOnMainThreadException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends Activity implements SocketListener {

	private static final String HOST = "192.168.1.3";

	private static final int PORT = 25346;

	int counter = 0;
	
    private Runnable updateViews = new Runnable() {
		@Override
		public void run() {			
			
			updateTextView();
			
		}		
	};
	
	private TextView textView;
	
	private Handler handler;

	private Socket socket;

	private Scanner scanner;

	private PrintStream serverPrintStream;

	private boolean keepTaskRunning = true;

	private SocketAsynkTask socketTask;	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //setContentView(R.layout.activity_main);
        configViews();        
//        startHandlers();
        
        startSocketTask();
    }

	private void startSocketTask() {
		socketTask = new SocketAsynkTask(this);
		socketTask.execute();
	}

	private void updateTextView() {
		textView.append("\n"+DateTimeUtils.getDateNow()+" linha" + ++counter);			
	}
    private void startHandlers() {    	
    	handler = new Handler();        
		handler.postDelayed(updateViews , 0);    	
    }

	private void configViews() {
    	LinearLayout container = new LinearLayout(this);
		container.setOrientation(LinearLayout.VERTICAL);
		container.setPadding(50, 50, 50, 50);
		setContentView(container);
		
        final EditText edtCmd = new EditText(this);
        Button btSend = new Button(this);
        btSend.setText("Enviar");
        btSend.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (socketTask != null) {
					sendMessageToServer(edtCmd.getText().toString());
					textView.append("\n" + DateTimeUtils.getDateNow()
							+ " enviado para servidor" + edtCmd.getText());
				}
			}			
		});
        textView = new TextView(this);
        textView.setText("inicio.....");
        container.addView(edtCmd);
        container.addView(btSend);
        container.addView(textView);
	}

	private void sendMessageToServer(String string) {
		socketTask.sendMessage(string);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public String getIp() {
		// TODO Auto-generated method stub
		return HOST;
	}

	@Override
	public int getHostPort() {
		// TODO Auto-generated method stub
		return PORT;
	}

	@Override
	public void notify(String string) {		
		textView.append("\n"+DateTimeUtils.getDateNow()+" "+string);
	}

	@Override
	public boolean keepTaskRunnig() {
		return keepTaskRunning;
	}
	private void stopTask(){
		keepTaskRunning = false;
	}
}
