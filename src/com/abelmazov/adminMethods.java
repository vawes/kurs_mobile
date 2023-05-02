package com.abelmazov;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class adminMethods {
    static Scanner in = new Scanner(System.in);
    static String command ="";
    public static ResultSet result;
    static void adminUsers() throws SQLException {                                  //������� ��������������, ���������� �� ������
        int menuInput=100;
        while(menuInput!=0) {
            System.out.println("1. �������� ���� �������������");
            System.out.println("2. �������� ������������");
            System.out.println("3. ������� ������������");
            System.out.println("4. �������� ������������");
            System.out.println("5. ����������� ������ ������������");
            System.out.println("0. �����");
            System.out.print("����� ������� >>> ");
            menuInput = in.nextInt();
            in.nextLine();
            switch (menuInput) {
                case (1):
                    adminUsersShow();
                    break;
                case (2):
                    adminUsersAdd();
                    break;
                case (3):
                    adminUsersDel();
                    break;
                case (4):
                    adminUserEdit();
                    break;
                case (5):
                    adminUserReserves();
                    break;
                case (0):
                    break;
                default:
                    System.out.println("����� ������� �� ������!");
                    ;
                    break;
            }
        }
    }
    static void adminUsersShow() throws SQLException {                                      //������� � ������� ���� �������������
        command = "SELECT * FROM `user`";
        result = DbConnect.statement.executeQuery(command);
        while(result.next()) {
            System.out.print("id: " + result.getString(1) + "\t");
            System.out.print("login: " + result.getString(2) + "\t");
            System.out.println("password: " + result.getString(3) + "\t");
            System.out.print("name: " + result.getString(4) + "\t");
            System.out.print("phone: " + result.getString(5) + "\t");
            System.out.println("mail: " + result.getString(6) + "\t");
            System.out.println("comment: " + result.getString(7) + "\t");
            System.out.println();
        }
    }
    static void adminUsersAdd() throws SQLException {                                           //��������� ������ ������������
        String login;
        String password;
        String name;
        String phone;
        String mail;
        String comment;
        System.out.print("������� ����� ������������: ");
        login = in.nextLine();
        System.out.print("������� ������ ������������: ");
        password = in.nextLine();
        System.out.print("������� ��� ������������ (Petrova A.N.): ");
        name = in.nextLine();
        System.out.print("������� ������� ������������ (89803232323): ");
        phone = in.nextLine();
        System.out.print("������� ������� ������������ (example@yandex.ru): ");
        mail = in.nextLine();
        System.out.print("������� ����������� ��� ������������: ");
        comment = in.nextLine();
        command = "INSERT INTO `user` (`id_user`, `login`, `password`, `name`, `phone`, `mail`, `comment`) VALUES (NULL, '"+login+"', '"+password+"', '"+name+"', '"+phone+"', '"+mail+"', '"+comment+"')";
        DbConnect.statement.executeUpdate(command);
    }
    static void adminUsersDel() throws SQLException {                                   //������� ������������
        String nameUserForDelete="";
        int idForDelete;
        String confirm="";
        System.out.println("������� id ������������(0 - ������)");
        idForDelete = in.nextInt();
        if(idForDelete==0) System.out.println("������ ��������!");
        else if(getSourse.userCheck(idForDelete)){
            in.nextLine();
            command = "SELECT name FROM `user` WHERE id_user= "+ idForDelete;
            result = DbConnect.statement.executeQuery(command);
            while(result.next()) {
                nameUserForDelete = result.getString(1);
            }
            command = "DELETE FROM user WHERE `user`.`id_user` = "+idForDelete;
            while(!confirm.equals("��") || !confirm.equals("���")) {
                System.out.println("�� �������, ��� ������ ������� ������� "+nameUserForDelete+"? (��/���)");
                confirm = in.nextLine();
                if (confirm.equals("��")) {
                    DbConnect.statement.executeUpdate(command);
                    System.out.println("������������ �����!");
                    return;
                }
                if(confirm.equals("���")){
                    System.out.println("������ ��������");
                    return;
                }
            }
        } else System.out.println("������ ������������ �� ����������!");
    }
    static void adminUserReserves() throws SQLException {               //������� ������ ������������ �� id
        int idForReserves;
        System.out.println("������� id ������������(0 - ������)");
        idForReserves = in.nextInt();
        in.nextLine();
        if(idForReserves==0) System.out.println("������ ��������!");
        else if(getSourse.userCheck(idForReserves)){
            command = "SELECT id_reserve,start,service.name, service.duration,user_comment FROM `reserve`JOIN service ON reserve.id_service = service.id_service WHERE id_user="+idForReserves+" ORDER BY reserve.start";
            result = DbConnect.statement.executeQuery(command);
            while(result.next()) {
                System.out.print("id: " + result.getString(1) + " |");
                System.out.print("����: " + result.getDate(2) + " |");
                System.out.print("�����: " + result.getTime(2).getHours() + ":00 |");
                System.out.print("������: " + result.getString(3) + " |");
                System.out.print("������������(�): " + result.getString(4) + " |");
                System.out.print("����������� �������: " + result.getString(5) + " |");
                System.out.println();
            }
            String deleteConfirm="";
            System.out.println("������ �������� ������?(��/���)");
            deleteConfirm = in.nextLine();
            if(deleteConfirm.equals("��")) adminUserReservesDel(idForReserves);

        } else System.out.println("������ ������������ �� ����������!");
    }
    static void adminUserReservesDel(int id) throws SQLException {                 //������� ������ ������������
        String deleteChoise="";
        String confirm="";
        System.out.println("����� ������ ������ ��������?(0 - �����, 00 - �������� ���)");
        deleteChoise = in.nextLine();
        if(!getSourse.reserveCheck(Integer.parseInt(deleteChoise)) && !deleteChoise.equals("00")) System.out.println("����� ������ ���!");
        else {
            if (deleteChoise.equals("0")) System.out.println("������");
            else if (deleteChoise.equals("00")) {
                command = " FROM reserve WHERE `reserve`.`id_user` = " + id;
                while (!confirm.equals("��") || !confirm.equals("���")) {
                    System.out.println("�� �������, ��� ������ �������� ��� ������? (��/���)");
                    confirm = in.nextLine();
                    if (confirm.equals("��")) {
                        DbConnect.statement.executeUpdate(command);
                        System.out.println("������ ��������!");
                        return;
                    }
                    if (confirm.equals("���")) {
                        System.out.println("������ ��������");
                        return;
                    }
                }
            } else {
                command = "DELETE FROM reserve WHERE `reserve`.`id_reserve` = " + Integer.parseInt(deleteChoise);
                DbConnect.statement.executeUpdate(command);
                System.out.println("������ ��������");
            }
        }
    }
    static void adminUserEditField(int id, String field) throws SQLException {      //������������� ����� ��������� ��������� �����
        String editParametr;
        System.out.print("������� ����� ��������(0 - ������): ");
        editParametr = in.nextLine();
        if(editParametr.equals("0")) System.out.println("\n������ ��������.");
        else {
            command = "UPDATE `user` SET `"+field+"` = '"+editParametr+"' WHERE `user`.`id_user` = "+ id;
            DbConnect.statement.executeUpdate(command);
            System.out.println("��������!");
        }
    }
    static void adminUserEdit() throws SQLException {                     //�������������� �����
        String editField = "";
        int idForEdit;
        System.out.println("������� Id ������������(0 - ������)");
        idForEdit = in.nextInt();
        in.nextLine();
        if(idForEdit==0) System.out.println("������ ��������!");
        else {
            if(getSourse.userCheck(idForEdit)){
                int menuInput=100;
                while(menuInput!=0) {
                    System.out.println("1. �������� �����");
                    System.out.println("2. �������� ������");
                    System.out.println("3. �������� ���");
                    System.out.println("4. �������� �������");
                    System.out.println("5. �������� �����");
                    System.out.println("6. �������� �����������");
                    System.out.println("0. �����");
                    System.out.print("����� ������� >>> ");
                    menuInput = in.nextInt();
                    in.nextLine();
                    switch (menuInput) {
                        case  (1):
                            editField = "login";
                            break;
                        case (2):
                            editField = "password";
                            break;
                        case (3):
                            editField = "name";
                            break;
                        case (4):
                            editField = "phone";
                            break;
                        case (5):
                            editField = "mail";
                            break;
                        case (6):
                            editField = "comment";
                            break;
                        case (0):
                            break;
                        default:
                            System.out.println("����� ������� �� ������!");;
                            break;
                    }
                    if(menuInput!=0)adminUserEditField(idForEdit,editField);
                }
            } else System.out.println("������ ������������ �� ����������!");
            command = "SELECT FROM `user` WHERE id_user= " + idForEdit;
        }
    }
    static void adminReserves() throws SQLException {                //���� ���������� �������
        int menuInput=100;
        while(menuInput!=0) {
            System.out.println("1. ������������� ������ �� ����(�� �����������)");
            System.out.println("2. ������������� ������ �� ����(�� ��������)");
            System.out.println("3. ������������� ������ �� ���� ������");
            System.out.println("4. ������������� ������ �� ������������(�� ��������)");
            System.out.println("5. ������������� ������ �� ������������(�� �����������)");
            System.out.println("6. ������������� ������ �� ������������");
            System.out.println("0. �����");
            System.out.print("����� ������� >>> ");
            menuInput = in.nextInt();
            in.nextLine();
            switch (menuInput) {
                case (1):
                    command = "SELECT reserve.id_reserve, start,service.name,service.duration, user.login FROM `reserve` JOIN service ON reserve.id_service = service.id_service JOIN user On reserve.id_user=user.id_user ORDER BY start ASC;";
                    break;
                case (2):
                    command = "SELECT reserve.id_reserve, start,service.name,service.duration, user.login FROM `reserve` JOIN service ON reserve.id_service = service.id_service JOIN user On reserve.id_user=user.id_user ORDER BY start DESC;";
                    break;
                case (3):
                    command = "SELECT reserve.id_reserve, start,service.name,service.duration, user.login FROM `reserve` JOIN service ON reserve.id_service = service.id_service JOIN user On reserve.id_user=user.id_user ORDER BY service.name ASC;";
                    break;
                case (4):
                    command = "SELECT reserve.id_reserve, start,service.name,service.duration, user.login FROM `reserve` JOIN service ON reserve.id_service = service.id_service JOIN user On reserve.id_user=user.id_user ORDER BY service.duration DESC;";
                    break;
                case (5):
                    command = "SELECT reserve.id_reserve, start,service.name,service.duration, user.login FROM `reserve` JOIN service ON reserve.id_service = service.id_service JOIN user On reserve.id_user=user.id_user ORDER BY service.duration ASC;";
                    break;
                case (6):
                    command = "SELECT reserve.id_reserve, start,service.name,service.duration, user.login FROM `reserve` JOIN service ON reserve.id_service = service.id_service JOIN user On reserve.id_user=user.id_user ORDER BY user.login ASC;";
                    break;
                case (0):
                    break;
                default:
                    System.out.println("����� ������� �� ������!");
                    break;
            }
            if(menuInput!=0)
                result = DbConnect.statement.executeQuery(command);

            while(result.next()) {
                System.out.print("id ������: " + result.getString(1) + " | ");
                System.out.print("����: " + result.getDate(2) + " | ");
                System.out.print("�����: " + result.getTime(2).getHours() + ":00 | ");
                System.out.print("������: " + result.getString(3) + " | ");
                System.out.print("������������: " + result.getString(4) + " | ");
                System.out.print("����� ������������: " + result.getString(5) + " | ");
                System.out.println();
            }
        }
    }
}
