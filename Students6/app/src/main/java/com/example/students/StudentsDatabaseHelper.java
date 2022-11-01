package com.example.students;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentsDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "students";
    private static final int DB_VERSION = 2;

    public StudentsDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String sqlText = "CREATE TABLE Groups (\n" +
                "id                 INTEGER     PRIMARY KEY AUTOINCREMENT,\n" +
                "number             TEXT(10)    NOT NULL,\n" +
                "facultyName        TEXT(100),\n" +
                "educationLevel     INTEGER,\n" +
                "contractExistsFlg  BOOLEAN,\n" +
                "privilegeExistsFlg BOOLEAN\n"+
                ");";

        db.execSQL(sqlText);
        updateSchema(db, 0);
        populateDB(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
        updateSchema(db, oldV);
    }

    private void populateDB(SQLiteDatabase db){
        populateGroups(db);
        populateStudents(db);
    }

    private void populateGroups(SQLiteDatabase db){
        for (StudentsGroup group : StudentsGroup.getGroups()){
            insertRowToGroup(db, group);
        }
    }

    private void populateStudents(SQLiteDatabase db){
        for (Student student : Student.getStudents()){
            insertRowToStudent(db, student);
        }
    }

    private void insertRowToGroup(SQLiteDatabase db, StudentsGroup group){
        ContentValues contentValues = new ContentValues();

        contentValues.put("number", group.getNumber());
        contentValues.put("facultyName", group.getFacultyName());
        contentValues.put("educationLevel", group.getEducationLevel());
        contentValues.put("contractExistsFlg", group.isContractExistsFlg());
        contentValues.put("privilegeExistsFlg", group.isPrivilageExistsFlg());

        db.insert("Groups", null, contentValues);
    }

    private void insertRowToStudent(SQLiteDatabase db, Student student){
        db.execSQL("INSERT INTO students(name, group_id)\n" +
                "SELECT '" + student.getName() + "', id\n" +
                "FROM groups\n" +
                "WHERE number='" + student.getGroupNumber() + "'");
    }

    private void updateSchema(SQLiteDatabase db, int oldV){
        if(oldV < 2){
            db.execSQL("CREATE TABLE Students (\n" +
                    "id       INTEGER   PRIMARY KEY AUTOINCREMENT,\n" +
                    "name     TEXT(100) NOT NULL,\n" +
                    "group_id INTEGER   REFERENCES Groups (id) ON DELETE RESTRICT\n" +
                    "                                          ON UPDATE RESTRICT\n" +
                    ");");

            populateStudents(db);
        }
    }
}
