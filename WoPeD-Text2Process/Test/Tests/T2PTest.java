package Tests;

import java.util.Iterator;
import java.util.LinkedList;

public abstract class T2PTest {

    /*
    Some Basic Utils specifically for all T2P Tests
    */

    protected boolean euqualsWeakly(String exspected, String actual){
        boolean equals=true;
        String a= sanitizeText(exspected);
        String b= sanitizeText(actual);
        diff_match_patch dmp = new diff_match_patch();
        LinkedList<diff_match_patch.Diff> x = dmp.diff_main(exspected, actual);
        Iterator<diff_match_patch.Diff> i = x.iterator();
        while(i.hasNext()){
            diff_match_patch.Diff diff = i.next();
            if(!diff.operation.equals(diff_match_patch.Operation.EQUAL))
                equals=false;
        }

        if(!equals){
            System.out.println("Actual and exscpect differ. The  following characters need to be fixed: ");
            LinkedList<diff_match_patch.Patch> pl = dmp.patch_make(actual, exspected);
            Iterator<diff_match_patch.Patch> k = pl.iterator();
            while(k.hasNext()){
                diff_match_patch.Patch patch = k.next();
                System.out.println("Mismatch at "+patch.start1+": ");
                System.out.println(patch.diffs.toString());
            }
        }

        return equals;

    }

    protected static String sanitizeText(String text){
        //get rid of tabs and newlines
        text = text.replace("\t", "");
        text = text.replace("\n", "");

        //deal with x*space based tabs
        while (text.contains("  ")){
            text=text.replace("  "," ");
        }
        return text;
    }

}
