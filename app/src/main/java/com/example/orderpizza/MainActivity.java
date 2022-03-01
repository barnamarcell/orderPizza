package com.example.orderpizza;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    Spinner foodTypeSpinner, foodTypeTypeSpinner, foodSauceSpinner, extraSpinner;
    Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foodTypeSpinner = findViewById(R.id.foodType);
        foodTypeTypeSpinner = findViewById(R.id.foodTypeType);
        foodSauceSpinner = findViewById(R.id.SauceType);
        extraSpinner = findViewById(R.id.ExtraSpinner);
        orderButton = findViewById(R.id.orderButton);

        String fullname = "Vendég";

        String[] foodTypes = new String[]{"Pizzák", "Saláták", "Hamburgerek"};
        final ArrayAdapter<String> foodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, foodTypes);
        foodTypeSpinner.setAdapter(foodAdapter);

        String[] foodTypePizza = new String[]{"Sonkás","Kukoricás","Sándor"};
        final ArrayAdapter<String> pizzaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,foodTypePizza);

        String[] foodTypePizzaExtra = new String[]{"Extra-sajt","Extra-sonka","Extra-kukorica"};
        final ArrayAdapter<String> pizzaExtraAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,foodTypePizzaExtra);


        String[] pizzaSauceType = new String[]{"Paradicsomos","Csípős","Tejfölös"};
        final ArrayAdapter<String> pizzaSauceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,pizzaSauceType);


        String[] foodTypeSalad = new String[]{"Cézár","Bianka","Görög"};
        final ArrayAdapter<String> saladAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,foodTypeSalad);


        String[] sauceTypeSalad = new String[]{"Joghurtos","Fokhagymás-joghurtos","Csípős"};
        final ArrayAdapter<String> saladSauceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,sauceTypeSalad);


        String[] foodTypeHamburger = new String[]{"Kukoricás","Dupla húsos","Dupla bacon"};
        final ArrayAdapter<String> hamburgerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,foodTypeHamburger);

        String[] foodTypeHamburgerExtra = new String[]{"Extra-sajt","Extra-bacon","Extra-kukorica"};
        final ArrayAdapter<String> hamburgerExtraAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,foodTypeHamburgerExtra);


        String[] sauceTypeHamburger = new String[]{"Mustár-Majonéz-Ketchup","Mustár-Ketchup","Ketchup-Majonéz"};
        final ArrayAdapter<String> hamburgerSauceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,sauceTypeHamburger);




        foodTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spinnerValue = foodTypeSpinner.getSelectedItem().toString();
                if (spinnerValue.equals("Pizzák")) {
                    foodTypeTypeSpinner.setAdapter(pizzaAdapter);
                    foodSauceSpinner.setAdapter(pizzaSauceAdapter);
                    extraSpinner.setAdapter(pizzaExtraAdapter);
                    extraSpinner.setVisibility(View.VISIBLE);
                } else if (spinnerValue.equals("Hamburgerek")) {
                    foodTypeTypeSpinner.setAdapter(hamburgerAdapter);
                    foodSauceSpinner.setAdapter(hamburgerSauceAdapter);
                    extraSpinner.setAdapter(hamburgerExtraAdapter);
                    extraSpinner.setVisibility(View.VISIBLE);
                }else if(spinnerValue.equals("Saláták")){
                    foodTypeTypeSpinner.setAdapter(saladAdapter);
                    foodSauceSpinner.setAdapter(saladSauceAdapter);
                    extraSpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        orderButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String foodType = foodTypeSpinner.getSelectedItem().toString();
                String foodTypeType = foodTypeTypeSpinner.getSelectedItem().toString();
                String sauceType = foodSauceSpinner.getSelectedItem().toString();
                String extra = extraSpinner.getSelectedItem().toString();

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String orderDate = dtf.format(now);

                if(!foodType.equals("")) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[6];
                            field[0] = "fullname";
                            field[1] = "foodType";
                            field[2] = "foodTypeType";
                            field[3] = "sauceType";
                            field[4] = "extra";
                            field[5] = "orderDate";
                            //Creating array for data
                            String[] data = new String[6];
                            data[0] = fullname;
                            data[1] = foodType;
                            data[2] = foodTypeType;
                            data[3] = sauceType;
                            data[4] = extra;
                            data[5] = orderDate;
                            //PutData putData = new PutData("http://localhost/registerlogin/signup.php", "POST", field, data);
                            PutData putData = new PutData("http://10.0.11.107/foodorder/order.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Order Success")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                    Log.i("PutData", result);
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "All fields required!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}