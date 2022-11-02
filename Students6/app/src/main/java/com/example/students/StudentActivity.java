package com.example.students;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class StudentActivity extends AppCompatActivity {

    public static final String GROUP_NUMBER = "groupnumber";
    public static final String STUDENT_NAME = "name";

    private String studentNameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        Bundle arguments = getIntent().getExtras();
        String groupNameStr = arguments.get(GROUP_NUMBER).toString();
        studentNameStr = arguments.get(STUDENT_NAME).toString();

        StudentsGroup group = null;

        SQLiteOpenHelper sqLiteOpenHelper = new StudentsDatabaseHelper(this);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.query("groups",
                    new String[] {"number", "facultyName", "educationLevel",
                            "contractExistsFlg", "privilegeExistsFlg", "id"},
                    "id=?", new String[] {groupNameStr},
                    null, null, null);

            if(cursor.moveToFirst()){
                group = new StudentsGroup(
                        cursor.getInt(5),
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        (cursor.getInt(3) > 0),
                        (cursor.getInt(4) > 0)
                );
            }
            else{
                Toast toast = Toast.makeText(this,
                        "Can't find group with id " + groupNameStr,
                        Toast.LENGTH_SHORT);
                toast.show();
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this,
                    e.getMessage(),
                    Toast.LENGTH_SHORT);
            toast.show();
        }

        EditText groupNameText = (EditText) findViewById(R.id.studentGroupText);
        groupNameText.setText(group.getNumber());

        EditText studentNameText = (EditText) findViewById(R.id.studentNameText);
        studentNameText.setText(studentNameStr);
    }

    // edit student
    public void onOkBtnClick(View view){
        SQLiteOpenHelper sqLiteOpenHelper = new StudentsDatabaseHelper(this);

        ContentValues contentValues = new ContentValues();
        contentValues.put("name",
                ((EditText) findViewById(R.id.studentNameText)).getText().toString());

        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            db.update("students",
                    contentValues,
                    "name=?",
                    new String[] { studentNameStr });

            db.close();
            finish();
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this,
                    e.getMessage(),
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    // delete student
    public void onDelete(View view){
        SQLiteOpenHelper sqLiteOpenHelper = new StudentsDatabaseHelper(this);

        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            db.delete("students",
                    "name=?",
                    new String[] { studentNameStr });

            db.close();
            finish();
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this,
                    e.getMessage(),
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}