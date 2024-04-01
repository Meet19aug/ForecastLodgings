import org.example.HotelInfoClass;
import org.example.TrendingSearchFunctionality;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.example.HotelInfoClass.loadKObjectFromCSV;

public class Test {


    public static void main(String[] args) {
//        int kload=5;
//        HotelInfoClass[] hi = loadKObjectFromCSV("cheapflights.csv", kload);
//        for (int i = 0; i < hi.length; i++) {
//            System.out.println(hi[i]);
//        }
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(), "assets");
        String filename="city_search_frequencies.csv";
        filename = dirpath.toString() + "/" + filename;
        String inputWord = "admenton";
        TrendingSearchFunctionality.updateFrequency(filename, inputWord);

    }
}
