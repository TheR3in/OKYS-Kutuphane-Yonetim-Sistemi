import java.util.ArrayList;
import java.util.List;

public class Kitap {
    private String isbn;
    private String baslik;
    private String yazar;
    private int yayinYili;
    private String durum;
    
    // Composition (Kompozisyon) ilişkisi (1..*)
    private List<KitapKopyasi> fizikselKopyalar;

    // Default Constructor
    public Kitap() {
        this.fizikselKopyalar = new ArrayList<>();
    }

    // Parametreli Constructor
    public Kitap(String isbn, String baslik, String yazar, int yayinYili, String durum) {
        this.isbn = isbn;
        this.baslik = baslik;
        this.yazar = yazar;
        this.yayinYili = yayinYili;
        this.durum = durum;
        this.fizikselKopyalar = new ArrayList<>();
    }

    // Kopya ekleme metodu
    public void kopyaEkle(KitapKopyasi kopya) {
        if (kopya != null) {
            this.fizikselKopyalar.add(kopya);
        }
    }

    // Görsel 7 UML çiziminde istenilen oduncIste(t) metodu
    public boolean oduncIste(Object t) {
        // Eğer kütüphanede o kitaptan kopya varsa ödünç alınabilir (true), yoksa alınamaz (false)
        return !this.fizikselKopyalar.isEmpty();
    }

    // Görsel 8 Implementation slaytında istenilen kitapBilgisi metodu
    public String kitapBilgisi() {
        String tanitim = "ADI: " + this.baslik + " YAZARI: " + this.yazar +
                         " yayın yılı: " + this.yayinYili + " ISBN: " + this.isbn;
        return tanitim;
    }

    // Getters and Setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        this.baslik = baslik;
    }

    public String getYazar() {
        return yazar;
    }

    public void setYazar(String yazar) {
        this.yazar = yazar;
    }

    public int getYayinYili() {
        return yayinYili;
    }

    public void setYayinYili(int yayinYili) {
        this.yayinYili = yayinYili;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public List<KitapKopyasi> getFizikselKopyalar() {
        return fizikselKopyalar;
    }

    public void setFizikselKopyalar(List<KitapKopyasi> fizikselKopyalar) {
        this.fizikselKopyalar = fizikselKopyalar;
    }
}
