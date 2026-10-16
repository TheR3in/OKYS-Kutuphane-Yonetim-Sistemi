import java.util.ArrayList;
import java.util.List;

public class Uye extends Kullanici {
    // UML Diyagramındaki okuduguKitaplar (0..*) ilişkisi
    private final List<OduncAlma> okuduguKitaplar = new ArrayList<>();

    // Parametreli Constructor
    public Uye(int id, String ad, String eposta, String sifre) {
        super(id, ad, eposta, sifre);
    }

    public List<OduncAlma> getOkuduguKitaplar() {
        return okuduguKitaplar;
    }

    // Geriye dönük uyumluluk için alias
    public List<OduncAlma> getOkudugumKitaplar() {
        return okuduguKitaplar;
    }

    // Davranış metodu: Doğrudan liste set etmek yerine kontrollü ekleme
    public void oduncEkle(OduncAlma odunc) {
        if (odunc != null && !okuduguKitaplar.contains(odunc)) {
            okuduguKitaplar.add(odunc);
        }
    }
}
