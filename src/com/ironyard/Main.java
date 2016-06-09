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

                    int offset = 0;
                    String off = request.queryParams("offset");
                    if (off != null) {
                        offset = Integer.valueOf(off);
                    }

                    HashMap m = new HashMap();

                    ArrayList<Person> temp = new ArrayList<>(arrayList.subList(offset, offset + 20));
                    m.put("arrayList", temp);
                    m.put("offsetDown", offset - 20);
                    m.put("offsetUp", offset + 20);
                    m.put("previous", offset > 0 );
                    m.put("next", offset + 20 < arrayList.size());

                    return new ModelAndView(m, "index.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/person",
                (request, response) -> {

                    int id = Integer.valueOf(request.queryParams("id"));
                    Person per = arrayList.get(id - 1);

                    return new ModelAndView(per, "person.html");
                },
                new MustacheTemplateEngine()
        );
    }
}
