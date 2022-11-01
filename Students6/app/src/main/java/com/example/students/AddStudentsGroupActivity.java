package com.example.students;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddStudentsGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students_group);
    }

    // add group
    public void onGrpAddClick(View view){
        EditText number = (EditText) findViewById(R.id.addGroupNumber);
        EditText faculty = (EditText) findViewById(R.id.addFaculty);

        SQLiteOpenHelper sqLiteOpenHelper = new StudentsDatabaseHelper(this);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("number", number.getText().toString());
            contentValues.put("facultyName", faculty.getText().toString());
            contentValues.put("educationLevel", 0);
            contentValues.put("contractExistsFlg", 0);
            contentValues.put("privilegeExistsFlg", 0);

            db.insert("groups", null, contentValues);
            db.close();
            NavUtils.navigateUpFromSameTask(this);
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this,
                    e.getMessage(),
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}