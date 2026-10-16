import java.util.ArrayList;
import java.util.List;

public class Kitap {
    private final String isbn;
    private String baslik;
    private String yazar;
    private int yayinYili;
    private final List<KitapKopyasi> fizikselKopyalar = new ArrayList<>();

    // Constructor (Zorunlu alanlar ve validasyon)
    public Kitap(String isbn, String baslik, String yazar, int yayinYili) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN bilgisi boş olamaz.");
        }
        if (baslik == null || baslik.trim().isEmpty()) {
            throw new IllegalArgumentException("Kitap başlığı boş olamaz.");
        }
        this.isbn = isbn;
        this.baslik = baslik;
        this.yazar = yazar;
        this.yayinYili = yayinYili;
    }

    // Slayt 5'teki metot: Çift yönlü ilişki ve güvenlik kontrolü
    public void kitapKopyasiEkle(KitapKopyasi kopya) {
        if (kopya == null) {
            throw new IllegalArgumentException("Kitap kopyası boş olamaz.");
        }
        if (kopya.getKitap() != this) {
            throw new IllegalArgumentException("Bu kitap kopyası başka bir kitaba ait.");
        }
        if (!fizikselKopyalar.contains(kopya)) {
            fizikselKopyalar.add(kopya);
        }
    }

    // Geriye dönük uyumluluk için alias
    public void kopyaEkle(KitapKopyasi kopya) {
        kitapKopyasiEkle(kopya);
    }

    public List<KitapKopyasi> getKopyalar() {
        return fizikselKopyalar;
    }

    public List<KitapKopyasi> getFizikselKopyalar() {
        return fizikselKopyalar;
    }

    // Ödünç verilebilir (MEVCUT) en az bir kopya var mı kontrolü
    public boolean oduncIste(Object t) {
        for (KitapKopyasi k : fizikselKopyalar) {
            if (k.getDurum() == KitapDurumu.MEVCUT) {
                return true;
            }
        }
        return false;
    }

    // Kitap bilgisi metodu
    public String kitapBilgisi() {
        return "ADI: " + this.baslik + " | YAZARI: " + this.yazar +
               " | YAYIN YILI: " + this.yayinYili + " | ISBN: " + this.isbn +
               " | TOPLAM KOPYA: " + this.fizikselKopyalar.size();
    }

    // Getters and Setters (Yalnızca mantıklı olanlar)
    public String getIsbn() {
        return isbn;
    }
    // isbn 'final' olduğu için setIsbn metodu bilinçli olarak yazılmadı (Immutability).

    public String getBaslik() {
        return baslik;
    }

    public void setBaslik(String baslik) {
        if (baslik == null || baslik.trim().isEmpty()) {
            throw new IllegalArgumentException("Kitap başlığı boş olamaz.");
        }
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
}
