package com.cmteam4.throughoutportugal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmteam4.throughoutportugal.card_view_point_interest.PointsInterest;

public class InfoRegion extends AppCompatActivity {

    private ImageView imageView;
    private TextView txtHistory;
    private TextView txtTitle;
    private ImageView imgPoints;

    private ImageView btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_region);

        txtTitle = findViewById(R.id.txtName);
        imageView = findViewById(R.id.imageView);
        txtHistory = findViewById(R.id.txtAreaHistory);
        imgPoints = findViewById(R.id.imgPoints);

        btnBack = findViewById(R.id.imgBack);

        Bundle variables = getIntent().getExtras();
        if (variables != null) {
            if (variables.getString("REGION_POINT").equals("Braga")) {
                txtTitle.setText("Braga");
                txtHistory.setText(R.string.bragaHist);
                imageView.setImageResource(R.drawable.braga);
            } else if (variables.getString("REGION_POINT").equals("Viana do Castelo")) {
                txtTitle.setText("Viana do Castelo");
                txtHistory.setText(R.string.VianaCasteloHist);
                imageView.setImageResource(R.drawable.vianacastelo);
            } else if (variables.getString("REGION_POINT").equals("Vila Real")) {
                txtTitle.setText("Vila Real");
                txtHistory.setText(R.string.VilaRealHist);
                imageView.setImageResource(R.drawable.vilareal);
            } else if (variables.getString("REGION_POINT").equals("Bragança")) {
                txtTitle.setText("Bragança");
                txtHistory.setText(R.string.BragancaHist);
                imageView.setImageResource(R.drawable.braganca);
            } else if (variables.getString("REGION_POINT").equals("Porto")) {
                txtTitle.setText("Porto");
                txtHistory.setText(R.string.PortoHist);
                imageView.setImageResource(R.drawable.porto);
            } else if (variables.getString("REGION_POINT").equals("Aveiro")) {
                txtTitle.setText("Aveiro");
                txtHistory.setText(R.string.AveiroHist);
                imageView.setImageResource(R.drawable.aveiro);
            } else if (variables.getString("REGION_POINT").equals("Viseu")) {
                txtTitle.setText("Viseu");
                txtHistory.setText(R.string.ViseuHist);
                imageView.setImageResource(R.drawable.viseu);
            } else if (variables.getString("REGION_POINT").equals("Guarda")) {
                txtTitle.setText("Guarda");
                txtHistory.setText(R.string.GuardaHist);
                imageView.setImageResource(R.drawable.guarda);
            } else if (variables.getString("REGION_POINT").equals("Coimbra")) {
                txtTitle.setText("Coimbra");
                txtHistory.setText(R.string.CoimbraHist);
                imageView.setImageResource(R.drawable.coimbra);
            } else if (variables.getString("REGION_POINT").equals("Castelo Branco")) {
                txtTitle.setText("Castelo Branco");
                txtHistory.setText(R.string.CasteloBrancoHist);
                imageView.setImageResource(R.drawable.castelobranco);
            }else if (variables.getString("REGION_POINT").equals("Leiria")) {
                txtTitle.setText("Leiria");
                txtHistory.setText(R.string.LeiriaHist);
                imageView.setImageResource(R.drawable.leiria);
            } else if (variables.getString("REGION_POINT").equals("Santarém")) {
                txtTitle.setText("Santarém");
                txtHistory.setText(R.string.SantaremHist);
                imageView.setImageResource(R.drawable.santarem);
            } else if (variables.getString("REGION_POINT").equals("Lisboa")) {
                txtTitle.setText("Lisboa");
                txtHistory.setText(R.string.LisboaHist);
                imageView.setImageResource(R.drawable.lisboa);
            } else if (variables.getString("REGION_POINT").equals("Portalegre")) {
                txtTitle.setText("Portalegre");
                txtHistory.setText(R.string.PortalegreHist);
                imageView.setImageResource(R.drawable.portalegre);
            }else if (variables.getString("REGION_POINT").equals("Évora")) {
                txtTitle.setText("Évora");
                txtHistory.setText(R.string.EvoraHist);
                imageView.setImageResource(R.drawable.evora);
            } else if (variables.getString("REGION_POINT").equals("Setúbal")) {
                txtTitle.setText("Setúbal");
                txtHistory.setText(R.string.SetubalHist);
                imageView.setImageResource(R.drawable.setubal);
            } else if (variables.getString("REGION_POINT").equals("Beja")) {
                txtTitle.setText("Beja");
                txtHistory.setText(R.string.BejaHist);
                imageView.setImageResource(R.drawable.beja);
            } else if (variables.getString("REGION_POINT").equals("Faro")) {
                txtTitle.setText("Faro");
                txtHistory.setText(R.string.FaroHist);
                imageView.setImageResource(R.drawable.faro);
            }
        }

        imgPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PointsInterest.class);
                if (variables != null) {
                    intent.putExtra("REGION_POINT", variables.getString("REGION_POINT"));
                    startActivity(intent);
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}