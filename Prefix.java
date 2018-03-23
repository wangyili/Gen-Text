// Name: Yilin Wang
// USC loginid: wangyili
// CS 455 PA4
// Spring 2016

import java.util.Arrays;
import java.util.LinkedList;


public class Prefix {
    private LinkedList<String> prefixList;
    String sprefix;

    public Prefix(String prefixString) {
        prefixList = new LinkedList(Arrays.asList(prefixString.split(" ")));
    }

    
    public String getPrefix() {
        sprefix = "";
        for(int i = 0; i < prefixList.size(); i++){
            sprefix = sprefix + prefixList.get(i) + " ";
        }
        sprefix = sprefix.substring(0, sprefix.length() -1);
        return sprefix;
    }

    
    public void update(String newprefix) {
        prefixList.removeFirst();
        prefixList.add(newprefix);
    }
}
