package jsesh.android.app;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class RecentFiles {

    private static final int MAX_RECENT_FILES = 10;

    private RecentFiles() {}

    public static ArrayList<String> read(Context context) {

        ArrayList<String> list = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput("recentFiles.txt");
            BufferedReader bis = new BufferedReader(new InputStreamReader(fis));
            String s;
            while ((s = bis.readLine()) != null) {
                list.add(s);
            }
            bis.close();
        } catch (IOException e) {
            //TODO Error?
        }

        return list;

    }

    public static void write(Context context, ArrayList<String> list) {

        try {
            FileOutputStream fos = context.openFileOutput("recentFiles.txt", 0);
            BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(fos));
            if (list != null) for (String s : list) {
                System.out.println("FILE"+s);
                bos.write(s);
                bos.newLine();
            }
            bos.flush();
            bos.close();
        } catch (IOException e) {
            //TODO Error?
        }

    }

    public static void update(Context context, String s) {

        ArrayList<String> list = read(context);
        while (list.remove(s));
        list.add(0, s);
        if (list.size() > MAX_RECENT_FILES) list.remove(MAX_RECENT_FILES);
        write(context, list);

    }

    public static void clear(Context context) {
        write(context, null);
    }

    public static void updateMenu(Context context, MenuItem openRecentMenuItem) {

        Menu menu = openRecentMenuItem.getSubMenu();
        menu.clear();
        for (String s : read(context)) {
            menu.add(R.id.recent_files_group, Menu.NONE, Menu.NONE, s);
        }
        menu.add(Menu.NONE, R.id.clear_recent_files, Menu.NONE, R.string.clear_recent_files);


    }

}
