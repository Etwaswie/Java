import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.System.out;

class Commands {

        private static Date date = new Date();
        private static SimpleDateFormat fDate = new SimpleDateFormat("dd.MM.yyy hh:mm:ss");


        static void add(String argue, ArrayBlockingQueue<Posuda> posuda) {
            if (argue.equals("")) {
                out.println("You can't add null element!");
            } else {
                posuda.add(new Posuda(argue));
                /*out.println("Item " + argue + " was successfully added");*/
            }
        }


        static void show(ArrayBlockingQueue<Posuda> posuda) {
            if (posuda.size() == 0) {
                out.println("This collection is empty :( Put some items!");
            } else {
                out.println("Your collection consist of: ");
                for (Posuda p : posuda) {
                    out.println(p.getName());
                }
            }
        }


        static void info(ArrayBlockingQueue<Posuda> posuda) {
            out.println("Collection type: " + posuda.getClass() + "\n" + "Date of initialization: " + fDate.format(date));
            out.println("Number of items: " + posuda.size());
        }


        static void clear(ArrayBlockingQueue<Posuda> posuda) {
            posuda.clear();
            out.println("You deleted all items!");
        }

        static void delete(String argue, ArrayBlockingQueue<Posuda> posuda) {
            for (Posuda p : posuda) {

                if (p.getName().equals(argue)) {
                    posuda.remove(p);
                }
            }
            out.println("Item " + argue + " was deleted!");
        }


        static void remove_lower(String argue, ArrayBlockingQueue<Posuda> posuda) {
            for (Posuda p : posuda) {
                if (p.getName().compareTo(argue) < 0) {
                    posuda.remove(p);
                }
            }
            out.println("All items lower than this was deleted!");
        }


        static void exit() {
            out.println("See you later :)");
        }


        static void importer(String argue, ArrayBlockingQueue<Posuda> posuda) throws FileNotFoundException {
            File file2 = new File(argue);
            Scanner scanner2 = new Scanner(file2);
            while (scanner2.hasNext()) {
                String line = scanner2.nextLine();
                if (line.contains("<Name")) {
                    String name = line.substring(line.indexOf("<") + 6, line.lastIndexOf(">") - 6);
                    posuda.add(new Posuda(name));
                }
            }
            out.println("The file was imported successfully!");
        }

    }
