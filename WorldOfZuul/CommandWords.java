public class CommandWords
{
    private static final String validCommands[] = {
        "go", "quit", "help", "look" 
    };

    public CommandWords()
    {

    }

    public boolean isCommand(String commandString)
    {
        for(int i=0; i<validCommands.length; i++){
            if(validCommands[i].equals(commandString))
                return true;
        }
        return false;
    }
}