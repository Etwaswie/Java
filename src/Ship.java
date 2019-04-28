import java.util.Comparator;
import java.util.Date;

public class Ship implements Comparator {

    private String name;
    int size;
    Date date;
    String place;

    public Ship(String name,int size,String place, Date date) {
        this.name = name;
        this.size = size;
        this.place = place;
        this.date = date;
    }
    public Ship(String name,int size,String place) {
        this.name = name;
        this.size = size;
        this.place = place;

    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public Date getDatу(){
        return date;
    }

    public String getPlace(){
        return place;
    }


    public int compareTo(Ship s) {
        return this.size - s.size;
    }



    public String toString() {
        return (name +" "+size + " " + place+ " "+date+"\n");
    }

    @Override
    public int compare(Object o1, Object o2) {
        return o1.toString().compareTo(o2.toString());
    }
}