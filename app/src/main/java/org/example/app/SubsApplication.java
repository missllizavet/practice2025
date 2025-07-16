/*Полугодовая информация о подписных изданиях по каждому подписчику имеет следующую структуру:
ФИО;участок доставки;адрес;количество выписанных изданий;список изданий. Список изданий состоит из следующих компонентов: название;
вид, то есть газета или журнал;дата начала подписки; дата конца подписки;

Напишите программу обработки файла подписных изданий; которая по заданному месяцу и названию найдет количество экземпляров, подлежащих доставке.

По заданным ФИО распечатайте список подписных изданий данного подписчика

По заданному названию и месяцу определите участок, получающий больше всего экземпляров для доставки

В задаче должны использоваться элементы функционального программирования
Задача должна быть покрыта тестами с помощью JUnit
*/

package org.example.app;

import org.example.fileservice.FileReader;
import org.example.model.Month;
import org.example.model.Publication;
import org.example.model.PublicationType;
import org.example.model.Subscriber;
import org.example.service.Service;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SubsApplication {
    private static final String PATH = "C:\\Users\\lizan\\IdeaProjects\\practice2025\\app\\src\\main\\resources\\Task.txt";

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
                    handleExpiringSubscriptions(service, entities, scanner);
                    break;
                case 5:
                    handleSubscribersByPublicationType(service, entities, scanner);
                    break;
                case 6:
                    handleDeliveryAreaStatistics(service, entities);
                    break;
                case 7:
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
        System.out.println("4. Подписки, истекающие в указанном месяце");
        System.out.println("5. Подписчики по типу издания");
        System.out.println("6. Статистика по районам доставки");
        System.out.println("7. Выход");
        System.out.print("Выберите вариант (1-7): ");
    }

    // Новый функционал: поиск подписок, истекающих в указанном месяце
    private static void handleExpiringSubscriptions(Service service, List<Subscriber> entities, Scanner scanner) {
        System.out.println("\nПодписки, истекающие в указанном месяце");
        System.out.println("Доступные месяцы:");
        for (Month month : Month.values()) {
            System.out.println((month.ordinal() + 1) + ". " + month.getName());
        }
        System.out.print("Выберите месяц (1-" + Month.values().length + "): ");
        int monthIndex = scanner.nextInt() - 1;
        scanner.nextLine();
        Month month = Month.values()[monthIndex];

        List<Publication> expiring = service.getExpiringSubscriptions(entities, month);
        System.out.println("\nИстекающие подписки:");
        if (expiring.isEmpty()) {
            System.out.println("Подписки не найдены");
        } else {
            expiring.forEach(pub -> System.out.printf(
                    "%s (%s), истекает: %s, подписчик: %s%n",
                    pub.title(), pub.type(), pub.endDate(),
                    getSubscriberNameForPublication(entities, pub)
            ));
        }
    }

    // Новый функционал: поиск подписчиков по типу издания
    private static void handleSubscribersByPublicationType(Service service, List<Subscriber> entities, Scanner scanner) {
        System.out.println("\nПодписчики по типу издания");
        System.out.print("Введите тип издания (NEWSPAPER/MAGAZINE): ");
        String typeInput = scanner.nextLine().toUpperCase();

        try {
            PublicationType type = PublicationType.valueOf(typeInput);
            List<Subscriber> subscribers = service.getSubscribersByPublicationType(entities, type);

            System.out.println("\nПодписчики:");
            subscribers.forEach(sub -> System.out.printf(
                    "%s (%s), количество изданий: %d%n",
                    sub.fullName(), sub.deliveryArea(), sub.publications().size()
            ));
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: неверный тип издания");
        }
    }

    // Новый функционал: статистика по районам доставки
    private static void handleDeliveryAreaStatistics(Service service, List<Subscriber> entities) {
        System.out.println("\nСтатистика по районам доставки:");
        Map<String, Long> stats = service.getDeliveryAreaStatistics(entities);

        if (stats.isEmpty()) {
            System.out.println("Данные не найдены");
        } else {
            stats.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(entry -> System.out.printf(
                            "%s: %d подписчиков%n",
                            entry.getKey(), entry.getValue()
                    ));
        }
    }

    // Вспомогательный метод для получения имени подписчика по изданию
    private static String getSubscriberNameForPublication(List<Subscriber> subscribers, Publication publication) {
        return subscribers.stream()
                .filter(sub -> sub.publications().contains(publication))
                .findFirst()
                .map(Subscriber::fullName)
                .orElse("Неизвестный подписчик");
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