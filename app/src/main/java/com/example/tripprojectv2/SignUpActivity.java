package com.example.tripprojectv2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    final static int GALLERY = 3;

    final static int CAMERA = 4;

    private FirebaseAuth firebaseAuth;

    FirebaseFirestore firebaseDB = FirebaseFirestore.getInstance();

    EditText signUpEmail;

    EditText signUpPassword;

    EditText signUpFirstname;

    EditText signUpLastname;

    Button signUpButton;

    RadioGroup signUpRadioGroup;

    RadioButton signUpRadioButtonMale;

    RadioButton signUpRadioButtonFemale;

    ImageView signUpImageView;

    String gender = null;

    String defaultImageUrl = "https://firebasestorage.googleapis.com/v0/b/ic10-1.appspot.com/o/images%2Fprofileadd.svg?alt=media&token=2121107b-14b4-404a-81e2-33ec85ba4cc8";

    String imageUrl = null;

    Bitmap imageReceived;

    String email;

    String password;

    String firstName;

    String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("SignUp");
        signUpFirstname = findViewById(R.id.signUpFirstname);
        signUpLastname = findViewById(R.id.signUpLastname);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
        signUpRadioGroup = findViewById(R.id.signUpGenderRadioGroup);
        signUpRadioButtonMale = findViewById(R.id.signUpRadioButtonMale);
        signUpRadioButtonFemale = findViewById(R.id.signUpRadioButtonFemale);
        signUpImageView = findViewById(R.id.signUpImageView);
        signUpButton = findViewById(R.id.signUpSubmitButton);

        firebaseAuth = FirebaseAuth.getInstance();

        signUpImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        signUpRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.signUpRadioButtonMale) {
                    gender = "Male";
                }
                if (checkedId == R.id.signUpRadioButtonFemale) {
                    gender = "Female";
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = signUpEmail.getText().toString();
                password = signUpPassword.getText().toString();
                firstName = signUpFirstname.getText().toString();
                lastName = signUpLastname.getText().toString();
                if (email.length() != 0 && password.length() >= 6 && firstName.length() != 0 & gender != null) {
                    if (imageReceived == null) {
                        final String emailStr = signUpEmail.getText().toString();
                        final User_local userObj = new User_local(firstName, lastName, defaultImageUrl, gender, null);
                        Map<String, Object> userMap = userObj.toUserMap();
                        firebaseDB.collection("users").document(emailStr)
                                .set(userMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                                                //  Toast.makeText(SignUpActivity.this, "UID is : " + user.getUid(), Toast.LENGTH_SHORT).show();

                                                                userObj.setUid(user.getUid());
                                                                //  Call to update the Uid Reference for created user
                                                                DocumentReference referenceForCurrentUser = firebaseDB.collection("users").document(emailStr);
                                                                referenceForCurrentUser
                                                                        .update("Uid", user.getUid())
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Log.d("demo", "User Details Upadated Successfully!");
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.d("demo", "User Details Update FAILED");
                                                                            }
                                                                        });
                                                                Toast.makeText(SignUpActivity.this, "New User_local Created",
                                                                        Toast.LENGTH_SHORT).show();

                                                                //  If required to store user details in shared preferences
                                                                /*Gson gson = new Gson();
                                                                String jsonString = gson.toJson(userObj);
                                                                Context ctx = getApplicationContext();
                                                                SharedPreferences sharedPreferences = ctx.getSharedPreferences("ProfileData", MODE_PRIVATE);
                                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                editor.putString("Key", jsonString);
                                                                editor.commit();*/

                                                                //  Intent intent = new Intent(SignUpActivity.this, TripsActivity.class);
                                                                Intent intent = new Intent(SignUpActivity.this, Login.class);
                                                                startActivity(intent);
                                                                finish();
                                                            } else {
                                                                // If sign in fails, display a message to the user.
                                                                Log.d("demo", "User Creation Failed : " + task.getException());
                                                                Toast.makeText(SignUpActivity.this, "Failed to create user" + task.getException(),
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "User Profile Creation Failed", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {

                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

                        final StorageReference storageReference = firebaseStorage.getReference();

                        String ref = "images/Image" + System.currentTimeMillis() + ".png";

                        final StorageReference imageRepository = storageReference.child(ref);

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                        imageReceived.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                        byte[] data = byteArrayOutputStream.toByteArray();

                        UploadTask uploadTask = imageRepository.putBytes(data);

                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("demo", "Upload Failed, Message: " + e.getMessage());
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("demo", "Image Uploaded Successfully");
                            }
                        });

                        Task<Uri> task = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }

                                return imageRepository.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Log.d("demo", "Image" + task.getResult());
                                    imageUrl = task.getResult().toString();
                                    final User_local userObj = new User_local(firstName, lastName, imageUrl, gender, null);
                                    Map<String, Object> userMap = userObj.toUserMap();
                                    final String emailStr = signUpEmail.getText().toString();
                                    firebaseDB.collection("users").document(emailStr)
                                            .set(userMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                                                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                                        if (task.isSuccessful()) {
                                                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                                                            Toast.makeText(SignUpActivity.this, "New User_local Created",
                                                                                    Toast.LENGTH_SHORT).show();

                                                                            userObj.setUid(user.getUid());
                                                                            //  Call to update the Uid Reference for created user
                                                                            DocumentReference referenceForCurrentUser = firebaseDB.collection("users").document(emailStr);
                                                                            referenceForCurrentUser
                                                                                    .update("Uid", user.getUid())
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            Log.d("demo", "User_local Details Upadated Successfully!");
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Log.d("demo", "User_local Details Upadated FAILED");
                                                                                        }
                                                                                    });

                                                                            /*
                                                                            Gson gson = new Gson();
                                                                            String jsonString = gson.toJson(userObj);
                                                                            Context ctx = getApplicationContext();
                                                                            SharedPreferences sharedPreferences = ctx.getSharedPreferences("ProfileData", MODE_PRIVATE);
                                                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                            editor.putString("Key", jsonString);
                                                                            editor.commit();
                                                                            */

                                                                            //  After Signing up
                                                                            //  Intent intent = new Intent(SignUpActivity.this, TripsActivity.class);
                                                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                                            startActivity(intent);

                                                                        } else {
                                                                            Log.d("demo", "User_local Creation Failed : " + task.getException());
                                                                            Toast.makeText(SignUpActivity.this, "Failed to create user" + task.getException(),
                                                                                    Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        Toast.makeText(SignUpActivity.this, "User_local Profile Creation Failed", Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                }
                            }
                        });
                    }
                } else {
                    if (email.length() == 0) {
                        signUpEmail.setError("Email Address Required");
                    }
                    if (password.length() == 0) {
                        signUpPassword.setError("Enter A Password");
                    }
                    if (password.length() > 0 && password.length() < 6) {
                        signUpPassword.setError("Password must be at least six characters");
                    }

                    if (firstName.length() == 0) {
                        signUpFirstname.setError("Firstname Required");
                    }

                    if (gender == null) {
                        Toast.makeText(SignUpActivity.this, "Select A Gender", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showPictureDialog() {

        AlertDialog.Builder imageSelectionChoice = new AlertDialog.Builder(this);

        imageSelectionChoice.setTitle("Select Image Source");

        String[] imageSelectionOptions = {"Select Existing Image From Gallery", "Click A Photo With Camera"};

        imageSelectionChoice.setItems(imageSelectionOptions,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openGalleryForImages();
                                break;
                            case 1:
                                openCameraForImages();
                                break;
                        }
                    }
                });
        imageSelectionChoice.show();
    }

    public void openGalleryForImages() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void openCameraForImages() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    signUpImageView.setImageBitmap(bitmap);
                    imageReceived = bitmap;
                } catch (IOException e) {
                    Toast.makeText(SignUpActivity.this, "Gallery Image Failed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            signUpImageView.setImageBitmap(thumbnail);
            imageReceived = thumbnail;
        }
    }

}