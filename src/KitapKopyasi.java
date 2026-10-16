import java.util.ArrayList;
import java.util.List;

public class KitapKopyasi {
    private final String barkod;
    private final Kitap kitap;
    private KitapDurumu durum;
    private final List<OduncAlma> oduncAlmalar = new ArrayList<>();

    // Constructor (Slayt 5'teki hocanın yazdığı kodla birebir)
    public KitapKopyasi(String barkod, Kitap kitap) {
        if (barkod == null || barkod.trim().isEmpty()) {
            throw new IllegalArgumentException("Barkod boş olamaz.");
        }
        if (kitap == null) {
            throw new IllegalArgumentException("Kitap bilgisi boş olamaz.");
        }
        this.barkod = barkod;
        this.kitap = kitap;
        // Yeni oluşturulan kopya başlangıçta mevcut
        this.durum = KitapDurumu.MEVCUT;
        
        // Çift yönlü ilişki: kopya oluşturulunca kitaba kendisini otomatik ekler
        kitap.kitapKopyasiEkle(this);
    }

    public String getBarkod() {
        return barkod;
    }

    // Geriye dönük uyumluluk için alias
    public String getBarkodNo() {
        return barkod;
    }

    public Kitap getKitap() {
        return kitap;
    }

    public KitapDurumu getDurum() {
        return durum;
    }

    public void setDurum(KitapDurumu durum) {
        if (durum == null) {
            throw new IllegalArgumentException("Kitap durumu boş olamaz.");
        }
        this.durum = durum;
    }

    public List<OduncAlma> getOduncAlmalar() {
        return oduncAlmalar;
    }

    // UML diyagramındaki oduncListesi ile uyumluluk için alias
    public List<OduncAlma> getOduncListesi() {
        return oduncAlmalar;
    }

    public void oduncEkle(OduncAlma odunc) {
        if (odunc != null && !oduncAlmalar.contains(odunc)) {
            oduncAlmalar.add(odunc);
        }
    }
}
