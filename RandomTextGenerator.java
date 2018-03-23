// Name: Yilin Wang
// USC loginid: wangyili
// CS 455 PA4
// Spring 2016

import java.util.*;

public class RandomTextGenerator {
    private Map<String, LinkedList<Map.Entry<String, Integer>>> stringsMap = new HashMap<>();


    public RandomTextGenerator(Map<String, LinkedList<Map.Entry<String, Integer>>> wordsmap) {
        this.stringsMap = wordsmap;
    }

    
    public String Generateword(Prefix prefix) {
        ArrayList<String> possiblewords = new ArrayList<>();
        for (Map.Entry<String, Integer> e : stringsMap.get(prefix.getPrefix())) {
            for (int i = 0; i < e.getValue(); i++) {
                possiblewords.add(e.getKey());
            }
        }
        Collections.shuffle(possiblewords);
        return possiblewords.get(new Random().nextInt(possiblewords.size()));
    }

    
    public String getPossibilities(Prefix prefix) {
        ArrayList<String> possiblewords = new ArrayList<>();
        for (Map.Entry<String, Integer> e : stringsMap.get(prefix.getPrefix())) {
            for (int i = 0; i < e.getValue(); i++) {
                possiblewords.add(e.getKey());
            }
        }
        return possiblewords.toString();
    }

    
    public String getRandom() {
        List<String> wordsArray = new ArrayList<>(stringsMap.keySet());
        return wordsArray.get(new Random().nextInt(wordsArray.size()));
    }
}
