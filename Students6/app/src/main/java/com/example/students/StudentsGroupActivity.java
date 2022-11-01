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
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class StudentsGroupActivity extends AppCompatActivity {

    public static final String GROUP_NUMBER = "groupnumber";

    private StudentsGroup group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_group2);

        Intent intent = getIntent();
        int grpNumber = intent.getIntExtra(GROUP_NUMBER, 0);
        group = null;

        SQLiteOpenHelper sqLiteOpenHelper = new StudentsDatabaseHelper(this);
        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            Cursor cursor = db.query("groups",
                    new String[] {"number", "facultyName", "educationLevel",
                    "contractExistsFlg", "privilegeExistsFlg", "id"},
                    "id=?", new String[] {Integer.toString(grpNumber)},
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
                        "Can't find group with id " + Integer.toString(grpNumber),
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

        EditText txtGrpNumber = (EditText) findViewById(R.id.grpNumberEdit);
        txtGrpNumber.setText(group.getNumber());

        EditText txtFacultyName = (EditText) findViewById(R.id.facultyEdit);
        txtFacultyName.setText(group.getFacultyName());

        TextView txtImgGrp = (TextView) findViewById(R.id.grpNumberImageTxt);
        txtImgGrp.setText(group.getNumber());

        TextView txtImgFaculty = (TextView) findViewById(R.id.facultyNameImageTxt);
        txtImgFaculty.setText(group.getFacultyName());

        if(group.getEducationLevel() == 0){
            ((RadioButton) findViewById(R.id.edu_level_bachelor)).setChecked(true);
        }
        else{
            ((RadioButton) findViewById(R.id.edu_level_master)).setChecked(true);
        }

        ((CheckBox) findViewById(R.id.contract_flg)).setChecked(group.isContractExistsFlg());
        ((CheckBox) findViewById(R.id.privelege_flg)).setChecked(group.isPrivilageExistsFlg());
    }

    // edit group
    public void onOkBtnClick(View view){
        SQLiteOpenHelper sqLiteOpenHelper = new StudentsDatabaseHelper(this);

        ContentValues contentValues = new ContentValues();
        contentValues.put("number",
                ((TextView) findViewById(R.id.grpNumberEdit)).getText().toString());
        contentValues.put("facultyName",
                ((TextView) findViewById(R.id.facultyEdit)).getText().toString());
        contentValues.put("educationLevel",
                ((RadioButton) findViewById(R.id.edu_level_master)).isChecked() ? 1 : 0);
        contentValues.put("contractExistsFlg",
                ((CheckBox) findViewById(R.id.contract_flg)).isChecked());
        contentValues.put("privilegeExistsFlg",
                ((CheckBox) findViewById(R.id.privelege_flg)).isChecked());

        Intent intent = getIntent();
        int grpNumber = intent.getIntExtra(GROUP_NUMBER, 0);

        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            db.update("groups",
                    contentValues,
                    "id=?",
                    new String[] {Integer.toString(grpNumber)});

            db.close();
            NavUtils.navigateUpFromSameTask(this);
        } catch (SQLiteException e){
            Toast toast = Toast.makeText(this,
                    e.getMessage(),
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void onBtnStudListClick(View view){
        Intent newIntent = new Intent(this, StudentsListActivity.class);
        newIntent.putExtra(StudentsListActivity.GROUP_NUMBER, group.getId());
        startActivity(newIntent);
    }

    // delete group
    public void onDelete(View view){
        SQLiteOpenHelper sqLiteOpenHelper = new StudentsDatabaseHelper(this);

        Intent intent = getIntent();
        int grpNumber = intent.getIntExtra(GROUP_NUMBER, 0);

        try {
            SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
            db.delete("groups",
                    "id=?",
                    new String[] {Integer.toString(grpNumber)});

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