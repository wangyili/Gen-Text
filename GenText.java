// Name: Yilin Wang
// USC loginid: wangyili
// CS 455 PA4
// Spring 2016

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map;
import java.util.AbstractMap;
import java.util.LinkedList;

public class GenText {
    private static final int NORMAL_ARGUMENTS_NUMBER = 4;
    private static final int DEBUG_ARGUMENTS_NUMBER = 5;
    private static final int NUMBER_CHARS_PER_LINE = 80;

    public static void main(String[] args) {
        Map<String, LinkedList<Map.Entry<String, Integer>>> nwordsMap = new HashMap<>();
        Map<String, Integer> mapArguments = new HashMap<>();

        
        String[] filenames = new String[2];

        try {

            
            get_command_arguments(args, mapArguments, filenames);

            File infile = new File(filenames[0]);
            Scanner in = new Scanner(infile);

            
            readData(nwordsMap, in, mapArguments.get("prefixLength"));

           
            RandomTextGenerator wordsgenerator = new RandomTextGenerator(nwordsMap);

            String init_prefix = wordsgenerator.getRandom();

            Prefix prefix = new Prefix(init_prefix);

            if ( mapArguments.get("active_debug") == 1) {
                
                debug_model(nwordsMap, prefix, wordsgenerator, mapArguments.get("numwords"), filenames[1]);
            } else {
               
                write_to_file(nwordsMap, prefix, wordsgenerator, mapArguments.get("numwords"), filenames[1]);
            }

        } catch (FileNotFoundException e) {
            System.out.print("Error: Can't find file as input.");
        } catch (BadDataException e) {
            System.out.print(e.getMessage() + " ");
        } catch (NumberFormatException e) {
            System.out.println("Error: Prefixlength and numWords should be Integer");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

   
    private static void get_command_arguments(String args[], Map<String, Integer> mapArguments,
                                              String[] filenames) throws BadDataException {

        
        int is5arguments = 0;
        
        if (args.length < NORMAL_ARGUMENTS_NUMBER) {
            throw new BadDataException("Usage: java Gentext [-d] prefixLength numWords sourceFile outFile");
        }
        if (args.length == DEBUG_ARGUMENTS_NUMBER) {
            if (!args[0].equals("-d")) {
                throw new BadDataException("Usage: java Gentext [-d] prefixLength numWords sourceFile outFile");
            }
            mapArguments.put("active_debug", 1);
            is5arguments = 1;
        }
        else {
            mapArguments.put("active_debug", 0);
        }
     
        mapArguments.put("prefixLength", Integer.parseInt(args[0 + is5arguments]));
        mapArguments.put("numwords", Integer.parseInt(args[1 + is5arguments]));
        if (mapArguments.get("prefixLength") < 1 || mapArguments.get("numwords") < 0) {
            throw new BadDataException("The parameter should follow such situation: numWords > 0 and pre" +
                    ">= 1");
        }
        
        filenames[0] = args[2 + is5arguments];
        filenames[1] = args[3 + is5arguments];
    }


  
    private static void debug_model(Map<String, LinkedList<Map.Entry<String, Integer>>> nwordsMap,
                                    Prefix prefix, RandomTextGenerator wordsgenerator, int wordnums,
                                    String filename) throws IOException{
        System.out.println("DEBUG: chose a new initial prefix: " + prefix.getPrefix());
        Writer writer = new BufferedWriter(new OutputStreamWriter(new
                FileOutputStream(filename), StandardCharsets.UTF_8));
        String tempLine = "";
        for (int i = 0; i < wordnums; i++) {
            while (!nwordsMap.containsKey(prefix.getPrefix())) {
                System.out.println("DEBUG: prefix: " + prefix.getPrefix());
                System.out.println("DEBUG: successor: <END OD FILE>");
                prefix = new Prefix(wordsgenerator.getRandom());
                System.out.println("DEBUG: chose a new initial prefix: " + prefix.getPrefix());
               
            }
            String generatedword = wordsgenerator.Generateword(prefix);

            System.out.println("DEBUG: prefix: " + prefix.getPrefix());
            System.out.println("DEBUG: successor: " + wordsgenerator.getPossibilities(prefix));
            System.out.println("DEBUG: word generated: " + generatedword);

            tempLine = write_line(writer, tempLine, generatedword, wordnums, i);
            prefix.update(generatedword);
        }
        writer.close();
    }

    private static void write_to_file(Map<String, LinkedList<Map.Entry<String, Integer>>> nwordsMap,
                                      Prefix prefix, RandomTextGenerator wordsgenerator, int wordnums,
                                      String filename) throws IOException {
        String tempLine = "";
     
        Writer writer = new BufferedWriter(new OutputStreamWriter(new
                FileOutputStream(filename), StandardCharsets.UTF_8));
        for (int i = 0; i < wordnums; i++) {
           
            while (!nwordsMap.containsKey(prefix.getPrefix())) {
                prefix = new Prefix(wordsgenerator.getRandom());
            }
            String generatedword = wordsgenerator.Generateword(prefix);
            prefix.update(generatedword);

            tempLine = write_line(writer, tempLine, generatedword, wordnums, i);
        }
        writer.close();
    }

  
    private static String write_line(Writer writer, String line, String generatedword, int wordnums, int index)
            throws IOException{
        if(line.length() + generatedword.length() <= NUMBER_CHARS_PER_LINE){
            line =line + generatedword + " ";
        }else{
            writer.write(line.substring(0, line.length()-1));
            writer.write('\n');
            line = generatedword + " ";
        }
        if(index == wordnums - 1){
            writer.write(line.substring(0, line.length()-1));
        }
        return line;
    }


    
    private static void readData(Map<String, LinkedList<Map.Entry<String, Integer>>> words, Scanner in, int prefixlen)
            throws BadDataException {

        String wordsinarticle = "";
        String afterword;
        LinkedList<String> wordsList = new LinkedList<>();

        
        for(int i = 0; i < prefixlen; i ++){
            if(in.hasNext()){
                wordsList.add(in.next());
                wordsinarticle = wordsinarticle +  wordsList.get(i) + " ";
            }
            else{
                throw new BadDataException("The prefix length should be less than the length of the source file");
            }
        }
        wordsinarticle = wordsinarticle.substring(0, wordsinarticle.length()-1);
      
        if(in.hasNext()){
            afterword = in.next();
        }else{
            throw new BadDataException("The length of prefix is equal to the length of the file, which is always " +
                    "reach the end of the fail, resulting in generating no words");
        }

        words.put(wordsinarticle, new LinkedList<Map.Entry<String, Integer>>());
        words.get(wordsinarticle).addLast(new AbstractMap.SimpleEntry(afterword, 1));

        while (in.hasNext()) {
       
            wordsList.removeFirst();
            wordsList.addLast(afterword);

            afterword = in.next();
          

            wordsinarticle = "";
            for(int i = 0; i < wordsList.size(); i++){
                wordsinarticle = wordsinarticle +  wordsList.get(i) + " ";
            }
            wordsinarticle = wordsinarticle.substring(0, wordsinarticle.length() - 1);

            if (words.containsKey(wordsinarticle)) {  
                boolean booleanForAfterwords = false;
                for (Map.Entry<String, Integer> e : words.get(wordsinarticle)) {
                    if (e.getKey().equals(afterword)) { 
                        e.setValue(e.getValue() + 1);
                        booleanForAfterwords = !booleanForAfterwords;
                        break;
                    }
                }
                if (!booleanForAfterwords) {
                    words.get(wordsinarticle).addLast(new AbstractMap.SimpleEntry(afterword, 1));
                }
            } else { 
                words.put(wordsinarticle, new LinkedList<Map.Entry<String, Integer>>());
                words.get(wordsinarticle).addLast(new AbstractMap.SimpleEntry(afterword, 1));
            }
        }
    }
}
