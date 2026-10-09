import java.util.ArrayList;
import java.util.List;

public class KitapKopyasi {
    private String barkodNo; // Görsel 7'deki UML çizimine istinaden barkod -> barkodNo yapıldı.
    
    // UML Diyagramındaki oduncListesi (0..*) ilişkisi
    private List<OduncKaydi> oduncListesi;

    // Default Constructor
    public KitapKopyasi() {
        this.oduncListesi = new ArrayList<>();
    }

    // Parametreli Constructor
    public KitapKopyasi(String barkodNo) {
        this.barkodNo = barkodNo;
        this.oduncListesi = new ArrayList<>();
    }

    // Getters and Setters
    public String getBarkodNo() {
        return barkodNo;
    }

    public void setBarkodNo(String barkodNo) {
        this.barkodNo = barkodNo;
    }

    public List<OduncKaydi> getOduncListesi() {
        return oduncListesi;
    }

    public void setOduncListesi(List<OduncKaydi> oduncListesi) {
        this.oduncListesi = oduncListesi;
    }
}
