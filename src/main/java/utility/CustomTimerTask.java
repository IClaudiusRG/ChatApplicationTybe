package utility;

import java.util.Locale;
import java.util.TimerTask;

public abstract class CustomTimerTask extends TimerTask {

    public CustomTimerTask(){

    }

    public CustomTimerTask(long userOneId, long userTwoId) {

    }

    @Override
    public void run() {

    }

    public void run(long userOneId, long userTwoId) {
        run();
    }
}
