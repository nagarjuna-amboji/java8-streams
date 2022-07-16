package io.amigos;

import io.amigos.model.Employee;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Java8StreamsExamples {

    public static void main(String[] args) {

       List<Employee> employees = createEmployees();

//        How many male and female employees are there in the organization?
//        getCountOfEmployeesByGender(employees);

//         Print the name of all departments in the organization?
//        printAllDeptNames(employees);

//        What is the average age of male and female employees?
//        findAvgAgeOfMaleAndFemaleEmployees(employees);

//        Get the details of highest paid employee in the organization?
//        getHighestPaidEmployees(employees);

//        Get the names of all employees who have joined after 2015?
//        getAllEmployeeJoinedAfter2015(employees);

//         Count the number of employees in each department?
//        getCountOfEmployee_In_EachDepartment(employees);

//        What is the average salary of each department?
//        getAvgSalaryByDeptWise(employees);

//        Get the details of youngest male employee in the product development department?
//        getYoungestMaleEmp_In_ProductDevDept(employees);

//        Who has the most working experience in the organization?
//        getEmployee_With_MostExp(employees);

//        How many male and female employees are there in the sales and marketing team?
//        getMale_And_Female_Employees_In_Marketing_Dept(employees);

//        What is the average salary of male and female employees?
//        getAvgSalary_Of_Male_And_Female_Emp(employees);

//        List down the names of all employees in each department?
//        getAllEmployees_In_Dept(employees);

//        What is the average salary and total salary of the whole organization?
        getAvg_And_Total_Salary_Of_Org(employees);


    }

    private static void getAvg_And_Total_Salary_Of_Org(List<Employee> employees) {
        DoubleSummaryStatistics doubleSummaryStatistics = employees.stream()
                .collect(Collectors.summarizingDouble(Employee::getSalary));
        System.out.println("Average Salary : " +doubleSummaryStatistics.getAverage());
        System.out.println("Total Salary: " +doubleSummaryStatistics.getSum());
    }

    private static void getAllEmployees_In_Dept(List<Employee> employees) {
        employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment))
                .forEach((x, y) -> System.out.println(x +" : " +y.stream().map(Employee::getName).collect(Collectors.toList())));
    }

    private static void getAvgSalary_Of_Male_And_Female_Emp(List<Employee> employees) {
        employees.stream()
                .collect(Collectors.groupingBy(Employee::getGender, Collectors.averagingDouble(Employee::getSalary)))
                .forEach((x,y) -> System.out.println(x +" : " +y));
    }

    private static void getMale_And_Female_Employees_In_Marketing_Dept(List<Employee> employees) {

        Predicate<Employee> isSalesAndMarketingDeptPredicate = e -> "Sales And Marketing".equals(e.getDepartment());
        employees.stream()
                .filter(isSalesAndMarketingDeptPredicate)
                .collect(Collectors.groupingBy(Employee::getGender, Collectors.counting()))
                .forEach((x,y) -> System.out.println(x + " : " +y));

    }

    private static void getEmployee_With_MostExp(List<Employee> employees) {

        Employee employee = employees.stream()
                /*.collect(Collectors.minBy(Comparator.comparingInt(Employee::getYearOfJoining)))
                .get();*/

                                .sorted(Comparator.comparingInt(Employee::getYearOfJoining))
                                .findFirst()
                                        .get();

        System.out.println(employee);
    }

    private static void getYoungestMaleEmp_In_ProductDevDept(List<Employee> employees) {
        Predicate<Employee> isDeptBelongToProdDevPredicate = e -> e.getDepartment() == "Product Development";
        Predicate<Employee> isMaleEmpPredicate = e -> e.getGender() == "Male";

       String name = employees.stream()
//                .filter(e -> e.getGender() == "Male" && e.getDepartment() == "Product Development")
               .filter(isMaleEmpPredicate.and(isDeptBelongToProdDevPredicate))
               .min(Comparator.comparingInt(Employee::getAge))
                .get()
                .getName();
        System.out.println("Youngest Employee in Product Development Dept : " +name);
    }

    private static void getAvgSalaryByDeptWise(List<Employee> employees) {
        employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.averagingDouble(Employee::getSalary)))
                .forEach((x,y) -> System.out.println(x +" : " +y));
    }

    private static void getCountOfEmployee_In_EachDepartment(List<Employee> employees) {
        employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment, Collectors.counting()))
                .forEach((x,y) -> System.out.println(x +" : " +y));
    }
    private static void getAllEmployeeJoinedAfter2015(List<Employee> employees) {

        employees.stream()
                .filter(e -> e.getYearOfJoining() > 2015)
                .map(Employee::getName)
                .forEach(System.out::println);
    }

    private static void getHighestPaidEmployees(List<Employee> employees) {

        /*
        Double maxSalary = employees.stream()
                .max(Comparator.comparing(Employee::getSalary))
                .get()
                .getSalary();

         */

        Double maxSalary = employees.stream()
                .collect(Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary)))
                .get()
                .getSalary();

        Comparator<Employee>  sortBySalaryDescComparator = (a,b) -> (int) (b.getSalary() - a.getSalary());

        employees.stream()
                        .sorted(sortBySalaryDescComparator)
                                .limit(1)
                                .map(Employee::getSalary)
                                .forEach(System.out::println);

//        System.out.println("Max Salary:" +maxSalary);
    }

    private static void findAvgAgeOfMaleAndFemaleEmployees(List<Employee> employees) {

        employees.stream()
                .collect(Collectors.groupingBy(Employee::getGender,
                        Collectors.averagingInt(Employee::getAge)))
                .forEach((x,y) -> System.out.println(x +" : " +y));
    }

    private static void getCountOfEmployeesByGender(List<Employee> employees) {

        employees.stream()
                .collect(Collectors.groupingBy(Employee::getGender,  Collectors.counting()))
                .forEach((x,y) -> System.out.println(x + " : " +y));
//                .forEach(System.out::println);
    }


    private static void printAllDeptNames(List<Employee> employees) {
        employees.stream()
                .map(Employee::getDepartment)
                .distinct()
                .forEachOrdered(System.out::println);
    }


    private static List<Employee> createEmployees() {

        List<Employee> employeeList = new ArrayList<Employee>();

        employeeList.add(new Employee(111, "Jiya Brein", 32, "Female", "HR", 2011, 25000.0));
        employeeList.add(new Employee(122, "Paul Niksui", 25, "Male", "Sales And Marketing", 2015, 13500.0));
        employeeList.add(new Employee(133, "Martin Theron", 29, "Male", "Infrastructure", 2012, 18000.0));
        employeeList.add(new Employee(144, "Murali Gowda", 28, "Male", "Product Development", 2014, 32500.0));
        employeeList.add(new Employee(155, "Nima Roy", 27, "Female", "HR", 2013, 22700.0));
        employeeList.add(new Employee(166, "Iqbal Hussain", 43, "Male", "Security And Transport", 2016, 10500.0));
        employeeList.add(new Employee(177, "Manu Sharma", 35, "Male", "Account And Finance", 2010, 27000.0));
        employeeList.add(new Employee(188, "Wang Liu", 31, "Male", "Product Development", 2015, 34500.0));
        employeeList.add(new Employee(199, "Amelia Zoe", 24, "Female", "Sales And Marketing", 2016, 11500.0));
        employeeList.add(new Employee(200, "Jaden Dough", 38, "Male", "Security And Transport", 2015, 11000.5));
        employeeList.add(new Employee(211, "Jasna Kaur", 27, "Female", "Infrastructure", 2014, 15700.0));
        employeeList.add(new Employee(222, "Nitin Joshi", 25, "Male", "Product Development", 2016, 28200.0));
        employeeList.add(new Employee(233, "Jyothi Reddy", 27, "Female", "Account And Finance", 2013, 21300.0));
        employeeList.add(new Employee(244, "Nicolus Den", 24, "Male", "Sales And Marketing", 2017, 10700.5));
        employeeList.add(new Employee(255, "Ali Baig", 23, "Male", "Infrastructure", 2018, 12700.0));
        employeeList.add(new Employee(266, "Sanvi Pandey", 26, "Female", "Product Development", 2015, 28900.0));
        employeeList.add(new Employee(277, "Anuj Chettiar", 31, "Male", "Product Development", 2012, 35700.0));

        return employeeList;
    }
}
