import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;

public class Car implements Runnable {

    private static AtomicBoolean winner = new AtomicBoolean();

    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }

    private static CyclicBarrier cyclicBarrier  = new CyclicBarrier(MainClass.CARS_COUNT);;

    private Race race;
    private int speed;
    private String name;
    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            cyclicBarrier.await();
            System.out.println(this.name + " готов");
            cyclicBarrier.await();
            MainClass.cdlReady.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        if(winner.compareAndSet(false, true)){
            System.out.printf("%s - WIN!!!\n", this.getName());
        }
        MainClass.cdlFinish.countDown();
    }
}