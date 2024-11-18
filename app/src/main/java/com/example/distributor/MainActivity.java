package com.example.distributor;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.distributor.adapter.DistributorAdapter;
import com.example.distributor.models.Distributor;
import com.example.distributor.models.Response;
import com.example.distributor.services.HttpRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    List<Distributor> list;
    EditText edSearch;
    String TAG = "//===MainActivity";
    EditText edNameDistributor;
    Button btnSave, btnCancle;
    FloatingActionButton floatingActionButton;

    HttpRequest httpRequest;
    Dialog dialog;
    private RecyclerView recyclerView;
    private DistributorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        httpRequest = new HttpRequest();
        onResume();
        edSearch = findViewById(R.id.edSearch);
        floatingActionButton = findViewById(R.id.floatActionButton);
        recyclerView = (RecyclerView) findViewById(R.id.rcvDistributor);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new DistributorAdapter();
        adapter.setOnItemClickListener(new DistributorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id) {
                showDialogDelete(id);
            }

            @Override
            public void updateItem(String id, String name) {
                openDiaLog(id, name);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDiaLog("", "");
            }
        });

        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if(actionID== EditorInfo.IME_ACTION_SEARCH){
                    String key = edSearch.getText().toString().trim();
                    httpRequest.callAPI().searchDistributor(key).enqueue(searchDistributor);
                    return  true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        httpRequest.callAPI().getListDistributor().enqueue(getListDistributor);
    }

    Callback<Response<ArrayList<Distributor>>> getListDistributor = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    list = new ArrayList<>();
                    list = response.body().getData();
                    adapter.setData(list);
                    recyclerView.setAdapter(adapter);
                    for (Distributor item : list) {
                        Log.i(TAG, "//===" + item.toString());
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable throwable) {
            Log.i(TAG, "//==Error=" + throwable.getMessage());

        }
    };
    Callback<Response<Distributor>> addDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(getApplicationContext(), "Add succesfull", Toast.LENGTH_SHORT).show();
                    onResume();
                    dialog.dismiss();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable throwable) {
            Log.i(TAG, "//==Error=" + throwable.getMessage());

        }
    };
    Callback<Response<Distributor>> updateDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(getApplicationContext(), "Update succesfull", Toast.LENGTH_SHORT).show();
                    onResume();
                    dialog.dismiss();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable throwable) {
            Log.i(TAG, "//==Error=" + throwable.getMessage());
        }
    };


    Callback<Response<Distributor>> deleteDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(getApplicationContext(), "Delete succesfull", Toast.LENGTH_SHORT).show();
                    onResume();
                    dialog.dismiss();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable throwable) {
            Log.i(TAG, "//==Error=" + throwable.getMessage());
        }
    };

    public void showDialogDelete(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete ?");
        builder.setMessage("Ban co chac chan muon xoa khong");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                httpRequest.callAPI().deleteDistributor(id).enqueue(deleteDistributor);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    Callback<Response<ArrayList<Distributor>>> searchDistributor = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    list = response.body().getData();
                    adapter.setData(list);
                    recyclerView.setAdapter(adapter);
                    for (Distributor item : list) {
                        Log.i(TAG, "//===" + item.toString());
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable throwable) {
            Log.i(TAG, "//==Error=" + throwable.getMessage());
        }
    };

    public void openDiaLog(String id, String name) {
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.diaglog_distributor);
        edNameDistributor = dialog.findViewById(R.id.edName);
        edNameDistributor.setText(name);
        btnSave = dialog.findViewById(R.id.btnSave);
        btnCancle = dialog.findViewById(R.id.btnCancel);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = edNameDistributor.getText().toString();
                if (strName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please input Distributor", Toast.LENGTH_SHORT).show();
                } else {
                    Distributor distributor = new Distributor();
                    distributor.setName(strName);
                    if (id.isEmpty()) {
                        httpRequest.callAPI().addDistributor(distributor).enqueue(addDistributor);
                    } else {
                        httpRequest.callAPI().editDistributor(id, distributor).enqueue(updateDistributor);
                    }
                }
            }
        });
        dialog.show();
    }
}