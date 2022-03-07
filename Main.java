package ru.netology.lesson1;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    private static List<String> saveOfGames = new ArrayList<String>();

    public static void main(String[] args) {
	    // 1. Создаём три экземпляра класса GameProgress.
            GameProgress first = new GameProgress(9, 3, 1, 2.44);
            GameProgress second = new GameProgress(5, 8, 9, 9.76);
            GameProgress third = new GameProgress(2, 5, 4, 5.55);

        // 2. Сохраняем сериализованные объекты GameProgress в папку savegames из предыдущей задачи.
            saveGame("D://Games//savegames", first);
            saveGame("D://Games//savegames", second);
            saveGame("D://Games//savegames", third);

        // 3. Созданные файлы сохранений из папки savegames запаковываем в архив zip.
            zipFiles("D://Games//savegames//zip.zip");

        // 4. Удаляем файлы сохранений, лежащие вне архива.
            deleteFiles("D://Games//savegames", "zip.zip");

    }

    // Создаём метод сохранения текущего состояния игры
    public static void saveGame(String dir, GameProgress gameProgress) {
        int countOfSaveGame = saveOfGames.size() + 1;
        StringBuilder value = new StringBuilder();
        value.append(dir);
        value.append("//");
        value.append("save" + countOfSaveGame + ".dat");
        String preNewDir = value.toString();
        File newDir = new File(preNewDir);
        saveOfGames.add(preNewDir);
        try (FileOutputStream fos = new FileOutputStream(newDir);
            ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            System.out.println("Текущий статус игры " + gameProgress + " успешно сохранен!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Создаём метод запаковывания созданных файлов в архив zip.
    public static void zipFiles(String dir) {
        ZipEntry[] entry = new ZipEntry[saveOfGames.size()];
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(dir))) {
            for (int i = 0 ; i < saveOfGames.size() ; i++) {
                try (FileInputStream fis = new FileInputStream(saveOfGames.get(i))) {
                    String newName = "packed_save" + (i + 1) + ".dat";
                    entry[i] = new ZipEntry(newName);
                    zout.putNextEntry(entry[i]);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                    System.out.println("Файл " + newName + " был успешно добавлен в архив: " + dir);
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Создаём метод удаления файлов сохранений, лежащих вне архива.
    public static void deleteFiles(String dir, String zip) {
        File directory = new File(dir);
        for (File item : directory.listFiles()) {
            if (!zip.equals(item.getName())) {
                if (item.delete()) {
                    System.out.println("Успешно удален файл: " + item);
                }
            }
        }
    }

}
