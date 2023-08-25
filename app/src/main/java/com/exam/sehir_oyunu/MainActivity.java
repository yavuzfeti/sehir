package com.exam.sehir_oyunu;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    TextView textview1,textview2,textview3,textview4,textview5,puan,sure;
    String[] sehirler={ "AYDIN", "AFYON", "İZMİR" ,"MUĞLA", "ANTEP"};
    EditText editText;
    String secilen,gelen;
    String[] harfler;
    int kac = 0;
    int puan_i = 0;
    int sure_i = 60;
    Random rastgele = new Random();
    ListView ListView1;
    Button oyna;
    ArrayList<String> hatalilar=new ArrayList<>();
    ArrayAdapter<String> adapter;
    Boolean var= false;
    Boolean oyun= false;
    Timer zamanlayici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        baslangic();
    }

    public void baslangic()
    {
        secilen = sehirler[rastgele.nextInt(5)];
        Toast.makeText(this,secilen,Toast.LENGTH_LONG).show();
        harfler = secilen.split("");
        zaman();
    }

    public void init()
    {
        textview1=findViewById(R.id.bir_txt);
        textview2=findViewById(R.id.bir_txt_2);
        textview3=findViewById(R.id.bir_txt_3);
        textview4=findViewById(R.id.bir_txt_4);
        textview5=findViewById(R.id.bir_txt_5);
        puan=findViewById(R.id.puan);
        sure=findViewById(R.id.sure);
        editText = findViewById(R.id.edittxt);
        ListView1=findViewById(R.id.liste);
        oyna=findViewById(R.id.oyna);
    }

    public void oyna(View view)
    {
        if (!oyun){tahmin_et();}
        else if (oyun){yenioyun();}
    }

    public void yazdir()
    {
        if (var)
        {
            switch (kac)
            {
                case 0:
                    textview1.setText(gelen);
                    break;
                case 1:
                    textview2.setText(gelen);
                    break;
                case 2:
                    textview3.setText(gelen);
                    break;
                case 3:
                    textview4.setText(gelen);
                    break;
                case 4:
                    textview5.setText(gelen);
                    break;
                default:
                    Toast.makeText(this,"HATA!",Toast.LENGTH_LONG).show();
                    break;
            }
            puan_i++;
            var = false;
        }
            hatalilar.add(gelen);
            adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,hatalilar);
            ListView1.setAdapter(adapter);

        editText.setText("");
        puan.setText("Puan: "+ String.valueOf(puan_i));
        bitis();
    }

    public void bitis()
    {
        if (puan_i==5)
        {
            Toast.makeText(this,"Kazandınız",Toast.LENGTH_LONG).show();
            kapat();
        }
        else if (hatalilar.size()>=5)
        {
            Toast.makeText(this,"Oyun bitti",Toast.LENGTH_LONG).show();
            kapat();
        }
    }

    public void zaman()
    {
        if (zamanlayici!=null)
        {
            zamanlayici.cancel();
            zamanlayici=null;
        }
        else
        {
            zamanlayici = new Timer();
            TimerTask gorev = new TimerTask()
            {
                @Override
                public void run()
                {
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if (sure_i > 0)
                            {
                                sure_i--;
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Süre bitti", Toast.LENGTH_LONG).show();
                                kapat();
                            }
                            sure.setText("Süre: " + String.valueOf(sure_i));
                        }
                    });
                }
            };
            zamanlayici.schedule(gorev, 0, 1000);
        }
    }

    public void kapat()
    {
        editText.setEnabled(false);
        zamanlayici.cancel();
        zamanlayici=null;
        oyna.setText("Yeni Oyun");
        oyun = true;
        Toast.makeText(this,"Cevap: "+secilen,Toast.LENGTH_LONG).show();
    }

    public void yenioyun()
    {
        sure_i = 60;
        puan_i=0;
        puan.setText("Puan: "+String.valueOf(puan_i));
        textview1.setText("_");
        textview2.setText("_");
        textview3.setText("_");
        textview4.setText("_");
        textview5.setText("_");

        baslangic();

        hatalilar.clear();
        adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,hatalilar);
        ListView1.setAdapter(adapter);

        oyna.setText("Tahmin Et");
        oyun = false;
        editText.setEnabled(true);
    }

    public void tahmin_et()
    {
        gelen = editText.getText().toString();
        for (int i=0 ; i<5 ; i++ )
        {
            if (gelen.equals(harfler[i]))
            {
                kac = i;
                var = true;
            }
        }
        yazdir();
    }
}