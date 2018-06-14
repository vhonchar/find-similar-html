package org.finder;

import org.finder.command.FindSimilarCommand;
import org.finder.html.PageElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Console {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("You didn't specified one of parameters. Should be: id srcFile targetFile");
            System.exit(1);
            return;
        }

        String idToFind = args[0];
        File src = new File(args[1]);
        File changed = new File(args[2]);

        try {
            PageElement found = FindSimilarCommand
                    .buildDefault()
                    .findSimilar(idToFind, new FileInputStream(src), new FileInputStream(changed));
            System.out.print(found.toString());
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
