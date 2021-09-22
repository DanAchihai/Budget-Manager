import java.util.*;

class SelectionContext {

    private PersonSelectionAlgorithm algorithm;

    public void setAlgorithm(PersonSelectionAlgorithm algorithm) {
        // write your code here
        this.algorithm = algorithm;
    }

    public Person[] selectPersons(Person[] persons) {
        // write your code here
        return algorithm.select(persons);
    }
}

interface PersonSelectionAlgorithm {

    Person[] select(Person[] persons);
}

class TakePersonsWithStepAlgorithm implements PersonSelectionAlgorithm {

    private int step;

    public TakePersonsWithStepAlgorithm(int step) {
        // write your code here
        if (step < 1) {
            throw new IllegalArgumentException("The step = " + step + " but it's not allowed less than 1");
        }
        this.step = step;
    }

    @Override
    public Person[] select(Person[] persons) {
        // write your code here
        int length = persons.length;
        if (length <= 1 || step == 1) {
            return persons;
        }
        int selectedLength = 1 + (length - 1) / step;
        Person[] selected = new Person[selectedLength];
        for (int i = 0; i < selectedLength; i++) {
            selected[i] = persons[step * i];
        }
        return selected;
    }
}


class TakeLastPersonsAlgorithm implements PersonSelectionAlgorithm {

    private int count;

    public TakeLastPersonsAlgorithm(int count) {
        // write your code here
        if (count < 1) {
            throw new IllegalArgumentException("The step = " + count + " but it's not allowed less than 1");
        }
        this.count = count;
    }

    @Override
    public Person[] select(Person[] persons) {
        // write your code here
        int p = persons.length;
        if (count >= p) {
            return persons;
        }
        Person[] selected = new Person[count];
        int start = p - count;
        for (int i = 0; i < count; i++) {
            selected[i] = persons[start + i];
        }
        return selected;
    }
}

class Person {

    String name;

    public Person(String name) {
        this.name = name;
    }
}

/* Do not change code below */
public class Main {

    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final int count = Integer.parseInt(scanner.nextLine());
        final Person[] persons = new Person[count];

        for (int i = 0; i < count; i++) {
            persons[i] = new Person(scanner.nextLine());
        }

        final String[] configs = scanner.nextLine().split("\\s+");

        final PersonSelectionAlgorithm alg = create(configs[0], Integer.parseInt(configs[1]));
        SelectionContext ctx = new SelectionContext();
        ctx.setAlgorithm(alg);

        final Person[] selected = ctx.selectPersons(persons);
        for (Person p : selected) {
            System.out.println(p.name);
        }
    }

    public static PersonSelectionAlgorithm create(String algType, int param) {
        switch (algType) {
            case "STEP": {
                return new TakePersonsWithStepAlgorithm(param);
            }
            case "LAST": {
                return new TakeLastPersonsAlgorithm(param);
            }
            default: {
                throw new IllegalArgumentException("Unknown algorithm type " + algType);
            }
        }
    }
}
