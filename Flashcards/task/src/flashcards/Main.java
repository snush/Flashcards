package flashcards;

import java.util.*;
import java.io.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        Map<String, String> cards = new LinkedHashMap<>();
        List<String> keys = new ArrayList<>();
        List<String> log = new ArrayList<>();
        SortedMap<String, Integer> mistakes = new TreeMap<>();
        File file;

        if ((args.length == 2 || args.length == 4) && args[0].equals("-import")) {
            file = new File(args[1]);
            log.add("-import " + file);
            loadFromFile(cards, keys, log, mistakes, file, true);
        }
        if (args.length == 4 && args[2].equals("-import")) {
            file = new File(args[3]);
            log.add("-import " + file);
            loadFromFile(cards, keys, log, mistakes, file, true);
        }

        System.out.println("Input the action (add, remove, import, export, ask, exit, " +
                "log, hardest card, reset stats):");
        log.add("Input the action (add, remove, import, export, ask, exit, " +
                "log, hardest card, reset stats):");
        String action = scanner.nextLine();
        log.add(action);

        while(!action.equals("exit")) {
            switch (action) {
                case "add":
                    addCard(cards, keys, log, mistakes);
                    break;
                case "remove":
                    removeCard (cards, keys, log, mistakes);
                    break;
                case "import":
                    System.out.println("File name:");
                    log.add("File name:");
                    file = new File(scanner.nextLine());
                    log.add(file + "");
                    loadFromFile(cards, keys, log, mistakes, file, false);
                    break;
                case "export":
                    System.out.println("File name:");
                    log.add("File name:");
                    file = new File(scanner.nextLine());
                    log.add(file + "");
                    saveToFile(cards, keys, log, mistakes, file, false);
                    break;
                case "ask":
                    askForDefinition(cards, keys, log, mistakes);
                    break;
                case "log":
                    log(log);
                    break;
                case "hardest card":
                    hardestCard(mistakes, log);
                    break;
                case "reset stats":
                    resetStats(mistakes, log);
                    break;
                default:
                    break;
            }
            System.out.println();
            System.out.println("Input the action (add, remove, import, export, ask, exit, " +
                    "log, hardest card, reset stats):");
            log.add("Input the action (add, remove, import, export, ask, exit, " +
                    "log, hardest card, reset stats):");
            action = scanner.nextLine();
            log.add(action);
        }

        if ((args.length == 2 || args.length == 4) && args[0].equals("-export")) {
            file = new File(args[1]);
            log.add("-export " + file);
            saveToFile(cards, keys, log, mistakes, file, true);
        } else if (args.length == 4 && args[2].equals("-export")) {
            file = new File(args[3]);
            log.add("-export " + file);
            saveToFile(cards, keys, log, mistakes, file, true);
        } else {
            System.out.println("Bye bye!");
            log.add("Bye bye!");
        }
    }

    public static void addCard(Map<String, String> cards, List<String> keys, List<String> log, Map<String, Integer> mistakes) {
        System.out.println("The card:");
        log.add("The card:");
        String term = scanner.nextLine();
        log.add(term);

        if (cards.containsKey(term)) {
            System.out.println("The card \"" + term + "\" already exists.");
            log.add("The card \"" + term + "\" already exists.");
        } else {
            System.out.println("The definition of the card:");
            log.add("The definition of the card:");
            String definition = scanner.nextLine();
            log.add(definition);

            if (cards.containsValue(definition)) {
                System.out.println("The definition \"" + definition + "\" already exists.");
                log.add(definition);
            } else {
                cards.put(term, definition);
                keys.add(term);
                mistakes.put(term, 0);

                System.out.println("The pair (\"" + term + "\":\"" + definition + "\") has been added.");
                log.add("The pair (\"" + term + "\":\"" + definition + "\") has been added.");
            }
        }
    }

    public static void removeCard (Map<String, String> cards, List<String> keys, List<String> log, Map<String, Integer> mistakes) {
        System.out.println("The card:");
        log.add("The card:");
        String term = scanner.nextLine();
        log.add(term);

        if (cards.containsKey(term)) {
            cards.remove(term);
            keys.remove(term);
            mistakes.remove(term);
            System.out.println("The card has been removed.");
            log.add("The card has been removed.");
        } else {
            System.out.println("Can't remove \"" + term + "\": there is no such card.");
            log.add("Can't remove \"" + term + "\": there is no such card.");
        }
    }

    public static void loadFromFile(Map<String, String> cards, List<String> keys, List<String> log, Map<String, Integer> mistakes, File file, boolean arg) {
        int count = 0;
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String term = scanner.nextLine();
                log.add(term);
                String definition = scanner.nextLine();
                log.add(definition);
                int mistake = Integer.parseInt(scanner.nextLine());
                cards.put(term, definition);
                keys.add(term);
                mistakes.put(term, mistake);
                count++;
            }
            System.out.println(count + " cards have been loaded.");
            log.add(count + " cards have been loaded.");
        } catch (FileNotFoundException e) {
            if(!arg) {
                System.out.println("File not found.");
                log.add("File not found.");
            }
        }
    }

    public static void saveToFile(Map<String, String> cards, List<String> keys, List<String> log, Map<String, Integer> mistakes, File file, boolean arg) {
        int count = 0;
        try {
            FileWriter writer = new FileWriter(file);
            for (int i = 0; i < cards.size(); i++) {
                writer.write(keys.get(i) + "\n");
                writer.write(cards.get(keys.get(i)) + "\n");
                writer.write(mistakes.get(keys.get(i)) + "\n");
                count++;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(arg) {
            System.out.println("Bye bye!\n" + count + " cards have been saved.");
            log.add("Bye bye!\n" +  count + " cards have been saved.");
        } else {
            System.out.println(count + " cards have been saved.");
            log.add(count + " cards have been saved.");
        }
    }

    public static void askForDefinition(Map<String, String> cards, List<String> keys, List<String> log, Map<String, Integer> mistakes) {
        System.out.println("How many times to ask?");
        log.add("How many times to ask?");
        int count = Integer.parseInt(scanner.nextLine());
        log.add(count + "");

        for(int i = 0; i < count; i++) {
            Random random = new Random();
            int index = random.nextInt(keys.size());

            System.out.println("Print the definition of \"" + keys.get(index) + "\":");
            log.add("Print the definition of \"" + keys.get(index) + "\":");
            String answer = scanner.nextLine();
            log.add(answer);

            if (answer.equals(cards.get(keys.get(index)))) {
                System.out.println("Correct answer.");
                log.add("Correct answer.");
            } else if (cards.containsValue(answer)) {
                for (int j = 0; j < cards.size(); j++) {
                    if (answer.equals(cards.get(keys.get(j)))) {
                        System.out.println("Wrong answer. The correct one is \"" + cards.get(keys.get(index)) + "\", " +
                                "you've just written the definition of \"" + keys.get(j) + "\".");
                        log.add("Wrong answer. The correct one is \"" + cards.get(keys.get(index)) + "\", " +
                                "you've just written the definition of \"" + keys.get(j) + "\".");
                        break;
                    }
                }
                mistakes.put(keys.get(index), mistakes.get(keys.get(index)) + 1);
            } else {
                System.out.println("Wrong answer. The correct one is \"" + cards.get(keys.get(index)) + "\".");
                log.add("Wrong answer. The correct one is \"" + cards.get(keys.get(index)) + "\".");
                mistakes.put(keys.get(index), mistakes.get(keys.get(index)) + 1);
            }
        }
    }

    public static void log(List<String> log) {
        System.out.println("File name:");
        log.add("File name:");
        File file = new File(scanner.nextLine());
        log.add(file + "");
        log.add("The log has been saved.");

        try {
            FileWriter writer = new FileWriter(file);
            for (String line : log) {
                writer.write(line + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The log has been saved.");
    }

    public static void hardestCard(Map<String, Integer> mistakes, List<String> log) {
        if (mistakes.isEmpty()) {
            System.out.println("There are no cards with errors.");
            log.add("There are no cards with errors.");
        } else {
            int maxErrors = 0;
            ArrayList<String> hardestCard = new ArrayList<>();

            for(String term : mistakes.keySet()) {
                if(mistakes.get(term) > maxErrors) {
                    maxErrors = mistakes.get(term);
                    hardestCard.clear();
                    hardestCard.add(term);
                } else if(mistakes.get(term) == maxErrors) {
                    hardestCard.add(term);
                }
            }
            if(hardestCard.size() == 1) {
                System.out.println("The hardest card is \"" + hardestCard.get(0) + "\". " +
                        "You have " + maxErrors + " errors answering it.");
                log.add("The hardest card is \"" + hardestCard.get(0) + "\". " +
                        "You have " + maxErrors + " errors answering it.");
            } else {
                System.out.print("The hardest cards are ");
                log.add("The hardest cards are ");
                for(int i = 0; i < hardestCard.size() - 1; i++) {
                    System.out.print("\"" + hardestCard.get(i) + "\", ");
                    log.add("\"" + hardestCard.get(i) + "\", ");
                }
                System.out.println("\"" + hardestCard.get(hardestCard.size() - 1) +
                        "\". You have " + maxErrors + " errors answering them.");
                log.add("\"" + hardestCard.get(hardestCard.size() - 1) +
                        "\". You have " + maxErrors + " errors answering them.");
            }
        }
    }

    public static void resetStats(Map<String, Integer> mistakes, List<String> log) {
        mistakes.clear();
        System.out.println("Card statistics has been reset.");
        log.add("Card statistics has been reset.");
    }
