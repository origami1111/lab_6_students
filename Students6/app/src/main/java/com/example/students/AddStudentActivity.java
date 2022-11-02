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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddStudentActivity extends AppCompatActivity {

    public static final String GROUP_NUMBER = "groupnumber";
    private String grpNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        Bundle arguments = getIntent().getExtras();
        grpNumber = arguments.get(GROUP_NUMBER).toString();
        StudentsGroup group = null;

        SQLiteOpenHelper sqLiteOpenHelper = new StudentsDatabaseHelper(this);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.query("groups",
                    new String[] {"number", "facultyName", "educationLevel",
                            "contractExistsFlg", "privilegeExistsFlg", "id"},
                    "id=?", new String[] {grpNumber},
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
                        "Can't find group with id " + grpNumber,
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

        TextView groupName = (TextView) findViewById(R.id.groupNumberText);
        groupName.setText(group.getNumber());
    }

    // add student
    public void onAddStudentClick(View view){
        EditText studentName = (EditText) findViewById(R.id.studentNameText);

        SQLiteOpenHelper sqLiteOpenHelper = new StudentsDatabaseHelper(this);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("name", studentName.getText().toString());
            contentValues.put("group_id", grpNumber);

            db.insert("students", null, contentValues);
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