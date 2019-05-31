import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.Date;

public class Ship implements Comparator {

    private String name;
    int size;
    private OffsetDateTime date;
    String creator;
    String place;


    public Ship(String name,int size,String place, OffsetDateTime date, String creator) {
        this.name = name;
        this.size = size;
        this.place = place;
        this.date = date;
        this.creator = creator;
    }
    public Ship(String name,int size,String place,String creator) {
        this.name = name;
        this.size = size;
        this.place = place;
        this.date = OffsetDateTime.now();
        this.creator = creator;

    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public int getSize() {
        return size;
    }

    public OffsetDateTime getDatу(){
        return date;
    }

    public String getPlace(){
        return place;
    }


    public int compareTo(Ship s) {
        return this.size - s.size;
    }



    public String toString() {
        return (name +" "+size + " " + place+ " "+creator+" "+date+"\n");
    }

    @Override
    public int compare(Object o1, Object o2) {
        return o1.toString().compareTo(o2.toString());
    }

//в слуаче, если мы берем запись из базы данных, конструктор меняется, добавляя не свою дату создания, а
//     * дату создания объекта из базы данных
    Ship(String name, int size, String place, String creator, OffsetDateTime dateOfCreation) {
        this.name = name;
        this.size = size;
        this.date = dateOfCreation;
        this.place = place;
        this.creator = creator;
    }
}