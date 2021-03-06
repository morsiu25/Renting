package com.inzynierka.morski.inzynierka.Activity.costume;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import  com.inzynierka.morski.inzynierka.Activity.rent.ScanNFCActivity;
//import com.inzynierka.morski.inzynierka.Activity.Rent.ScanNFCActivity;
import com.inzynierka.morski.inzynierka.DataBase.Costume.Costume;
import com.inzynierka.morski.inzynierka.DataBase.Costume.CostumeDataSource;
import com.inzynierka.morski.inzynierka.Exeptions.NoCostumeInDataBaseException;
import com.inzynierka.morski.inzynierka.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CostumeAddActivity extends AppCompatActivity {
    private CostumeDataSource dataSource;
    private EditText editTextCostumeName;
    private EditText editTextCostumeLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costume_add);

        dataSource = new CostumeDataSource(this);
        dataSource.open();
        editTextCostumeName     = (EditText)findViewById(R.id.editTextCostumeName);
        editTextCostumeLabel    = (EditText)findViewById(R.id.editTextCostumeLabel);
    }

    public void onClickAddCostumeView (View view){
        switch (view.getId()) {
            case R.id.btnAddCostume:
                if(filledData()){
                    String costumeName = editTextCostumeName.getText().toString();
                    String costumeLabel = editTextCostumeLabel.getText().toString();

                    try {
                        Costume costume = dataSource.getCostume(costumeLabel);
                        if(costume!=null){
                            Toast.makeText(getBaseContext(),"NIE MOŻNA DODAĆ \nKOSTIUM O PODANEJ METCE ISTNIEJE \nJEGO NAZWA TO:\n" + costume.getName() , Toast.LENGTH_LONG).show();
                        }
                    } catch (NoCostumeInDataBaseException e) {
                        dataSource.createCostume(costumeName, costumeLabel);
                        Toast.makeText(getBaseContext(), "POMYŚLNIE DODANO", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.btnScannId:
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
                break;

            case R.id.btnAddCostumeScannNFC:
                Intent nfcIntent = new Intent(CostumeAddActivity.this, ScanNFCActivity.class);
                startActivityForResult(nfcIntent, 1);
                break;

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                EditText text = (EditText)findViewById(R.id.editTextCostumeLabel);
                text.setText(contents);
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
        else if(requestCode == 1){
            String contents = intent.getStringExtra("SCAN_RESULT");
            EditText text = (EditText)findViewById(R.id.editTextCostumeLabel);
            text.setText(contents);
        }
    }

    private boolean filledData(){
        if(TextUtils.isEmpty(editTextCostumeName.getText().toString())) {
            return false;
        }
        else if(TextUtils.isEmpty(editTextCostumeLabel.getText().toString())){
            return false;
        }
        return true;
    }

}
