package scripts.tasks;

import org.tribot.api.General;
import org.tribot.script.sdk.ChatScreen;
import org.tribot.script.sdk.Waiting;
import org.tribot.script.sdk.query.Query;
import scripts.api.framework.Priority;
import scripts.api.framework.Task;
import scripts.api.functions.Logger;

import java.util.List;

public class Test implements Task {
    private final int AMOUNT_INTERFACE = 162;
    private final Logger logger = new Logger().setHeader("SkrrtTest");
    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public void execute() {
        List<String> options = ChatScreen.getOptions();
        if(!options.isEmpty())
        for(String o:options){
            logger.setMessage(o).print();
        }
        General.sleep(600);
    }
}
