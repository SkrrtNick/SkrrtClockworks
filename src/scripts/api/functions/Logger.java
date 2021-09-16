package scripts.api.functions;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.tribot.api.General;

public class Logger {
    @Setter @Getter @Accessors(chain = true)
    private String header;
    @Setter @Getter @Accessors(chain = true)
    private String message;
    @Setter @Getter @Accessors(chain = true)
    private Loggable loggable;
    @Setter @Getter @Accessors(chain = true)
    private boolean system;


    public void print() {
        if (isSystem()) {
            System.out.println("[" + this.header + "] " + this.message);
        } else {
            General.println("[" + this.header + "] " + this.message, this.getLoggable().getForegroundColor(), this.getLoggable().getBackgroundColor());
        }
    }

}
