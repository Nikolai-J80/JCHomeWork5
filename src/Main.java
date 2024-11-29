import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static final String NAME_ZIP_FILE = "packedSaveFile";

    public static void main(String[] args) {

        List<File> listDir = new ArrayList<>();
        List<File> listFile = new ArrayList<>();

        File dirSrc = new File("C://Games/src");
        listDir.add(dirSrc);
        File dirRes = new File("C://Games/res");
        listDir.add(dirRes);
        File dirSavegames = new File("C://Games/savegames");
        listDir.add(dirSavegames);
        File dirTemp = new File("C://Games/temp");
        listDir.add(dirTemp);

        File dirMain = new File("C://Games/src/main");
        listDir.add(dirMain);
        File dirTest = new File("C://Games/src/test");
        listDir.add(dirTest);

        File dirDrawables = new File("C://Games/res/drawables");
        listDir.add(dirDrawables);
        File dirVectors = new File("C://Games/res/vectors");
        listDir.add(dirVectors);
        File dirIcons = new File("C://Games/res/icons");
        listDir.add(dirIcons);

        File mainFile = new File(dirMain, "Main.java");
        listFile.add(mainFile);
        File utilsFile = new File(dirMain, "Utils.java");
        listFile.add(utilsFile);

        File tempFile = new File(dirTemp, "temp.txt");
        listFile.add(tempFile);

        StringBuilder sb = new StringBuilder();
        //Создаем папки
        for (File dir : listDir) {
            if (dir.mkdir()) {
                sb.append("Folder " + dir.getName() + " created.\n");
            } else {
                sb.append("Folder " + dir.getName() + " not created.\n");
            }
        }
        //Создаем файлы
        for (File file : listFile) {
            try {
                if (file.createNewFile()) {
                    sb.append("File " + file.getName() + " created.\n");
                } else {
                    sb.append("File " + file.getName() + " not created.\n");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            fileWriter.write(sb.toString());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        String zipFile = "C://Games/savegames/archive.zip";

        GameProgress gamePr1 = new GameProgress(20, 15, 9, 255.2);
        GameProgress gamePr2 = new GameProgress(11, 9, 5, 200.3);
        GameProgress gamePr3 = new GameProgress(8, 2, 2, 98.6);

        saveGame("C://Games/savegames/save1.dat", gamePr1);
        saveGame("C://Games/savegames/save2.dat", gamePr2);
        saveGame("C://Games/savegames/save3.dat", gamePr3);

        List<String> saveFile = new ArrayList<>();
        saveFile.add("C://Games/savegames/save1.dat");
        saveFile.add("C://Games/savegames/save2.dat");
        saveFile.add("C://Games/savegames/save3.dat");

        zipFiles(zipFile, saveFile);

        Path dir = Paths.get("C://Games/savegames/");
        clearDirectory(dir);
    }

    public static void saveGame(String saveFile, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(saveFile); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String zipFile, List<String> saveFile) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            int cnt = 1;
            for (String sf : saveFile) {
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sf))) {
                    zos.putNextEntry(new ZipEntry(NAME_ZIP_FILE + cnt + ".dat"));
                    cnt++;
                    byte[] buffer = new byte[bis.available()];
                    bis.read(buffer);
                    zos.write(buffer);
                    zos.closeEntry();

                } catch (FileNotFoundException ex) {
                    System.out.println(ex.getMessage());
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void clearDirectory(Path dir) {
        File folder = new File(String.valueOf(dir));
        File fList[] = folder.listFiles();

        for (File f : fList) {
            if (f.getName().endsWith(".dat")) {
                f.delete();
            }
        }
    }
}