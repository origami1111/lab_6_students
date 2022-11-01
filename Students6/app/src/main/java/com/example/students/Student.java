package com.example.students;

import java.util.ArrayList;
import java.util.Arrays;

public class Student {
    private String name;
    private String groupNumber;

    public Student(String name, String groupNumber){
        this.name = name;
        this.groupNumber = groupNumber;
    }

    public String getName(){
        return name;
    }

    public String getGroupNumber(){
        return groupNumber;
    }

    private final static ArrayList<Student> students = new ArrayList<Student>(
    Arrays.asList(
            new Student("Островський Вячеслав", "ІПЗ19-1"),
            new Student("Ванжа Микита", "ІПЗ19-1"),
            new Student("Орел Вадим", "ІПЗ19-1"),
            new Student("Миргородський Данило", "ІПЗ19-1"),
            new Student("Хоменко Микиьа", "ІПЗ19-2"),
            new Student("Полеводін Евген", "ІПЗ19-2")
            )
    );

    public static ArrayList<Student> getStudents(){
        return getStudents("");
    }

    public static ArrayList<Student> getStudents(String groupNumber){
        ArrayList<Student> stList = new ArrayList<>();

        for (Student s : students){
            if(s.getGroupNumber().equals(groupNumber) || (groupNumber == "")){
                stList.add(s);
            }
        }

        return stList;
    }

    @Override
    public String toString(){
        return name;
    }
}
