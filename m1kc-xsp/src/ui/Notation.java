/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

/**
 *
 * @author m1kc
 */
public class Notation
{
    /**
     * Представляет размер в удобном для чтения человеком виде.
     * @param sizeInBytes размер в байтах
     * @return Размер в удобном для чтения человеком виде (например, "20 байт", "260 Гб")
     */
    public static String bestSize(long sizeInBytes)
    {
        if (sizeInBytes<1024L*10L) return ""+sizeInBytes+" байт";
        if (sizeInBytes>=1024L*1024L*1024L*10L) return ""+sizeInBytes/(1024L*1024L*1024L)+" Гб";
        if (sizeInBytes>=1024L*1024L*10L) return ""+sizeInBytes/(1024L*1024L)+" Мб";
        return ""+sizeInBytes/1024L+" Кб";
    }

    /**
     * Представляет скорость в удобном для чтения человеком виде.
     * @param speedInBytesPerSecond скорость в байт/сек
     * @return Скорость в удобном для чтения человеком виде (например, "10 байт/cек", "50 Кб/сек")
     */
    public static String bestSpeed(long speedInBytesPerSecond)
    {
        if (speedInBytesPerSecond<1024L*10L) return ""+speedInBytesPerSecond+" байт/сек";
        return ""+speedInBytesPerSecond/1024L+" Кб/сек";
    }

    private static String oNumber(long num)
    {
        return num<10 ? "0"+num : ""+num;
    }

    /**
     * Представляет время в удобном для чтения человеком виде.
     * @param timeInMs время в мс
     * @return Время в удобном для чтения человеком виде (например, "10:54")
     */
    public static String bestTime(long timeInMs)
    {
        //if (timeInMs < 10000L) return ""+timeInMs+" мс";
        return ""+(timeInMs/1000L/60)+":"+oNumber(timeInMs/1000L%60);
    }

    /**
     * Предоставляет данные в виде, угодном XSP.
     * @param prefix префикс
     * @param curr текущее значение размера
     * @param size общее значение размера
     * @param speed скорость
     * @return Строка, содержащая все аргументы в удобном для чтения человеком виде.
     */
    public static String formatAll(String prefix, long curr, long size, long speed, long rest)
    {
        return prefix+Notation.bestSize(curr)+"/"+Notation.bestSize(size)
                +" ("+Notation.bestSpeed(speed)+"), осталось "+bestTime(rest);
    }
}
