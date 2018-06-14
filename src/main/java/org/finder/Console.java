package org.finder;

import org.finder.command.FindSimilarCommand;
import org.finder.html.PageElement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Console {

    public static void main(String[] args) {

        File src = new File(args[0]);
        File changed = new File(args[1]);

        try {
            PageElement found = FindSimilarCommand
                    .buildDefault()
                    .findSimilar("make-everything-ok-button", new FileInputStream(src), new FileInputStream(changed));
            System.out.print(found.toString());
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
