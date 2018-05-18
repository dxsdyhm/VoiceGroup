package com.view.dansesshou.voicegroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.view.dansesshou.library.BatteryRound;
import com.view.dansesshou.library.IndicatorsViewGroup;
import com.view.dansesshou.library.VoiceView;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SlidingUpPanelLayout mLayout;

    private RecyclerView rlTest;
    private MultiTypeAdapter adapter;

    private Items items;

    private TextView t;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    private Button btnAdd,btnDelete;
    private IndicatorsViewGroup oritaView;
    private VoiceView voiceView;
    private BatteryRound batteryRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        gridLayoutManager=new GridLayoutManager(this,3);
        //setSupportActionBar((Toolbar) findViewById(R.id.main_toolbar));
        rlTest=findViewById(R.id.list);
        adapter = new MultiTypeAdapter();

        /* 注册类型和 View 的对应关系 */
        adapter.register(Device.class, new DeviceBindView());
        rlTest.setLayoutManager(linearLayoutManager);
        rlTest.setAdapter(adapter);

        items = new Items();
        setData();
        adapter.setItems(items);
        adapter.notifyDataSetChanged();

        btnAdd=findViewById(R.id.btn_add);
        btnDelete=findViewById(R.id.btn_delete);
        oritaView=findViewById(R.id.vg_oritation);
        voiceView=findViewById(R.id.v_voice);
        batteryRound=findViewById(R.id.v_battery);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        voiceView.setOnClickListener(this);

        mLayout = findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                oritaView.setShowProgress(slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState==SlidingUpPanelLayout.PanelState.COLLAPSED){
                    //线性
                    rlTest.setLayoutManager(linearLayoutManager);
                    oritaView.showOrHidenSpecialView(IndicatorsViewGroup.STATE_HIDDEN);
                }else if(newState==SlidingUpPanelLayout.PanelState.EXPANDED){
                    //表格
                    rlTest.setLayoutManager(gridLayoutManager);
                    oritaView.showOrHidenSpecialView(IndicatorsViewGroup.STATE_SHOW);
                }else{
                    oritaView.showOrHidenSpecialView(IndicatorsViewGroup.STATE_HIDDEN);
                }
            }
        });
        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


    }

    private void setData(){
        for(int i=0;i<10;i++){
            Device device=new Device(String.valueOf(i));
            items.add(device);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    private  boolean isPlaying=false;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                items.add(new Device(String.valueOf(items.size())));
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_delete:
                items.remove(0);
                adapter.notifyDataSetChanged();
                break;
            case R.id.v_voice:
                isPlaying=!isPlaying;
                voiceView.startPlay(isPlaying);
                batteryRound.setCurrentValue((int) (Math.random()*100));
                break;
        }
    }
}
