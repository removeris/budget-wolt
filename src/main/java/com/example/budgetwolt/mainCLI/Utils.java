package com.example.budgetwolt.mainCLI;

import com.example.budgetwolt.models.User;

import java.io.*;

public class Utils {
    public static void writeUserToFile(User user) {
        try (FileOutputStream file = new FileOutputStream("saved-users.txt")) {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(file));
            oos.writeObject(user);

            oos.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User readUserFromFile() {
        User user = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("saved-users.txt")));
            Object o = ois.readObject();
            user = (User) o;
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void writeWoltToFile(Wolt wolt) {
        try (FileOutputStream file = new FileOutputStream("wolt-db.txt")) {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(file));
            oos.writeObject(wolt);

            oos.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Wolt readWoltFromFile() {
        Wolt wolt = null;

        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("wolt-db.txt")));
            Object o = ois.readObject();
            wolt = (Wolt) o;
            ois.close();
        } catch(EOFException e) {
            // Do nothing:)
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return wolt;
    }
}
