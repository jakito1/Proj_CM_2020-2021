package com.cmteam4.throughoutportugal.pointInterest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cmteam4.throughoutportugal.R;
import com.cmteam4.throughoutportugal.cardViewPointInterest.PointsInterest;

public class InfoRegion extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_region);

        TextView txtTitle = findViewById(R.id.txtName);
        ImageView imageView = findViewById(R.id.imageView);
        TextView txtHistory = findViewById(R.id.txtAreaHistory);
        ImageView imgPoints = findViewById(R.id.imgPoints);

        ImageView btnBack = findViewById(R.id.imgBack);

        Bundle variables = getIntent().getExtras();
        if (variables != null) {
            switch (variables.getString("REGION_POINT")) {
                case "Braga":
                    txtTitle.setText("Braga");
                    txtHistory.setText(R.string.bragaHist);
                    imageView.setImageResource(R.drawable.braga);
                    break;
                case "Viana do Castelo":
                    txtTitle.setText("Viana do Castelo");
                    txtHistory.setText(R.string.VianaCasteloHist);
                    imageView.setImageResource(R.drawable.vianacastelo);
                    break;
                case "Vila Real":
                    txtTitle.setText("Vila Real");
                    txtHistory.setText(R.string.VilaRealHist);
                    imageView.setImageResource(R.drawable.vilareal);
                    break;
                case "Bragança":
                    txtTitle.setText("Bragança");
                    txtHistory.setText(R.string.BragancaHist);
                    imageView.setImageResource(R.drawable.braganca);
                    break;
                case "Porto":
                    txtTitle.setText("Porto");
                    txtHistory.setText(R.string.PortoHist);
                    imageView.setImageResource(R.drawable.porto);
                    break;
                case "Aveiro":
                    txtTitle.setText("Aveiro");
                    txtHistory.setText(R.string.AveiroHist);
                    imageView.setImageResource(R.drawable.aveiro);
                    break;
                case "Viseu":
                    txtTitle.setText("Viseu");
                    txtHistory.setText(R.string.ViseuHist);
                    imageView.setImageResource(R.drawable.viseu);
                    break;
                case "Guarda":
                    txtTitle.setText("Guarda");
                    txtHistory.setText(R.string.GuardaHist);
                    imageView.setImageResource(R.drawable.guarda);
                    break;
                case "Coimbra":
                    txtTitle.setText("Coimbra");
                    txtHistory.setText(R.string.CoimbraHist);
                    imageView.setImageResource(R.drawable.coimbra);
                    break;
                case "Castelo Branco":
                    txtTitle.setText("Castelo Branco");
                    txtHistory.setText(R.string.CasteloBrancoHist);
                    imageView.setImageResource(R.drawable.castelobranco);
                    break;
                case "Leiria":
                    txtTitle.setText("Leiria");
                    txtHistory.setText(R.string.LeiriaHist);
                    imageView.setImageResource(R.drawable.leiria);
                    break;
                case "Santarém":
                    txtTitle.setText("Santarém");
                    txtHistory.setText(R.string.SantaremHist);
                    imageView.setImageResource(R.drawable.santarem);
                    break;
                case "Lisboa":
                    txtTitle.setText("Lisboa");
                    txtHistory.setText(R.string.LisboaHist);
                    imageView.setImageResource(R.drawable.lisboa);
                    break;
                case "Portalegre":
                    txtTitle.setText("Portalegre");
                    txtHistory.setText(R.string.PortalegreHist);
                    imageView.setImageResource(R.drawable.portalegre);
                    break;
                case "Évora":
                    txtTitle.setText("Évora");
                    txtHistory.setText(R.string.EvoraHist);
                    imageView.setImageResource(R.drawable.evora);
                    break;
                case "Setúbal":
                    txtTitle.setText("Setúbal");
                    txtHistory.setText(R.string.SetubalHist);
                    imageView.setImageResource(R.drawable.setubal);
                    break;
                case "Beja":
                    txtTitle.setText("Beja");
                    txtHistory.setText(R.string.BejaHist);
                    imageView.setImageResource(R.drawable.beja);
                    break;
                case "Faro":
                    txtTitle.setText("Faro");
                    txtHistory.setText(R.string.FaroHist);
                    imageView.setImageResource(R.drawable.faro);
                    break;
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