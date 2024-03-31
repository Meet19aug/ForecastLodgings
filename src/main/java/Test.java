import org.example.HotelInfoClass;

import static org.example.HotelInfoClass.loadKObjectFromCSV;

public class Test {
    public static void main(String[] args) {
        int kload=5;
        //System.out.println(HotelInfoClass.extractScore(""));
        HotelInfoClass[] hi = loadKObjectFromCSV("cheapflights_Windsor.csv", kload);
        for (int i = 0; i < hi.length; i++) {
            System.out.println(hi[i]);
        }
    }
}
