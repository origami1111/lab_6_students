package com.example.students;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class StudentsListActivity extends AppCompatActivity {

    public static final String GROUP_NUMBER = "groupnumber";
    public static final String STUDENT_NAME = "name";

    private int grpNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        Intent intent = getIntent();
        grpNumber = intent.getIntExtra(GROUP_NUMBER, 0);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                Student student = (Student) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(StudentsListActivity.this,
                        StudentActivity.class);
                intent.putExtra(StudentActivity.GROUP_NUMBER, Integer.toString(grpNumber));
                intent.putExtra(STUDENT_NAME, student.getName());
                startActivity(intent);
            }
        };

        ListView listView = (ListView) findViewById(R.id.studentsList);
        listView.setOnItemClickListener(listener);

    }

    @Override
    public void onStart(){
        super.onStart();

        ListView listView = (ListView) findViewById(R.id.studentsList);
        ArrayAdapter<Student> adapter = new ArrayAdapter<Student>(
                this,
                android.R.layout.simple_list_item_1,
                getDataFromDB(grpNumber)
        );

        listView.setAdapter(adapter);
    }

    // вызов активности "добавления студента" по нажатию на кнопку
    public void onAddStudentClick(View view){
        Intent intent = new Intent(this, AddStudentActivity.class);
        intent.putExtra(GROUP_NUMBER, Integer.toString(grpNumber));
        startActivity(intent);
    }
    //

    public void onSendBtnClick(View view){
        TextView textView = (TextView) findViewById(android.R.id.text1);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, textView.getText().toString());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Список студентів");
        startActivity(intent);
    }

    private ArrayList<Student> getDataFromDB(int groupId){
        ArrayList<Student> students = new ArrayList<Student>();

        SQLiteOpenHelper sqLiteOpenHelper = new StudentsDatabaseHelper(this);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT s.id _id, name, number\n" +
                    "FROM students s INNER JOIN groups g ON s.group_id = g.id\n" +
                    "WHERE g.id = ?", new String[] {Integer.toString(groupId)});

            while (cursor.moveToNext()){
                students.add(
                        new Student(
                                cursor.getString(1),
                                cursor.getString(0)
                        )
                );
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this,
                    e.getMessage(),
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        return students;
    }

}