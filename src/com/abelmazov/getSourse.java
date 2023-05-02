package com.abelmazov;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;

public class getSourse {
    static boolean adminStatus = false;             // ���� ����������� ������
    static boolean userStatus = false;              // ���� �����������
    static String userName;                         // ��� ������������ �� ��
    static String userId;                           // Id ������������ �� ��
    static int userService;
    static Date userDate = new Date();              // ���� ������������ ��� ������
    static int userServiceStart;
    static String userComment;
    static Date currentDate = new Date();           // ������� ����
    static boolean userSetHourCheck = true;
    public static int serviceDuration;
    public static Scanner in = new Scanner(System.in);     // �������
    public static String command;
    public static ResultSet result;
    static final String[] month = {"������","�������","����","������","���","����","����","������","��������","�������","������","�������"}; // ������ ������� ��� ������ � �������������
    public static boolean[] timetable = {true,true,true,true,true,true,true,true,true};       // ������ ��� �������� ���������� �� 1 ����

    static void timetableReset(boolean resetType){               //����� ���������� � �������� ���������
        for(int i = 0; i< timetable.length;i++){
            timetable[i]= resetType;
        }
    }
    static void autorization() throws SQLException {                            //����������� �� ������/������
        Scanner in = new Scanner(System.in);
        String login;
        String password;
        System.out.println("�����������");
        do {
            System.out.print("������� �����: ");
            login = in.nextLine();
            System.out.print("������� ������: ");
            password = in.nextLine();
            String command = "SELECT * FROM `user` WHERE login=\"" + login+ "\"";
            ResultSet result = DbConnect.statement.executeQuery(command);
            if(!result.next()) System.out.println("����� ��� ������ ����� �����������!");
                else
                    if (result.getString(3).equals(password)) {
                        userStatus = true;
                        if(login.equals("admin")) adminStatus=true;
                        userId = result.getString(1);
                        userName = result.getString(4);
                        System.out.println("������������, " + userName);
                    } else System.out.println("����� ��� ������ ����� �����������!");
        } while(!userStatus);
    }
    static int getDayOfWeek(int year,int month,int day){        //���������� �������� 0-6, ��� 0 - �����������, 1-  ����������� � �.�.
        Date date = new Date(year,month,day);
        return date.getDay();
    }
    static int maxDaysOfMouth(int i){                                   //���������� ������������ ���-�� ���� ��� ������������� ������(0 - ������ � �.�.)
        switch (i){                                                     //���������� 0, ���� ������������ ����� �����������
            case 0,2,4,6,7,9,11: return 31;
            case 3,5,8,10: return 30;
            case 1: return 28;
            default:{
                return 0;
            }
        }

    }
    static boolean userCheck(int id) throws SQLException {                      //�������� ���� �� ���� � ����� id
        command = "SELECT * FROM `user` WHERE id_user = "+ id;
        result = DbConnect.statement.executeQuery(command);
        while(result.next()) {
            return true;
        }
        return false;
    }
    static boolean reserveCheck(int id) throws SQLException {                           //�������� ���� �� ������ � ����� id
        command = "SELECT * FROM `reserve` WHERE id_reserve= "+ id;
        result = DbConnect.statement.executeQuery(command);
        while(result.next()) {
            return true;
        }
        return false;
    }

    static void menu_admin() throws SQLException {                                          //���� ��������������
        int menuInput=100;
        while(menuInput!=0) {
            System.out.println("1. ����������� ������");
            System.out.println("2. ����������");
            System.out.println("3. ����������� ���� ������");
            System.out.println("4. �������� ���� ������");
            System.out.println("5. [ADMIN] ������ ���������� ��������������");
            System.out.println("6. [ADMIN] �������� �����");
            System.out.println("0. �����");
            System.out.print("����� ������� >>> ");
            menuInput = in.nextInt();
            in.nextLine();
            switch (menuInput) {
                case  (1):
                    showGraphic();
                    break;
                case (2):
                    zapis();
                    break;
                case (3):
                    showUserReserves();
                    break;
                case (4):
                    deleteUserReserves();
                    break;
                case (5):
                    adminMethods.adminUsers();
                    break;
                case (6):
                    adminMethods.adminReserves();
                    break;
                case (0):
                    break;
                default:
                    System.out.println("����� ������� �� ������!");;
                    break;
            }
        }
    }
    static void menu_user() throws SQLException {                               //���� ������������
        int menuInput=100;
        while(menuInput!=0) {
            System.out.println("1. ����������� ������");
            System.out.println("2. ����������");
            System.out.println("3. ����������� ���� ������");
            System.out.println("4. �������� ���� ������");
            System.out.println("0. �����");
            System.out.print("����� ������� >>> ");
            menuInput = in.nextInt();
            in.nextLine();
            switch (menuInput) {
                case  (1):
                    showGraphic();
                    break;
                case (2):
                    zapis();
                    break;
                case (3):
                    showUserReserves();
                    break;
                case (4):
                    deleteUserReserves();
                    break;
                case (0):
                    break;
                default:
                    System.out.println("����� ������� �� ������!");;
                    break;
            }
        }
    }
    static void menu() throws SQLException {                            //�������� menu_admin() ��� menu_user()
        if(getSourse.adminStatus){
            getSourse.menu_admin();
        }
        else getSourse.menu_user();
    }

    static void userSetService() throws SQLException {                          //����� ������������� ������
        boolean inputCheckCorrect = false;
        while (!inputCheckCorrect) {
            System.out.println("�������� ����� ������, �� ������� ������ ����������(0-�����):");
            command = "SELECT * FROM `service` ORDER BY `id_service` ASC";
            result = DbConnect.statement.executeQuery(command);
            while (result.next()) {
                System.out.println(result.getString(1) + ". " + result.getString(2) + " (������������ " + result.getString(3) + "�)");
            }
            userService = in.nextInt();
            in.nextLine();
            command = "SELECT * FROM `service` WHERE id_service=" + userService;
            result = DbConnect.statement.executeQuery(command);
            while (result.next()) {
                inputCheckCorrect = true;
                serviceDuration = result.getInt(3);
            }
            if(userService==0) System.out.println("���� ���");
            else if (!inputCheckCorrect) System.out.println("����� ������ ���! ��������� ��� ���.");
        }

    }
    static void userSetMonth(){                         //����� ������������� ������
        System.out.println("�������� �����:");
        boolean inputCheckCorrect=false;
        while(!inputCheckCorrect) {
            inputCheckCorrect = false;
            int j = 0;
            for (int i = currentDate.getMonth(); j < 11; i++) {
                if (i == currentDate.getMonth()) System.out.print(month[i] + "(�������), ");
                else if(j<10)System.out.print(month[i]+ ", ");
                else System.out.print(month[i]);
                j++;
                if (i == 11) i = 0;
            }
            userDate.setYear(currentDate.getYear()); // 123=2023
            System.out.print("\n������� �������� ������: ");
            String tempMonth;
            tempMonth = in.nextLine();
            tempMonth = tempMonth.substring(0,1).toUpperCase()+tempMonth.substring(1);
            for (int i = 0; i < month.length; i++) {
                if (month[i].equals(tempMonth)) {
                    userDate.setMonth(i);
                    inputCheckCorrect = true;
                }
            }
            if (!inputCheckCorrect) {
                System.out.println("����� �� ���������, ��������� �������");
            }
        }
    }
    static void userSetDay(){                       //����� ������������� ���
        boolean check;
        int tempDay;
        do {
            check=false;
            System.out.print("�������� ����� ������: ");
            tempDay = in.nextInt();
            in.nextLine();
            userDate.setDate(tempDay);

            if (tempDay>maxDaysOfMouth(userDate.getMonth()) || tempDay<0) System.out.println("������� ���������� �����!");
            else if(getDayOfWeek(currentDate.getYear(),userDate.getMonth(),userDate.getDate())==0 || getDayOfWeek(currentDate.getYear(),userDate.getMonth(),userDate.getDate())==6){
                System.out.println("��� �������� ����! �������� ������ �����.");
            } else{
                check = true;
            }
        } while(!check);
    }
    static void userSetHour() throws SQLException {             //����� ������������� ���� � �������� ������ � ��
        userSetHourCheck = true;
        boolean check1;
        boolean check2;
        boolean check3 = false;
        while (!check3) {
            System.out.print("������� ���� �����(� �����, 0 - �����):");
            userServiceStart = in.nextInt();
            in.nextLine();
            if (userServiceStart==0){
                userSetHourCheck = false;
                return;
            }
            check1 = false;
            check2 = false;
            if (userServiceStart < 9 || userServiceStart > 17) {
                System.out.println("������� ������� �����!");
            } else {
                check1 = true;
            }
            if(check1) {
                for (int i = userServiceStart; i < (userServiceStart + serviceDuration); i++) {
                    if(userServiceStart + serviceDuration-9>9) {
                        check2 = true;
                        break;
                    }
                    else if (timetable[i-9] == false) check2 = true;
                }
                if (check2)
                    System.out.println("� ��� ����� ������! ��������� ����������������� ������ � ��������� ������");
                if (check1 && !check2) check3 = true;
            }
        }
        System.out.println("������ ������ ����������� � ������:");
        userComment = in.nextLine();
        //System.out.println("INSERT INTO `reserve` (`id_reserve`, `id_user`, `id_service`, `start`, `user_comment`) VALUES (NULL, '"+ userId+"', '"+userService+"', '2023-"+ (userDate.getMonth()+1)+"-"+ userDate.getDate()+" "+userServiceStart+":00:00', '"+userComment+"')");
        command = "INSERT INTO `reserve` (`id_reserve`, `id_user`, `id_service`, `start`, `user_comment`) VALUES (NULL, '"+ userId+"', '"+userService+"', '2023-"+ (userDate.getMonth()+1)+"-"+ userDate.getDate()+" "+userServiceStart+":00:00', '"+userComment+"')";
        DbConnect.statement.executeUpdate(command);
    }
    static void zapis() throws SQLException {                       //�������� ��� ������ �� ������ ���� � ������� � ����
        userSetService();
        userSetMonth();
        do {
            userSetDay();
            System.out.println("\t\t\t|09:00|10:00|11:00|12:00|13:00|14:00|15:00|16:00|17:00|");
            getGraphic(userDate.getMonth(),userDate.getDate());
            userSetHour();
        }
        while (!userSetHourCheck);
        System.out.println();


    }
    static void showGraphic() throws SQLException {                                      //������� ������ ������ � ��������� ���������
        boolean inputCheckCorrect = true;
        int dayStart;
        int dayEnd;
        userSetMonth();
        System.out.println("������� � ������ � �� ����� ����� �������� ������.");
        System.out.print("�������� � ");
        dayStart = in.nextInt();
        in.nextLine();
        System.out.print("�� ");
        dayEnd = in.nextInt();
        in.nextLine();
        if(dayStart<0 || dayStart>maxDaysOfMouth(userDate.getMonth())){
            if (userDate.getMonth()==currentDate.getMonth()){
            dayStart = currentDate.getDate();
            inputCheckCorrect = false;
            } else {
                dayStart =1;
                inputCheckCorrect = false;
            }
        }
        if(dayEnd<dayStart || dayEnd>maxDaysOfMouth(userDate.getMonth()) || dayStart==dayEnd){
            if(dayStart+10<=maxDaysOfMouth(userDate.getMonth())){
                dayEnd = dayStart+10;
            }else dayEnd = maxDaysOfMouth(userDate.getMonth()+1);
            inputCheckCorrect = false;
        }
        if(!inputCheckCorrect) System.out.println("�������� ������ �����������! ����� ������� ������ � "+dayStart+" �� "+dayEnd+" �����.");
        else System.out.println("������ � "+dayStart+" �� "+dayEnd+" �����.");
        System.out.println("\t\t\t|09:00|10:00|11:00|12:00|13:00|14:00|15:00|16:00|17:00|");
        for (int i=dayStart;i<dayEnd;i++){
            getGraphic(userDate.getMonth(),i);
        }
    }
    static void getGraphic(int month, int day) throws SQLException {       //������ ������� ������ ������
        String command = "SELECT id_reserve,start,service.duration, service.name FROM reserve JOIN service ON reserve.id_service = service.id_service WHERE start BETWEEN \"2023-" + (month+1) + "-" + day + "\" AND \"2023-" + (month+1) + "-" + (day + 1) + "\" ORDER BY reserve.start";
        //System.out.println(command);
        final String[] daysOfWeek = {"��", "��", "��", "��", "��", "��", "��"};
        ResultSet result = DbConnect.statement.executeQuery(command);
        timetableReset(true);
        int serviceDuration = 0;
        Time serviceStart;
        if (month < 0 || month > 11 || day < 0 || day > maxDaysOfMouth(month)) System.out.println("������������ ������!");
        else {
            while (result.next()) {
                serviceStart = result.getTime(2);
                serviceDuration = result.getInt(3);
                while (serviceDuration != 0) {
                    timetable[serviceStart.getHours() + serviceDuration - 10] = false;
                    serviceDuration--;
                }
            }
            int index = getDayOfWeek(123, month, day);
            if (index == 0 || index == 6) timetableReset(false);

            //System.out.println("\t\t\t|09:00|10:00|11:00|12:00|13:00|14:00|15:00|16:00|17:00|");
            System.out.print(day + "/" + month + " " + daysOfWeek[index] + ": \t|");
            for (int i = 0; i < timetable.length; i++) {
                if (timetable[i]) System.out.print("     |");
                else System.out.print("�����|");
            }
        }
        System.out.println();

    }
    static void showUserReserves() throws SQLException {                            //���������� ������������ ��� ������
        //System.out.println("SELECT reserve.start, service.name, service.duration, reserve.id_reserve FROM `reserve` JOIN service ON reserve.id_service = service.id_service WHERE id_user="+userId+" ORDER BY start;");
        command = "SELECT reserve.start, service.name, service.duration, reserve.id_reserve FROM `reserve` JOIN service ON reserve.id_service = service.id_service WHERE id_user="+userId+" AND start> CURRENT_DATE ORDER BY start;";
        result = DbConnect.statement.executeQuery(command);
        int i = 1;
        while(result.next()) {
            System.out.print(result.getInt(4)+". ");
            System.out.print("����: " + result.getDate(1) + " | ");
            System.out.print("�����: " + result.getTime(1).getHours() + ":00 | ");
            System.out.print("������: " + result.getString(2) + " | ");
            System.out.print("������������: " + result.getInt(3) + " | ");
            System.out.println();
            i++;
        }
        System.out.println();
    }
    static void deleteUserReserves() throws SQLException {          //������� ������ ��������� �����
        String reserveChoose;
        String confirm="";
        showUserReserves();
        System.out.print("������� ����� ���������� ������(0 - �����, 00 - �������� ��� ������): ");
        reserveChoose = in.nextLine();
        if(reserveChoose.equals("00")){
            while(!confirm.equals("��") || !confirm.equals("���")) {
                System.out.println("�� �������? (��/���)");
                confirm = in.nextLine();
                if (confirm.equals("��")) {
                    command = "DELETE FROM reserve WHERE id_user = " + userId;
                    DbConnect.statement.executeUpdate(command);
                    System.out.println("��� ������ ��������!");
                    return;
                }
                if (confirm.equals("���")){
                    System.out.println("������ ��������");
                    return;
                }
            }
        } else if(!reserveChoose.equals("0")){
            command = "DELETE FROM reserve WHERE `reserve`.`id_reserve` = " + reserveChoose + " and id_user = " + userId + " ";
            DbConnect.statement.executeUpdate(command);
            System.out.println("������ ��������!");
        }
    }

}
