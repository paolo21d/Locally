package pckLocally;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class AllPlaylists {
    private ArrayList<Playlist> playlists;
    private int playlistsAmount = 0;

    AllPlaylists(){
        playlists = new ArrayList<Playlist>();
        playlistsAmount = 0;
    }

    public int getPlaylistsAmount() {
        return playlistsAmount;
    }

    public void addPlaylist(Playlist pl){
        playlists.add(pl);
        playlistsAmount++;
    }

    public boolean writePlaylistsToFile(){
        String path = new String("playlists.json");
        Gson json = new Gson();
        String response = json.toJson(this);

        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(response);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return  false;
        }
        return true;
    }
    public boolean readPlaylistsFromFile(){
        String path = new String("playlists.json");
        String data = new String("");
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return  false;
        }

        Gson json = new Gson();
        AllPlaylists tmp = json.fromJson(data, AllPlaylists.class);
        this.playlists = tmp.playlists;
        this.playlistsAmount = tmp.playlistsAmount;
        return true;
    }
}
