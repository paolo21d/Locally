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

    AllPlaylists() {
        playlists = new ArrayList<Playlist>();

        Playlist defPl = new Playlist();
        defPl.setPlName("defaultPlaylist");
        addPlaylist(defPl);
//        playlistsAmount = 0;
    }

    public int getPlaylistsAmount() {
        return playlistsAmount;
    }

    public void addPlaylist(Playlist pl) {
        playlists.add(pl);
        playlistsAmount++;
    }

    public boolean writePlaylistsToFile(String argFile) {
        String path;
        if (argFile.equals("")) {
            //path = new File("src/main/resources/playlists.json").getAbsolutePath();
            path = "playlists.json";
        } else
            path = "src/main/resources/" + argFile;
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
            return false;
        }
        return true;
    }

    public boolean readPlaylistsFromFile(String argFile) {
        String path;
        if (argFile.equals("")) {
//            path = new File("src/main/resources/playlists.json").getAbsolutePath();
            path = "playlists.json";
        } else
            path = "src/main/resources/" + argFile;
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
            return false;
        }
        if (data.equals("")) {
            return false;
        }
        Gson json = new Gson();
        AllPlaylists tmp = json.fromJson(data, AllPlaylists.class);
        if (tmp.playlists == null) {
//            playlists = new ArrayList<Playlist>();
//            playlistsAmount = 0;
            return false;
        }

        this.playlists = tmp.playlists;
        this.playlistsAmount = tmp.playlistsAmount;
        //playlists = new ArrayList<Playlist>();
        //playlistsAmount = 0;

        return true;
    }

    public Playlist getPlaylistByName(String name) {
        for (Playlist pl : playlists) {
            if (pl.getPlName().equals(name))
                return pl;
        }
        return null;
    }

    public ArrayList<Playlist> getAllPlaylists() {
        return playlists;
    }
}
