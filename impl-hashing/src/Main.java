import hashing.map.concrete.MyHashMap;
import hashing.map.interfaces.MyMap;

public class Main {
    public static void main(String[] args) {
        MyMap<String, Integer> map = new MyHashMap<>();
        map.put("Smith", 54);
        map.put("Anderson", 44);
        map.put("Lewis", 18);
        map.put("Cook", 31);
        map.put("Smith", 43);

        System.out.println("Entries in map: " + map);

        System.out.println("The age for Lewis is " + map.get("Lewis"));

        System.out.println("Is Smith in the map? " + map.containsKey("Smith"));

        map.remove("Smith");
        System.out.println("Entries in map: " + map);

        map.clear();
        System.out.println("Entries in map: " + map);
    }
}