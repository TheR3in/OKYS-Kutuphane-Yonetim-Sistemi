import java.util.ArrayList;
import java.util.List;

public class Uye extends Kullanici {
    // UML Diyagramındaki okudugumKitaplar (0..*) ilişkisi
    private List<OduncKaydi> okudugumKitaplar;

    // Default Constructor
    public Uye() {
        super();
        this.okudugumKitaplar = new ArrayList<>();
    }

    // Parametreli Constructor
    public Uye(int id, String ad, String eposta, String sifre) {
        super(id, ad, eposta, sifre);
        this.okudugumKitaplar = new ArrayList<>();
    }

    public List<OduncKaydi> getOkudugumKitaplar() {
        return okudugumKitaplar;
    }

    public void setOkudugumKitaplar(List<OduncKaydi> okudugumKitaplar) {
        this.okudugumKitaplar = okudugumKitaplar;
    }
}
