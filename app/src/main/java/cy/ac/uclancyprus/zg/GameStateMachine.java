package cy.ac.uclancyprus.zg;

/**
 * @author Nearchos Paspallis
 * 17/07/2015.
 */
public class GameStateMachine
{
    public static final int UNINITIALIZED           = 0x10000;
    public static final int LOADING                 = 0x10010;
    public static final int LOADED                  = 0x10020;
    public static final int RESET                   = 0x10030;
    public static final int DIALOG_GET_READY        = 0x10040;
    public static final int STAGE_1_ALL_GRAY        = 0x10050;
    public static final int STAGE_2                 = 0x10060;
    public static final int STAGE_3                 = 0x10070;
    public static final int STAGE_4                 = 0x10080;
    public static final int STAGE_5                 = 0x10090;
    public static final int STAGE_6                 = 0x10100;
    public static final int RACE_STARTED            = 0x10200;
    public static final int RACE_STOPPED            = 0x10300;
    public static final int RACE_INVALID            = 0x10400;
    public static final int DIALOG_AFTER_STOPPED    = 0x10500;
    public static final int ACTIVITY_CLOSED         = 0x10600;

    private int state = UNINITIALIZED;

    public GameStateMachine()
    {
        // init
    }

    public int getState()
    {
        return state;
    }

    public void setState(final int state)
    {
        this.state = state;
    }
}