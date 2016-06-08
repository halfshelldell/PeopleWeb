package com.ironyard;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        ArrayList<Person> arrayList = new ArrayList<>();
        File f = new File("person.txt");
        Scanner fileScanner = new Scanner(f);
        fileScanner.nextLine();
        while(fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            String[] columns = line.split(",");
            Person person = new Person((Integer.valueOf(columns[0])), columns[1], columns[2], columns[3], columns[4], columns[5]);
            arrayList.add(person);
        }
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    HashMap m = new HashMap();

                    ArrayList<Person> subset = new ArrayList<>();

                    m.put("arrayList", arrayList);

                    return new ModelAndView(m, "index.html");
                },
                new MustacheTemplateEngine()
        );
    }
}
