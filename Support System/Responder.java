import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Responder
{
    private HashMap<String, String> responseMap;
    private ArrayList<String> defaultResponses;
    private Random randomGenerator;

    public Responder()
    {
        responseMap = new HashMap<>();
        defaultResponses = new ArrayList<>();
        fillResponseMap();
        fillDefaultResponses();
        randomGenerator = new Random();
    }

    
    public String generateResponse(String inputLine)
    {
        String response = responseMap.get(inputLine);
        
        if(response != null) {
            return response;
        }
        else {
            return pickDefaultResponse();
        }
    }

    private void fillResponseMap()
    {
        responseMap.put("tidak bisa akses frs", 
                          "Pastikan Anda mengakses FRS sesuai jadwal yang telah ditentukan.\n" +
                          "Jika masih terkendala, hubungi Bantuan Akademik.\n");
    
        responseMap.put("kuota penuh", 
                          "Kuota mata kuliah tersebut sudah penuh. Anda dapat mencari kelas lain dengan jadwal berbeda.\n" +
                          "Anda juga bisa menghubungi dosen wali atau departemen terkait kemungkinan penambahan kuota.\n");
    
        responseMap.put("gagal login", 
                          "Login gagal. Mohon periksa kembali username dan password Anda.\n" +
                          "Jika Anda lupa password, silakan gunakan fitur 'Lupa Password'.\n");
    
        responseMap.put("menambah mata kuliah", 
                          "Silahkan masuk ke menu 'Pengisian FRS', cari mata kuliah yang diinginkan, \n" +
                          "lalu klik tombol 'Ambil' pada kelas yang tersedia.\n");
    
        responseMap.put("drop mata kuliah", 
                          "Masuk ke daftar mata kuliah yang sudah Anda ambil di laman FRS,\n" +
                          "kemudian klik tombol 'Drop'. Perhatikan batas waktu drop mata kuliah yang berlaku.\n");
    
        responseMap.put("frs belum disetujui", 
                          "Status FRS Anda masih 'Menunggu Persetujuan'. Silakan hubungi Dosen Wali Anda\n " +
                          "untuk meminta persetujuan agar FRS dapat segera diproses.\n");
    
        responseMap.put("info", 
                          "Jadwal pengisian FRS semester ini adalah dari 10 Agustus 2025 hingga 14 Agustus 2025.\n" +
                          "Untuk panduan lengkap dan informasi lebih lanjut, silakan kunjungi laman akademik.\n");
    
        responseMap.put("jadwal bentrok", 
                          "Sistem mendeteksi adanya jadwal yang bentrok. Anda tidak bisa mengambil dua mata kuliah pada waktu yang bersamaan. \n" +
                          "Silakan pilih kelas lain untuk salah satu mata kuliah tersebut.\n");
    
        responseMap.put("kuota kelas", 
                          "Anda dapat melihat sisa kuota yang tersedia pada setiap kelas saat melakukan pencarian mata kuliah. \n" +
                          "Informasi kuota biasanya ditampilkan dalam format 'Terisi / Total Kuota' (contoh: 35/40).\n");
    
        responseMap.put("ipk kurang", 
                          "Jumlah SKS maksimal yang dapat Anda ambil bergantung pada IPK semester sebelumnya. \n" +
                          "Jika IPK Anda tidak memenuhi syarat untuk jumlah SKS yang Anda pilih, sistem akan menolaknya.\n" +
                          "Silakan konsultasi dengan Dosen Wali Anda.\n");
    }

    private void fillDefaultResponses()
    {
       
        defaultResponses.add("Maaf, saya tidak mengerti maksud Anda. Bisa gunakan kata kunci yang lebih spesifik?");
        defaultResponses.add("Saya kurang paham dengan pertanyaan Anda. Mohon coba ulangi dengan kalimat yang berbeda.");
        defaultResponses.add("Hmm, sepertinya kata kunci tersebut tidak ada dalam sistem kami. Mungkin Anda bisa mencoba kata lain?");
        defaultResponses.add("Input Anda tidak dikenali. Silakan coba lagi atau gunakan kata kunci bantuan seperti 'Info'.");
        defaultResponses.add("Perintah yang Anda masukkan tidak valid. Silakan merujuk pada daftar perintah yang tersedia.");
        defaultResponses.add("Maaf, sistem tidak dapat memproses permintaan Anda. Pastikan input yang Anda berikan sudah benar.");
        defaultResponses.add("Wah, sepertinya itu di luar jangkauan saya. Ada lagi yang bisa dibantu terkait FRS?");
        defaultResponses.add("Aduh, saya tidak ketemu jawaban untuk itu. Coba tanya tentang 'jadwal' atau 'kuota' mungkin?");
        defaultResponses.add("Saya rasa saya butuh informasi lebih lanjut mengenai itu.");
    }

    private String pickDefaultResponse()
    {
        int index = randomGenerator.nextInt(defaultResponses.size());
        return defaultResponses.get(index);
    }
}
