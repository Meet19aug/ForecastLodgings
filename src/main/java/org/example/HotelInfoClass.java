package org.example;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HotelInfoClass implements Comparable<HotelInfoClass> {
    String hotelName;
    String linkToHotel;
    String ratingNumber;
    String ratingWord;
    String priceOfHotel;

    HotelInfoClass(){
    }

    HotelInfoClass(String hn, String lh, String pr, String rn, String rw){
        hotelName=hn;
        linkToHotel=lh;
        priceOfHotel=pr;
        ratingNumber=rn;
        ratingWord=rw;
    }


    // Getter methods

    public String getHotelName() {
        return hotelName;
    }

    public String getLinkToHotel() {
        return linkToHotel;
    }

    public String getPriceOfHotel() {
        return priceOfHotel;
    }

    public String getRatingNumber() {
        return ratingNumber;
    }

    public String getRatingWord() {
        return ratingWord;
    }

    // Setter methods
    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public void setLinkToHotel(String linkToHotel) {
        this.linkToHotel = linkToHotel;
    }

    public void setPriceOfHotel(String pr) {
        this.priceOfHotel = pr;
    }

    public void setRatingNumber(String ratingNumber) {
        this.ratingNumber = ratingNumber;
    }

    public void setRatingWord(String ratingWord) {
        this.ratingWord = ratingWord;
    }

    // toString method
    @Override
    public String toString() {
        return "\"" + hotelName +"\"" + "," +"\"" +
                linkToHotel  +"\"" + "," +"\"" +
                priceOfHotel  +"\"" + "," +"\"" +
                ratingNumber  +"\"" + "," +"\"" +
                ratingWord  + "\"\n";
    }

    public void saveObjectToCSV(String filename) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(),"assets");

        filename = dirpath.toString() +"/"+filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {

            // Write the content to the file
            writer.write(this.toString());
            System.out.println("Content successfully written to the file: " + filename);
        } catch (IOException e) {
            System.err.println("Error writing to the file: " + e.getMessage());
        }
    }
    public static HotelInfoClass[] loadKObjectFromCSV(String filename, int maxExtract) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path dirpath = Paths.get(currentPath.toString(), "assets");
        filename = dirpath.toString() + "/" + filename;
        System.out.println("LoadKObjectFromCSV" + filename);
        HotelInfoClass[] hotelsdata = new HotelInfoClass[maxExtract];
        for (int i = 0; i < maxExtract; i++) {
            hotelsdata[i]=new HotelInfoClass();
        }
        int cnt=0;
        System.out.println("Working After intitlizing object");
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            System.out.println("line ="+ line);
            if(line==null){
                System.err.println("Empty file: " + filename);
                return null;
            }else{
                while (true) {
                    if(line!=null && cnt<maxExtract) {
                        String[] data = line.split("\",\"");
                        for (int i = 0; i < data.length; i++) {
                            System.out.println(data[i]);
                        }
                        System.out.println(data.length);
                        if (data.length == 5) { // Assuming there are exactly 5 fields
                            hotelsdata[cnt].setHotelName(data[0].replace("\"", ""));
                            hotelsdata[cnt].setLinkToHotel(data[1].replace("\"", ""));
                            hotelsdata[cnt].setPriceOfHotel(data[2].replace("\"", ""));
                            hotelsdata[cnt].setRatingNumber(data[3].replace("\"", ""));
                            hotelsdata[cnt].setRatingWord(data[4].replace("\"", ""));
                            cnt++;
                        }
                    }else{
                        return hotelsdata;
                    }
                    line = reader.readLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading from the file: " + e.getMessage());
        }
        return null; // Return null if something goes wrong
    }


    @Override
    public int compareTo(HotelInfoClass o) {
        if(Integer.parseInt(this.ratingNumber)>Integer.parseInt(o.ratingNumber)){
            return 0;
        }else{
            return 1;
        }
    }
}
