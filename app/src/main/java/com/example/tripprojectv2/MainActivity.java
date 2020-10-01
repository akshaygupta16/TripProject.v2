package com.example.tripprojectv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CreateTrip.OnFragmentInteractionListener,ProfileView.OnFragmentInteractionListener,HomeScreen.OnFragmentInteractionListener {

    FirebaseUser currentUser;
    FirebaseFirestore db;
    DocumentReference user_docRef;
    CollectionReference trips_docRef;
    User_local currentUserObj;
    TextView home_name_tv;
    RecyclerView home_recyclerView;
    RecyclerView.Adapter home_adapter;
    RecyclerView.LayoutManager home_layoutManager;
    ArrayList<Trip> tripArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        user_docRef =db.collection("users").document(currentUser.getUid());
        user_docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d("demo",document.getData().toString());
                        currentUserObj = new User_local(document.getData());

                        home_name_tv = findViewById(R.id.home_name_tv);
                        home_name_tv.setText(currentUserObj.getFirstName());
                    }else{
                        Log.d("demo", "No such document");
                    }
                }
                else {
                    Log.d("demo", "get failed with ", task.getException());
                }
            }
        });

        tripArrayList = new ArrayList<>();
        trips_docRef = db.collection("trips");
        trips_docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("trips", document.getId() + " => " + document.getData());
                                Trip curr_trip = new Trip(document.getData());
                                tripArrayList.add(curr_trip);

                            }
                            Log.d("alltrips",tripArrayList.toString());
                        } else {
                            Log.d("demo", "Error getting documents: ", task.getException());
                        }
                    }
                });






        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container,new HomeScreen())
                .commit();


        findViewById(R.id.create_trip_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container,new CreateTrip())
                        .commit();


            }
        });



    }


    @Override
    public void save() {

        // go to firebase and update all the details

        getSupportFragmentManager().beginTransaction()
                .add(R.id.main_container,new ProfileView())
                .commit();
    }

    @Override
    public void sign_out() {

    }

    @Override
    public void onCreateButtonPress() {

        Log.d("here","heerreeee");

        EditText title = findViewById(R.id.create_trip_title);
        EditText location = findViewById(R.id.create_trip_location);
        EditText startDate = findViewById(R.id.create_trip_start_date);
        EditText endDate  =findViewById(R.id.create_trip_end_date);

        Trip tripToAdd = new Trip(title.getText().toString(),location.getText().toString(),null,startDate.getText().toString(),endDate.getText().toString(),currentUser.getUid());
        Map tripMap = tripToAdd.toUserMap();
        db.collection("trips").document()
                .set(tripMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(MainActivity.this, "Trip Created", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("fail","fail");
                Toast.makeText(MainActivity.this, "Error creating trip", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void uploadCoverPhoto() {

    }
}
