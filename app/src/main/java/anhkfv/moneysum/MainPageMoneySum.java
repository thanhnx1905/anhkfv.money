package anhkfv.moneysum;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.anhth.myapplication.R;

/**
 * Created by ADJ on 2/21/2017.
 */
public class MainPageMoneySum extends AppCompatActivity{

    Button getData;
    Button sendData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.money_sum_main_page);

        getData=(Button)findViewById(R.id.insertUser);
        sendData=(Button)findViewById(R.id.viewUser);

        getData.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), PostData.class);
                startActivity(intent);

            }

        });
        sendData.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), PostData.class);
                startActivity(intent);
            }

        });


    };



}
