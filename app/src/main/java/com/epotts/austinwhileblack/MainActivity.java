package com.epotts.austinwhileblack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private Button signOutButt;
    private FirebaseAuth auth;
    private RecyclerView recycler;
    private FirebaseRecyclerAdapter adapter;
    private LinearLayoutManager linearManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        //declare all variables
        signOutButt = findViewById(R.id.signOutButton);
        auth = FirebaseAuth.getInstance();
        recycler = findViewById(R.id.businessRecycler);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("businesses").push();
        linearManage = new LinearLayoutManager(this);
        recycler.setLayoutManager(linearManage);
        recycler.setHasFixedSize(true);
        fetch();

        signOutButt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Toast.makeText(MainActivity.this, "Signed out!", Toast.LENGTH_LONG).show();
                auth.signOut();
                startActivity(new Intent(MainActivity.this, Signup.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void fetch(){
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("1ETQW7TPMRDcNb7-hEpZ-Wm8uWr2m3E8Ip8o1LV8-XSU")
                .child("Sheet1");

        FirebaseRecyclerOptions<BusinessData> options =
                new FirebaseRecyclerOptions.Builder<BusinessData>()
                        .setQuery(query, new SnapshotParser<BusinessData>() {
                            @NonNull
                            @Override
                            public BusinessData parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new BusinessData(snapshot.child("Business").getValue().toString(),
                                        snapshot.child("Address").getValue().toString(),
                                        snapshot.child("Phone Number").getValue().toString(),
                                        snapshot.child("Website").getValue().toString(),
                                        snapshot.child("Img URL").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<BusinessData, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.business_list_item, parent, false);

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, BusinessData model) {
                holder.setBusinessName(model.getName());
                holder.setAddressBox(model.getAddress());
                holder.setPhone(model.getPhoneNumber());
                holder.setWebsite(model.getWebsite());
                holder.setImage(model.getImgSrc());

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        };
        recycler.setAdapter(adapter);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout root;
        TextView businessName;
        TextView addressBox;
        TextView phoneNumber;
        TextView website;
        ImageView picture;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.cardRoot);
            businessName = itemView.findViewById(R.id.businessName);
            addressBox = itemView.findViewById(R.id.addressBox);
            phoneNumber = itemView.findViewById(R.id.phoneBox);
            website = itemView.findViewById(R.id.websiteBox);
            picture = itemView.findViewById(R.id.logoImg);
        }

        public void setBusinessName(String string) {
            businessName.setText(string);
        }

        public void setAddressBox(String string) {
            addressBox.setText(string);
        }

        public void setPhone(String string) {
            phoneNumber.setText(string);
        }

        public void setWebsite(String string) {
            website.setText(string);
        }

        public void setImage(String url){
            Picasso.get().load(url).into(picture);
        }

    }
}
