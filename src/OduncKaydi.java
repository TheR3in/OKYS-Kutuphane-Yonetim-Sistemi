public class OduncKaydi {
    private static int islemSayaci = 0;
    private final int islemNo;
    
    private Uye uye;
    private KitapKopyasi kopya;
    private String oduncTarihi;
    private String sonTeslimTarihi;
    private String teslimTarihi;

    // Parametreli Constructor
    public OduncKaydi(Uye uye, KitapKopyasi kopya, String oduncTarihi, String sonTeslimTarihi) {
        islemSayaci++;
        this.islemNo = islemSayaci;
        this.uye = uye;
        this.kopya = kopya;
        this.oduncTarihi = oduncTarihi;
        this.sonTeslimTarihi = sonTeslimTarihi;
    }

    // Getters and Setters
    
    public static int getIslemSayaci() {
        return islemSayaci;
    }

    public static void setIslemSayaci(int islemSayaci) {
        OduncKaydi.islemSayaci = islemSayaci;
    }

    public int getIslemNo() {
        return islemNo;
    }
    // islemNo 'final' olduğu için Setter metodu eklenmedi.

    public Uye getUye() {
        return uye;
    }

    public void setUye(Uye uye) {
        this.uye = uye;
    }

    public KitapKopyasi getKopya() {
        return kopya;
    }

    public void setKopya(KitapKopyasi kopya) {
        this.kopya = kopya;
    }

    public String getOduncTarihi() {
        return oduncTarihi;
    }

    public void setOduncTarihi(String oduncTarihi) {
        this.oduncTarihi = oduncTarihi;
    }

    public String getSonTeslimTarihi() {
        return sonTeslimTarihi;
    }

    public void setSonTeslimTarihi(String sonTeslimTarihi) {
        this.sonTeslimTarihi = sonTeslimTarihi;
    }

    public String getTeslimTarihi() {
        return teslimTarihi;
    }

    public void setTeslimTarihi(String teslimTarihi) {
        this.teslimTarihi = teslimTarihi;
    }
}
