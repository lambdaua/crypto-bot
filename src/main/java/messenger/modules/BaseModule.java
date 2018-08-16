package messenger.modules;

import database.UserService;
import messenger.logger.EventLogger;
import messenger.Messenger;

public abstract class BaseModule implements Module {

    protected EventLogger eventLogger;
    protected Messenger messenger;
    protected UserService userService;

    public BaseModule(EventLogger eventLogger, Messenger messenger, UserService userService) {
        this.eventLogger = eventLogger;
        this.messenger = messenger;
        this.userService = userService;
    }
}
