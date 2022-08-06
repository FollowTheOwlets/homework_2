import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    protected final static String mainPath = "Games";
    protected static StringBuilder logs = new StringBuilder("Logs:\n");
    protected static File Games, src, res, savegames, temp,
            main, test,
            Main, Utils,
            drawables, vectors, icons,
            tempTxt;

    public static void main(String[] args) {
        initFiles();
        create();
        printLogs();

        saveGame(mainPath + "/savegames/save1.dat", new GameProgress(10, 10, 10, 10));
        saveGame(mainPath + "/savegames/save2.dat", new GameProgress(20, 20, 20, 20));
        saveGame(mainPath + "/savegames/save3.dat", new GameProgress(30, 30, 30, 30));
        zipFiles(
                mainPath + "/savegames/saveGames.zip",
                new String[]{
                        mainPath + "/savegames/save1.dat",
                        mainPath + "/savegames/save2.dat",
                        mainPath + "/savegames/save3.dat"
                }, new String[]{
                        "packed_save1.dat",
                        "packed_save2.dat",
                        "packed_save3.dat"
                }
        );

        openZip(mainPath + "/savegames/saveGames.zip", mainPath + "/savegames");
        System.out.println(openProgress(mainPath + "/savegames/packed_save2.dat"));
    }

    private static void printLogs() {
        String log = logs.toString();

        try (FileWriter fileWriter = new FileWriter(tempTxt)) {
            fileWriter.write(log);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initFiles() {
        Games = new File(mainPath);
        src = new File(mainPath + "/src");
        res = new File(mainPath + "/res");
        savegames = new File(mainPath + "/savegames");
        temp = new File(mainPath + "/temp");

        main = new File(mainPath + "/src/main");
        test = new File(mainPath + "/src/test");

        Main = new File(mainPath + "/src/main/Main.java");
        Utils = new File(mainPath + "/src/main/Utils.java");

        drawables = new File(mainPath + "/res/drawables");
        vectors = new File(mainPath + "/res/vectors");
        icons = new File(mainPath + "/res/icons");

        tempTxt = new File(mainPath + "/temp/temp.txt");
    }

    public static void create() {

        logs.append(src.mkdir() ? "Dir - src - Created.\n" : "Dir - src - not Created.\n");
        logs.append(res.mkdir() ? "Dir - res - Created.\n" : "Dir - res - not Created.\n");
        logs.append(savegames.mkdir() ? "Dir - savegames - Created.\n" : "Dir - savegames - not Created.\n");
        logs.append(temp.mkdir() ? "Dir - temp - Created.\n" : "Dir - temp - not Created.\n");

        logs.append(main.mkdir() ? "Dir - src/main - Created.\n" : "Dir - src/main - not Created.\n");
        logs.append(test.mkdir() ? "Dir - src/test - Created.\n" : "Dir - src/test - not Created.\n");

        try {
            logs.append(Main.createNewFile() ? "File - src/main/Main.java - Created.\n" : "File - src/main/Main.java - not Created.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            logs.append(Utils.createNewFile() ? "File - src/main/Utils.java - Created.\n" : "File - src/main/Utils.java - not Created.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        logs.append(drawables.mkdir() ? "Dir - res/drawables - Created.\n" : "Dir - res/drawables - not Created.\n");
        logs.append(vectors.mkdir() ? "Dir - res/vectors - Created.\n" : "Dir - res/vectors - not Created.\n");
        logs.append(icons.mkdir() ? "Dir - res/icons - Created.\n" : "Dir - res/icons - not Created.\n");

        try {
            logs.append(tempTxt.createNewFile() ? "File - temp/temp.txt - Created.\n" : "File - temp/temp.txt - not Created.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveGame(String path, GameProgress progress) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ObjectOutputStream stream = new ObjectOutputStream(fileOutputStream)) {
            stream.writeObject(progress);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void zipFiles(String path, String[] objectPaths, String[] nameZipFile) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)) {

            for (int i = 0; i < 3; i++) {
                FileInputStream fis = new FileInputStream(objectPaths[i]);
                ZipEntry zipEntry = new ZipEntry(nameZipFile[i]);
                zipOutputStream.putNextEntry(zipEntry);

                byte[] buffer = new byte[fis.available()];

                fis.read(buffer);
                zipOutputStream.write(buffer);
                zipOutputStream.closeEntry();
                fis.close();

                new File(objectPaths[i]).delete();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void openZip(String zipPath, String newPath) {
        try (FileInputStream fileInputStream = new FileInputStream(zipPath);
             ZipInputStream zipInputStream = new ZipInputStream(fileInputStream)) {
            ZipEntry zipEntry;
            String name;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                name = zipEntry.getName();
                FileOutputStream fileOutputStream = new FileOutputStream(newPath + "/" + name);

                for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
                    fileOutputStream.write(c);
                }

                fileOutputStream.flush();
                zipInputStream.closeEntry();
                fileOutputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameProgress openProgress(String savePath) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(savePath);
             ObjectInputStream stream = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) stream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return gameProgress;
    }
}


