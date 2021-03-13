package pers.jyb.K_on;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.text.TextUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import static pers.jyb.K_on.MainActivity.musicList;


public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchView musicSearch;
    private List<Music> data;
    private int positionList;
    private ListOfLists listOfLists;
    private Music music;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listOfLists=ListOfLists.get(getApplicationContext());
        Intent intent = getIntent();

        positionList=intent.getIntExtra("CLICK_POSITION",0);
        musicList= listOfLists.getList().get(positionList);
        recyclerView= findViewById(R.id.recycler_view_musics);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        data = new ArrayList<>();
        data.addAll(musicList.getList());
        //adapter = new MusicsAdapter(R.layout.list_item_music,musicList.getList());
        adapter = new MusicsAdapter(R.layout.list_item_music,data);

        musicSearch = findViewById(R.id.music_search);

        ((MusicsAdapter) adapter).setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intentPlayer = new Intent(ListActivity.this, PlayerActivity.class);
                intentPlayer.putExtra("POSITION",positionList);
                intentPlayer.putExtra("CLICK_MUSIC",position);
                startActivity(intentPlayer);
            }
        });
        recyclerView.setAdapter(adapter);
        ((MusicsAdapter) adapter).setEmptyView(getView(R.layout.empty_music_list));
        Toolbar listToolbar = findViewById(R.id.toolbar_list);
        listToolbar.setTitle(musicList.getName());
        setSupportActionBar(listToolbar);
        ActionBar ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);
        listToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSearchList();
    }

    private void setSearchList() {
        //设置SearchView默认是否自动缩小为图标
        musicSearch.setIconifiedByDefault(true);
        musicSearch.setFocusable(false);
        //设置搜索框监听器
        musicSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //点击搜索按钮时激发
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //输入时激发

                if(TextUtils.isEmpty(s)){
                    //没有过滤条件内容
                    data.clear();
                    data.addAll(musicList.getList());
                    adapter.notifyDataSetChanged();
                } else {
                    //根据输入内容对RecycleView搜索
//                    List<LocalMusicBean> FilterData = new ArrayList<>();
                    data.clear();
                    for (Music bean:musicList.getList()){
                        if(bean.getName().contains(s) || bean.getArtist().contains(s)){
                            data.add(bean);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                return true;
            }
        });

    }



    private View getView(int viewId) {
        return LayoutInflater.from(this).inflate(viewId, new RelativeLayout(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                if(positionList!=0&&positionList!=1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("删除此列表？")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            listOfLists.getList().remove(positionList);
                            finish();
                        }
                    });
                    builder.show();
                    adapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(this, "此列表不可删除", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_settings:
                if(positionList!=0&&positionList!=1) {
                    Intent intentDrag = new Intent(ListActivity.this, ItemDragActivity.class);
                    intentDrag.putExtra("POSITION",positionList);
                    startActivity(intentDrag);
                }else{
                    Toast.makeText(this, "此列表不可修改", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
