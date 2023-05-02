package com.abelmazov;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class adminMethods {
    static Scanner in = new Scanner(System.in);
    static String command ="";
    public static ResultSet result;
    static void adminUsers() throws SQLException {                                  //подменю администратора, отвечающее за юзеров
        int menuInput=100;
        while(menuInput!=0) {
            System.out.println("1. Показать всех пользователей");
            System.out.println("2. Добавить пользователя");
            System.out.println("3. Удалить пользователя");
            System.out.println("4. Изменить пользователя");
            System.out.println("5. Просмотреть записи пользователя");
            System.out.println("0. Назад");
            System.out.print("Номер команды >>> ");
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
                    System.out.println("Номер команды не найден!");
                    ;
                    break;
            }
        }
    }
    static void adminUsersShow() throws SQLException {                                      //выводит в консоль всех пользователей
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
    static void adminUsersAdd() throws SQLException {                                           //добавляет нового пользователя
        String login;
        String password;
        String name;
        String phone;
        String mail;
        String comment;
        System.out.print("Введите логин пользователя: ");
        login = in.nextLine();
        System.out.print("Введите пароль пользователя: ");
        password = in.nextLine();
        System.out.print("Введите имя пользователя (Petrova A.N.): ");
        name = in.nextLine();
        System.out.print("Введите телефон пользователя (89803232323): ");
        phone = in.nextLine();
        System.out.print("Введите телефон пользователя (example@yandex.ru): ");
        mail = in.nextLine();
        System.out.print("Введите комментарий для пользователя: ");
        comment = in.nextLine();
        command = "INSERT INTO `user` (`id_user`, `login`, `password`, `name`, `phone`, `mail`, `comment`) VALUES (NULL, '"+login+"', '"+password+"', '"+name+"', '"+phone+"', '"+mail+"', '"+comment+"')";
        DbConnect.statement.executeUpdate(command);
    }
    static void adminUsersDel() throws SQLException {                                   //удаляет пользователя
        String nameUserForDelete="";
        int idForDelete;
        String confirm="";
        System.out.println("Введите id пользователя(0 - отмена)");
        idForDelete = in.nextInt();
        if(idForDelete==0) System.out.println("Отмена действия!");
        else if(getSourse.userCheck(idForDelete)){
            in.nextLine();
            command = "SELECT name FROM `user` WHERE id_user= "+ idForDelete;
            result = DbConnect.statement.executeQuery(command);
            while(result.next()) {
                nameUserForDelete = result.getString(1);
            }
            command = "DELETE FROM user WHERE `user`.`id_user` = "+idForDelete;
            while(!confirm.equals("Да") || !confirm.equals("Нет")) {
                System.out.println("Вы уверены, что хотите удалить аккаунт "+nameUserForDelete+"? (Да/Нет)");
                confirm = in.nextLine();
                if (confirm.equals("Да")) {
                    DbConnect.statement.executeUpdate(command);
                    System.out.println("Пользователь удалён!");
                    return;
                }
                if(confirm.equals("Нет")){
                    System.out.println("Отмена действия");
                    return;
                }
            }
        } else System.out.println("Такого пользователя не существует!");
    }
    static void adminUserReserves() throws SQLException {               //выводит записи пользователя по id
        int idForReserves;
        System.out.println("Введите id пользователя(0 - отмена)");
        idForReserves = in.nextInt();
        in.nextLine();
        if(idForReserves==0) System.out.println("Отмена действия!");
        else if(getSourse.userCheck(idForReserves)){
            command = "SELECT id_reserve,start,service.name, service.duration,user_comment FROM `reserve`JOIN service ON reserve.id_service = service.id_service WHERE id_user="+idForReserves+" ORDER BY reserve.start";
            result = DbConnect.statement.executeQuery(command);
            while(result.next()) {
                System.out.print("id: " + result.getString(1) + " |");
                System.out.print("Дата: " + result.getDate(2) + " |");
                System.out.print("Время: " + result.getTime(2).getHours() + ":00 |");
                System.out.print("Услуга: " + result.getString(3) + " |");
                System.out.print("Длительность(ч): " + result.getString(4) + " |");
                System.out.print("Комментарий клиента: " + result.getString(5) + " |");
                System.out.println();
            }
            String deleteConfirm="";
            System.out.println("Хотите отменить запись?(Да/Нет)");
            deleteConfirm = in.nextLine();
            if(deleteConfirm.equals("Да")) adminUserReservesDel(idForReserves);

        } else System.out.println("Такого пользователя не существует!");
    }
    static void adminUserReservesDel(int id) throws SQLException {                 //удаляет записи пользователя
        String deleteChoise="";
        String confirm="";
        System.out.println("Какую запись хотите отменить?(0 - назад, 00 - отменить все)");
        deleteChoise = in.nextLine();
        if(!getSourse.reserveCheck(Integer.parseInt(deleteChoise)) && !deleteChoise.equals("00")) System.out.println("Такой записи нет!");
        else {
            if (deleteChoise.equals("0")) System.out.println("Отмена");
            else if (deleteChoise.equals("00")) {
                command = " FROM reserve WHERE `reserve`.`id_user` = " + id;
                while (!confirm.equals("Да") || !confirm.equals("Нет")) {
                    System.out.println("Вы уверены, что хотите отменить все записи? (Да/Нет)");
                    confirm = in.nextLine();
                    if (confirm.equals("Да")) {
                        DbConnect.statement.executeUpdate(command);
                        System.out.println("Записи отменены!");
                        return;
                    }
                    if (confirm.equals("Нет")) {
                        System.out.println("Отмена действия");
                        return;
                    }
                }
            } else {
                command = "DELETE FROM reserve WHERE `reserve`.`id_reserve` = " + Integer.parseInt(deleteChoise);
                DbConnect.statement.executeUpdate(command);
                System.out.println("Запись отменена");
            }
        }
    }
    static void adminUserEditField(int id, String field) throws SQLException {      //универсальный метод изменения параметра юзера
        String editParametr;
        System.out.print("Введите новое значение(0 - отмена): ");
        editParametr = in.nextLine();
        if(editParametr.equals("0")) System.out.println("\nОтмена действия.");
        else {
            command = "UPDATE `user` SET `"+field+"` = '"+editParametr+"' WHERE `user`.`id_user` = "+ id;
            DbConnect.statement.executeUpdate(command);
            System.out.println("Изменено!");
        }
    }
    static void adminUserEdit() throws SQLException {                     //редактирование юзера
        String editField = "";
        int idForEdit;
        System.out.println("Введите Id пользователя(0 - отмена)");
        idForEdit = in.nextInt();
        in.nextLine();
        if(idForEdit==0) System.out.println("Отмена действия!");
        else {
            if(getSourse.userCheck(idForEdit)){
                int menuInput=100;
                while(menuInput!=0) {
                    System.out.println("1. Изменить логин");
                    System.out.println("2. Изменить пароль");
                    System.out.println("3. Изменить имя");
                    System.out.println("4. Изменить телефон");
                    System.out.println("5. Изменить почту");
                    System.out.println("6. Изменить комментарий");
                    System.out.println("0. Назад");
                    System.out.print("Номер команды >>> ");
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
                            System.out.println("Номер команды не найден!");;
                            break;
                    }
                    if(menuInput!=0)adminUserEditField(idForEdit,editField);
                }
            } else System.out.println("Такого пользователя не существует!");
            command = "SELECT FROM `user` WHERE id_user= " + idForEdit;
        }
    }
    static void adminReserves() throws SQLException {                //меню сортировки записей
        int menuInput=100;
        while(menuInput!=0) {
            System.out.println("1. Отсортировать записи по дате(по возрастанию)");
            System.out.println("2. Отсортировать записи по дате(по убыванию)");
            System.out.println("3. Отсортировать записи по типу услуги");
            System.out.println("4. Отсортировать записи по длительности(по убыванию)");
            System.out.println("5. Отсортировать записи по длительности(по возрастанию)");
            System.out.println("6. Отсортировать записи по пользователю");
            System.out.println("0. Назад");
            System.out.print("Номер команды >>> ");
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
                    System.out.println("Номер команды не найден!");
                    break;
            }
            if(menuInput!=0)
                result = DbConnect.statement.executeQuery(command);

            while(result.next()) {
                System.out.print("id записи: " + result.getString(1) + " | ");
                System.out.print("Дата: " + result.getDate(2) + " | ");
                System.out.print("Время: " + result.getTime(2).getHours() + ":00 | ");
                System.out.print("Услуга: " + result.getString(3) + " | ");
                System.out.print("Длительность: " + result.getString(4) + " | ");
                System.out.print("Логин пользователя: " + result.getString(5) + " | ");
                System.out.println();
            }
        }
    }
}
