import java.util.HashSet;

public class SupportSystem
{
    private InputReader reader;
    private Responder responder;
    
    /**
     * Creates a technical support system.
     */
    public SupportSystem()
    {
        reader = new InputReader();
        responder = new Responder();
    }

    
   public void start()
    {
        boolean finished = false;
    
        printWelcome();
    
        while(!finished) {
            String input = reader.getInput();
    
            if(input.equals("selesai")) {
                finished = true;
            }
            else {
                String response = responder.generateResponse(input);
                System.out.println(response);
            }
        }
        printGoodbye();
    }

    private void printWelcome()
    {
        System.out.println("==============================================");
        System.out.println("Selamat Datang di Sistem Bantuan Akademik");
        System.out.println("==============================================");
        System.out.println();
        System.out.println("Silakan sampaikan kendala atau pertanyaan Anda.");
        System.out.println("Kami siap membantu menyelesaikan masalah yang Anda hadapi.");
        System.out.println("Ketik 'selesai' untuk keluar dari sistem.");
    }

    private void printGoodbye()
    {
        System.out.println("Senang berbicara dengan anda...");
    }
}
