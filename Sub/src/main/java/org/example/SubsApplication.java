/*Полугодовая информация о подписных изданиях по каждому подписчику имеет следующую структуру:
ФИО;участок доставки;адрес;количество выписанных изданий;список изданий. Список изданий состоит из следующих компонентов: название;
вид, то есть газета или журнал;дата начала подписки; дата конца подписки;

Напишите программу обработки файла подписных изданий; которая по заданному месяцу и названию найдет количество экземпляров, подлежащих доставке.

По заданным ФИО распечатайте список подписных изданий данного подписчика

По заданному названию и месяцу определите участок, получающий больше всего экземпляров для доставки

В задаче должны использоваться элементы функционального программирования
Задача должна быть покрыта тестами с помощью JUnit
*/

package org.example;

import org.example.FileService.FileReader;
import org.example.Model.Month;
import org.example.Model.Publication;
import org.example.Model.Subscriber;
import org.example.Service.Service;

import java.util.List;
import java.util.Scanner;

public class SubsApplication {
    private static final String PATH = "C:\\Users\\lizan\\IdeaProjects\\practice\\src\\main\\java\\org\\example\\Task.txt";

    public static void main(String[] args) {
        FileReader reader = new FileReader();
        Service service = new Service();
        List<Subscriber> entities = reader.read(PATH);

        if (entities.isEmpty()) {
            System.out.println("Ошибка: не удалось загрузить данные подписчиков");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    handlePublicationsCount(service, entities, scanner);
                    break;
                case 2:
                    handlePublicationsPerSubscriber(service, entities, scanner);
                    break;
                case 3:
                    handleAreaWithMostSubscriptions(service, entities, scanner);
                    break;
                case 4:
                    System.out.println("Выход из программы...");
                    return;
                default:
                    System.out.println("Неверный выбор, попробуйте снова.");
            }

            System.out.println("\nНажмите Enter чтобы продолжить...");
            scanner.nextLine();
        }
    }

    private static void printMenu() {
        System.out.println("\n=== Меню обработки подписных изданий ===");
        System.out.println("1. Найти количество экземпляров для доставки (по месяцу и названию)");
        System.out.println("2. Список изданий подписчика (по ФИО)");
        System.out.println("3. Участок с максимальными доставками (по названию и месяцу)");
        System.out.println("4. Выход");
        System.out.print("Выберите вариант (1-4): ");
    }

    private static void handlePublicationsCount(Service service, List<Subscriber> entities, Scanner scanner) {
        System.out.println("\nНайти количество экземпляров для доставки");
        System.out.print("Введите название издания: ");
        String title = scanner.nextLine();

        System.out.println("Доступные месяцы:");
        for (Month month : Month.values()) {
            System.out.println((month.ordinal() + 1) + ". " + month);
        }
        System.out.print("Выберите месяц (1-" + Month.values().length + "): ");
        int monthIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        Month month = Month.values()[monthIndex];

        long count = service.getSubscriptionCountByMonthAndTitle(entities, title, month);
        System.out.println("\nКоличество экземпляров для доставки: " + count);
    }

    private static void handlePublicationsPerSubscriber(Service service, List<Subscriber> entities, Scanner scanner) {
        System.out.println("\nСписок изданий подписчика");
        System.out.print("Введите ФИО подписчика: ");
        String name = scanner.nextLine();

        List<Publication> publications = service.getPublicationsBySubscriberName(entities, name);
        System.out.println("\nСписок изданий:");
        if (publications.isEmpty()) {
            System.out.println("Издания не найдены");
        } else {
            publications.stream()
                    .map(pub -> String.format("%s (%s), подписка: %s - %s",
                            pub.title(), pub.type(), pub.startDate(), pub.endDate()))
                    .forEach(System.out::println);
        }
    }

    private static void handleAreaWithMostSubscriptions(Service service, List<Subscriber> entities, Scanner scanner) {
        System.out.println("\nУчасток с максимальными доставками");
        System.out.print("Введите название издания: ");
        String title = scanner.nextLine();

        System.out.println("Доступные месяцы:");
        for (Month month : Month.values()) {
            System.out.println((month.ordinal() + 1) + ". " + month);
        }
        System.out.print("Выберите месяц (1-" + Month.values().length + "): ");
        int monthIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        Month month = Month.values()[monthIndex];

        String area = service.getAreaWithMostSubscriptions(entities, title, month)
                .orElse("Участок не найден");
        System.out.println("\nУчасток с наибольшим количеством доставок: " + area);
    }
}