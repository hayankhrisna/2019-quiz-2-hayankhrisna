package com.hayankhrisna.kuis2_hayan.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hayankhrisna.kuis2_hayan.Application;
import com.hayankhrisna.kuis2_hayan.Constant;
import com.hayankhrisna.kuis2_hayan.R;
import com.hayankhrisna.kuis2_hayan.Session;

import com.hayankhrisna.kuis2_hayan.adapters.TodoAdapter;
import com.hayankhrisna.kuis2_hayan.generator.ServiceGenerator;
import com.hayankhrisna.kuis2_hayan.models.Envelope;
import com.hayankhrisna.kuis2_hayan.models.Todo;
import com.hayankhrisna.kuis2_hayan.services.TodoService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements TodoAdapter.OnTodoClickedListener {

    private RecyclerView todosRecyclerView;
    private Session session;
    private TodoService service;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.hayankhrisna.kuis2_hayan.activities.MainActivity.this, SaveTodoActivity.class);
                intent.putExtra(Constant.KEY_REQUEST_CODE, Constant.ADD_TODO);
                startActivityForResult(intent, Constant.ADD_TODO);
            }
        });
        session = Application.provideSession();
        if (!session.isLogin()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        todosRecyclerView = findViewById(R.id.rv_todos);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        todosRecyclerView.setLayoutManager(layoutManager);
        adapter = new TodoAdapter(this, this);
        todosRecyclerView.setAdapter(adapter);
        service = ServiceGenerator.createService(TodoService.class);
        loadTodos();
    }

    private void loadTodos() {
        Call<Envelope<List<Todo>>> todos = service.getTodos(null, 1, 10);
        todos.enqueue(new Callback<Envelope<List<Todo>>>() {
            @Override
            public void onResponse(Call<Envelope<List<Todo>>> call, Response<Envelope<List<Todo>>> response) {
                if (response.code() == 200) {
                    Envelope<List<Todo>> okResponse = response.body();
                    List<Todo> items = okResponse.getData();
                    adapter.setItems(items);
                }
            }

            @Override
            public void onFailure(Call<Envelope<List<Todo>>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(Todo todo) {
        Intent intent = new Intent(this, SaveTodoActivity.class);
        intent.putExtra(Constant.KEY_TODO, todo);
        intent.putExtra(Constant.KEY_REQUEST_CODE, Constant.UPDATE_TODO);
        startActivityForResult(intent, Constant.UPDATE_TODO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadTodos();
        }
    }
}
