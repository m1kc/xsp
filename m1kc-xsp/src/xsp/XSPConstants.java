/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xsp;

/**
 *
 * @author m1kc
 */
public interface XSPConstants {

    // Виды пакетов
    final static byte TEXT = 1;
    final static byte BINARY = 2;

    // Текстовые типы
    // Служебные
    final static int OK = 0;
    final static int INVALID = 1;
    final static int ERROR = 2;
    final static int REFUSED = 3;
    // Обычные
    final static int PING = 4;
    final static int CAPSCHECK = 5;
    final static int MESSAGE = 6;
    final static int TERMINAL = 7;
    final static int FILERQ = 8;
    final static int MICROPHONERQ = 9;
    final static int DIALOGRQ = 10;
    final static int MICROPHONESTOP = 11;
    final static int DIALOGSTOP = 12;
    final static int MOUSE = 13;

    // Бинарные типы
    final static int FILEPART = 0;

    // Возможности клиента
    final static String[] CAPS = {"PING","MESSAGE","TERMINAL","FILERQ",
    "MICROPHONERQ","DIALOGRQ","MICROPHONESTOP","DIALOGSTOP","DIALOG","FILE",
    "MICROPHONE"};
}
