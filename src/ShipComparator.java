import java.util.Comparator;

public class ShipComparator implements Comparator<Ship> {

    @Override
    public int compare(Ship o1, Ship o2) {
        if (o1.getSize()>o2.getSize()){
            return 1;
        }
        if (o1.getSize()==o2.getSize()){
            return 0;
        }
        return -1;
    }

}
