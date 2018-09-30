package com.example.lenovo.qwe;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Main2Activity extends Activity implements View.OnClickListener{

    private Button[][] buttons = new Button[3][3];
    private boolean player1turn = true;
    private int roundCount;

    private int player1points;
    private int player2points;

    private Button buttonReset;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    TextToSpeech ts1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textViewPlayer1=(TextView)findViewById(R.id.text_view_p1);
        textViewPlayer2=(TextView)findViewById(R.id.text_view_p2);
        ts1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                ts1.setLanguage(Locale.ENGLISH);
                ts1.setSpeechRate(0.3f);
            }
        });
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                String buttonID = "button_" + i + j;
                int resID=getResources().getIdentifier(buttonID,"id",getPackageName());
                buttons[i][j] =findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(!((Button) v).getText().toString().equals("")){
            return;
        }
        if(player1turn) {
            ((Button) v).setText("X");
        } else {
            ((Button) v).setText("0");
        }

        roundCount++;

        if(checkForWin()) {
            if(player1turn) {
                player1wins();
            } else {
                player2wins();
            }
        } else if (roundCount==9) {
            draw();
        }else {
            player1turn =!player1turn;
        }
    }
    private boolean checkForWin() {
        String[][] field =new String[3][3];
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                field[i][j] =buttons[i][j].getText().toString();
            }
        }
        for(int i=0;i<3;i++) {
            if(field[i][0].equals(field[i][1])&& field[i][0].equals(field[i][2]) && !field[i][0].equals("")) {
                return true;
            }
        }

        for(int i=0;i<3;i++) {
            if(field[0][i].equals(field[1][i])&& field[0][i].equals(field[2][i]) && !field[0][i].equals("")) {
                return true;
            }
        }

        if(field[0][0].equals(field[1][1])&& field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }

        if(field[0][2].equals(field[1][1])&& field[0][2].equals(field[2][0]) && !field[0][2].equals("")) {
            return true;
        }
        return false;


    }

    private void player1wins() {
        player1points++;
        Toast.makeText(this, "Player 1 wins", Toast.LENGTH_SHORT).show();
        ts1.speak("Player 1 Wins",TextToSpeech.QUEUE_FLUSH,null);
        updatePointsText();
        resetBoard();
    }

    private void player2wins() {
        player2points++;
        Toast.makeText(this, "Player 2 wins", Toast.LENGTH_SHORT).show();
        ts1.speak("Player 2 Wins",TextToSpeech.QUEUE_FLUSH,null);
        updatePointsText();
        resetBoard();
    }
    private void draw() {
        Toast.makeText(this, "Match Draw!!!", Toast.LENGTH_SHORT).show();
        ts1.speak("Match Draw",TextToSpeech.QUEUE_FLUSH,null);
        resetBoard();
    }
    private void updatePointsText() {
        textViewPlayer1.setText("Player 1:" + player1points );
        textViewPlayer2.setText("Player 2:" + player2points );

    }
    private void resetBoard() {
        for(int i=0;i<3;i++) {
            for(int j=0;j<3;j++) {
                buttons[i][j].setText("");
            }
        }
        roundCount=0;
        player1turn=true;

    }
    private void resetGame() {
        player1points=0;
        player2points=0;
        updatePointsText();
        resetBoard();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("roundCount",roundCount);
        outState.putInt("Player1points",player1points);
        outState.putInt("Player@points",player2points);
        outState.putBoolean("Player1turn",player1turn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        roundCount=savedInstanceState.getInt("roundcount");
        player1points=savedInstanceState.getInt("player1points");
        player2points=savedInstanceState.getInt("player2points");
        player1turn=savedInstanceState.getBoolean("player1turn");
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setMessage("Are You Sure You Want To exit ?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }
}
