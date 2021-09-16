package scripts.tasks;

import org.tribot.script.sdk.Skill;
import scripts.api.framework.Priority;
import scripts.api.framework.Task;

public class ConstructionTraining implements Task {

    @Override
    public Priority priority() {
        return null;
    }

    @Override
    public boolean validate() {
        return Skill.CONSTRUCTION.getActualLevel() < 50;
    }

    @Override
    public void execute() {

    }
}
