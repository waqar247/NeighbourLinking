package com.example.waqarahmed.neighbourlinking.Activities.AdminActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waqarahmed.neighbourlinking.Activities.TanantActivities.RequestForm;
import com.example.waqarahmed.neighbourlinking.Classes.ServiceMan;
import com.example.waqarahmed.neighbourlinking.Interfaces.AsynResonseForMenPowerList;
import com.example.waqarahmed.neighbourlinking.Listener.RecyclerItemClickListener;
import com.example.waqarahmed.neighbourlinking.R;
import com.example.waqarahmed.neighbourlinking.Services.RetrievAllMenPowerList;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ManPowerList_admin extends AppCompatActivity implements AsynResonseForMenPowerList{
    String getIntent_skillName;
    RecyclerView menPowerList_recyclerView;
    TextView textView;
    ArrayList<ServiceMan> menlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_man_power_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Services Men");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.admin_toolbar)));
        menPowerList_recyclerView= (RecyclerView) findViewById(R.id.recyclerView_menPower);
        textView = (TextView) findViewById(R.id.messageView);
        menPowerList_recyclerView.setHasFixedSize(true);
        menPowerList_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        menlist = new ArrayList<ServiceMan>();
        getIntent_skillName = getIntent().getStringExtra("Skill").toString();
        getSupportActionBar().setTitle(getIntent_skillName);

        menPowerList_recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                    }
                })
        );
        String type = "login";
        RetrievAllMenPowerList retrievdAllMenList = new RetrievAllMenPowerList(this);
        retrievdAllMenList.asyncResponse = this;
        retrievdAllMenList.execute(type,getIntent_skillName);
    }
    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    public void processFinish(ArrayList<ServiceMan> Manlist) {
        menlist.clear();
        if(Manlist != null) {

          for(int i = 0; i<Manlist.size(); i++){
            if(Manlist.get(i).getStatus().equals("active") && Manlist.get(i).getIsAccountSetUp().equals("yes") ) {
          //      String s = Manlist.get(i).getIsAccountSetUp();
                Log.e("TAG", "processFinish: " );
                menlist.add(Manlist.get(i));
            }

          }
          if(menlist.size()>0) {
                  menPowerList_recyclerView.setVisibility(View.VISIBLE);
                  textView.setVisibility(View.INVISIBLE);
                 // this.menlist = Manlist;
                  RVAdapterForMenList rvAdapterForMenList = new RVAdapterForMenList(menlist, this);
                  menPowerList_recyclerView.setAdapter(rvAdapterForMenList);

              }
              else{
                  menPowerList_recyclerView.setVisibility(View.INVISIBLE);
                  textView.setVisibility(View.VISIBLE);

              }

        }
        else{
            menPowerList_recyclerView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);


        }
    }


    public class RVAdapterForMenList extends RecyclerView.Adapter<RVAdapterForMenList.contactViewHolder> {


        List<ServiceMan> list;
        Context mContext;

        RVAdapterForMenList(List<ServiceMan> userList, Context context) {
            list = userList;
            mContext = context;
        }

        @Override
        public RVAdapterForMenList.contactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_list_item, parent, false);
            contactViewHolder cvh = new contactViewHolder(v);
           return cvh;
        }

        @Override
        public void onBindViewHolder(RVAdapterForMenList.contactViewHolder holder, int position) {
            ServiceMan serviceMan   = list.get(position);

            holder.setContact_name(serviceMan.getName());
            holder.setContact_image(serviceMan.getImage_url(),mContext);
            holder.setContact_no(serviceMan.getContact());
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public  class contactViewHolder extends RecyclerView.ViewHolder{

            View mView;
            ImageView imageView ;
            TextView nameView,dateView;

            public contactViewHolder(View itemView) {
                super(itemView);

                mView = itemView;
                imageView = (ImageView)mView.findViewById(R.id.contentImage_imageView_contentView);
                nameView = (TextView) itemView.findViewById(R.id.contentName_textView_contentView);
                dateView = (TextView)itemView.findViewById(R.id.created_date_contentView);
            }
            public void setContact_name(String name) {

                nameView.setText(name);

            }
            public void setContact_image(final String image , final Context cxt) {
                Picasso.with(cxt).load(image).centerCrop().resize(75,75).placeholder(R.mipmap.ic_launcher).networkPolicy(NetworkPolicy.OFFLINE).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(cxt).load(image).centerCrop().placeholder(R.mipmap.ic_launcher).resize(75,75).into(imageView);
                    }
                });
            }
            public void setContact_no(String no) {

                dateView.setText(no);

            }
        }
}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
