package com.hayankhrisna.kuis2_hayan.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;


import com.hayankhrisna.kuis2_hayan.Constant;
import com.hayankhrisna.kuis2_hayan.R;
import com.hayankhrisna.kuis2_hayan.generator.ServiceGenerator;
import com.hayankhrisna.kuis2_hayan.models.Envelope;
import com.hayankhrisna.kuis2_hayan.models.Todo;
import com.hayankhrisna.kuis2_hayan.services.TodoService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveTodoActivity extends AppCompatActivity {

    private EditText todoText;

    private TodoService service;
    private Todo todo;
    private int requestCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_todo);
        todoText = findViewById(R.id.input_todo);
        service = ServiceGenerator.createService(TodoService.class);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            todo = extras.getParcelable(Constant.KEY_TODO);
            if (todo != null) {
                todoText.setText(todo.getTodo());
            }
            requestCode = extras.getInt(Constant.KEY_REQUEST_CODE);
        }
    }

    public void handleSave(View view) {
        switch (requestCode) {
            case Constant.ADD_TODO: handleAdd(view);
                break;
            case Constant.UPDATE_TODO: handleUpdate(view);
                break;
        }
    }

    private void handleAdd(View view) {
        todo = new Todo();
        todo.setTodo(todoText.getText().toString());;
        Call<Envelope<Todo>> addTodo = service.addTodo(todo);
        addTodo.enqueue(new Callback<Envelope<Todo>>() {
            @Override
            public void onResponse(Call<Envelope<Todo>> call, Response<Envelope<Todo>> response) {
                if (response.code() == 200) {
                    Envelope<Todo> okResponse = response.body();
                    Todo data = okResponse.getData();
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KEY_TODO, data);
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Envelope<Todo>> call, Throwable t) {

            }
        });
    }

    private void handleUpdate(View view) {
        int id = todo.getId();
        todo.setTodo(todoText.getText().toString());
        todo.setDone(null);
        todo.setId(null);
        todo.setUser(null);
        Call<Envelope<Todo>> updateTodo = service.updateTodo(Integer.toString(id), todo);
        updateTodo.enqueue(new Callback<Envelope<Todo>>() {
            @Override
            public void onResponse(Call<Envelope<Todo>> call, Response<Envelope<Todo>> response) {
                if (response.code() == 200) {
                    Envelope<Todo> okResponse = response.body();
                    Todo data = okResponse.getData();
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KEY_TODO, data);
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Envelope<Todo>> call, Throwable t) {

            }
        });


    }
}
